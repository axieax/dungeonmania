package dungeonmania.model.entities.statics;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;

public class FloorSwitch extends Entity {

    public FloorSwitch(String entityId, Position position) {
        super(entityId, position);
        //TODO Auto-generated constructor stub
    }

    public boolean isTriggered(Dungeon dungeon) {
        return false;
    }
}