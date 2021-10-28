package dungeonmania.model.entities.movings;

import dungeonmania.model.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Mercenary extends MovingEntity implements Observer {
    final static int MAX_MERCENARY_HEALTH = 50;
    final static int MAX_MERCENARY_ATTACK_DMG = 5;

    public Mercenary(String entityId, Position position) {
        super(entityId, position, MAX_MERCENARY_HEALTH, MAX_MERCENARY_ATTACK_DMG);
    }

    public Mercenary(String entityId, Position position, int health, int attackDamage) {
        super(entityId, position, health, attackDamage);
    }

    @Override
    public void tick(Dungeon dungeon) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(SubjectPlayer player) {
        // TODO Auto-generated method stub
        
    }
}
