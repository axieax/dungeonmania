package dungeonmania.model.entities.movings.olderPlayer;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.model.entities.movings.SubjectPlayer;
import dungeonmania.util.Position;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class OlderPlayer extends Enemy {
    private static final int MAX_OLDER_PLAYER_ATTACK_DMG = 10;

    public OlderPlayer(Position position, int health, List<Position> moves) {
        super("older_player", position, health, MAX_OLDER_PLAYER_ATTACK_DMG, 1);
        this.setMovementState(new RewindMovementState(this, moves.iterator()));
    }

    public void tick(Game game) {
        this.getMovementState().move(game);
    }

    @Override
    public void update(SubjectPlayer player) {}

    @Override
    public JSONObject toJSON () {
        JSONObject info = super.toJSON();
        RewindMovementState movements = (RewindMovementState)getMovementState();
        List<Position> moves = movements.restOfList();
        JSONArray history = new JSONArray();
        for (Position position: moves) {
            JSONObject pos = new JSONObject();
            pos.put ("x", position.getX());
            pos.put ("y", position.getY());
            history.put (pos);
        }
        info.put ("moves", history);
        return info;
    }

}
