package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TestHelpers {

    public static <T extends Comparable<? super T>> void assertListAreEqualIgnoringOrder(
        List<T> a,
        List<T> b
    ) {
        Collections.sort(a);
        Collections.sort(b);
        assertArrayEquals(a.toArray(), b.toArray());
    }

    /********************************
     *  Map Generation              *
     ********************************/

    /**
     * Creates a 7x7 wall boundary
     * @return List<Entity>
     */
    public static List<Entity> sevenBySevenWallBoundary() {
        List<Entity> wallBorder = new ArrayList<>();

        // left border
        for (int i = 0; i < 7; i++) {
            Wall wall = new Wall(new Position(0, i));
            wallBorder.add(wall);
        }

        // right border
        for (int i = 0; i < 7; i++) {
            Wall wall = new Wall(new Position(6, i));
            wallBorder.add(wall);
        }

        // top border
        for (int i = 1; i < 6; i++) {
            Wall wall = new Wall(new Position(i, 0));
            wallBorder.add(wall);
        }

        // bottom border
        for (int i = 1; i < 6; i++) {
            Wall wall = new Wall(new Position(i, 6));
            wallBorder.add(wall);
        }

        return wallBorder;
    }

    /********************************
     *  DMC endpoints               *
     ********************************/

    /**
     * Ticks the player to move to the given direction.
     *
     * @param dmc
     * @param direction
     * @param times
     * @return DungeonResponse
     */
    public static DungeonResponse tickMovement(
        DungeonManiaController dmc,
        Direction direction,
        int times
    ) {
        DungeonResponse resp = null;
        for (int i = 0; i < times; i++) resp = dmc.tick(null, direction);
        return resp;
    }

    /********************************
     *  Game endpoints              *
     ********************************/

    /**
     * Ticks the player to move to the given direction.
     *
     * @param game
     * @param direction
     * @param times
     * @return DungeonResponse
     */
    public static DungeonResponse gameTickMovement(Game game, Direction direction, int times) {
        DungeonResponse resp = null;
        for (int i = 0; i < times; i++) resp = game.tick(null, direction);
        return resp;
    }

    /********************************
     *  Response helper methods     *
     ********************************/

    /**
     * Counts the number of entities specified by the prefix.
     * @param entities
     * @param prefix
     * @return int
     */
    public static int entityCount(List<EntityResponse> entities, String prefix) {
        return entities
            .stream()
            .filter(e -> e.getType().startsWith(prefix))
            .collect(Collectors.toList())
            .size();
    }

    /**
     * Given an inventory and prefix, retrieve the first instance of that item.
     * @param inventory
     * @param prefix
     * @return ItemResponse
     */
    public static ItemResponse getItemFromInventory(List<ItemResponse> inventory, String prefix) {
        return inventory
            .stream()
            .filter(e -> e.getType().startsWith(prefix))
            .findFirst()
            .orElse(null);
    }

    /**
     * Returns whether there is an entity with the given prefix on the position
     * @return boolean
     */
    public static boolean isEntityAtPosition(
        List<EntityResponse> entities,
        String prefix,
        Position position
    ) {
        return entities
            .stream()
            .anyMatch(e -> e.getType().startsWith(prefix) && e.getPosition().equals(position));
    }

    /**
     * Gets a list of entities at position given
     * @return List<EntityResponse>
     */
    public static List<EntityResponse> getEntitiesAtPosition(
        List<EntityResponse> entities,
        Position pos
    ) {
        return entities
            .stream()
            .filter(e -> e.getPosition().equals(pos))
            .collect(Collectors.toList());
    }

    /**
     * Returns whether there is an entity with the given prefix in the entity list given
     * @return boolean
     */
    public static boolean isEntityInEntitiesList(List<EntityResponse> entities, String prefix) {
        return entities.stream().anyMatch(e -> e.getType().startsWith(prefix));
    }

    /**
     * Returns the player
     *
     * @param entities entities from DungeonResponse
     *
     * @return player response, or null if old player does not exist
     */
    public static EntityResponse getPlayer(List<EntityResponse> entities) {
        return entities.stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
    }

    /**
     * Find first instance of entity with prefix in the entity responses
     * @param entities
     * @param prefix
     *
     * @return EntityResponse
     */
    public static EntityResponse findFirstInstance(
        List<EntityResponse> entities,
        String objectType
    ) {
        return entities
            .stream()
            .filter(e -> e.getType().equals(objectType))
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
    public static Position getPlayerPosition(List<EntityResponse> entities) {
        EntityResponse player = entities
            .stream()
            .filter(e -> e.getType().equals("player"))
            .findFirst()
            .orElse(null);
        return (player != null) ? player.getPosition().asLayer(0) : null;
    }

    /**
     * Given a dungeonResponse returns the id of an object within the player's inventory
     * @param resp
     * @param objectType
     * @return
     */
    public static String getInventoryId(DungeonResponse resp, String objectType) {
        for (ItemResponse item : resp.getInventory()) {
            if (objectType.equals(item.getType())) return item.getId();
        }
        return null;
    }

    /**
     * Given a dungeonResponse returns the id of an object within the game entity
     * @param resp
     * @param objectType
     * @return
     */
    public static String getEntityId(DungeonResponse resp, String objectType) {
        for (EntityResponse entity : resp.getEntities()) {
            if (objectType.equals(entity.getType())) return entity.getId();
        }
        return null;
    }
}
