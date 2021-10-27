package dungeonmania.model.entities.movings;

import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class MovingEntity extends Entity {
    private int health;
    private int attackDamage;
    
    public MovingEntity(String entityId, Position position, int health, int attackDamage) {
        super(entityId, position, true);
        this.health = health;
        this.attackDamage = attackDamage;
    }
    
    public abstract void move(Direction direction);

    public abstract void moveTo(Position position);

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
