package dungeonmania.model.entities.statics;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.movings.MovingEntityBehaviour;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends Entity {

    public ZombieToastSpawner(String entityId, Position position) {
        super(entityId, position);
    }

    /**
     * If the Player interacts with the ZombieToastSpawner with a weapon equipped,
     * the player destroys the spawner and the weapon loses durability.
     */
    @Override
    public void interact(Dungeon dungeon, MovingEntityBehaviour character) {
        if (character instanceof Player) {
            Player player = (Player) character;
            if (player.hasWeapon()) {
                Equipment weapon = player.getWeapon();
                weapon.useEquipment();
                dungeon.removeEntity(this);
            }
        }        
    }
}