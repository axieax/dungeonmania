package dungeonmania.model.entities.collectables.equipment;

import dungeonmania.model.entities.DefenceEquipment;
import dungeonmania.util.Position;

public class Armour extends CollectableEquipment implements DefenceEquipment {

    public final double MULTIPLIER = 0.5;

    public Armour() {
        super("armour", null);
    }

    public Armour(Position position) {
        super("armour", position);
    }
}
