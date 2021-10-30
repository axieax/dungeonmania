package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.statics.Door;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class DoorTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new Door("door1", new Position(1, 1), 1));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("door1").getPosition()));
    }

    /**
     * If character does not have key, check if the door blocks movement.
     */
    @Test
    public void doorBlockWithoutKey() {
        Dungeon dungeon = new Dungeon(3, 3);
        Door door = new Door("door1", new Position(1, 1), 1);
        dungeon.addEntity(door);

        Player player = new Player("player1", new Position(1, 2));

        player.move(Direction.UP);

        assertTrue(new Position(1, 2).equals(player.getPosition()));
        assertFalse(door.isOpen());
    }

    /**
     * If the player has the correct key, test that the door will unlock.
    */
    @Test
    public void doorUnlockWithKey() {
        Dungeon dungeon = new Dungeon(3, 3);
        Door door = new Door("door1", new Position(1, 1), 1);
        dungeon.addEntity(door);

        Player player = new Player("player1", new Position(1, 3));

        String collectableId = "key1";
        Key key = new Key(collectableId, new Position(1, 2), 1);
        
        // Player moves onto the position of the key and will pick it up
        player.move(Direction.UP);
        assertTrue(player.getItem(collectableId).equals(key));

        // Player opens the door
        player.move(Direction.UP);
        assertTrue(new Position(1, 1).equals(player.getPosition()));
        assertTrue(door.isOpen());
    }

    /**
     * If the player has the wrong key, test that the door will remain locked.
     */
    @Test
    public void doorLockWithIncorrectKey() {
        Dungeon dungeon = new Dungeon(3, 3);
        Door door = new Door("door1", new Position(1, 1), 1);
        dungeon.addEntity(door);

        Player player = new Player("player1", new Position(1, 3));

        String collectableId = "key1";
        Key key = new Key(collectableId, new Position(1, 2), 2);
        
        // Player moves onto the position of the key and will pick it up
        player.move(Direction.UP);
        assertTrue(player.getItem(collectableId).equals(key));

        // Player cannot open the door as it is not the correct key
        player.move(Direction.UP);
        assertTrue(new Position(1, 1).equals(player.getPosition()));
        assertFalse(door.isOpen());
    }

    /**
     * If the player has the correct key, test that the door will unlock.
    */
    @Test
    public void multipleDoorUnlocking() {
        Dungeon dungeon = new Dungeon(3, 3);
        Door door1 = new Door("door1", new Position(1, 1), 1);
        dungeon.addEntity(door1);
        Door door2 = new Door("door2", new Position(2, 1), 2);
        dungeon.addEntity(door2);

        String collectableId1 = "key1";
        Key key1 = new Key(collectableId1, new Position(1, 3), 1);

        String collectableId2 = "key2";
        Key key2 = new Key(collectableId2, new Position(2, 3), 2);

        Player player = new Player("player1", new Position(3, 3));

        // __  __  __  __
        // __  D1  D2  __
        // __  __  __  __
        // __  K2  K1  P
        
        // Player moves onto the position of key1 and will pick it up
        player.move(Direction.LEFT);
        assertTrue(player.getItem(collectableId1).equals(key1));

        // Attempt to unlock door2, fails so player stays in the same position
        player.move(Direction.UP);
        player.move(Direction.UP);
        assertFalse(door2.isOpen());
        assertTrue(new Position(2, 2).equals(player.getPosition()));

        // Attempt to unlock door1, succeeds so player moves to the door1 position
        player.move(Direction.LEFT);
        player.move(Direction.UP);
        assertTrue(door1.isOpen());
        assertTrue(new Position(1, 1).equals(player.getPosition()));

        // Player moves onto the position of key2 and will pick it up
        player.move(Direction.DOWN);
        player.move(Direction.DOWN);
        assertTrue(player.getItem(collectableId2).equals(key2));

        // Attempt to unlock door2, succeeds so player moves to the door2 position
        player.move(Direction.RIGHT);
        player.move(Direction.UP);
        player.move(Direction.UP);
        assertTrue(door2.isOpen());
        assertTrue(new Position(2, 1).equals(player.getPosition()));
        
    }
}
