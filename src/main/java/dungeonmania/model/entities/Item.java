package dungeonmania.model.entities;

import org.json.JSONObject;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.potion.Potion;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public abstract class Item extends Entity {

    public Item(String prefix, Position position) {
        super(prefix, position, false, true);
    }
    
    public Item(String prefix) {
        this(prefix, null);
    }

    public ItemResponse getItemInfo() {
        return new ItemResponse(this.getId(), this.getClass().getSimpleName());
    }

    /**
     * If the Player interacts with the Item, collect the item and put it in the
     * inventory.
     */
    @Override
    public void interact(Game game, MovingEntity character) {
        if (character instanceof Player) {
            ((Player) character).collect(this);
            game.removeEntity(this);
        }
    }
}
