package dungeonmania.model.entities;

import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Position;
import org.json.JSONObject;

public abstract class DefenceEquipment extends Equipment {

    private double defenceMultiplier;
    private int bonusAttackDamage = 0;

    public DefenceEquipment(String prefix, double defenceMultiplier) {
        this(prefix, defenceMultiplier, null);
    }

    public DefenceEquipment(String prefix, double defenceMultiplier, Position position) {
        super(prefix, position);
        this.defenceMultiplier = defenceMultiplier;
    }

    public int getBonusAttackDamage() {
        return bonusAttackDamage;
    }

    public void setAttackDamage(int bonusAttackDamage) {
        this.bonusAttackDamage = bonusAttackDamage;
    }

    @Override
    public double useEquipment(Player player) {
        super.useEquipment(player);
        return defenceMultiplier;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject info = super.toJSON();
        info.put("defenceMultiplier", defenceMultiplier);
        info.put("bonusAttackDamage", bonusAttackDamage);
        return info;
    }
}
