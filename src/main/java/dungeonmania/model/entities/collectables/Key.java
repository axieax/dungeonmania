package dungeonmania.model.entities.collectables;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.statics.Consumable;
import dungeonmania.util.Position;

public class Key extends Item implements Consumable {

    private int key;

    public Key(Position position, int key) {
        super("key", position);
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    /**
     * If the Player interacts with the Key, collect the Key into the player's 
     * inventory if there are no keys. Since the Player can only hold one key at
     * a time.
     */
    @Override
    public void interact(Dungeon dungeon, MovingEntity character) {
        if (character instanceof Player && !((Player) character).hasKey()) {
            ((Player) character).collect(this);
            dungeon.removeEntity(this);
        }
    }

    @Override
    public void consume(Player player) {
        player.removeInventoryItem(this.getId());
    }
}
