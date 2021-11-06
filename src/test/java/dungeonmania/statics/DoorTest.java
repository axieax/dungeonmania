package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Door;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class DoorTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Door door = new Door(new Position(1, 1), 1);
        game.addEntity(door);

        assertTrue(new Position(1, 1).equals(game.getEntity(door.getId()).getPosition()));
    }

    /**
     * If character does not have key, check if the door blocks movement.
     */
    @Test
    public void doorBlockWithoutKey() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Door door = new Door(new Position(1, 1), 1);
        game.addEntity(door);

        Player player = new Player(new Position(1, 2));
        game.addEntity(player);

        player.move(game, Direction.UP);

        assertTrue(new Position(1, 2).equals(player.getPosition()));
        assertFalse(door.isOpen());
    }

    /**
     * If the player has the correct key, test that the door will unlock.
    */
    @Test
    public void doorUnlockWithKey() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Door door = new Door(new Position(1, 1), 1);
        game.addEntity(door);

        Player player = new Player(new Position(1, 3));

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
     * If the player has the wrong key, test that the door will remain locked.
     */
    @Test
    public void doorLockWithIncorrectKey() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Door door = new Door(new Position(1, 1), 1);
        game.addEntity(door);

        Player player = new Player(new Position(1, 3));
        game.addEntity(player);

        Key key = new Key(new Position(1, 2), 2);
        game.addEntity(key);

        // Player moves onto the position of the key and will pick it up
        player.move(game, Direction.UP);
        assertTrue(player.getInventoryItem(key.getId()).equals(key));

        // Player cannot open the door as it is not the correct key
        player.move(game, Direction.UP);
        assertFalse(door.isOpen());
    }

    /**
     * If the player has the correct key, test that the door will unlock.
    */
    @Test
    public void multipleDoorUnlocking() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Door door1 = new Door(new Position(1, 1), 1);
        game.addEntity(door1);
        Door door2 = new Door(new Position(2, 1), 2);
        game.addEntity(door2);

        Key key1 = new Key(new Position(2, 3), 1);
        game.addEntity(key1);
        Key key2 = new Key(new Position(1, 3), 2);
        game.addEntity(key2);

        Player player = new Player(new Position(3, 3));
        game.addEntity(player);

        // __  __  __  __
        // __  D1  D2  __
        // __  __  __  __
        // __  K2  K1  P
        
        // Player moves onto the position of key1 and will pick it up
        player.move(game, Direction.LEFT);
        assertTrue(player.getInventoryItem(key1.getId()).equals(key1));

        // Attempt to unlock door2, fails so player stays in the same position
        player.move(game, Direction.UP);
        player.move(game, Direction.UP);
        assertFalse(door2.isOpen());
        assertTrue(new Position(2, 2).equals(player.getPosition()));

        // Attempt to unlock door1, succeeds so player moves to the door1 position
        player.move(game, Direction.LEFT);
        player.move(game, Direction.UP);
        assertTrue(door1.isOpen());
        assertTrue(new Position(1, 1).equals(player.getPosition()));

        // Player moves onto the position of key2 and will pick it up
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);
        assertTrue(player.getInventoryItem(key2.getId()).equals(key2));

        // Attempt to unlock door2, succeeds so player moves to the door2 position
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.UP);
        player.move(game, Direction.UP);
        assertTrue(door2.isOpen());
        assertTrue(new Position(2, 1).equals(player.getPosition()));
    }
}
