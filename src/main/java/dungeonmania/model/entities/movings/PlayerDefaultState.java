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
        // Do not battle opponent if it is an ally
        List<MovingEntity> allies = player.getAllies();
        if (allies.contains(opponent)) {
            return;
        }

        // Battles only last a single tick
        while (player.getHealth() > 0 && opponent.getHealth() > 0) {

            int playerAttackDamage = player.getTotalAttackDamage(opponent);
            int opponentAttackDamage = player.applyDefenceToOpponentAttack(opponent.getBaseAttackDamage());
            int originalHealth = player.getHealth();
            
            // Use defensive equipment
            List<Equipment> defenseEquipments = player.getDefenceEquipmentList();
            defenseEquipments.forEach(defenseEquipment -> defenseEquipment.useEquipment(player));
            player.setHealth(originalHealth - ((opponent.getHealth() * opponentAttackDamage) / 10));
            
            /**
             * TODO: Instead of using all attack equipment for each battle, only use them if the default
             * attack of the character is not enough to kill the opponent.
             * 
             * Something to consider for Milestone 3.
             */

            // Use attack equipment
            List<Equipment> attackEquipments = player.getAttackEquipmentList();
            while (!attackEquipments.isEmpty()) {
                Equipment currEquipment = attackEquipments.get(0);
                currEquipment.useEquipment(player);
                attackEquipments.remove(currEquipment);
            }
            opponent.setHealth(opponent.getHealth() - ((originalHealth * playerAttackDamage) / 5));
        }

        // Remove the entity from the game if dead after battle.
        if (player.isAlive()) {
            player.removeAlly(opponent);
            game.removeEntity(opponent);
        } else {
            game.removeEntity(player);    
        }

        // If a player wins a battle, there is a small chance that items could be 
        // dropped and be added to the character's inventory.

        // An opponent can have the potential to drop multiple items.

        /**
         * TODO: Instead of making a random chance of dropping an item after battle, 
         * perhaps the opponent could have their own inventory instead.
         * 
         * i.e. A ZombieToast with an armour can apply the defensive effects.
         * Note: Possible bonus feature for Milestone 3.
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

    public int ticksLeft() {
        return 0;
    }

    @Override
    public void updateState(Player player) {}
}
