package dungeonmania.model.goal;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.MovingEntity;

public class DestroyEnemies extends GoalLeaf {

    public DestroyEnemies() {
        super("enemies");
    }

    @Override
    public int numRemaining(Game game) {
        Entity character = game.getCharacter();
        return (int) game
            .getEntities()
            .stream()
            .filter(e -> (e != character) && (e instanceof MovingEntity))
            .count();
    }
}
