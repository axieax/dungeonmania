package dungeonmania.model.entities.movings;

import dungeonmania.model.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToast extends MovingEntity {
    final static int MAX_ZOMBIE_HEALTH = 20;
    final static int MAX_ZOMBIE_ATTACK_DMG = 2;

    public ZombieToast(String entityId, Position position) {
        super(entityId, position, MAX_ZOMBIE_HEALTH, MAX_ZOMBIE_ATTACK_DMG);
    }

    public ZombieToast(String entityId, Position position, int health, int attackDamage) {
        super(entityId, position, health, attackDamage);
    }

    @Override
    public void tick(Dungeon dungeon) {
        
    }
}
