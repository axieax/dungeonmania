package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.movement.MovementState;
import dungeonmania.model.entities.statics.SwampTile;
import dungeonmania.util.Position;
import org.json.JSONObject;

public abstract class Enemy extends MovingEntity implements Observer {

    private int damageMultiplier;
    private MovementState movementState;
    private int movementTick;

    private double armourDropRate;

    public Enemy(
        String prefix,
        Position position,
        int health,
        int attackDamage,
        int damageMultiplier
    ) {
        super(prefix, position, health, attackDamage);
        this.damageMultiplier = damageMultiplier;
        this.movementTick = 1;
        this.armourDropRate = 0.2;
    }

    /**
     * Get base attack damage of Enemy
     *
     * @return base attack damage
     */
    public int getBaseAttackDamage() {
        return super.getBaseAttackDamage() * damageMultiplier;
    }

    /**
     * Set damage multiplier of Enemy
     *
     * @param damageMultiplier damage multiplier
     */
    public void setDamageMultiplier(int damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    /**
     * Get movement state of Enemy
     *
     * @return MovementState
     */
    public MovementState getMovementState() {
        return movementState;
    }

    /**
     * Set movement state of Enemy
     *
     * @param movementState
     */
    public void setMovementState(MovementState movementState) {
        this.movementState = movementState;
    }

    /**
     * Get armour drop rate
     *
     * @return armourDropRate
     */
    public double getArmourDropRate() {
        return armourDropRate;
    }

    /**
     * Set armour drop rate
     *
     * @param armourDropRate
     */
    public void setArmourDropRate(double armourDropRate) {
        this.armourDropRate = armourDropRate;
    }

    /**
     * Get movement tick
     *
     * @return movement tick
     */
    public int getMovementTick() {
        return movementTick;
    }

    /**
     * Set movement tick
     *
     * @param movementTick
     */
    public void setMovementTick(int movementTick) {
        this.movementTick = movementTick;
    }

    /**
     * Move an Enemy
     *
     * @param game game state
     */
    public void move(Game game) {
        SwampTile swampTile = game.getSwampTile(this.getPosition());
        if (swampTile != null && this.movementTick < swampTile.getMovementFactor()) {
            this.movementTick++;
        } else {
            this.movementTick = 1;
            this.movementState.move(game);
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject info = super.toJSON();
        info.put("damageMultiplier", damageMultiplier);
        info.put("movementState", movementState.getClass().getSimpleName());
        info.put("movementTick", movementTick);
        return info;
    }
}
