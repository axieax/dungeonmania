package dungeonmania.extensions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class TimeTurnerTest {

    @Test
    public void testTimeTurnerMissing() {
        // create game
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("hourglass", "hard");

        // no time turner
        assertThrows(IllegalArgumentException.class, () -> dmc.rewind(1));

        // pick up time turner
        DungeonResponse resp = dmc.tick(null, Direction.DOWN);
        assertFalse(TimeTravelUtil.hasTimeTurner(resp.getInventory()));
        resp = dmc.tick(null, Direction.DOWN);
        assertTrue(TimeTravelUtil.hasTimeTurner(resp.getInventory()));
    }

    @Test
    public void testInvalidRewind() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("hourglass", "standard");
        TimeTravelUtil.goToTimeTurnerFromSpawnPoint(dmc);

        // invalid ticks
        assertThrows(IllegalArgumentException.class, () -> dmc.rewind(0));
        assertThrows(IllegalArgumentException.class, () -> dmc.rewind(-1));
    }

    @Test
    public void testRewindPosition() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("hourglass", "standard");
        DungeonResponse resp = TimeTravelUtil.goToTimeTurnerFromSpawnPoint(dmc);
        assertEquals(new Position(1, 13), TimeTravelUtil.getPlayerPosition(resp.getEntities()));

        // go back one position
        resp = dmc.rewind(1);
        assertEquals(new Position(1, 13), TimeTravelUtil.getPlayerPosition(resp.getEntities()));
        assertEquals(new Position(1, 12), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));
    }

    @Test
    public void testRewindExceeds() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initialState = dmc.newGame("hourglass", "standard");
        TimeTravelUtil.goToTimeTurnerFromSpawnPoint(dmc);

        // try to rewind more than 2 moves
        assertDoesNotThrow(
            () -> {
                DungeonResponse resp = dmc.rewind(3);
                assertEquals(
                    new Position(1, 13),
                    TimeTravelUtil.getPlayerPosition(resp.getEntities())
                );
                assertEquals(
                    new Position(1, 11),
                    TimeTravelUtil.getOldPlayerPosition(resp.getEntities())
                );
            }
        );
    }

    @Test
    public void testRewindEdge() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initialState = dmc.newGame("hourglass", "standard");
        TimeTravelUtil.goToTimeTurnerFromSpawnPoint(dmc);

        // try to rewind 2 moves
        assertDoesNotThrow(
            () -> {
                DungeonResponse resp = dmc.rewind(2);
                assertEquals(
                    new Position(1, 13),
                    TimeTravelUtil.getPlayerPosition(resp.getEntities())
                );
                assertEquals(
                    new Position(1, 11),
                    TimeTravelUtil.getOldPlayerPosition(resp.getEntities())
                );
            }
        );
    }

    /**
     * Test rewind with old player following set path using time turner (1 tick)
     */
    @Test
    public void testRewindOneTickOldPlayerMovement() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initialState = dmc.newGame("hourglass", "standard");
        TimeTravelUtil.goToTimeTurnerFromSpawnPoint(dmc);
        assertDoesNotThrow(() -> dmc.tick(null, Direction.RIGHT));

        // try to rewind 1 move
        assertDoesNotThrow(
            () -> {
                DungeonResponse resp = dmc.rewind(1);
                assertEquals(
                    new Position(2, 13),
                    TimeTravelUtil.getPlayerPosition(resp.getEntities())
                );
                assertEquals(
                    new Position(1, 13),
                    TimeTravelUtil.getOldPlayerPosition(resp.getEntities())
                );

                // checks past player moves right
                resp = dmc.tick(null, Direction.RIGHT);
                assertEquals(
                    new Position(2, 13),
                    TimeTravelUtil.getOldPlayerPosition(resp.getEntities())
                );
                assertEquals(
                    new Position(3, 13),
                    TimeTravelUtil.getPlayerPosition(resp.getEntities())
                );

                // old player should disappear
                resp = dmc.tick(null, Direction.RIGHT);
                assertEquals(null, TimeTravelUtil.getOldPlayer(resp.getEntities()));
                assertEquals(
                    new Position(4, 13),
                    TimeTravelUtil.getPlayerPosition(resp.getEntities())
                );
            }
        );
    }

    /**
     * Test rewind with old player following set path using time turner (5 ticks)
     */
    @Test
    public void testRewindFiveTicksOldPlayerMovement() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initialState = dmc.newGame("hourglass", "standard");
        TimeTravelUtil.goToTimeTurnerFromSpawnPoint(dmc);
        assertDoesNotThrow(() -> dmc.tick(null, Direction.RIGHT));
        assertDoesNotThrow(() -> dmc.tick(null, Direction.RIGHT));
        assertDoesNotThrow(() -> dmc.tick(null, Direction.UP));
        assertDoesNotThrow(() -> dmc.tick(null, Direction.DOWN));
        assertDoesNotThrow(() -> dmc.tick(null, Direction.RIGHT));

        // try to rewind 5 move
        assertDoesNotThrow(
            () -> {
                DungeonResponse resp = dmc.rewind(5);
                assertEquals(
                    new Position(4, 13),
                    TimeTravelUtil.getPlayerPosition(resp.getEntities())
                );
                assertEquals(
                    new Position(1, 13),
                    TimeTravelUtil.getOldPlayerPosition(resp.getEntities())
                );

                // checks past player movements
                resp = dmc.tick(null, Direction.NONE);
                assertEquals(
                    new Position(2, 13),
                    TimeTravelUtil.getOldPlayerPosition(resp.getEntities())
                );
                resp = dmc.tick(null, Direction.NONE);
                assertEquals(
                    new Position(3, 13),
                    TimeTravelUtil.getOldPlayerPosition(resp.getEntities())
                );
                resp = dmc.tick(null, Direction.NONE);
                assertEquals(
                    new Position(3, 12),
                    TimeTravelUtil.getOldPlayerPosition(resp.getEntities())
                );
                resp = dmc.tick(null, Direction.NONE);
                assertEquals(
                    new Position(3, 13),
                    TimeTravelUtil.getOldPlayerPosition(resp.getEntities())
                );

                // old player be on the tile where the current player used the time turner
                Position currPos = TimeTravelUtil.getPlayerPosition(resp.getEntities());
                resp = dmc.tick(null, Direction.RIGHT);
                assertEquals(currPos, TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));

                // old player should now disappear
                resp = dmc.tick(null, Direction.NONE);
                assertEquals(null, TimeTravelUtil.getOldPlayer(resp.getEntities()));
            }
        );
    }

    /**
     * Test rewind with old player and initiate a battle
     */
    @Test
    public void testRewindBattle() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initialState = dmc.newGame("hourglass", "standard");
        TimeTravelUtil.goToTimeTurnerFromSpawnPoint(dmc);

        // try to rewind 1 move
        assertDoesNotThrow(
            () -> {
                DungeonResponse resp = dmc.rewind(1);
                assertEquals(
                    new Position(1, 13),
                    TimeTravelUtil.getPlayerPosition(resp.getEntities())
                );
                assertEquals(
                    new Position(1, 12),
                    TimeTravelUtil.getOldPlayerPosition(resp.getEntities())
                );

                // past player starts battle with current player
                resp = dmc.tick(null, Direction.NONE);
                assertTrue(
                    TimeTravelUtil.getOldPlayer(resp.getEntities()) == null ||
                    TimeTravelUtil.getPlayer(resp.getEntities()) == null
                );
            }
        );
    }
}
