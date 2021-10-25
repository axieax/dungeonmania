package dungeonmania.model.entities.item;

import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;

public class Key extends Entity implements Collectable {
    public static final int MAX_COLLECTABLE_LIMIT = 1;

    private String doorId;

    public Key(String entityId, Position position, String doorId) {
        super(entityId, position);
        this.doorId = doorId;
    }

    public String getDoorId() {
        return doorId;
    }

    public void setDoorId(String doorId) {
        this.doorId = doorId;
    }
}
