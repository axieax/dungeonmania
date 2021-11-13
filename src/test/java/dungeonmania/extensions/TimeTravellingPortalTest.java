package dungeonmania.extensions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TimeTravellingPortalTest {

    /**
     * Test if player is send back less than 30 ticks in when entering into the time travelling 
     * portal. i.e. state should be the same as when the game begin
     */
    @Test
    public void testTimeTravellingPortalFirstTick() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("hourglass", "hard");

        DungeonResponse resp = TimeTravelUtil.goToTimeTravellingPortal1FromSpawnPoint(dmc);

        // confirm state of 30 ticks prior
        // check if older player is at spawning position
        assertEquals(new Position(1, 11), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));

        // check if player is onto of the portal
        assertEquals(new Position(9, 14), TimeTravelUtil.getPlayerPosition(resp.getEntities()));

        // check if spider is at spawning position
        assertTrue(TimeTravelUtil.isEntityAtPosition(resp.getEntities(), "spider", new Position(21, 5)));

        // confirm state on next tick
        resp = dmc.tick(null, Direction.UP);
        // check if older player following movement path
        assertEquals(new Position(1, 12), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));

        // check if player is onto new position
        assertEquals(new Position(9, 13), TimeTravelUtil.getPlayerPosition(resp.getEntities()));

        // check if spider is performing initial position movement (i.e. Direction.UP)
        assertTrue(TimeTravelUtil.isEntityAtPosition(resp.getEntities(), "spider", new Position(21, 4)));

        // confirm state on next tick
        resp = dmc.tick(null, Direction.UP);
        // check if older player following movement path
        assertEquals(new Position(1, 13), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));

        // check if player is onto new position
        assertEquals(new Position(9, 12), TimeTravelUtil.getPlayerPosition(resp.getEntities()));

        // check if spider performing circular motion
        assertTrue(TimeTravelUtil.isEntityAtPosition(resp.getEntities(), "spider", new Position(22, 4)));
    }

    /**
     * Test if player is send back 30 ticks in when entering into the time travelling 
     * portal. i.e. state should be after the first tick since it takes 31 ticks to reach
     * to the time travelling portal
     */
    @Test
    public void testTimeTravellingPortalSecondTick() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("hourglass", "hard");

        DungeonResponse resp = TimeTravelUtil.goToTimeTravellingPortal2FromSpawnPoint(dmc);

        // confirm state of 30 ticks prior
        // check if older player is at it's second tick position
        assertEquals(new Position(1, 12), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));

        // check if player is onto of the portal
        assertEquals(new Position(21, 12), TimeTravelUtil.getPlayerPosition(resp.getEntities()));

        // check if spider is at it's second tick position
        assertTrue(TimeTravelUtil.isEntityAtPosition(resp.getEntities(), "spider", new Position(21, 4)));

        // confirm state on next tick
        resp = dmc.tick(null, Direction.UP);
        // check if there is only one spider
        assertEquals(1, TimeTravelUtil.entityCount(resp.getEntities(), "spider"));

        // check if older player following movement path
        assertEquals(new Position(1, 13), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));

        // check if player is onto new position
        assertEquals(new Position(21, 11), TimeTravelUtil.getPlayerPosition(resp.getEntities()));
    }
}
