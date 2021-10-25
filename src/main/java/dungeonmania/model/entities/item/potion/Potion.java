package dungeonmania.model.entities.item.potion;

import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.item.Collectable;
import dungeonmania.util.Position;

public class Potion extends Entity implements Collectable {

    public Potion(String entityId, Position position) {
        super(entityId, position);
    }
}
