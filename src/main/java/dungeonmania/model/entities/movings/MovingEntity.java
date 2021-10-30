package dungeonmania.model.entities.movings;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class MovingEntity extends Entity implements MovingEntityBehaviour  {
    private int health;
    private int attackDamage;
    private int defaultBattleDamage;
    
    public MovingEntity(Position position, int health, int attackDamage) {
        super(position, true, true);
        this.health = health;
        this.attackDamage = attackDamage;
        this.defaultBattleDamage = this.getHealth() *  this.getAttackDamage() / 10;
    }

    public MovingEntity(Position position, int health, int attackDamage, int defaultBattleDamage) {
        this(position, health, attackDamage);
        this.defaultBattleDamage = defaultBattleDamage;
    }
    
    public abstract void tick(Dungeon dungeon);

    public void interact(Dungeon dungeon, MovingEntity character) { return; }
    
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
    
    public int getAttackDamage() {
        return attackDamage;
    }
    
    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }
    
    public int getDefaultBattleDamange() {
        return this.getHealth() *  this.getAttackDamage() / 5;
    }

    public void setDefaultBattleDamage(int defaultBattleDamage) {
        this.defaultBattleDamage = defaultBattleDamage;
    }
    
    public abstract Direction getDirection();

    public abstract boolean isCollidable(Entity entity);

    public abstract void moveTo(Position position);
}
