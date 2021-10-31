package dungeonmania.model.entities.movings;

import org.json.JSONObject;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Tickable;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class MovingEntity extends Entity implements Tickable {
    private int health;
    private int attackDamage;
    private Direction movingDirection;
    private boolean isEnemy;

    public final double ARMOUR_DROP_RATE = 0;
    public final double THE_ONE_RING_DROP_RATE = 0.1; // 10% of dropping one_ring
    
    public MovingEntity(String prefix, Position position, int health, int attackDamage, boolean isEnemy) {
        super(prefix, position, true, true);
        this.health = health;
        this.attackDamage = attackDamage;
        this.isEnemy = isEnemy;
    }

    public Direction getDirection() {
        return this.movingDirection;
    }
    public void setDirection(Direction direction) {
        this.movingDirection = direction;
    }

    public void interact(Game game, MovingEntity character) { return; }
    
    /**
     * Returns true if the player has positive health, else false
    */
    public void kill() {
        this.health = 0;
    }

    public boolean isAlive() {
        return getHealth() > 0 ? true : false;
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
        info.put ("health", health);
        return info;
    }


    public abstract Direction getDirection();

    public void moveTo(Position position) {
        this.setPosition(position);
    };

    public abstract void moveTo(Position position);

    
}
