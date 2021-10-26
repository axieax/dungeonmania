package dungeonmania.model.entities.statics;

import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;

public class Door extends Entity {

    private boolean isOpen = false;
    private int key;

    public Door(String entityId, Position position, int key) {
        super(entityId, position);
        this.key = key;
    }

    public boolean isOpen() {
        return this.isOpen;
    }
}
