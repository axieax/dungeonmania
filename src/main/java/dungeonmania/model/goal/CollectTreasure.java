package dungeonmania.model.goal;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.Treasure;

public class CollectTreasure extends GoalLeaf {

    public CollectTreasure() {
        super("treasure");
    }

    @Override
    public int numRemaining(Game game) {
        return (int) game.getEntities().stream().filter(e -> e instanceof Treasure).count();
    }
}
