package dungeonmania.model.entities.movings.player;

import dungeonmania.model.Game;
import dungeonmania.model.entities.AttackEquipment;
import dungeonmania.model.entities.DefenceEquipment;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.collectables.TheOneRing;
import dungeonmania.model.entities.collectables.equipment.Armour;
import dungeonmania.model.entities.movings.BribableEnemy;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.ZombieToast;
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

            if (!player.isAlive()) {
                Item item = player.findInventoryItem("one_ring");
                if (item != null && (item instanceof Consumable)) {
                    // Use one ring if it is in inventory
                    ((Consumable) item).consume(game, player);
                }
            }
        }

        // Remove the entity from the game if dead after battle.
        if (player.isAlive()) game.removeEntity(opponent); else game.removeEntity(player);

        // An opponent can have the potential to drop multiple items.

        /**
         * TODO: Instead of making a random chance of dropping an item after battle,
         * perhaps the opponent could have their own inventory instead.
         *
         * i.e. A ZombieToast with an armour can apply the defensive effects.
         * Note: Possible bonus feature for Milestone 3.
         */
        Random armourRand = new Random();
        if (
            (opponent instanceof Mercenary &&
            armourRand.nextDouble() <= ((Mercenary) opponent).ARMOUR_DROP_RATE) ||
            (opponent instanceof ZombieToast &&
            armourRand.nextDouble() <= ((ZombieToast) opponent).ARMOUR_DROP_RATE)
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
