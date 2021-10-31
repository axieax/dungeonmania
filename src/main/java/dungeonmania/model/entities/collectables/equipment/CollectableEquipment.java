package dungeonmania.model.entities.collectables.equipment;

import dungeonmania.model.entities.Equipment;
import dungeonmania.util.Position;

public abstract class CollectableEquipment extends Equipment {

    public CollectableEquipment(String prefix, Position position) {
        super(prefix, position);
    }
}
