package dungeonmania.model.goal;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.BribableEnemy;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.model.entities.statics.ZombieToastSpawner;

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
            .filter(
                e ->
                    // Ignore character
                    (e != character) &&
                    (
                        // ZombieToastSpawner counts
                        e instanceof ZombieToastSpawner ||
                        // Enemy which are enemies
                        (e instanceof Enemy && !(e instanceof BribableEnemy && ((BribableEnemy) e).isBribed()))
                    )
            )
            .count();
    }
}
