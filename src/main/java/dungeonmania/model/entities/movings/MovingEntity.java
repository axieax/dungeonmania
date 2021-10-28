package dungeonmania.model.entities.movings;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class MovingEntity extends Entity implements MovingEntityBehaviour  {
    private int health;
    private int attackDamage;
    
    public MovingEntity(String entityId, Position position, int health, int attackDamage) {
        super(entityId, position, true);
        this.health = health;
        this.attackDamage = attackDamage;
    }
    
    public abstract void tick(Dungeon dungeon);
    
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

}
