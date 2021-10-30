package dungeonmania.model.entities.collectables.equipment;

import dungeonmania.util.Position;

public class Armour extends CollectableEquipment {

    public Armour(String entityId, Position position) {
        super(entityId, position);
    }
    
    /**
     * Each armour has a specific durability that dictates the number of 
     * times it can be used before it deteriorates.
     * @return durability level
     */
    public int getDurability() {
        return 0;
    }
}
