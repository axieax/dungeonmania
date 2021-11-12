package dungeonmania.model.entities;

import org.json.JSONObject;

import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Position;

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

    public double getBonusAttackDamage() {
        return bonusAttackDamage;
    }

    @Override
    public double useEquipment(Player player, Entity enemy) {
        super.useEquipment(player, enemy);
        if (enemy instanceof MovingEntity) {
            MovingEntity movingEnemy = (MovingEntity) enemy;
            return movingEnemy.getBaseAttackDamage() * defenceMultiplier;
        }

        return 0;
    }

    @Override 
    public JSONObject toJSON() {
        JSONObject info = super.toJSON();
        info.put ("defenceMultiplier", defenceMultiplier);
        info.put ("bonusAttackDamage", bonusAttackDamage);
        return info;
    }
}
