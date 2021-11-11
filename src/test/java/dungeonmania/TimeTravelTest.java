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
            .filter(e -> e.getType().equals("old_player"))
            .findFirst()
            .orElse(null);
        return (player != null) ? player.getPosition().asLayer(0) : null;
    }

    /**
     * Filters the old player from DungeonResponse
     *
     * @param original DungeonResponse
     *
     * @return DungeonResponse without the old_player entity
     */
    private DungeonResponse respWithoutOldPlayer(DungeonResponse original) {
        return new DungeonResponse(
            original.getDungeonId(),
            original.getDungeonName(),
            original
                .getEntities()
                .stream()
                .filter(e -> !e.getType().equals("old_player"))
                .collect(Collectors.toList()),
            original.getInventory(),
            original.getBuildables(),
            original.getGoals()
        );
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
        for (int i = 0; i < 2; ++i) dmc.tick(null, Direction.DOWN);

        // invalid ticks
        assertThrows(IllegalArgumentException.class, () -> dmc.rewind(0));
        assertThrows(IllegalArgumentException.class, () -> dmc.rewind(-1));
    }

    @Test
    public void testRewindPosition() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("hourglass", "standard");
        DungeonResponse resp = null;
        for (int i = 0; i < 2; ++i) resp = dmc.tick(null, Direction.DOWN);
        assertEquals(new Position(1, 13), getPlayerPosition(resp.getEntities()));

        // go back one position
        resp = dmc.rewind(1);
        assertEquals(new Position(1, 12), getPlayerPosition(resp.getEntities()));
    }

    @Test
    public void testRewindExceeds() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initialState = dmc.newGame("hourglass", "standard");
        for (int i = 0; i < 2; ++i) dmc.tick(null, Direction.DOWN);

        // try to rewind more than 2 moves
        assertDoesNotThrow(() -> {
            DungeonResponse resp = dmc.rewind(3);
            // add old_player
            assertEquals(initialState, respWithoutOldPlayer(resp));
            assertEquals(new Position(1, 13), getOldPlayerPosition(resp.getEntities()));
        });
    }

    @Test
    public void testRewindEdge() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initialState = dmc.newGame("hourglass", "standard");
        for (int i = 0; i < 2; ++i) dmc.tick(null, Direction.DOWN);

        // try to rewind 2 moves
        assertDoesNotThrow(() -> {
            DungeonResponse resp = dmc.rewind(2);
            assertEquals(initialState, respWithoutOldPlayer(resp));
            assertEquals(new Position(1, 13), getOldPlayerPosition(resp.getEntities()));
        });
    }

    @Test
    public void testRewindDoesNotExceed() {
        // create game and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("hourglass", "standard");
        DungeonResponse secondMove = dmc.tick(null, Direction.DOWN);

        // try to rewind 1 move
        assertDoesNotThrow(() -> {
            DungeonResponse resp = dmc.rewind(1);
            assertEquals(secondMove, respWithoutOldPlayer(resp));
            assertEquals(new Position(1, 13), getOldPlayerPosition(resp.getEntities()));
        });
    }
}
