package dungeonmania.extensions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import dungeonmania.DungeonManiaController;
import dungeonmania.TestHelpers;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.List;

public class TimeTravelUtil extends TestHelpers {

    /**
     * Helper method for checking whether a DungeonResponse contains a time_turner
     *
     * @param inventory inventory from DungeonResponse
     *
     * @return true if inventory contains time_turner, false otherwise
     */
    public static boolean hasTimeTurner(List<ItemResponse> inventory) {
        return inventory.stream().filter(item -> item.getType().equals("time_turner")).count() > 0;
    }

    public static DungeonResponse goToTimeTurnerFromSpawnPoint(DungeonManiaController dmc) {
        DungeonResponse resp = null;
        for (int i = 0; i < 2; ++i) resp = dmc.tick(null, Direction.DOWN);
        return resp;
    }

    public static DungeonResponse goToTimeTravellingPortal1FromSpawnPoint(
        DungeonManiaController dmc
    ) {
        for (int i = 0; i < 2; ++i) assertDoesNotThrow(() -> dmc.tick(null, Direction.DOWN));
        for (int i = 0; i < 2; ++i) assertDoesNotThrow(() -> dmc.tick(null, Direction.RIGHT));
        for (int i = 0; i < 2; ++i) assertDoesNotThrow(() -> dmc.tick(null, Direction.DOWN));
        for (int i = 0; i < 6; ++i) assertDoesNotThrow(() -> dmc.tick(null, Direction.RIGHT));
        // enter time travel portal
        return dmc.tick(null, Direction.UP);
    }

    public static DungeonResponse goToTimeTravellingPortal2FromSpawnPoint(
        DungeonManiaController dmc
    ) {
        for (int i = 0; i < 2; ++i) assertDoesNotThrow(() -> dmc.tick(null, Direction.DOWN));
        for (int i = 0; i < 2; ++i) assertDoesNotThrow(() -> dmc.tick(null, Direction.RIGHT));
        for (int i = 0; i < 2; ++i) assertDoesNotThrow(() -> dmc.tick(null, Direction.DOWN));
        for (int i = 0; i < 10; ++i) assertDoesNotThrow(() -> dmc.tick(null, Direction.RIGHT));
        for (int i = 0; i < 2; ++i) assertDoesNotThrow(() -> dmc.tick(null, Direction.DOWN));
        for (int i = 0; i < 4; ++i) assertDoesNotThrow(() -> dmc.tick(null, Direction.RIGHT));
        for (int i = 0; i < 4; ++i) assertDoesNotThrow(() -> dmc.tick(null, Direction.UP));
        for (int i = 0; i < 4; ++i) assertDoesNotThrow(() -> dmc.tick(null, Direction.RIGHT));
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
    public static EntityResponse getOldPlayer(List<EntityResponse> entities) {
        return entities
            .stream()
            .filter(e -> e.getType().equals("older_player"))
            .findFirst()
            .orElse(null);
    }

    /**
     * Returns the position of the old player
     *
     * @param entities entities from DungeonResponse
     *
     * @return position of old player, or null if old player does not exist
     */
    public static Position getOldPlayerPosition(List<EntityResponse> entities) {
        EntityResponse player = entities
            .stream()
            .filter(e -> e.getType().equals("older_player"))
            .findFirst()
            .orElse(null);
        return (player != null) ? player.getPosition().asLayer(0) : null;
    }

    /**
     * Returns the position of the player
     *
     * @param entities entities from DungeonResponse
     *
     * @return position of player, or null if player does not exist
     */
    public static Position getPlayerPosition(List<EntityResponse> entities) {
        EntityResponse player = entities
            .stream()
            .filter(e -> e.getType().equals("player"))
            .findFirst()
            .orElse(null);
        return (player != null) ? player.getPosition().asLayer(0) : null;
    }
}
