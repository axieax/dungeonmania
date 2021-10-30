package dungeonmania.model.entities.buildables;

import java.util.List;

import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.Item;
import dungeonmania.util.Position;

public abstract class BuildableEquipment extends Equipment {

    public BuildableEquipment(String entityId, Position position) {
        super(entityId, position);
    }
    
}
