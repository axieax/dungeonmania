package dungeonmania.model.entities.buildables;

import dungeonmania.model.entities.Equipment;
import dungeonmania.util.Position;

public abstract class BuildableEquipment extends Equipment {

    public BuildableEquipment(String entityId, Position position) {
        super(entityId, position);
    }
    
    public abstract boolean isBuildable();
}
