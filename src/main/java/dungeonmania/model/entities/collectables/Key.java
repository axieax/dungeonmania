package dungeonmania.model.entities.collectables;

import dungeonmania.model.entities.Item;
import dungeonmania.util.Position;

public class Key extends Item {
    private final int MAX_COLLECTABLE_LIMIT = 1;

    private int key;

    public Key(String entityId, Position position, int key) {
        super(entityId, position);
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
