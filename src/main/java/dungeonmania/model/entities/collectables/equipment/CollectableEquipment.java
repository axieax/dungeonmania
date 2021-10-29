package dungeonmania.model.entities.collectables.equipment;

import dungeonmania.model.entities.Equipment;
import dungeonmania.util.Position;

public abstract class CollectableEquipment extends Equipment {

    public CollectableEquipment(String entityId, Position position) {
        super(entityId, position);
    }
}
