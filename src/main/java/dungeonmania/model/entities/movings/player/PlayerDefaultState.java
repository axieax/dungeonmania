package dungeonmania.model.entities.movings.player;

import dungeonmania.model.Game;
import dungeonmania.model.entities.AttackEquipment;
import dungeonmania.model.entities.DefenceEquipment;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.collectables.TheOneRing;
import dungeonmania.model.entities.collectables.equipment.Armour;
import dungeonmania.model.entities.movings.BribableEnemy;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.statics.Consumable;
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
        List<BribableEnemy> allies = player.getAllies();
        if (allies.contains(opponent)) {
            return;
        }

        // Battles only last a single tick
        while (player.getHealth() > 0 && opponent.getHealth() > 0) {
            int playerAttackDamage = player.getTotalAttackDamage(opponent);
            int opponentAttackDamage = player.applyDefenceToOpponentAttack(
                opponent.getBaseAttackDamage()
            );
            int originalHealth = player.getHealth();

            // Use defensive equipment
            List<DefenceEquipment> defenseEquipments = player.getDefenceEquipmentList();
            defenseEquipments.forEach(defenseEquipment -> defenseEquipment.useEquipment(player));
            player.setHealth(originalHealth - ((opponent.getHealth() * opponentAttackDamage) / 10));

            /**
             * TODO: Instead of using all attack equipment for each battle, only use them if the default
             * attack of the character is not enough to kill the opponent.
             *
             * Something to consider for Milestone 3.
             */

            // Use attack equipment
            List<AttackEquipment> attackEquipments = player.getAttackEquipmentList();
            while (!attackEquipments.isEmpty()) {
                AttackEquipment currEquipment = attackEquipments.get(0);
                currEquipment.useEquipment(player);
                attackEquipments.remove(currEquipment);
            }
            
            opponent.reduceHealthFromBattle(((originalHealth * playerAttackDamage) / 5));

            // Check if player is dead
            if (!player.isAlive()) {
                Item item = player.findInventoryItem("one_ring");
                if (item != null && (item instanceof Consumable)) {
                    // Use one ring if it is in inventory
                    ((Consumable) item).consume(game, player);
                }
            }
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
        Random armourRand = new Random();
        if (
            opponent instanceof Enemy &&
            armourRand.nextDouble() <= ((Enemy) opponent).getArmourDropRate()
        ) {
            player.addInventoryItem(new Armour());
        }
        Random oneRingRand = new Random();
        if (
            opponent instanceof Enemy &&
            oneRingRand.nextDouble() <= ((Enemy) opponent).THE_ONE_RING_DROP_RATE
        ) {
            player.addInventoryItem(new TheOneRing());
        }
    }

    public int ticksLeft() {
        return 0;
    }

    @Override
    public void updateState(Player player) {}
}