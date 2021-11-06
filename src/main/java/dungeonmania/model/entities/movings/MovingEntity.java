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
    private boolean enemy;
    private int damageMultiplier;

    public final double ARMOUR_DROP_RATE = 0;
    public final double THE_ONE_RING_DROP_RATE = 0.1; // 10% of dropping one_ring
    
    public MovingEntity(String prefix, Position position, int health, int attackDamage, boolean enemy) {
        super(prefix, position, true, true);
        this.health = health;
        this.attackDamage = attackDamage;
        this.enemy = enemy;
        this.damageMultiplier = 1;
    }

    public MovingEntity(String prefix, Position position, int health, int attackDamage, boolean enemy, int damageMultiplier) {
        super(prefix, position, true, true);
        this.health = health;
        this.attackDamage = attackDamage;
        this.enemy = enemy;
        this.damageMultiplier = damageMultiplier;
    }


    public Direction getDirection() {
        return this.movingDirection;
    }
    public void setDirection(Direction direction) {
        this.movingDirection = direction;
    }

    public boolean isEnemy() {
        return enemy;
    }

    public void setEnemy(boolean enemy) {
        this.enemy = enemy;
    }

    public void interact(Game game, MovingEntity character) { return; }
    
    public void kill() {
        this.health = 0;
    }

    /**
     * Returns true if the player has positive health, else false
    */
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


    public void moveTo(Position position) {
        this.setPosition(position);
    };
    
    public boolean collision(Entity entity) {
        return !entity.isPassable();
    }
}
