package dungeonmania.model.entities.collectables.equipment;

import dungeonmania.model.entities.DefenceEquipment;
import dungeonmania.util.Position;

public class Armour extends DefenceEquipment {

    private static final double DEFENCE_MULTIPLIER = 0.5;

    public Armour() {
        this(null);
    }

    public Armour(Position position) {
        super("armour", DEFENCE_MULTIPLIER, position);
    }
}
