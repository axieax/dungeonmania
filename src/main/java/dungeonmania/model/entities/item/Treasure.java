package dungeonmania.model.entities.item;

import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;

public class Treasure extends Entity implements Collectable {

    public Treasure(String entityId, Position position) {
        super(entityId, position);
    }
}
