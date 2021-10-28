package dungeonmania.model.entities.movings;

import dungeonmania.model.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Mercenary extends MovingEntity {
    public final static int MAX_MERCENARY_HEALTH = 50;
    public final static int MAX_MERCENARY_ATTACK_DMG = 5;

    public Mercenary(String entityId, Position position) {
        super(entityId, position, MAX_MERCENARY_HEALTH, MAX_MERCENARY_ATTACK_DMG);
    }

    public Mercenary(String entityId, Position position, int health, int attackDamage) {
        super(entityId, position, health, attackDamage);
    }

    @Override
    public void move(Direction direction) {
        // TODO Auto-generated method stub

    }

    @Override
    public void moveTo(Position position) {
        // TODO Auto-generated method stub

    }

    @Override
    public void interact(Dungeon dungeon, MovingEntityBehaviour character) {
        // TODO Auto-generated method stub
        
    }
}
