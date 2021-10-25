package dungeonmania.model.entities.item;

import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;

public class Arrows extends Entity implements Collectable {

    public Arrows(String entityId, Position position) {
        super(entityId, position);
    }
}
