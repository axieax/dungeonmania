package dungeonmania.model.entities.collectables;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.movings.MovingEntityBehaviour;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Position;

public class Key extends Item {

    private int key;

    public Key(String entityId, Position position, int key) {
        super(entityId, position);
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
    public void interact(Dungeon dungeon, MovingEntityBehaviour character) {
        if (character instanceof Player && !((Player) character).hasKey()) {
            ((Player) character).collect(this);
            dungeon.removeEntity(this);
        }
    }
}
