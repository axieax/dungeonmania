package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.FloorSwitch;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class FloorSwitchTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new FloorSwitch("floorswitch1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("floorswitch1").getPosition()));
    }

    /**
     * Test if FloorSwitch behaves like an empty square.
     */
    @Test
    public void floorSwitchEmptySquare() {
        Dungeon dungeon = new Dungeon(5, 5);
        FloorSwitch floorSwitch = new FloorSwitch("floorswitch1", new Position(1, 1));
        dungeon.addEntity(floorSwitch);

        Player player = new Player("player1", new Position(0, 1));
        dungeon.addEntity(player);

        player.move(dungeon, Direction.RIGHT);
        assertTrue(new Position(1, 1).equals(player.getPosition()));
        assertTrue(new Position(1, 1).equals(floorSwitch.getPosition()));

        player.move(dungeon, Direction.UP);

        assertTrue(new Position(1, 0).equals(player.getPosition()));
        assertTrue(new Position(1, 1).equals(floorSwitch.getPosition()));
    }

    /**
     * Test if FloorSwitch is triggered by boulder and is untriggered.
     */
    @Test
    public void wallBlockEnemies() {
        Dungeon dungeon = new Dungeon(5, 5);
        FloorSwitch floorSwitch = new FloorSwitch("floorswitch1", new Position(2, 0));
        dungeon.addEntity(floorSwitch);

        Player player = new Player("player1", new Position(0, 0));
        dungeon.addEntity(player);

        Boulder boulder = new Boulder("boulder1", new Position(1, 0));
        dungeon.addEntity(boulder);

        // Move boulder on top of switch
        player.move(dungeon, Direction.RIGHT);
        assertTrue(new Position(1, 0).equals(player.getPosition()));
        assertTrue(new Position(2, 0).equals(floorSwitch.getPosition()));
        assertTrue(new Position(2, 0).equals(boulder.getPosition()));

        assertTrue(floorSwitch.isTriggered(dungeon));

        // Untrigger the floor switch
        player.move(dungeon, Direction.RIGHT);
        assertTrue(new Position(2, 0).equals(player.getPosition()));
        assertTrue(new Position(2, 0).equals(floorSwitch.getPosition()));
        assertTrue(new Position(3, 0).equals(boulder.getPosition()));

        assertFalse(floorSwitch.isTriggered(dungeon));
    }
}
