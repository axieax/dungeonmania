package dungeonmania.model.entities.movings;

import dungeonmania.model.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Spider extends MovingEntity {
    final static int MAX_SPIDER_HEALTH = 20;
    final static int MAX_SPIDER_ATTACK_DMG = 2;

    public Spider(String entityId, Position position) {
        super(entityId, position, MAX_SPIDER_HEALTH, MAX_SPIDER_ATTACK_DMG);
    }

    public Spider(String entityId, Position position, int health, int attackDamage) {
        super(entityId, position, health, attackDamage);
    }

    @Override
    public void tick(Dungeon dungeon) {
        // TODO Auto-generated method stub
        
    }
    
}
