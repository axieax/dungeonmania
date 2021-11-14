package dungeonmania.model.entities.movings;

import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Tickable;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.json.JSONObject;

public abstract class MovingEntity extends Entity implements Tickable {

    private int health;
    private int attackDamage;
    private Direction movingDirection;

    public MovingEntity(String prefix, Position position, int health, int attackDamage) {
        super(prefix, position, true, true);
        this.health = health;
        this.attackDamage = attackDamage;
    }

    /**
     * Get direction that a MovingEntity is heading towards
     *
     * @return Direction
     */
    public Direction getDirection() {
        return this.movingDirection;
    }

    /**
     * Set direction that a MovingEntity is heading towards
     *
     * @param Direction
     */
    public void setDirection(Direction direction) {
        this.movingDirection = direction;
    }

    /**
     * Suicide.
     */
    public void kill() {
        this.health = 0;
    }

    /**
     * Checks if an Entity is alive
     *
     * @return true if entity has positive health, false otherwise
     */
    public boolean isAlive() {
        return getHealth() > 0;
    }

    /**
     * Reduces health from battle by the specified amount
     *
     * @param amount damage received
     */
    public void reduceHealthFromBattle(int amount) {
        this.setHealth(this.getHealth() - amount);
    }

    /**
     * Get health of MovingEntity
     *
     * @return health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Set health of MovingEntity
     *
     * @param health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Get base attack damage of MovingEntity
     *
     * @return attack damage
     */
    public int getBaseAttackDamage() {
        return attackDamage;
    }

    /**
     * Checks if the MovingEntity collides with the given Entity
     *
     * @param entity to collide with
     * @return true if not passable, false otherwise
     */
    public boolean collision(Entity entity) {
        return !entity.isPassable();
    }

    @Override
    public JSONObject toJSON() {
        JSONObject info = super.toJSON();
        info.put("health", health);
        info.put(
            "movingDirection",
            (movingDirection != null) ? movingDirection.toString() : Direction.NONE.toString()
        );
        return info;
    }
}
