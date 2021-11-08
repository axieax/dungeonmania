package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.movement.MovementState;
import dungeonmania.util.Position;

public abstract class Enemy extends MovingEntity implements Observer {

    private int damageMultiplier;
    private MovementState movementState;

    public final double ARMOUR_DROP_RATE = 0;
    public final double THE_ONE_RING_DROP_RATE = 0.1; // 10% of dropping one_ring
    
    public Enemy(String prefix, Position position, int health, int attackDamage, int damageMultiplier) {
        super(prefix, position, health, attackDamage);
        this.damageMultiplier = damageMultiplier;
    }

    public int getBaseAttackDamage() {
        return super.getBaseAttackDamage() * damageMultiplier;
    }

    public MovementState getMovementState() {
        return movementState;
    }

    public void setMovementState(MovementState movementState) {
        this.movementState = movementState;
    }

    public void move(Game game) {
        this.movementState.move(game);
    }
    
}
