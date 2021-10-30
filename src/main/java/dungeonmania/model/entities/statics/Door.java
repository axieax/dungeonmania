package dungeonmania.model.entities.statics;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Position;

public class Door extends Entity {

    private boolean open = false;
    private int key;

    public Door(Position position, int key) {
        super("door", position);
        this.key = key;
    }

    /**
     * Returns if the door is open.
     * @return
     */
    public boolean isOpen() {
        return this.open;
    }

    /**
     * Unlocks the door given the corresponding key.
     * @param key
     * @return
     */
    public boolean unlockDoor(Key key) {
        if (key.getKey() == this.key) {
            this.open = true;
            this.setPassable(true);
        }
        return this.open;
    }

    /**
     * If the Player interacts the Door with the correct key, it unlocks the door.
     * @param game
     * @param character
     */
    @Override
    public void interact(Game game, MovingEntity character) {
        if (character instanceof Player) {
            Player player = (Player) character;
            Key key = player.getKey();
            if (this.unlockDoor(key)) key.consume(player);
        }
    }
}
