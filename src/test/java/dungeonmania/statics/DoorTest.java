package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.characters.Player;
import dungeonmania.model.entities.staticEntity.Door;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class DoorTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new Door("door1", new Position(1, 1), 1));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("door1").getPosition()));
    }

    /**
     * If character does not have key, check if the door blocks movement
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
     * Test if the door is unlocked if the player has the correct key.
     */
    @Test
    public void doorUnlockWithKey() {
        fail();
    }

    /**
     * Test if the door is still locked if the player has the wrong key.
     */
    @Test
    public void doorLockWithIncorrectKey() {
        fail();
    }


}
