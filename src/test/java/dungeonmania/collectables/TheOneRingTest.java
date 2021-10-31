package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.TheOneRing;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class TheOneRingTest {

    /**
     * Test whether the entity instance has been created with the correct positions
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
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
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
        fail();
    }

    /**
     * Test TheOneRing drop rate after winning a battle.
     */
    @Test
    public void dropRateTest() {
        fail();
    }
}