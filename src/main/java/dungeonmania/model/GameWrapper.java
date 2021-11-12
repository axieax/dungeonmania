package dungeonmania.model;

import dungeonmania.GameLoader;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.older_player.OlderPlayer;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.goal.Goal;
import dungeonmania.model.mode.Mode;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public final class GameWrapper {

    private List<JSONObject> states = new ArrayList<>();
    private Game activeGame;

    public GameWrapper(Game game) {
        activeGame = game;
        states.add(GameLoader.gameToJSONObject(activeGame));
    }

    public GameWrapper(String dungeonName, List<Entity> entities, Goal goal, Mode mode) {
        this(new Game(dungeonName, entities, goal, mode));
    }

    public final DungeonResponse tick(String itemUsedId, Direction movementDirection)
        throws IllegalArgumentException, InvalidActionException {
        DungeonResponse resp = activeGame.tick(itemUsedId, movementDirection);
        return (activeGame.playerReachedTTPortal()) ? rewind(30) : resp;
    }

    public final DungeonResponse interact(String entityId) {
        return activeGame.interact(entityId);
    }

    public final DungeonResponse build(String buildable) {
        return activeGame.build(buildable);
    }

    public final DungeonResponse rewind(int ticks) throws IllegalArgumentException {
        // extra check that time turner has been obtained
        if (!activeGame.playerHasTimeTurner()) throw new IllegalArgumentException(
            "time_turner required to rewind"
        );

        // requested rewind index
        int restoreIndex = Math.max(states.size() - ticks - 1, 0);
        JSONObject gameState = states.get(restoreIndex);
        Game restoreGame = GameLoader.JSONObjectToGame(gameState);

        // get positions since rewinded index
        List<Position> moves = new ArrayList<>(states.size() - restoreIndex);
        for (int i = restoreIndex; i < states.size(); ++i) {
            JSONObject state = states.get(i);

            // find player position
            // TODO: move to GameLoader?
            JSONArray entities = state.getJSONArray("entities");
            for (int j = 0; j < entities.length(); ++j) {
                JSONObject entity = entities.getJSONObject(j);
                if (entity.getString("type").startsWith("player")) {
                    moves.add(new Position(entity.getInt("x"), entity.getInt("y"), 31));
                    break;
                }
            }
        }

        // add old player
        Player player = (Player) activeGame.getCharacter();
        OlderPlayer ilNam = new OlderPlayer(player.getPosition(), moves, player);
        restoreGame.addEntity(ilNam);

        // rewind
        activeGame = restoreGame;
        states = states.subList(0, restoreIndex + 1);

        return activeGame.getDungeonResponse();
    }

    public final DungeonResponse getDungeonResponse() {
        return activeGame.getDungeonResponse();
    }

    public Game getActiveGame() {
        return activeGame;
    }
}
