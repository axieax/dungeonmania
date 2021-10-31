package dungeonmania.model.entities.collectables.equipment;

import dungeonmania.model.entities.AttackEquipment;
import dungeonmania.util.Position;

public class Sword extends CollectableEquipment implements AttackEquipment {

    public Sword(Position position) {
        super("sword", position);
    }

    @Override
    public int setAttackMultiplier(int attackDamage) {
        // TODO Auto-generated method stub
        return 0;
    }

}
