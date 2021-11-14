package dungeonmania.model.entities;

import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Position;
import org.json.JSONObject;

public abstract class Equipment extends Item {

    private int durability = 5;

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
     * Reduces the durability of the equipment and returns the attack/defence amount
     */
    public double useEquipment(Player player, Entity enemy) {
        this.durability--;
        if (this.durability == 0) player.removeInventoryItem(this.getId());
        return 0;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject info = super.toJSON();
        info.put("durability", durability);
        return info;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }
}
