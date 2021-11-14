package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Collections;
import java.util.List;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestHelpers {
    public static<T extends Comparable<? super T>> void assertListAreEqualIgnoringOrder(List<T> a, List<T> b) {
        Collections.sort(a);
        Collections.sort(b);
        assertArrayEquals(a.toArray(), b.toArray());
    }

    public static DungeonResponse tickMovement(DungeonManiaController dmc, Direction direction, int times) {
        DungeonResponse resp = null;
        for (int i = 0; i < times; i++) resp = dmc.tick(null, direction);
        return resp;
    }

    public static ItemResponse getItemFromInventory(List<ItemResponse> inventory, String prefix) {
        return inventory.stream().filter(e -> e.getType().startsWith(prefix)).findFirst().orElse(null);
    }

    public static EntityResponse findInstance(List<EntityResponse> entities, String prefix) {
        return entities.stream().filter(e -> e.getType().startsWith(prefix)).findFirst().orElse(null);
    }

    public static boolean isEntityAtPosition(List<EntityResponse> entities, String prefix, Position position) {
        return entities.stream().anyMatch(e -> e.getType().startsWith(prefix) && e.getPosition().equals(position));
    }

}
