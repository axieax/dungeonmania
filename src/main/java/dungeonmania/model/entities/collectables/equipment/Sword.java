package dungeonmania.model.entities.collectables.equipment;

import dungeonmania.model.entities.AttackEquipment;
import dungeonmania.util.Position;

public class Sword extends AttackEquipment {

    private static final int ATTACK_DAMAGE = 20;
    private static final int HIT_RATE = 1;

    public Sword(Position position) {
        super("sword", ATTACK_DAMAGE, HIT_RATE, position);
    }
}
