package dungeonmania.model.entities.collectables.potion;

import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.statics.Consumable;
import dungeonmania.util.Position;

public abstract class Potion extends Item implements Consumable {
    public Potion(String entityId, Position position) {
        super(entityId, position);
    }
}
