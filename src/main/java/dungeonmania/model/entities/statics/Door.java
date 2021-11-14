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
        this.open = false;
        this.key = key;
    }

    /**
     * Returns if the door is open.
     * @return true if open, false otherwise
     */
    public boolean isOpen() {
        return this.open;
    }

    /**
     * Unlocks the door.
     * @param key to unlock the door with
     */
    private void unlockDoor() {
        this.open = true;
        this.setPassable(true);
    }

    @Override
    public void interact(Game game, Entity character) {
        // If the Player interacts the Door with the correct key, it unlocks the door.
        if (character instanceof Player) return;
        Player player = (Player) character;
        Key key = player.getKey();

        if (player.hasItemQuantity("sun_stone", 1)) {
            this.unlockDoor();
        } else if (key != null && key.getKey() == this.key) {
            this.unlockDoor();
            key.consume(game, player);
        }
    }

    @Override
    public EntityResponse getEntityResponse() {
        String doorStatus = String.format("%s_%d", getType(), key);
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
