package dungeonmania.model.entities.statics;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import org.json.JSONObject;

public class Door extends Entity {

    private boolean open;
    private int key;

    public Door(Position position, int key) {
        super("door", position, false, false);
        this.key = key;
        this.open = false;
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
    public void interact(Game game, Entity character) {
        if (character instanceof Player) {
            Player player = (Player) character;
            Key key = player.getKey();
            if (key != null && this.unlockDoor(key)) key.consume(game, player);
        }
    }

    @Override
    public EntityResponse getEntityResponse() {
        String doorStatus = String.format("%s_%d", getPrefix(), key);
        if (open) doorStatus += "_open";

        return new EntityResponse(getId(), doorStatus, getPosition(), isInteractable());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject info = super.toJSON();
        info.put("key", key);
        return info;
    }
}
