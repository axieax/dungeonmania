package dungeonmania.model.entities;

import dungeonmania.util.Position;

public abstract class DefenceEquipment extends Equipment {
    private double defenceMultiplier;

    public DefenceEquipment(String prefix, double defenceMultiplier) {
        this(prefix, defenceMultiplier, null);
    }

    public DefenceEquipment(String prefix, double defenceMultiplier, Position position) {
        super(prefix, position);
        this.defenceMultiplier = defenceMultiplier;
    }

    public double getDefenceMultiplier() {
        return defenceMultiplier;
    }
}
