package dungeonmania.model;

import dungeonmania.GameLoader;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.Observer;
import dungeonmania.model.entities.movings.olderPlayer.OlderPlayer;
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

    /**
     * Ticks the game state. When a tick occurs: - The player moves in the specified
     * direction one square - All enemies move respectively - Any items which are
     * used are 'actioned' and interact with the relevant entity
     *
     * @param itemUsedId id of item used
     * @param movementDirection Direction to move the player
     * @return DungeonResponse of current game
     * @throws IllegalArgumentException if itemUsed is not a bomb, health_potion
     *                                  invincibility_potion, or an invisibility_potion,
     *                                  or null (if no item is being used)
     * @throws InvalidActionException   if itemUsed is not in the player's inventory
     */
    public final DungeonResponse tick(String itemUsedId, Direction movementDirection)
        throws IllegalArgumentException, InvalidActionException {
        DungeonResponse resp = activeGame.tick(itemUsedId, movementDirection);
        states.add(GameLoader.gameToJSONObject(activeGame));
        return (activeGame.playerReachedTTPortal()) ? rewind(30) : resp;
    }

    /**
     * Interacts with a mercenary (where the character bribes the mercenary) or a
     * zombie spawner, where the character destroys the spawner.
     *
     * @param entityId of entity to interact with
     * @return DungeonResponse of current game
     * @throws IllegalArgumentException if entityId does not refer to a valid entity ID
     * @throws InvalidActionException   if the player is not cardinally adjacent to
     *                                  the given entity; if the player does not have
     *                                  any gold/sun stones and attempts to bribe or
     *                                  mind-control a mercenary; if the player does
     *                                  not have a weapon and attempts to destroy a spawner
     */
    public final DungeonResponse interact(String entityId) {
        return activeGame.interact(entityId);
    }

    /**
     * Builds the given entity
     *
     * @param buildable name of entity to be built
     * @return DungeonResponse of current game
     * @throws IllegalArgumentException If buildable is not one of bow, shield, sceptre, midnight_armour
     * @throws InvalidActionException   If the player does not have sufficient items
     *                                  to craft the buildable
     */
    public final DungeonResponse build(String buildable) {
        return activeGame.build(buildable);
    }

    /**
     * Rewinds the active game by the specified number of ticks
     *
     * @param ticks number of ticks to go back
     * @return DungeonResponse for the active game
     * @throws IllegalArgumentException if ticks <= 0
     */
    public final DungeonResponse rewind(int ticks) throws IllegalArgumentException {
        // extra check that time turner has been obtained
        if (!activeGame.playerHasTimeTurner()) throw new IllegalArgumentException(
            "time_turner required to rewind"
        );

        // requested rewind index
        int restoreIndex = Math.max(states.size() - ticks - 1, 0);
        JSONObject gameState = states.get(restoreIndex);
        Game restoreGame = GameLoader.JSONObjectToGame(gameState);

        // get positions since rewind index
        List<Position> moves = new ArrayList<>(states.size() - restoreIndex);
        for (int i = restoreIndex + 1; i < states.size(); ++i) {
            JSONObject state = states.get(i);
            moves.add(getPlayerPosition(state));
        }

        // retrieve activeGame player
        Player activePlayer = (Player) activeGame.getCharacter();

        // retrieve restoreGame player
        Player restorePlayer = (Player) restoreGame.getCharacter();

        // old_player in activeGame has restorePlayer's position
        OlderPlayer ilNam = new OlderPlayer(
            restorePlayer.getPosition().asLayer(30),
            restorePlayer.getMaxCharacterHealth(),
            moves
        );
        restoreGame.addEntity(ilNam);
        restoreGame.removeEntity(restorePlayer);

        restoreGame.addEntity(activePlayer);

        // reset active player
        activePlayer.removeObservers();
        activePlayer.removeAllies();
        activePlayer.setInBattle(false);
        activePlayer.setCurrentBattleOpponent(null);
        // re-attach observers to updated player
        restoreGame
            .getEntities()
            .stream()
            .filter(e -> e instanceof Observer)
            .forEach(e -> activePlayer.attach((Observer) e));

        // time travel
        activeGame = restoreGame;
        states = states.subList(0, restoreIndex + 1);

        return activeGame.getDungeonResponse();
    }

    /**
     * Helper method to get the player's position from a game state
     *
     * @param state JSONObject for game state to check
     *
     * @return Position of player
     */
    private final Position getPlayerPosition(JSONObject state) {
        JSONArray entities = state.getJSONArray("entities");
        for (int j = 0; j < entities.length(); ++j) {
            JSONObject entity = entities.getJSONObject(j);
            if (entity.getString("type").startsWith("player")) {
                return new Position(entity.getInt("x"), entity.getInt("y"), 30);
            }
        }
        return null;
    }

    /**
     * Get the DungeonResponse for the active game
     *
     * @return DungeonResponse for game
     */
    public final DungeonResponse getDungeonResponse() {
        return activeGame.getDungeonResponse();
    }

    /**
     * Get the active game
     *
     * @return Game object for active game
     */
    public Game getActiveGame() {
        return activeGame;
    }
}
