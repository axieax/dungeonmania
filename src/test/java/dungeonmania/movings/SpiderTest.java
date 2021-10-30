package dungeonmania;

import static java.time.Duration.ofMinutes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Entity;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(value = Lifecycle.PER_CLASS)
public class SpiderTest {

    static final String SPIDER_1 = "spider_1";
    static final String DUNGEON_NAME = "advanced";
    static final String GAME_MODE = "peaceful";

    @Test
    public void testEnsureSpiderSpawns() {
        // at least one spider must always spawn

        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_NAME, GAME_MODE);

        // at least one spider must spawn
        assertTimeout(
            ofMinutes(1),
            () -> {
                tickGameUntilSpiderSpawns(controller, response);
            }
        );
    }

    @Test
    public void testInitialSpiderMovement() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_NAME, GAME_MODE);

        response = tickGameUntilSpiderSpawns(controller, response);
        List<EntityResponse> entities = response.getEntities();

        EntityResponse spider = getSpiderEntity(entities);
        Position spiderSpawnPos = spider.getPosition();

        response = controller.tick(null, Direction.NONE);

        // initially spider must always move 1 block upwards
        Position spiderPos = getSpiderPosition(entities);
        assertTrue(spiderPos.equals(spiderSpawnPos.translateBy(Direction.UP)));
    }

    @Test
    public void testSpiderNeverMovesToInitalBlock() {
        // the distinguished movement of a spider never allows it go
        // back to the block upon which it spawned

        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_NAME, GAME_MODE);

        response = tickGameUntilSpiderSpawns(controller, response);
        List<EntityResponse> entities = response.getEntities();

        EntityResponse spider = getSpiderEntity(entities);
        Position spiderSpawnPos = spider.getPosition(); // initial spawn position

        // spider can never go back to initial spawn position
        for (int i = 0; i < 50; i++) {
            response = controller.tick(null, Direction.NONE);
            entities = response.getEntities();
            Position spiderPos = getSpiderPosition(entities);

            assertFalse(spiderPos.equals(spiderSpawnPos));
        }
    }

    @Test
    public void testSpiderPositionCompleteCircle() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_NAME, GAME_MODE);

        response = tickGameUntilSpiderSpawns(controller, response);
        List<EntityResponse> entities = response.getEntities();

        EntityResponse spider = getSpiderEntity(entities);
        Position oldSpiderPos = spider.getPosition();

        // initially spider moves 1 block up
        response = controller.tick(null, Direction.NONE);
        Position spiderPos = getSpiderPosition(entities);
        assertTrue(spiderPos.equals(oldSpiderPos.translateBy(Direction.UP)));

        // next movement is to the right
        response = controller.tick(null, Direction.NONE);
        spiderPos = getSpiderPosition(entities);
        assertTrue(spiderPos.equals(oldSpiderPos.translateBy(Direction.RIGHT)));

        // next movement is down twice
        for (int i = 0; i < 2; i++) {
            response = controller.tick(null, Direction.NONE);
            spiderPos = getSpiderPosition(entities);
            assertTrue(spiderPos.equals(oldSpiderPos.translateBy(Direction.DOWN)));
        }

        // next movement is to the left twice
        response = controller.tick(null, Direction.NONE);
        spiderPos = getSpiderPosition(entities);
        assertTrue(spiderPos.equals(oldSpiderPos.translateBy(Direction.LEFT)));

        // next movement is up twice
        response = controller.tick(null, Direction.NONE);
        spiderPos = getSpiderPosition(entities);
        assertTrue(spiderPos.equals(oldSpiderPos.translateBy(Direction.UP)));

        // to complete the circle, next movement one right
        response = controller.tick(null, Direction.NONE);
        spiderPos = getSpiderPosition(entities);
        assertTrue(spiderPos.equals(oldSpiderPos.translateBy(Direction.RIGHT)));
    }

    @Test
    public void testSpiderMovementThroughVariousEntities() {
        // spiders can traverse through walls, doors, switches, portals and exits
    }

    @Test
    public void testSpiderMovementIntoExitNoEffect() {}

    @Test
    public void testSpiderBoulderReverseDirection() {}

    @Test
    public void testMovementIntoSpaceWithEntity() {}

    @Test
    public void testMaxSpiderSpawn() {}

    @Test
    public void testSpawnOnEdgeAndMoveOutsideMap() {
        // https://edstem.org/au/courses/7065/discussion/645072
        // #4
    }

    public DungeonResponse tickGameUntilSpiderSpawns(
        DungeonManiaController controller,
        DungeonResponse response
    ) {
        // Won't be an infinite loop as spider guaranteed to spawn
        DungeonResponse updatedResponse = null;
        while (true) {
            updatedResponse = controller.tick(null, Direction.LEFT);
            List<EntityResponse> entities = response.getEntities();
            EntityResponse spider = getSpiderEntity(entities);
            if (spider != null) {
                return updatedResponse;
            }
        }
    }

    private Position getSpiderPosition(List<EntityResponse> entities) {
        EntityResponse spider = getSpiderEntity(entities);
        return spider.getPosition();
    }

    public EntityResponse getSpiderEntity(List<EntityResponse> entities)
        throws IllegalArgumentException, InvalidActionException {
        for (EntityResponse e : entities) {
            if (e.getId() == SPIDER_1) {
                return e;
            }
        }

        return null;
    }
}
