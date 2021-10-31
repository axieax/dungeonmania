package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.collectables.TheOneRing;
import dungeonmania.model.entities.collectables.equipment.Armour;
import java.util.List;
import java.util.Random;

public class PlayerDefaultState implements PlayerState {

    private Player player;
    public static final int ARMOUR_DROP_CHANCE = 10;

    public PlayerDefaultState(Player player) {
        this.player = player;
    }

    @Override
    public void battle(Game game, MovingEntity opponent) {
        // do not battle opponent it it is an ally
        List<MovingEntity> allies = player.getAllies();
        if (allies.contains(opponent)) {
            return;
        }

        // battles only last a single tick
        while (player.getHealth() >= 0 && opponent.getHealth() >= 0) {

            int playerAttackDamage = player.getTotalAttackDamage();
            int opponentAttackDamage = player.applyDefenceToOpponentAttack(opponent.getBaseAttackDamage());
            
            // use durability of defensive equipments
            List<Equipment> defenseEquipments = player.getDefenceEquipmentList();
            defenseEquipments.forEach(defenseEquipment -> defenseEquipment.useEquipment(player));
            player.setHealth(player.getHealth() - ((opponent.getHealth() * playerAttackDamage) / 10));
            
            // Use all attack equipments only when it is not enough to kill the opponent
            List<Equipment> attackEquipments = player.getAttackEquipmentList();
            while (opponent.getHealth() <= 0 && !attackEquipments.isEmpty()) {
                Equipment currEquipment = attackEquipments.get(0);
                currEquipment.useEquipment(player);
                attackEquipments.remove(currEquipment);
            }
            opponent.setHealth(opponent.getHealth() - ((player.getHealth() * opponentAttackDamage) / 10));
        }

        // remove the entity from the game if dead after battle.
        if (player.isAlive()) game.removeEntity(opponent);
        else game.removeEntity(player);

        // If a player wins a battle, there is a small chance that items could be 
        // dropped and be added to the character's inventory.

        // An opponent can have the potential to drop multiple items.

        /**
         * TODO: Instead of making a random chance of dropping an item after battle, 
         * perhaps the opponent could have their own inventory instead.
         * 
         * i.e. A ZombieToast with an armour can apply the defensive effects.
         * Note: Possible bonus feature for milestone 3.
         */
        Random armourRand = new Random();
        if (armourRand.nextDouble() <= opponent.ARMOUR_DROP_RATE) {
            player.addInventoryItem(new Armour());
        }
        Random oneRingRand = new Random();
        if (oneRingRand.nextDouble() <= opponent.THE_ONE_RING_DROP_RATE) {
            player.addInventoryItem(new TheOneRing());
        }
    }

    @Override
    public void updateState(Player player) {}
}
