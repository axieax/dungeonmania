package dungeonmania.model.entities.collectables.potion;

import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Position;

public abstract class Potion extends Item {

    public Potion(String entityId, Position position) {
        super(entityId, position);
    }

    public abstract void consume(Player player);
}
