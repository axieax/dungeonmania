package dungeonmania.extensions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class TimeTravelBattleTest {

    /**
     * Test encounter with older self with sunstone
     */
    @Test
    public void testEncounterSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("hourglass", "hard");

        resp = TimeTravelUtil.goToTimeTravellingPortal2FromSpawnPoint(dmc);

        // go down to collect sun stone
        resp = TimeTravelUtil.tickMovement(dmc, Direction.DOWN, 2);

        assertTrue(resp.getInventory().stream().anyMatch(e -> e.getType().startsWith("sun_stone")));

        // go up and left and wait for player
        resp = dmc.tick(null, Direction.UP);
        resp = TimeTravelUtil.tickMovement(dmc, Direction.LEFT, 4);

        // wait until old player reaches to current player position
        resp = TimeTravelUtil.tickMovement(dmc, Direction.NONE, 18);

        // old player should be same position as current player
        assertEquals(
            TimeTravelUtil.getOldPlayerPosition(resp.getEntities()),
            TimeTravelUtil.getPlayerPosition(resp.getEntities())
        );

        // next tick, old player should continue to next path
        resp = dmc.tick(null, Direction.NONE);
        assertEquals(new Position(18, 13), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));
    }

    /**
     * Test encounter with older self with midnight armour
     */
    @Test
    public void testEncounterMidnightArmour() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("hourglass", "hard");

        resp = TimeTravelUtil.goToTimeTravellingPortal2FromSpawnPoint(dmc);

        // go down to collect sun stone
        resp = TimeTravelUtil.tickMovement(dmc, Direction.DOWN, 2);

        assertTrue(resp.getInventory().stream().anyMatch(e -> e.getType().startsWith("sun_stone")));

        // go up and right to collect armour
        resp = dmc.tick(null, Direction.UP);
        resp = dmc.tick(null, Direction.RIGHT);
        assertTrue(resp.getInventory().stream().anyMatch(e -> e.getType().startsWith("armour")));

        // build midnight armour
        resp = dmc.build("midnight_armour");
        assertTrue(
            resp.getInventory().stream().anyMatch(e -> e.getType().startsWith("midnight_armour"))
        );

        // go left and wait for player
        resp = TimeTravelUtil.tickMovement(dmc, Direction.LEFT, 5);

        // wait until old player reaches to current player position
        resp = TimeTravelUtil.tickMovement(dmc, Direction.NONE, 16);

        // old player should be same position as current player
        assertEquals(
            TimeTravelUtil.getOldPlayerPosition(resp.getEntities()),
            TimeTravelUtil.getPlayerPosition(resp.getEntities())
        );

        // next tick, old player should continue to next path
        resp = dmc.tick(null, Direction.NONE);
        assertEquals(new Position(18, 13), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));
    }

    /**
     * Test encounter with older self when invisible
     */
    @Test
    public void testEncounterInvisible() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse resp = dmc.newGame("hourglass", "hard");

        resp = TimeTravelUtil.goToTimeTravellingPortal2FromSpawnPoint(dmc);

        // go down
        resp = TimeTravelUtil.tickMovement(dmc, Direction.DOWN, 1);

        // assert that there is no sun stone or midnight armour
        assertFalse(
            resp.getInventory().stream().anyMatch(e -> e.getType().startsWith("sun_stone"))
        );
        assertFalse(
            resp.getInventory().stream().anyMatch(e -> e.getType().startsWith("midnight_armour"))
        );

        // go left and wait for player
        resp = TimeTravelUtil.tickMovement(dmc, Direction.LEFT, 4);

        // wait until old player reaches to current player position
        resp = TimeTravelUtil.tickMovement(dmc, Direction.NONE, 18);
        // consume potion
        ItemResponse item = TimeTravelUtil.getItemFromInventory(
            resp.getInventory(),
            "invisibility_potion"
        );
        assertNotNull(item);
        resp = dmc.tick(item.getId(), Direction.NONE);

        // player should be invisible now
        resp = dmc.tick(null, Direction.NONE);

        // old player should be same position as current player
        assertEquals(
            TimeTravelUtil.getOldPlayerPosition(resp.getEntities()),
            TimeTravelUtil.getPlayerPosition(resp.getEntities())
        );

        // next tick, old player should continue to next path
        resp = dmc.tick(null, Direction.NONE);
        assertEquals(new Position(18, 13), TimeTravelUtil.getOldPlayerPosition(resp.getEntities()));
    }
}
