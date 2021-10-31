package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.util.Position;

public class DefaultState implements EnemyMovementState {
    private MovingEntity enemy;

    public DefaultState(MovingEntity enemy) {
        this.enemy = enemy;
    }

    /**
     * Default movement of enemy
     */
    @Override
    public void move(Game game, Position playerPos) {
        if (enemy instanceof Mercenary) {
            Mercenary mercenary = (Mercenary) enemy;
            mercenary.move(game, playerPos);
        } else if (enemy instanceof ZombieToast) {
            ZombieToast zombieToast = (ZombieToast) enemy;
            zombieToast.move(game, playerPos);
        }
    }
}
