package dungeonmania.model.entities.buildables;

import dungeonmania.model.entities.Equipment;
import dungeonmania.util.Position;

public abstract class BuildableEquipment extends Equipment {

    public BuildableEquipment(String prefix, Position position) {
        super(prefix, position);
    }
    
}
