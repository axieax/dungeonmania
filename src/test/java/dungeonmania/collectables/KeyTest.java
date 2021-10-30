package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.statics.Door;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class KeyTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Game game = new Game(3, 3);
        game.addEntity(new Key("key1", new Position(1, 1), 1));

        assertTrue(new Position(1, 1).equals(game.getEntity("key1").getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Game game = new Game(3, 3);

        String collectableId = "key1";
        Key item = new Key(collectableId, new Position(1, 1), 1);

        game.addEntity(item);

        Player player = new Player("player1", new Position(0, 1));
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(game.getEntity(collectableId) == null);
        assertTrue(player.getInventoryItem(collectableId).equals(item));
    }

    /**
     * Test that player can only carry one key.
     */
    @Test
    public void carryLimit() {
        Game game = new Game(3, 3);

        String collectableId1 = "key1";
        String collectableId2 = "key2";

        Key key1 = new Key(collectableId1, new Position(1, 1), 1);
        Key key2 = new Key(collectableId2, new Position(2, 1), 2);

        game.addEntity(key1);

        Player player = new Player("player1", new Position(0, 1)); 

        // Collect the first key
        player.move(game, Direction.RIGHT);
        assertTrue(new Position(1, 1).equals(player.getPosition()));       

        assertTrue(game.getEntity(collectableId1) == null);
        assertTrue(game.getEntity(collectableId2) == key2);
        assertTrue(player.getInventoryItem(collectableId1).equals(key1));

        // Attempt to collect the second key
        player.move(game, Direction.RIGHT);
        assertTrue(new Position(2, 1).equals(player.getPosition()));       

        // Check if the second key is still in the game
        assertTrue(game.getEntity(collectableId2) == key2);
        assertTrue(player.getInventoryItem(collectableId2) == null);
    }

    /**
     * Test if the key disappears after it has been used to its corresponding doors.
     */
    @Test
    public void keyDisappears() {
        Game game = new Game(3, 3);
        Door door = new Door("door1", new Position(1, 1), 1);
        game.addEntity(door);

        Player player = new Player("player1", new Position(1, 3));

        String collectableId = "key1";
        Key key = new Key(collectableId, new Position(1, 2), 1);
        
        // Player moves onto the position of the key and will pick it up
        player.move(game, Direction.UP);
        assertTrue(player.getInventoryItem(collectableId).equals(key));

        // Player opens the door
        player.move(game, Direction.UP);
        assertTrue(new Position(1, 1).equals(player.getPosition()));
        assertTrue(door.isOpen());
    }
}