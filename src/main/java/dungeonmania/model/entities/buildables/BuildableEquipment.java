package dungeonmania.model.entities.buildables;

import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.movings.Inventory;
import dungeonmania.util.Position;

public abstract class BuildableEquipment extends Equipment {

    
    public BuildableEquipment(String prefix, Position position) {
        super(prefix, position);
    }
    
    public abstract boolean isBuildable(Inventory inventory);

    public abstract void craft(Inventory inventory);

    public abstract BuildableEquipment clone();
    
}
