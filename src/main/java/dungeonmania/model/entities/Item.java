package dungeonmania.model.entities;

import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public abstract class Item extends Entity {

    private final int MAX_COLLECTABLE_LIMIT = Integer.MAX_VALUE;

    public Item(String id) {
        super(id, null);
    }

    public Item(String id, Position position) {
        super(id, position);
    }
    
    public ItemResponse getItemInfo() {
        return new ItemResponse(this.getId(), this.getClass().getSimpleName());
    }

}
