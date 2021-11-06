package dungeonmania.model.goal;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.FloorSwitch;
import dungeonmania.util.Position;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONObject;

public class ToggleSwitch extends GoalLeaf {

    public ToggleSwitch() {
        super("switch");
    }

    @Override
    public int numRemaining(Game game) {
        // S = set of switch positions
        Set<Position> switchPositions = new HashSet<>();
        // B = set of boulder positions
        Set<Position> boulderPositions = new HashSet<>();

        // Find S and B
        for (Entity e : game.getEntities()) {
            Position position = e.getPosition();
            Position noLayer = new Position(position.getX(), position.getY());
            if (e instanceof FloorSwitch) {
                switchPositions.add(noLayer);
            } else if (e instanceof Boulder) {
                boulderPositions.add(noLayer);
            }
        }

        // Set difference D = S - B
        switchPositions.removeAll(boulderPositions);
        // Return D.size()
        return switchPositions.size();
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("goal", "boulders");
        return json;
    }
}
