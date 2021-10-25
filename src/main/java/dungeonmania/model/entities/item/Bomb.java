package dungeonmania.model.entities.item;

import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;

public class Bomb extends Entity implements Collectable {

    public Bomb(String entityId, Position position) {
        super(entityId, position);
    }
}
