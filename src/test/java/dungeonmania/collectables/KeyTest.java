package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Door;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class KeyTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Key key = new Key(new Position(1, 1), 1);
        game.addEntity(key);

        assertTrue(new Position(1, 1).equals(game.getEntity(key.getId()).getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Key key = new Key(new Position(1, 1), 1);
        game.addEntity(key);

        Player player = new Player(new Position(0, 1), mode.initialHealth());
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));

        assertTrue(game.getEntity(key.getId()) == null);
        assertTrue(player.getInventoryItem(key.getId()).equals(key));
    }

    /**
     * Test that player can only carry one key.
     */
    @Test
    public void carryLimit() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Key key1 = new Key(new Position(1, 1), 1);
        Key key2 = new Key(new Position(2, 1), 2);

        game.addEntity(key1);
        game.addEntity(key2);

        Player player = new Player(new Position(0, 1), mode.initialHealth());

        // Collect the first key
        player.move(game, Direction.RIGHT);
        assertTrue(new Position(1, 1).equals(player.getPosition()));

        assertTrue(game.getEntity(key1.getId()) == null);
        assertTrue(game.getEntity(key2.getId()) == key2);
        assertTrue(player.getInventoryItem(key1.getId()).equals(key1));

        // Attempt to collect the second key
        player.move(game, Direction.RIGHT);
        assertTrue(new Position(2, 1).equals(player.getPosition()));

        // Check if the second key is still in the game
        assertTrue(game.getEntity(key2.getId()) == key2);
        assertTrue(player.getInventoryItem(key2.getId()) == null);
    }

    /**
     * Test if the key disappears after it has been used to its corresponding doors.
     */
    @Test
    public void keyDisappears() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Door door = new Door(new Position(1, 1), 1);
        game.addEntity(door);

        Player player = new Player(new Position(1, 3), mode.initialHealth());
        game.addEntity(player);

        Key key = new Key(new Position(1, 2), 1);
        game.addEntity(key);

        // Player moves onto the position of the key and will pick it up
        player.move(game, Direction.UP);
        assertTrue(player.getInventoryItem(key.getId()).equals(key));

        // Player opens the door
        player.move(game, Direction.UP);
        assertTrue(new Position(1, 1).equals(player.getPosition()));
        assertTrue(door.isOpen());
    }

    /**
     * Test that the key cannot be picked up by entities other than the player.
     */
    @Test
    public void keyMercenaryCannotPickup() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        Mercenary mercenary = new Mercenary(new Position(1, 3), mode.damageMultiplier(), player);
        game.addEntity(mercenary);

        Key key = new Key(new Position(1, 2), 1);
        game.addEntity(key);

        // Mercenary moves in the direction of the player (upwards)
        // which will pass the location of the key, but cannot pick it up
        mercenary.move(game);
        assertTrue(game.getEntities(new Position(1, 2)).contains(mercenary));
        assertTrue(game.getEntities(new Position(1, 2)).contains(key));
    }
}
