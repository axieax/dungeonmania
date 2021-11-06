package dungeonmania.model.entities.collectables.equipment;

import dungeonmania.model.entities.AttackEquipment;
import dungeonmania.util.Position;

public class Anduril extends AttackEquipment {

    private static final int ATTACK_DAMAGE = 50;
    private static final int HIT_RATE = 1;

    public Anduril(Position position) {
        super("anduril", ATTACK_DAMAGE, HIT_RATE, position);
    }

}
