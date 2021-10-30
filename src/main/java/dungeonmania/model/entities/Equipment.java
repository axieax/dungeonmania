package dungeonmania.model.entities;

import dungeonmania.util.Position;

public abstract class Equipment extends Item {

    private int durability = 5;

    public Equipment(Position position) {
        super(position);
    }

    /**
     * Each equipment has a specific durability that dictates the
     * number of times it can be used before it deteriorates.
     * @return durability level of the equipment
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
