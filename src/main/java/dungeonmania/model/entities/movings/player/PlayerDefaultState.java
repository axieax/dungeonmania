package dungeonmania.model.entities.movings.player;

import dungeonmania.exceptions.PlayerDeadException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.collectables.TheOneRing;
import dungeonmania.model.entities.collectables.equipment.Anduril;
import dungeonmania.model.entities.collectables.equipment.Armour;
import dungeonmania.model.entities.movings.BribableEnemy;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.model.entities.statics.Consumable;
import java.util.List;
import java.util.Random;

public class PlayerDefaultState implements PlayerState {

    private Player player;

    public PlayerDefaultState(Player player) {
        this.player = player;
    }

    public void battle(Game game, Enemy opponent) throws PlayerDeadException {
        // Do not battle opponent if it is an ally
        List<BribableEnemy> allies = player.getAllies();
        if (allies.contains(opponent)) return;

        // Notify the observers that the player is in battle
        player.setInBattle(true);
        player.setCurrentBattleOpponent(opponent);
        player.notifyObservers();

        // Battles only last a single tick
        while (player.getHealth() > 0 && opponent.getHealth() > 0) {
            // Equipment is "worn" (durability reduced) when getting applying attack/defence
            int playerAttackDamage = player.getTotalAttackDamage(opponent);
            int opponentAttackDamage = player.applyDefenceToOpponentAttack(opponent);

            int originalHealth = player.getHealth();
            player.setHealth(originalHealth - ((opponent.getHealth() * opponentAttackDamage) / 10));
            opponent.reduceHealthFromBattle(((originalHealth * playerAttackDamage) / 5));

            // Check if player is dead
            if (!player.isAlive()) {
                Item item = player.findInventoryItem("one_ring");
                // Use one ring if it is in inventory (Restore back to full health)
                if (item != null) ((Consumable) item).consume(game, player);
            }
        }

        // Remove player and/or opponent if they are not alive
        if (!opponent.isAlive()) {
            game.removeEntity(opponent);
            player.removeAlly(opponent);
        }
        if (!player.isAlive()) {
            game.removeEntity(player);
            throw new PlayerDeadException("Player has died");
        }

        // If a player wins a battle, there is a small chance that items could be
        // dropped and be added to the character's inventory.

        // An opponent can have the potential to drop multiple items.
        Random random = new Random();
        if (random.nextDouble() <= opponent.getArmourDropRate()) {
            player.addInventoryItem(new Armour());
        }

        // only one rare collectable item drops
        boolean dropRing = random.nextBoolean();
        if (dropRing && random.nextDouble() <= TheOneRing.DROP_RATE) {
            player.addInventoryItem(new TheOneRing());
        } else if (!dropRing && random.nextDouble() <= Anduril.DROP_RATE) {
            player.addInventoryItem(new Anduril());
        }

        player.setInBattle(false);
        player.setCurrentBattleOpponent(null);
    }

    public int ticksLeft() {
        return 0;
    }

    public void updateState(Player player) {}

    public void setTicksLeft(int ticks) {}
}
