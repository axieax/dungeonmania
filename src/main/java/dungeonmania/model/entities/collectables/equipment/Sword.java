package dungeonmania.model.entities.collectables.equipment;

import dungeonmania.util.Position;

public class Sword extends CollectableEquipment {

    public Sword(String entityId, Position position) {
        super(entityId, position);
    }
    
    /**
     * Each sword has a specific durability that dictates the
     * number of times it can be used before it deteriorates.
     * @return durability level
     */
    public int getDurability() {
        return 0;
    }

}
