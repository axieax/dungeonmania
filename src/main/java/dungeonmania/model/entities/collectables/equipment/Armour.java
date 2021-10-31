package dungeonmania.model.entities.collectables.equipment;

import dungeonmania.model.entities.DefenceEquipment;
import dungeonmania.util.Position;

public class Armour extends CollectableEquipment implements DefenceEquipment {

    public Armour(Position position) {
        super("armour", position);
    }

    @Override
    public int setDefenceMultiplier(int defendAmount) {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
