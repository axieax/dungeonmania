package dungeonmania.model.entities.collectables.equipment;

import dungeonmania.model.entities.AttackEquipment;
import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.util.Position;

public class Anduril extends Equipment implements AttackEquipment {

    private final int ATTACK_DAMAGE = 50;

    public Anduril(Position position) {
        super("sword", position);
    }

    @Override
    public int getAttackDamage(MovingEntity entity) {
        return ATTACK_DAMAGE;
    }
}
