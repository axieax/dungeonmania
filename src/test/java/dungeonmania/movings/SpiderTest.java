package dungeonmania.movings;

import static java.time.Duration.ofMinutes;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.Spider;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.Door;
import dungeonmania.model.entities.statics.Exit;
import dungeonmania.model.entities.statics.FloorSwitch;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Peaceful;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(value = Lifecycle.PER_CLASS)
public class SpiderTest {

    public static final String SPIDER = "spider";
    public static final String DUNGEON_ADVANCED = "advanced";
    public static final String DUNGEON_MAZE = "maze";
    public static final String DUNGEON_PORTAL = "portal";
    public static final String GAME_MODE_PEACEFUL = "Peaceful";

    @Test
    public void testEnsureSpiderSpawns() {
        // at least one spider must always spawn

        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_MAZE, GAME_MODE_PEACEFUL);

        int numEntities = response.getEntities().size();
        // at least one spider must spawn
        assertTimeout(
            ofMinutes(1),
            () -> {
                DungeonResponse updatedResponse = tickGameUntilSpiderSpawns(controller, response);
                assertTrue(updatedResponse != null);
                // spider spawn must increase num entities
                assertTrue(updatedResponse.getEntities().size() > numEntities);
            }
        );
    }

    @Test
    public void testInitialSpiderMovement() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_ADVANCED, GAME_MODE_PEACEFUL);

        response = tickGameUntilSpiderSpawns(controller, response);
        List<EntityResponse> entities = response.getEntities();

        EntityResponse spider = getSpiderEntity(entities);
        Position spiderSpawnPos = spider.getPosition();

        response = controller.tick(null, Direction.NONE);
        entities = response.getEntities();
        // initially spider must always move 1 block upwards
        Position spiderPos = getSpiderPosition(entities);
        assertTrue(spiderPos.equals(spiderSpawnPos.translateBy(Direction.UP)));
    }

    // TODO: this may involved a bit of RNG? This test may fail depending on where
    // the spider spawns
    @Test
    public void testSpiderNeverMovesToInitalBlock() {
        // the distinguished movement of a spider never allows it go
        // back to the block upon which it spawned

        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_ADVANCED, GAME_MODE_PEACEFUL);

        response = tickGameUntilSpiderSpawns(controller, response);
        List<EntityResponse> entities = response.getEntities();

        EntityResponse spider = getSpiderEntity(entities);
        Position spiderSpawnPos = spider.getPosition(); // initial spawn position

        // spider can never go back to initial spawn position
        for (int i = 0; i < 20; i++) {
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
        DungeonResponse response = controller.newGame(DUNGEON_ADVANCED, GAME_MODE_PEACEFUL);

        response = tickGameUntilSpiderSpawns(controller, response);
        List<EntityResponse> entities = response.getEntities();

        EntityResponse spider = getSpiderEntity(entities);
        Position oldSpiderPos = spider.getPosition();
        
        // initially spider moves 1 block up
        response = controller.tick(null, Direction.NONE);
        entities = response.getEntities();
        Position spiderPos = getSpiderPosition(entities);
        assertTrue(spiderPos.equals(oldSpiderPos.translateBy(Direction.UP)));

        // next movement is to the right
        oldSpiderPos = spiderPos;
        response = controller.tick(null, Direction.NONE);
        entities = response.getEntities();
        spiderPos = getSpiderPosition(entities);
        assertTrue(spiderPos.equals(oldSpiderPos.translateBy(Direction.RIGHT)));

        // next movement is down twice
        for (int i = 0; i < 2; i++) {
            oldSpiderPos = spiderPos;
            response = controller.tick(null, Direction.NONE);
            entities = response.getEntities();
            spiderPos = getSpiderPosition(entities);
            assertTrue(spiderPos.equals(oldSpiderPos.translateBy(Direction.DOWN)));
        }

        // next movement is to the left twice
        for(int i = 0; i < 2; i++) {
            oldSpiderPos = spiderPos;
            response = controller.tick(null, Direction.NONE);
            entities = response.getEntities();
            spiderPos = getSpiderPosition(entities);
            assertTrue(spiderPos.equals(oldSpiderPos.translateBy(Direction.LEFT)));
        }

        // next movement is up twice
        for(int i = 0; i < 2; i ++) {
            oldSpiderPos = spiderPos;
            response = controller.tick(null, Direction.NONE);
            entities = response.getEntities();
            spiderPos = getSpiderPosition(entities);
            assertTrue(spiderPos.equals(oldSpiderPos.translateBy(Direction.UP)));
        }

        // to complete the circle, next movement one right
        oldSpiderPos = spiderPos;
        response = controller.tick(null, Direction.NONE);
        entities = response.getEntities();
        spiderPos = getSpiderPosition(entities);
        assertTrue(spiderPos.equals(oldSpiderPos.translateBy(Direction.RIGHT)));
    }

    @Test
    public void testSpiderPositionCompleteReverseCircle() {
        // spider reverses direction while going left
        Mode mode = new Peaceful();

        Game game = new Game(
            "game",
            sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Position boulderPos = new Position(4, 2);
        Boulder boulder = new Boulder(boulderPos);
        game.addEntity(boulder);

        Position initialSpiderPos = new Position(3, 3);
        Spider spider = new Spider(initialSpiderPos, mode.damageMultiplier(), player);
        
        game.addEntity(spider);
        assertTrue(spider.getPosition().equals(new Position(3, 3)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 2)));

        // reverse direction
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 2)));

        // remove boulder
        game.removeEntity(boulder);
        
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(2, 2)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(2, 3)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(2, 4)));
        
        // spider attempts to move into boulder but fails and so, stays in the same position
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 4)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 4)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 3)));
        
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 2)));

        // back to initial position
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 2)));
    }

    @Test
    public void testMovementThroughDoor() {
        // can move through closed doors
        Mode mode = new Peaceful();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);
        
        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Position doorPos = new Position(2, 1);
        Door door = new Door(doorPos, 0);
        game.addEntity(door);

        Position initialSpiderPos = new Position(2, 2);
        Spider spider = new Spider(initialSpiderPos, mode.damageMultiplier(), player);
        game.addEntity(spider);

        // after the first tick, spider should move straight up, into the door
        game.tick(null, Direction.NONE);

        assertTrue(spider.getPosition().equals(doorPos));
        assertTrue(game.getEntities(doorPos).size() == 2); // door and spider in same tile
    }

    @Test
    public void testMovementThroughSwitch() {
        // switches have no effect
        Mode mode = new Peaceful();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), new Peaceful());
        
        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Position switchPos = new Position(2, 1);
        FloorSwitch floorSwitch = new FloorSwitch(switchPos);
        game.addEntity(floorSwitch);

        Position initialSpiderPos = new Position(2, 2);
        Spider spider = new Spider(initialSpiderPos, mode.damageMultiplier(), player);
        game.addEntity(spider);

        // after the first tick, spider should move straight up, into the switch
        game.tick(null, Direction.NONE);

        assertTrue(spider.getPosition().equals(switchPos));
        assertTrue(game.getEntities(switchPos).size() == 2); // switch and spider in same tile
    }
    @Test
    public void testMovementThroughPortal() {
        // portals have no effect on the spider
        Mode mode = new Peaceful();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);
        
        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Portal portal1 = new Portal(new Position(2, 1), "blue");
        game.addEntity(portal1);

        Portal portal2 = new Portal(new Position(5, 1), "blue");
        game.addEntity(portal2);

        Position initialSpiderPos = new Position(2, 2);
        Spider spider = new Spider(initialSpiderPos, mode.damageMultiplier(), player);
        game.addEntity(spider);

        // after the first tick, spider should move straight up, into the portal and be teleported
        game.tick(null, Direction.NONE);

        assertTrue(spider.getPosition().equals(new Position(5, 0)));
    }

    @Test
    public void testSpiderMovementIntoExitNoEffect() {
        Mode mode = new Peaceful();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);
        
        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Position exitPos = new Position(2, 1);
        Exit exit = new Exit(exitPos);
        game.addEntity(exit);

        Position initialSpiderPos = new Position(2, 2);
        Spider spider = new Spider(initialSpiderPos, mode.damageMultiplier(), player);
        game.addEntity(spider);

        // after the first tick, spider should move straight up, into the exit
        game.tick(null, Direction.NONE);

        assertTrue(spider.getPosition().equals(exitPos));
        assertTrue(game.getEntities(exitPos).size() == 2); // exit and spider in same tile
    }

    @Test
    public void testBoulderReverseDirectionToLeft() {
        // spider reverses direction while moving right
        Mode mode = new Peaceful();

        Game game = new Game(
            "game",
            sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Position boulderPos = new Position(4, 2);
        Boulder boulder = new Boulder(boulderPos);
        game.addEntity(boulder);

        Position initialSpiderPos = new Position(3, 3);
        Spider spider = new Spider(initialSpiderPos, mode.damageMultiplier(), player);

        game.addEntity(spider);
        assertTrue(spider.getPosition().equals(new Position(3, 3)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 2)));

        // spider attempts to move into boulder but fails and so, stays in the same position
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 2)));
        
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(2, 2)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(2, 3)));
    }

    @Test
    public void testBoulderReverseDirectionToUp() {
        // spider reverses direction while going downwards
        Mode mode = new Peaceful();

        Game game = new Game(
            "game",
            sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Position boulderPos = new Position(4, 4);
        Boulder boulder = new Boulder(boulderPos);
        game.addEntity(boulder);

        Position initialSpiderPos = new Position(3, 3);
        Spider spider = new Spider(initialSpiderPos, mode.damageMultiplier(), player);
        
        game.addEntity(spider);
        assertTrue(spider.getPosition().equals(new Position(3, 3)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 2)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 2)));
        
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 3)));

        // spider attempts to move into boulder but fails and so, stays in the same position
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 3)));
        
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 2)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 2)));
    }

    @Test
    public void testBoulderReverseDirectionToRight() {
        // spider reverses direction while going left
        Mode mode = new Peaceful();

        Game game = new Game(
            "game",
            sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Position boulderPos = new Position(2, 4);
        Boulder boulder = new Boulder(boulderPos);
        game.addEntity(boulder);

        Position initialSpiderPos = new Position(3, 3);
        Spider spider = new Spider(initialSpiderPos, mode.damageMultiplier(), player);
        
        game.addEntity(spider);
        assertTrue(spider.getPosition().equals(new Position(3, 3)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 2)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 2)));
        
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 3)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 4)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 4)));
        
        // spider attempts to move into boulder but fails and so, stays in the same position
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 4)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 4)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 3)));
    }

    @Test
    public void testBlockedBothSidesByBoulder() {
        // after intially moving up, there are two boulder on either side
        // of the spider, and so the spider should stay in the same position
        Mode mode = new Peaceful();

        Game game = new Game(
            "game",
            sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Position boulder1Pos = new Position(2, 2);
        Boulder boulder1 = new Boulder(boulder1Pos);
        game.addEntity(boulder1);
        
        Position boulder2Pos = new Position(4, 2);
        Boulder boulder2 = new Boulder(boulder2Pos);
        game.addEntity(boulder2);

        Position initialSpiderPos = new Position(3, 3);
        Spider spider = new Spider(initialSpiderPos, mode.damageMultiplier(), player);
        
        game.addEntity(spider);
        assertTrue(spider.getPosition().equals(new Position(3, 3)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 2)));
        
        // cannot move, attempts to reverse
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 2)));
        
        // further attempts to move cause spider to stay in the same position
        for(int i = 0; i < 100; i ++) {
            game.tick(null, Direction.NONE);
            assertTrue(spider.getPosition().equals(new Position(3, 2)));
        }
    }

    @Test
    public void testReverseTwice() {
        Mode mode = new Peaceful();

        Game game = new Game(
            "game",
            sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Position boulder1Pos = new Position(2, 2);
        Boulder boulder1 = new Boulder(boulder1Pos);
        game.addEntity(boulder1);
        
        Position boulder2Pos = new Position(4, 2);
        Boulder boulder2 = new Boulder(boulder2Pos);
        game.addEntity(boulder2);

        Position initialSpiderPos = new Position(3, 3);
        Spider spider = new Spider(initialSpiderPos, mode.damageMultiplier(), player);
        
        game.addEntity(spider);
        assertTrue(spider.getPosition().equals(new Position(3, 3)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 2)));
        
        // cannot move, attempts to reverse
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 2)));

        // attempts to move onto tile with left boudler
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 2)));
        
        // remove right boulder
        game.removeEntity(boulder2);
       
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 2)));
    }

    @Test
    public void testInitialUpwardsMovementBlocked() {
        // a boulder exists in the tile above where the spider spawns, and so,
        // the spider should not move anywhere
        Mode mode = new Peaceful();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);
        
        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Position boulderPos = new Position(3, 2);
        Boulder boulder = new Boulder(boulderPos);
        game.addEntity(boulder);

        Position initialSpiderPos = new Position(3, 3);
        Spider spider = new Spider(initialSpiderPos, mode.damageMultiplier(), player);
        game.addEntity(spider);

        assertTrue(game.getEntities(initialSpiderPos).size() == 1);
        // spider cannot move up
        game.tick(null, Direction.NONE);
        assertTrue(game.getEntities(initialSpiderPos).size() == 1);
    }

    @Test
    public void testSpawnOnEdgeAndMoveOutsideMap() {
        // spider spawns on the edge of the map (0, 0) and is able to traverse
        // outside the walls
        Mode mode = new Peaceful();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);
        
        Player player = new Player(new Position(5, 5));
        game.addEntity(player);

        Position initialSpiderPos = new Position(0, 0);
        Spider spider = new Spider(initialSpiderPos, mode.damageMultiplier(), player);
        game.addEntity(spider);
        
        assertTrue(game.getEntities(initialSpiderPos).size() == 2); // wall and spider
        
        // moves up, outside of walls
        game.tick(null, Direction.NONE);
        assertTrue(game.getEntities(initialSpiderPos).size() == 1);
    }

    @Test
    public void testBoulderReplacementClockwiseMovement() {
        // Boulder placed at right bottom corner, then moved to top right when
        // spider attempts to reverse direction.
        // Throughout, the spider should maintain a clockwise movement,
        // and at no point should it move anti-clockwise
        Mode mode = new Peaceful();

        Game game = new Game(
            "game",
            sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Position boulderPos = new Position(4, 4);
        Boulder boulder = new Boulder(boulderPos);
        game.addEntity(boulder);

        Position initialSpiderPos = new Position(3, 3);
        Spider spider = new Spider(initialSpiderPos, mode.damageMultiplier(), player);
        
        game.addEntity(spider);
        assertTrue(spider.getPosition().equals(new Position(3, 3)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 2)));
        
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 2)));

        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 3)));

        // next tile has boulder, so spider should stay in same position
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 3)));

        // Place boulder at (4, 2) so that spider cannot reverse direction and
        // remove boulder at (4, 4)
        boulder.setPosition(new Position(4, 2));
        // attempt to move upwards fails
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 3)));
        
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(4, 4)));
        
        game.tick(null, Direction.NONE);
        assertTrue(spider.getPosition().equals(new Position(3, 4)));
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

    @Test
    public void interactWithSpiderNoAction() {
        // utilising the 'interact' method in spider should do nothing
        Mode mode = new Peaceful();

        Game game = new Game(
            "game",
            sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Position playerPos = new Position(1, 1);
        Player player = new Player(playerPos);
        game.addEntity(player);

        int numEntitiesAtPlayerPos = game.getEntities(playerPos).size();

        Position boulderPos = new Position(4, 4);
        Boulder boulder = new Boulder(boulderPos);
        game.addEntity(boulder);

        Position initialSpiderPos = new Position(3, 3);
        Spider spider = new Spider(initialSpiderPos, mode.damageMultiplier(), player);
        game.addEntity(spider);

        int numEntitiesAtSpiderPos = game.getEntities(initialSpiderPos).size();

        assertTrue(spider.getPosition().equals(new Position(3, 3)));

        spider.interact(game, player);
        // player and spider still exist
        assertTrue(game.getEntities(playerPos).size() == numEntitiesAtPlayerPos);
        assertTrue(game.getEntities(initialSpiderPos).size() == numEntitiesAtSpiderPos);
    }
    
    public DungeonResponse tickGameUntilSpiderSpawns(
        DungeonManiaController controller,
        DungeonResponse response
    ) {
        // Won't be an infinite loop as spider guaranteed to spawn
        DungeonResponse updatedResponse = null;
        while (true) {
            updatedResponse = controller.tick(null, Direction.NONE);
            List<EntityResponse> entities = updatedResponse.getEntities();
            EntityResponse spider = getSpiderEntity(entities);
            if (spider != null) {
                return updatedResponse;
            }
        }
    }

    private Position getSpiderPosition(List<EntityResponse> entities) {
        EntityResponse spider = getSpiderEntity(entities);
        if(spider == null) {
            return null;
        }
        return spider.getPosition();
    }

    public EntityResponse getSpiderEntity(List<EntityResponse> entities)
        throws IllegalArgumentException, InvalidActionException {
        for (EntityResponse e : entities) {
            if (e.getType().startsWith(SPIDER)) {
                return e;
            }
        }

        return null;
    }
}