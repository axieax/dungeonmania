package dungeonmania.model.entities.item;

import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;

public class Key extends Entity implements Collectable {
    public static final int MAX_COLLECTABLE_LIMIT = 1;

    private int key;

    public Key(String entityId, Position position, int key) {
        super(entityId, position);
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
