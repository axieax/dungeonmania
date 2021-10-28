package dungeonmania.model.entities;

import dungeonmania.util.Position;

public abstract class Equipment extends Item {

    private int durability = 5;

    public Equipment(String entityId, Position position) {
        super(entityId, position);
        //TODO Auto-generated constructor stub
    }

    /**
     * @return durability of the equipment
     */
    public int getDurability() {
        return durability;
    }

    /**
     * @param durability
     * Reduces the durability of the equipment
     */
    public void useEquipment() {
        this.durability--;
    }
}
