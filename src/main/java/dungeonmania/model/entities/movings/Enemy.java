package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.movement.MovementState;
import dungeonmania.model.entities.statics.SwampTile;
import dungeonmania.util.Position;

public abstract class Enemy extends MovingEntity implements Observer {

    private int damageMultiplier;
    private MovementState movementState;
    private int movementTick;

    public final double ARMOUR_DROP_RATE = 0;
    public final double THE_ONE_RING_DROP_RATE = 0.1; // 10% of dropping one_ring
    
    public Enemy(String prefix, Position position, int health, int attackDamage, int damageMultiplier) {
        super(prefix, position, health, attackDamage);
        this.damageMultiplier = damageMultiplier;
        this.movementTick = 1;
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

    public int getMovementTick() {
        return movementTick;
    }

    public void setMovementTick(int movementTick) {
        this.movementTick = movementTick;
    }

    public void move(Game game) {
        SwampTile swampTile = game.getSwampTile(this.getPosition());
        if (swampTile != null && this.movementTick < swampTile.getMovementFactor()) {
            this.movementTick++;
        } else {
            this.movementTick = 1;
            this.movementState.move(game);
        }
    }
    
}
