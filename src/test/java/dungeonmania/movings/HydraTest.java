package dungeonmania.movings;

import java.util.ArrayList;
import java.util.List;

import static java.time.Duration.ofMinutes;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import dungeonmania.DungeonManiaController;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


@TestInstance(value = Lifecycle.PER_CLASS)
public class HydraTest {
    public static final String HYDRA = "hydra";
    public static final String DUNGEON_ADVANCED = "advanced";
    public static final String DUNGEON_MAZE = "maze";
    public static final String DUNGEON_PORTAL = "portal";
    public static final String GAME_MODE_HARD = "Hard";

    @Test
    public void testEnsureHydraSpawns() {
        // at least one hydra must always spawn

        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_MAZE, GAME_MODE_HARD);

        int numEntities = response.getEntities().size();
        // at least one hydra must spawn
        assertTimeout(
            ofMinutes(1),
            () -> {
                DungeonResponse updatedResponse = tickGameUntilHydraSpawns(controller, response);
                assertTrue(updatedResponse != null);
                // hydra spawn must increase num entities
                assertTrue(updatedResponse.getEntities().size() > numEntities);
            }
        );
    }
    
    private List<Entity> sevenBySevenWallBoundary() {
        ArrayList<Entity> wallBorder = new ArrayList<>();
        
        // left border
        for(int i = 0; i < 7; i ++) {
            Wall wall = new Wall(new Position(0, i));
            wallBorder.add(wall);
        }
        
        // right border
        for(int i = 0; i < 7; i ++) {
            Wall wall = new Wall(new Position(6, i));
            wallBorder.add(wall);
        }

        // top border
        for(int i = 1; i < 6; i ++) {
            Wall wall = new Wall(new Position(i, 0));
            wallBorder.add(wall);
        }

        // bottom border
        for(int i = 1; i < 6; i ++) {
            Wall wall = new Wall(new Position(i, 6));
            wallBorder.add(wall);
        }

        return wallBorder;
    }

    public DungeonResponse tickGameUntilHydraSpawns(
        DungeonManiaController controller,
        DungeonResponse response
    ) {
        // Won't be an infinite loop as hydra guaranteed to spawn in 50 ticks
        DungeonResponse updatedResponse = null;
        while (true) {
            updatedResponse = controller.tick(null, Direction.NONE);
            List<EntityResponse> entities = updatedResponse.getEntities();
            EntityResponse hydra = getHydraEntity(entities);
            if (hydra != null) {
                return updatedResponse;
            }
        }
    }

    public EntityResponse getHydraEntity(List<EntityResponse> entities) {
        for (EntityResponse e : entities) {
            if (e.getPrefix().startsWith(HYDRA)) {
                return e;
            }
        }

        return null;
    }
}
