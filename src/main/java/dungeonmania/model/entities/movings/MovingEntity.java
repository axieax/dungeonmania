package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class MovingEntity extends Entity {
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
    
    public abstract void tick(Game game);

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
    
    
    public boolean collision(Entity entity) {
        return !entity.isPassable();
    };

    public void moveTo(Position position) {
        this.setPosition(position);
    };

    public boolean isEnemy() {
        return this.isEnemy;
    };
}
