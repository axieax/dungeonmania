package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.TheOneRing;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Peaceful;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class TheOneRingTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        TheOneRing ring = new TheOneRing(new Position(1, 1));
        game.addEntity(ring);

        assertTrue(ring.getPosition().equals(new Position(1, 1)));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        TheOneRing ring = new TheOneRing(new Position(1, 1));
        game.addEntity(ring);

        Player player = new Player(new Position(0, 1), mode.initialHealth());
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(game.getEntity(ring.getId()) == null);
        assertTrue(player.getInventoryItem(ring.getId()).equals(ring));
    }

    /**
     * Test if TheOneRing restores the character's health.
     */
    @Test
    public void restoreHealthTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        TheOneRing ring = new TheOneRing(new Position(1, 1));
        game.addEntity(ring);

        Player player = new Player(new Position(0, 1), mode.initialHealth());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        assertTrue(player.getInventoryItem(ring.getId()).equals(ring));

        // After collecting the ring, the player then engages in a series of battles until it is 'defeated'.
        
        // Initially, the player's health is 100
        assertTrue(player.getHealth() == 100);

        // Spawn mercenaries next to the player - upon ticking, the mercenary would move to the player
        // Note that the during the first three ticks, the player is guaranteed have less than 100 health by the formula:
        // Player health - (50 * 5) / 10 --> e.g. 100 - 25 = 75.
        int previousHealth = 100;
        for (int i = 0; i < 3; i++) {
            Mercenary mercenary = new Mercenary(new Position(1, 2), mode.damageMultiplier(), player);
            game.addEntity(mercenary);
            game.tick(null, Direction.NONE);

            assertTrue(player.getHealth() < previousHealth);
            previousHealth = player.getHealth();
        }

        // Continue spawning mercenaries until the player is 'defeated'.
        // Since the player has the one ring, they should respawn to full health and continue battling.
        for (int i = 0; i < 20; i++) {
            Mercenary mercenary = new Mercenary(new Position(1, 2), mode.damageMultiplier(), player);
            game.addEntity(mercenary);
            game.tick(null, Direction.NONE);

            if (player.getHealth() > previousHealth) break;
            previousHealth = player.getHealth();
        }

        assertTrue(player.getHealth() > previousHealth);
    }

    /**
     * Test if TheOneRing respawns the character back to full health when the Player
     * is defeated as a confirmation.
     */
    @Test
    public void respawnFullHealthTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        TheOneRing ring = new TheOneRing(new Position(1, 1));
        game.addEntity(ring);

        Player player = new Player(new Position(0, 1), mode.initialHealth());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        assertTrue(player.getInventoryItem(ring.getId()).equals(ring));
        
        Mercenary mercenary = new Mercenary(new Position(1, 2), mode.damageMultiplier(), player);
        game.addEntity(mercenary);
        

        // Set the player's health to 25.
        // Normally, player's health upon battling the mercenary would be 25 - (50 * 5) / 10 = 0
        // However, since the player has the one ring, the player's health is restored to 100.
        player.setHealth(25);
        game.tick(null, Direction.NONE);
        assertTrue(player.getHealth() == 100);
    }

    /**
     * Test TheOneRing drop rate after winning a battle.
     */
    @Test
    public void dropRateTest() {
        Mode mode = new Peaceful();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);

        // Spawn mercenaries next to the player - upon ticking, the mercenary would move to the player
        // Since this is peaceful mode, the player's health will not change, so mercenaries will always die
        for (int i = 0; i < 50; i++) {
            Mercenary mercenary = new Mercenary(new Position(1, 2), mode.damageMultiplier(), player);
            game.addEntity(mercenary);
            game.tick(null, Direction.NONE);

            if (player.findInventoryItem("one_ring") != null) break;
        }

        assertTrue(player.getHealth() == 100);
        // Check that TheOneRing is in the player's inventory (there's a 0.9^50 = 0.5% otherwise)
        assertTrue(player.findInventoryItem("one_ring") != null);
    }
}