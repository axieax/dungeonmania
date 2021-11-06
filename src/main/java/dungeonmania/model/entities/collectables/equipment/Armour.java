package dungeonmania.model.entities.collectables.equipment;

import dungeonmania.model.entities.DefenceEquipment;
import dungeonmania.model.entities.Equipment;
import dungeonmania.util.Position;

public class Armour extends Equipment implements DefenceEquipment {

    public final double MULTIPLIER = 0.5;

    public Armour() {
        super("armour", null);
    }

    public Armour(Position position) {
        super("armour", position);
    }
}
