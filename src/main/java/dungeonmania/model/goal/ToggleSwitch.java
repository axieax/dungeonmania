package dungeonmania.model.goal;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.FloorSwitch;
import dungeonmania.util.Position;
import java.util.HashSet;
import java.util.Set;

public class ToggleSwitch extends GoalLeaf {

    public ToggleSwitch() {
        super("boulders");
    }

    @Override
    public int numRemaining(Game game) {
        // S = set of switch positions
        Set<Position> switchPositions = new HashSet<>();
        // B = set of boulder positions
        Set<Position> boulderPositions = new HashSet<>();

        // find S and B
        for (Entity e : game.getEntities()) {
            Position position = e.getPosition();
            if (e instanceof FloorSwitch) {
                switchPositions.add(position);
            } else if (e instanceof Boulder) {
                boulderPositions.add(position);
            }
        }

        // set difference D = S - B
        switchPositions.removeAll(boulderPositions);
        // return D.size()
        return switchPositions.size();
    }
}
