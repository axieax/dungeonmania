package dungeonmania.model.entities.buildables;

import dungeonmania.model.Game;
import dungeonmania.model.entities.DefenceEquipment;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.movings.player.Inventory;

public class MidnightArmour extends DefenceEquipment implements Buildable {

    private static final int ARMOUR_NEEDED = 1;
    private static final int SUNSTONE_NEEDED = 1;

    private static final double DEFENCE_MULTIPLIER = 0.3;
    public static final int BONUS_ATTACK_DAMAGE = 15;

    public MidnightArmour() {
        super("midnight_armour", DEFENCE_MULTIPLIER);
    }

    @Override
    public boolean isBuildable(Inventory inventory) {
        return (
            inventory.hasItemQuantity("armour", ARMOUR_NEEDED) && 
            inventory.hasItemQuantity("sun_stone", SUNSTONE_NEEDED)
        );
    }

    @Override
    public boolean checkNoZombies(Game game, Inventory inventory) {
        return game.getEntities()
            .stream()
            .filter(entity -> entity instanceof ZombieToast)
            .findAny()
            .isEmpty();
    }

    @Override
    public void craft(Inventory inventory) {
        inventory.removeItemQuantity("armour", ARMOUR_NEEDED);
        inventory.removeItemQuantity("sun_stone", SUNSTONE_NEEDED);
        inventory.addItem(new MidnightArmour());
    }

    @Override
    public double getBonusAttackDamage() {
        return BONUS_ATTACK_DAMAGE;
    }

    @Override
    public Buildable clone() {
        return new MidnightArmour();
    }   
}
