package dungeonmania.model.entities;

import org.json.JSONObject;

import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Position;

public abstract class Equipment extends Item {

    private int durability = 5;
    public final double MULTIPLIER = 1;

    public Equipment(String prefix, Position position) {
        super(prefix, position);
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
    public void useEquipment(Player player) {
        this.durability--;
        if (this.durability == 0) player.removeInventoryItem(this.getId());
    }

    public double getMultiplier() {
        return this.MULTIPLIER;
    }

    public JSONObject toJSON() {
        JSONObject info = super.toJSON();
        info.put ("durability", durability);
        return info;
    }

    public void setDurability (int durability) {
        this.durability = durability;
    }
}
