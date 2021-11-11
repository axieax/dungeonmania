package dungeonmania.model.entities.movings.player;

import dungeonmania.exceptions.PlayerDeadException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.collectables.TheOneRing;
import dungeonmania.model.entities.collectables.equipment.Armour;
import dungeonmania.model.entities.movings.BribableEnemy;
import dungeonmania.model.entities.movings.Enemy;
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
    public void battle(Game game, Enemy opponent) throws PlayerDeadException {
        // Do not battle opponent if it is an ally
        List<BribableEnemy> allies = player.getAllies();
        if (allies.contains(opponent)) {
            return;
        }

        // Notify the observers that the player is in battle
        player.setInBattle(true);
        player.setCurrentBattleOpponent(opponent);
        player.notifyObservers();

        // Battles only last a single tick
        while (player.getHealth() > 0 && opponent.getHealth() > 0) {
            // equipment is "worn" (durability reduced) when getting applying attack/defence
            int playerAttackDamage = player.getTotalAttackDamage(opponent);
            int opponentAttackDamage = player.applyDefenceToOpponentAttack(opponent);

            int originalHealth = player.getHealth();
            player.setHealth(originalHealth - ((opponent.getHealth() * opponentAttackDamage) / 10));
            opponent.reduceHealthFromBattle(((originalHealth * playerAttackDamage) / 5));

            /**
             * TODO: Instead of using all attack equipment for each battle, only use them if the default
             * attack of the character is not enough to kill the opponent.
             *
             * Something to consider for Milestone 3.
             */

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

        // Remove the entity that is dead (there must be one) after battle from the game.
        if (player.isAlive()) {
            player.removeAlly(opponent);
            game.removeEntity(opponent);
        }
        if (opponent.isAlive()) {
            game.removeEntity(player);
            throw new PlayerDeadException("Player has died");
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

        player.setInBattle(false);
        player.setCurrentBattleOpponent(null);
    }

    public int ticksLeft() {
        return 0;
    }

    @Override
    public void updateState(Player player) {}
}
