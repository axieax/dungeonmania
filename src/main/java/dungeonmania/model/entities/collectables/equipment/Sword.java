package dungeonmania.model.entities.collectables.equipment;

import dungeonmania.model.entities.AttackEquipment;
import dungeonmania.util.Position;

public class Sword extends CollectableEquipment implements AttackEquipment {

    private final int ATTACK_DAMAGE = 50;

    public Sword(Position position) {
        super("sword", position);
    }

    @Override
    public int getAttackDamage() {
        return ATTACK_DAMAGE;
    }
}
