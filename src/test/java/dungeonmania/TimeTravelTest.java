package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class TimeTravelTest {

    /**
     * Helper method for checking whether a DungeonResponse contains a time_turner
     *
     * @param inventory inventory from DungeonResponse
     *
     * @return true if inventory contains time_turner, false otherwise
     */
    private boolean hasTimeTurner(List<ItemResponse> inventory) {
        return inventory.stream().filter(item -> item.getType().equals("time_turner")).count() > 0;
    }

    private DungeonResponse goToTimeTurnerFromSpawnPoint(DungeonManiaController dmc) {
        DungeonResponse resp = null;
        for (int i = 0; i < 2; ++i) resp = dmc.tick(null, Direction.DOWN);
        return resp;
    }

    private DungeonResponse goToTimeTravellingPortal1FromSpawnPoint(DungeonManiaController dmc) {
        for (int i = 0; i < 2; ++i) assertDoesNotThrow(() -> dmc.tick(null, Direction.DOWN));
        for (int i = 0; i < 2; ++i) assertDoesNotThrow(() -> dmc.tick(null, Direction.RIGHT));
        for (int i = 0; i < 2; ++i) assertDoesNotThrow(() -> dmc.tick(null, Direction.DOWN));
        for (int i = 0; i < 6; ++i) assertDoesNotThrow(() -> dmc.tick(null, Direction.DOWN));
        // enter time travel portal
        return dmc.tick(null, Direction.UP);
    }

    /**
     * Returns the old player
     *
     * @param entities entities from DungeonResponse
     *
     * @return old player response, or null if old player does not exist
     */
    private EntityResponse getOldPlayer(List<EntityResponse> entities) {
        return entities
            .stream()
            .filter(e -> e.getType().equals("older_player"))
            .findFirst()
            .orElse(null);
    }

    /**
     * Returns the player
     *
     * @param entities entities from DungeonResponse
     *
     * @return player response, or null if old player does not exist
     */
    private EntityResponse getPlayer(List<EntityResponse> entities) {
        return entities
            .stream()
            .filter(e -> e.getType().equals("player"))
            .findFirst()
            .orElse(null);
    }

    /**
     * Returns the position of the player
     *
     * @param entities entities from DungeonResponse
     *
     * @return position of player, or null if player does not exist
     */
    private Position getPlayerPosition(List<EntityResponse> entities) {
        EntityResponse player = entities
            .stream()
            .filter(e -> e.getType().equals("player"))
            .findFirst()
            .orElse(null);
        return (player != null) ? player.getPosition().asLayer(0) : null;
    }

    /**
     * Returns the position of the old player
     *
     * @param entities entities from DungeonResponse
     *
     * @return position of old player, or null if old player does not exist
     */
    private Position getOldPlayerPosition(List<EntityResponse> entities) {
        EntityResponse player = entities
            .stream()
            .filter(e -> e.getType().equals("older_player"))
            .findFirst()
            .orElse(null);
        return (player != null) ? player.getPosition().asLayer(0) : null;
    }

    @Test
    public void testTimeTurnerMissing() {
        // create game
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("hourglass", "hard");

        // no time turner
        assertThrows(IllegalArgumentException.class, () -> dmc.rewind(1));

        // pick up time turner
        DungeonResponse resp = dmc.tick(null, Direction.DOWN);
        assertFalse(hasTimeTurner(resp.getInventory()));
        resp = dmc.tick(null, Direction.DOWN);
        assertTrue(hasTimeTurner(resp.getInventory()));
    }

    @Test
    public void testInvalidRewind() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("hourglass", "standard");
        goToTimeTurnerFromSpawnPoint(dmc);

        // invalid ticks
        assertThrows(IllegalArgumentException.class, () -> dmc.rewind(0));
        assertThrows(IllegalArgumentException.class, () -> dmc.rewind(-1));
    }

    @Test
    public void testRewindPosition() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("hourglass", "standard");
        DungeonResponse resp = goToTimeTurnerFromSpawnPoint(dmc);
        assertEquals(new Position(1, 13), getPlayerPosition(resp.getEntities()));

        // go back one position
        resp = dmc.rewind(1);
        assertEquals(new Position(1, 13), getPlayerPosition(resp.getEntities()));
        assertEquals(new Position(1, 12), getOldPlayerPosition(resp.getEntities()));
    }

    @Test
    public void testRewindExceeds() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initialState = dmc.newGame("hourglass", "standard");
        goToTimeTurnerFromSpawnPoint(dmc);

        // try to rewind more than 2 moves
        assertDoesNotThrow(() -> {
            DungeonResponse resp = dmc.rewind(3);
            assertEquals(new Position(1, 13), getPlayerPosition(resp.getEntities()));
            assertEquals(new Position(1, 11), getOldPlayerPosition(resp.getEntities()));
        });
    }

    @Test
    public void testRewindEdge() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initialState = dmc.newGame("hourglass", "standard");
        goToTimeTurnerFromSpawnPoint(dmc);

        // try to rewind 2 moves
        assertDoesNotThrow(() -> {
            DungeonResponse resp = dmc.rewind(2);
            assertEquals(new Position(1, 13), getPlayerPosition(resp.getEntities()));
            assertEquals(new Position(1, 11), getOldPlayerPosition(resp.getEntities()));
        });
    }

    /**
     * Test rewind with old player following set path using time turner (1 tick)
     */
    @Test
    public void testRewindOneTickOldPlayerMovement() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initialState = dmc.newGame("hourglass", "standard");
        goToTimeTurnerFromSpawnPoint(dmc);
        assertDoesNotThrow(() -> dmc.tick(null, Direction.RIGHT));

        // try to rewind 1 move
        assertDoesNotThrow(() -> {
            DungeonResponse resp = dmc.rewind(1);
            assertEquals(new Position(2, 13), getPlayerPosition(resp.getEntities()));
            assertEquals(new Position(1, 13), getOldPlayerPosition(resp.getEntities()));

            // checks past player moves right 
            resp = dmc.tick(null, Direction.RIGHT);
            assertEquals(new Position(2, 13), getOldPlayerPosition(resp.getEntities()));
            assertEquals(new Position(3, 13), getPlayerPosition(resp.getEntities()));

            // old player should disappear
            resp = dmc.tick(null, Direction.RIGHT);
            assertEquals(null, getOldPlayer(resp.getEntities()));
            assertEquals(new Position(4, 13), getPlayerPosition(resp.getEntities()));
        });
    }

    /**
     * Test rewind with old player following set path using time turner (5 ticks)
     */
    @Test
    public void testRewindFiveTicksOldPlayerMovement() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initialState = dmc.newGame("hourglass", "standard");
        goToTimeTurnerFromSpawnPoint(dmc);
        assertDoesNotThrow(() -> dmc.tick(null, Direction.RIGHT));
        assertDoesNotThrow(() -> dmc.tick(null, Direction.RIGHT));
        assertDoesNotThrow(() -> dmc.tick(null, Direction.UP));
        assertDoesNotThrow(() -> dmc.tick(null, Direction.DOWN));
        assertDoesNotThrow(() -> dmc.tick(null, Direction.RIGHT));

        // try to rewind 5 move
        assertDoesNotThrow(() -> {
            DungeonResponse resp = dmc.rewind(5);
            assertEquals(new Position(4, 13), getPlayerPosition(resp.getEntities()));
            assertEquals(new Position(1, 13), getOldPlayerPosition(resp.getEntities()));

            // checks past player movements 
            resp = dmc.tick(null, Direction.NONE);
            assertEquals(new Position(2, 13), getOldPlayerPosition(resp.getEntities()));
            resp = dmc.tick(null, Direction.NONE);
            assertEquals(new Position(3, 13), getOldPlayerPosition(resp.getEntities()));
            resp = dmc.tick(null, Direction.NONE);
            assertEquals(new Position(3, 12), getOldPlayerPosition(resp.getEntities()));
            resp = dmc.tick(null, Direction.NONE);
            assertEquals(new Position(3, 13), getOldPlayerPosition(resp.getEntities()));

            // old player be on the tile where the current player used the time turner
            Position currPos = getPlayerPosition(resp.getEntities());
            resp = dmc.tick(null, Direction.RIGHT);
            assertEquals(currPos, getOldPlayerPosition(resp.getEntities()));

            // old player should now disappear
            resp = dmc.tick(null, Direction.NONE);
            assertEquals(null, getOldPlayer(resp.getEntities()));
        });
    }

    /**
     * Test rewind with old player and initiate a battle
     */
    @Test
    public void testRewindBattle() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initialState = dmc.newGame("hourglass", "standard");
        goToTimeTurnerFromSpawnPoint(dmc);

        // try to rewind 1 move
        assertDoesNotThrow(() -> {
            DungeonResponse resp = dmc.rewind(1);
            assertEquals(new Position(1, 13), getPlayerPosition(resp.getEntities()));
            assertEquals(new Position(1, 12), getOldPlayerPosition(resp.getEntities()));

            // past player starts battle with current player
            resp = dmc.tick(null, Direction.NONE);
            assertTrue(getOldPlayer(resp.getEntities()) == null || getPlayer(resp.getEntities()) == null);
        });
    }
}
