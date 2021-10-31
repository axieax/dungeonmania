package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class MovingEntity extends Entity {
    private int health;
    private int attackDamage;

    public final double ARMOUR_DROP_RATE = 0;
    public final double THE_ONE_RING_DROP_RATE = 0.1; // 10% of dropping one_ring
    
    public MovingEntity(String prefix, Position position, int health, int attackDamage) {
        super(prefix, position, true, true);
        this.health = health;
        this.attackDamage = attackDamage;
    }
    
    public abstract void tick(Game game);

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
    
    
    public abstract Direction getDirection();

    public boolean collision(Entity entity) {
        return !entity.isPassable();
    };

    public abstract void moveTo(Position position);

    public abstract boolean isEnemy();
}
