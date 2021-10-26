package dungeonmania.model.entities.collectables;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Item;
import dungeonmania.util.Position;

public class Bomb extends Item {

    public Bomb(String entityId, Position position) {
        super(entityId, position);
    }

    public void explode(Dungeon dungeon) {
        
    }
}
