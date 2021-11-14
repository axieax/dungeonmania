package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
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

    public Direction getDirection() {
        return this.movingDirection;
    }

    public void setDirection(Direction direction) {
        this.movingDirection = direction;
    }

    public void interact(Game game, Entity character) {}

    public void kill() {
        this.health = 0;
    }

    /**
     * Returns true if entity has positive health, else false
     */
    public boolean isAlive() {
        return getHealth() > 0;
    }

    public void reduceHealthFromBattle(int amount) {
        this.setHealth(this.getHealth() - amount);
    }

    //////////////////////////////////////////////////////////////////////////////////
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getBaseAttackDamage() {
        return attackDamage;
    }

    public JSONObject toJSON() {
        JSONObject info = super.toJSON();
        info.put("health", health);
        info.put(
            "movingDirection",
            (movingDirection != null) ? movingDirection.toString() : Direction.NONE.toString()
        );
        return info;
    }

    public void moveTo(Position position) {
        this.setPosition(position);
    }

    public boolean collision(Entity entity) {
        return !entity.isPassable();
    }
}
