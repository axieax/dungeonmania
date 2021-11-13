package dungeonmania.extensions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

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
        assertTrue(
            TimeTravelUtil.isEntityAtPosition(resp.getEntities(), "spider", new Position(21, 5))
        );

        // confirm state on next tick
        resp = dmc.tick(null, Direction.UP);
        // check if older player following movement path
        assertEquals(new Position(1, 12), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));

        // check if player is onto new position
        assertEquals(new Position(9, 13), TimeTravelUtil.getPlayerPosition(resp.getEntities()));

        // check if spider is performing initial position movement (i.e. Direction.UP)
        assertTrue(
            TimeTravelUtil.isEntityAtPosition(resp.getEntities(), "spider", new Position(21, 4))
        );

        // confirm state on next tick
        resp = dmc.tick(null, Direction.UP);
        // check if older player following movement path
        assertEquals(new Position(1, 13), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));

        // check if player is onto new position
        assertEquals(new Position(9, 12), TimeTravelUtil.getPlayerPosition(resp.getEntities()));

        // check if spider performing circular motion
        assertTrue(
            TimeTravelUtil.isEntityAtPosition(resp.getEntities(), "spider", new Position(22, 4))
        );
    }

    /**
     * Test if player is send back 30 ticks in when entering into the time travelling
     * portal. i.e. state should be after the first tick since it takes 31 ticks to reach
     * to the time travelling portal
     */
    @Test
    public void testTimeTravellingPortalSecondTick() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("hourglass", "hard");

        int initialEntityCount = resp.getEntities().size();

        resp = TimeTravelUtil.goToTimeTravellingPortal2FromSpawnPoint(dmc);

        // confirm state of 30 ticks prior
        // confirm entity count - only old player should have been added to dungeon
        int timeTravelEntityCount = resp.getEntities().size();
        assertEquals(initialEntityCount + 1, timeTravelEntityCount);

        // check if older player is at it's second tick position
        assertEquals(new Position(1, 12), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));

        // check if player is onto of the portal
        assertEquals(new Position(21, 12), TimeTravelUtil.getPlayerPosition(resp.getEntities()));

        // check if spider is at it's second tick position
        assertTrue(
            TimeTravelUtil.isEntityAtPosition(resp.getEntities(), "spider", new Position(21, 4))
        );

        // check if mercenary is at it's second tick position
        assertTrue(
            TimeTravelUtil.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(15, 12))
        );

        // check if items has been respawned
        assertTrue(
            TimeTravelUtil.isEntityAtPosition(resp.getEntities(), "treasure", new Position(7, 15))
        );

        // confirm state on next tick
        resp = dmc.tick(null, Direction.UP);
        // check if there is only one spider
        assertEquals(1, TimeTravelUtil.entityCount(resp.getEntities(), "spider"));

        // check if older player following movement path
        assertEquals(new Position(1, 13), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));

        // check if player is onto new position
        assertEquals(new Position(21, 11), TimeTravelUtil.getPlayerPosition(resp.getEntities()));
    }

    /**
     * Test when a player time travels, that all entities now observe the current player.
     */
    @Test
    public void testTimeTravellingPortalMercenaryFollow() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("hourglass", "hard");

        resp = TimeTravelUtil.goToTimeTravellingPortal2FromSpawnPoint(dmc);

        // confirm state of 30 ticks prior
        // check if older player is at it's second tick position
        assertEquals(new Position(1, 12), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));

        // check if player is onto of the portal
        assertEquals(new Position(21, 12), TimeTravelUtil.getPlayerPosition(resp.getEntities()));

        // check if mercenary is at it's second tick position
        assertTrue(
            TimeTravelUtil.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(15, 12))
        );

        for (int i = 0; i < 11; ++i) resp = dmc.tick(null, Direction.DOWN);

        // after 11 ticks, mercenary two tiles above player
        assertTrue(
            TimeTravelUtil.isEntityAtPosition(resp.getEntities(), "mercenary", new Position(21, 13))
        );
        assertEquals(new Position(21, 15), TimeTravelUtil.getPlayerPosition(resp.getEntities()));
        assertEquals(new Position(9, 15), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));

        DungeonResponse tickRes = resp;
        // now interact and bribe with mercenary - we are bribing mercenary with sunstone since we picked it up on the map
        assertDoesNotThrow(
            () -> {
                DungeonResponse r = dmc.interact(
                    TimeTravelUtil
                        .getEntitiesAtPosition(tickRes.getEntities(), new Position(21, 13))
                        .get(0)
                        .getId()
                );
                // check if mercenary is an ally
                for (int i = 0; i < 5; i++) r = dmc.tick(null, Direction.NONE);
                assertTrue(r.getEntities().stream().anyMatch(e -> e.getType().startsWith("mercenary")));
            }
        );
    }

    /**
     * Test if you can use TimeTurner in conjunction with TimeTravellingPortal
     */
    @Test
    public void testTimeTravellingPortalWithTimeTurner() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("hourglass", "hard");

        int initialEntityCount = resp.getEntities().size();

        resp = TimeTravelUtil.goToTimeTravellingPortal2FromSpawnPoint(dmc);

        // confirm state of 30 ticks prior
        // confirm entity count - only old player should have been added to dungeon
        int timeTravelEntityCount = resp.getEntities().size();
        assertEquals(initialEntityCount + 1, timeTravelEntityCount);

        // check if older player is at it's second tick position
        assertEquals(new Position(1, 12), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));

        // check if player is onto of the portal
        assertEquals(new Position(21, 12), TimeTravelUtil.getPlayerPosition(resp.getEntities()));

        // confirm state on next tick
        resp = dmc.tick(null, Direction.UP);
        // check if there is only one spider
        assertEquals(1, TimeTravelUtil.entityCount(resp.getEntities(), "spider"));

        // check if older player following movement path
        assertEquals(new Position(1, 13), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));

        // check if player is above the tile of the portal
        assertEquals(new Position(21, 11), TimeTravelUtil.getPlayerPosition(resp.getEntities()));

        // use time turner to go back one tick
        resp = dmc.rewind(1);

        // confirm state one tick prior
        timeTravelEntityCount = resp.getEntities().size();
        assertEquals(initialEntityCount + 1, timeTravelEntityCount);

        // check if older player is at it's second tick position
        assertEquals(new Position(1, 12), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));

        // check if player is onto of the portal
        assertEquals(new Position(21, 11), TimeTravelUtil.getPlayerPosition(resp.getEntities()));

        // use time turner to go back five ticks
        resp = dmc.rewind(5);

        // confirm state is the initial tick
        timeTravelEntityCount = resp.getEntities().size();
        assertEquals(initialEntityCount + 1, timeTravelEntityCount);

        // check if older player is at it's first tick position
        assertEquals(new Position(1, 11), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));

        // check if player is above the tile of the portal
        assertEquals(new Position(21, 11), TimeTravelUtil.getPlayerPosition(resp.getEntities()));
    }

    @Test
    public void testRewindState() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("hourglass", "hard");
        TimeTravelUtil.goToTimeTravellingPortal1FromSpawnPoint(dmc);
        for (int i = 0; i < 5; i++) dmc.tick(null, Direction.UP);
        for (int i = 0; i < 6; i++) dmc.tick(null, Direction.DOWN);
        for (int i = 0; i < 5; i++) dmc.tick(null, Direction.LEFT);
        dmc.tick(null, Direction.LEFT);
        dmc.rewind(5);
    }
}
