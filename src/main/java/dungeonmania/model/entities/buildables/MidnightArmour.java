package dungeonmania.model.entities.buildables;

import dungeonmania.model.entities.DefenceEquipment;
import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.movings.player.Inventory;
import dungeonmania.util.Position;

public class MidnightArmour extends Equipment implements DefenceEquipment, Buildable {

    public MidnightArmour(String prefix, Position position) {
        super(prefix, position);
        //TODO Auto-generated constructor stub
    }

    @Override
    public boolean isBuildable(Inventory inventory) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void craft(Inventory inventory) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Buildable clone() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
