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

        Player player = new Player(new Position(0, 1));
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(game.getEntity(ring.getId()) == null);
        assertTrue(player.getInventoryItem(ring.getId()).equals(ring));
    }

    /**
     * Test if TheOneRing respawns the character back to full health when the Player
     * is defeated.
     */
    @Test
    public void respawnHealthTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        TheOneRing ring = new TheOneRing(new Position(1, 1));
        game.addEntity(ring);

        Player player = new Player(new Position(0, 1));
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        assertTrue(player.getInventoryItem(ring.getId()).equals(ring));

        // After collecting the ring, the player then engages in a series of battles until it is 'defeated'.
        
        // Initially, the player's health is 100
        assertTrue(player.getHealth() == 100);

        Mercenary mercenary1 = new Mercenary(new Position(1, 2), mode.damageMultiplier(), player);
        game.addEntity(mercenary1);
        player.move(game, Direction.DOWN);

        // Player's health is 100 - ((50 * 5) / 10) = 75
        assertTrue(player.getHealth() < 100);

        Mercenary mercenary2 = new Mercenary(new Position(1, 3), mode.damageMultiplier(), player);
        game.addEntity(mercenary2);
        player.move(game, Direction.DOWN);

        // Player's health is 75 - ((50 * 5) / 10) = 50
        assertTrue(player.getHealth() < 100);

        Mercenary mercenary3 = new Mercenary(new Position(2, 3), mode.damageMultiplier(), player);
        game.addEntity(mercenary3);
        player.move(game, Direction.RIGHT);

        // Player's health is 50 - ((50 * 5) / 10) = 25
        assertTrue(player.getHealth() < 100);

        Mercenary mercenary4 = new Mercenary(new Position(3, 3), mode.damageMultiplier(), player);
        game.addEntity(mercenary4);
        player.move(game, Direction.RIGHT);

        // Player's health is 25 - ((50 * 5) / 10) = 0
        // However, since TheOneRing is in the player's inventory, their health should be set back to 100
        assertTrue(player.getHealth() == 100);
    }

    /**
     * Test TheOneRing drop rate after winning a battle.
     */
    @Test
    public void dropRateTest() {
        Mode mode = new Peaceful();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);
        player.move(game, Direction.RIGHT);

        // Spawn mercenaries next to the player - upon ticking, the mercenary would move to the player
        // Since this is peaceful mode, the player's health will not change, so mercenaries will always die
        for (int i = 0; i < 50; i++) {
            Mercenary mercenary = new Mercenary(new Position(1, 2), mode.damageMultiplier(), player);
            game.addEntity(mercenary);
            game.tick(null, Direction.NONE);
        }

        assertTrue(player.getHealth() == 100);
        // Check that TheOneRing is in the player's inventory (there's a 0.9^50 = 0.5% otherwise)
        assertTrue(player.findInventoryItem("one_ring") != null);
    }
}