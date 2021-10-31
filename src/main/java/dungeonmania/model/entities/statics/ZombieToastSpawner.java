package dungeonmania.model.entities.statics;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends Entity {

    public ZombieToastSpawner(Position position) {
        super("zombie_toast_spawner", position);
    }

    /**
     * If the Player interacts with the ZombieToastSpawner with a weapon equipped,
     * the player destroys the spawner and the weapon loses durability.
     */
    @Override
    public void interact(Game game, MovingEntity character) {
        if (character instanceof Player) {
            Player player = (Player) character;
            if (player.hasWeapon()) {
                Equipment weapon = player.getWeapon();
                weapon.useEquipment();
                game.removeEntity(this);
            }
        }        
    }
}