package dungeonmania.model.entities.buildables;

import dungeonmania.model.entities.DefenceEquipment;
import dungeonmania.model.entities.movings.player.Inventory;

public class MidnightArmour extends DefenceEquipment implements Buildable {

    private static final double DEFENCE_MULTIPLIER = 0.25;
    public static final int BONUS_ATTACK_DAMAGE = 25;

    public MidnightArmour() {
        super("midnight_armour", DEFENCE_MULTIPLIER);
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
