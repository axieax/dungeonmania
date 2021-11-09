package dungeonmania.model.entities.collectables;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import org.json.JSONObject;

public class Key extends Item implements Consumable {

    private int key;

    public Key(Position position, int key) {
        super("key_" + key, position);
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
    public void interact(Game game, Entity character) {
        if (character instanceof Player && !((Player) character).hasKey()) {
            ((Player) character).collect(this);
            game.removeEntity(this);
        }
    }

    @Override
    public void consume(Game game, Player player) {
        player.removeInventoryItem(this.getId());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject info = super.toJSON();
        info.put("key", key);
        return info;
    }

    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(getId(), getPrefix(), getPosition(), isInteractable());
    }
}
