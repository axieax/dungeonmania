package dungeonmania.model.entities.item;

import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;

public class Wood extends Entity implements Collectable {

    public Wood(String entityId, Position position) {
        super(entityId, position);
    }
}
