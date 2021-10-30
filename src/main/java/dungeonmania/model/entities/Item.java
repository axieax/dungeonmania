package dungeonmania.model.entities;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public abstract class Item extends Entity {

    public Item(String prefix, Position position) {
        super(prefix, position, false, true);
    }

    public ItemResponse getItemInfo() {
        return new ItemResponse(this.getId(), this.getClass().getSimpleName());
    }

    /**
     * If the Player interacts with the Item, collect the item and put it in the
     * inventory.
     */
    @Override
    public void interact(Dungeon dungeon, MovingEntity character) {
        if (character instanceof Player) {
            ((Player) character).collect(this);
            dungeon.removeEntity(this);
        }
    }
}
