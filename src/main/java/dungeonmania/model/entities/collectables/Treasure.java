package dungeonmania.model.entities.collectables;

import dungeonmania.model.entities.Item;
import dungeonmania.util.Position;

public class Treasure extends Item {

    public Treasure(Position position) {
        super("treasure", position);
    }

}
