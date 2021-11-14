package dungeonmania.movings;

import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.DungeonManiaController;
import dungeonmania.TestHelpers;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.collectables.equipment.Anduril;
import dungeonmania.model.entities.movings.Hydra;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.Door;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Hard;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Peaceful;
import dungeonmania.model.mode.Standard;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(value = Lifecycle.PER_CLASS)
public class HydraTest {

    public static final String HYDRA = "hydra";
    public static final int HYDRA_TICK_RATE = 50;
    public static final String DUNGEON_ADVANCED = "advanced";
    public static final String DUNGEON_MAZE = "maze";
    public static final String DUNGEON_PORTAL = "portal";
    public static final String GAME_MODE_HARD = "Hard";

    public static final String SPIDER = "spider";

    @Test
    public void testHydraSpawnRate() {
        Mode mode = new Hard();

        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);
        int numEntities = game.getEntities().size();

        // Hydra should spawn
        TestHelpers.gameTickMovement(game, Direction.NONE, HYDRA_TICK_RATE);

        // Any other movingEntites that spawn will be removed
        List<Entity> toRemove = new ArrayList<>();
        for (Entity e : game.getEntities()) {
            if (e instanceof MovingEntity && !e.getType().equals(HYDRA) && !(e instanceof Player)) {
                toRemove.add(e);
            }
        }

        for (Entity e : toRemove) game.removeEntity(e);

        int numEntitesAfterFiftyTicks = game.getEntities().size();
        assertTrue(numEntitesAfterFiftyTicks == numEntities + 1);
    }

    @Test
    public void testHydraOnlySpawnsHardMode() {
        List<Mode> modes = Arrays.asList(new Peaceful(), new Standard());

        for (Mode m : modes) {
            Game game = new Game(
                "game",
                TestHelpers.sevenBySevenWallBoundary(),
                new ExitCondition(),
                m
            );

            Player player = new Player(new Position(1, 1), m.initialHealth());
            game.addEntity(player);
            int numEntities = game.getEntities().size();

            // Hydra should not spawn after 50 ticks
            for (int i = 0; i < HYDRA_TICK_RATE; i++) {
                game.tick(null, Direction.NONE);
            }

            // Any other movingEntites that spawn will be removed (other than player)
            List<Entity> toRemove = new ArrayList<>();
            for (Entity e : game.getEntities()) {
                if (
                    e instanceof MovingEntity &&
                    !e.getType().equals(HYDRA) &&
                    !(e instanceof Player)
                ) {
                    toRemove.add(e);
                }
            }

            for (Entity e : toRemove) game.removeEntity(e);

            int numEntitesAfterFiftyTicks = game.getEntities().size();
            assertTrue(numEntitesAfterFiftyTicks == numEntities);
        }
    }

    @Test
    public void testBasicMovement() {
        Mode mode = new Hard();

        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        Position hydraPos = new Position(5, 5);
        Hydra hydra = new Hydra(hydraPos, mode.damageMultiplier(), player);
        game.addEntity(hydra);

        game.tick(null, Direction.RIGHT);
        List<Entity> entitiesAtOldZombiePos = game.getEntities(hydraPos);

        // zombie should change position as there exists an open tile
        assertTrue(entitiesAtOldZombiePos.size() == 0);
    }

    @Test
    public void testWallBlockingMovement() {
        // if a hydra is surrounded by a wall, it should not move anywhere
        Mode mode = new Hard();

        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        Position hydraPos = new Position(5, 5);
        Hydra hydra = new Hydra(hydraPos, mode.damageMultiplier(), player);
        game.addEntity(hydra);
        int numEnties = game.getEntities(hydraPos).size();

        // surround hydra with a wall
        game.addEntity(new Wall(new Position(4, 4)));
        game.addEntity(new Wall(new Position(4, 5)));
        game.addEntity(new Wall(new Position(5, 4)));

        // hydra should stay in the tile at all times
        for (int i = 0; i < 10; i++) {
            game.tick(null, Direction.NONE);
            assertTrue(game.getEntities(hydraPos).size() == numEnties);
        }
    }

    @Test
    public void testHydraCannotWalkThroughClosedDoor() {
        Mode mode = new Standard();
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        Position hydraPos = new Position(5, 5);
        Hydra hydra = new Hydra(hydraPos, mode.damageMultiplier(), player);

        assertTrue(game.getEntities(hydraPos).size() == 0);
        game.addEntity(hydra);
        assertTrue(game.getEntities(hydraPos).size() > 0);

        game.addEntity(new Wall(new Position(4, 3)));
        game.addEntity(new Wall(new Position(4, 4)));
        game.addEntity(new Wall(new Position(4, 5)));

        Position doorPos = new Position(5, 4);
        Key key = new Key(new Position(1, 5), 1);
        game.addEntity(new Door(doorPos, 1));
        game.addEntity(key);

        // hydra is trapped in the corner and should not move in further ticks
        for (int i = 0; i < 5; i++) {
            hydra.tick(game);
            assertTrue(game.getEntities(hydraPos).size() > 0);
        }
    }

    @Test
    public void testZombieCanWalkThroughOpenDoor() {
        Mode mode = new Hard();

        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(4, 2), mode.initialHealth());
        game.addEntity(player);
        game.addEntity(new Key(new Position(4, 3), 1));

        Position doorPos = new Position(4, 4);
        Door door = new Door(doorPos, 1);
        game.addEntity(door);
        game.tick(null, Direction.DOWN);

        assertTrue(player.hasItemQuantity("key", 1));
        game.tick(null, Direction.DOWN);
        assertTrue(door.getPosition().equals(player.getPosition()));
        assertTrue(door.isOpen());

        game.tick(null, Direction.UP);

        // surround door with walls, so the only direction that Hydra can move is up
        game.addEntity(new Wall(new Position(3, 4)));
        game.addEntity(new Wall(new Position(3, 5)));
        game.addEntity(new Wall(new Position(5, 4)));
        game.addEntity(new Wall(new Position(5, 5)));

        Hydra hydra = new Hydra(new Position(4, 5), mode.damageMultiplier(), player);
        game.addEntity(hydra);

        game.tick(null, Direction.NONE);
        assertTrue(game.getEntities(doorPos).size() == 2);
    }

    @Test
    public void testPortalNoEffect() {
        // portals have no effect on hydra
        Mode mode = new Hard();
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        Position hydraPos = new Position(5, 5);
        Hydra hydra = new Hydra(hydraPos, mode.damageMultiplier(), player);
        assertTrue(game.getEntities(hydraPos).size() == 0);
        game.addEntity(hydra);
        assertTrue(game.getEntities(hydraPos).size() > 0);

        game.addEntity(new Wall(new Position(4, 3)));
        game.addEntity(new Wall(new Position(4, 4)));
        game.addEntity(new Wall(new Position(4, 5)));
        game.addEntity(new Wall(new Position(5, 3)));

        Position portalPos = new Position(5, 4);
        Portal portal = new Portal(portalPos, "blue");
        game.addEntity(portal);

        game.tick(null, Direction.NONE);

        // the only option for the hydra is to move to the portal which it cannot pass through
        assertTrue(hydra.getPosition().equals(hydraPos)); // portal has no effect
    }

    @Test
    public void testHydraCannotMoveBoulder() {
        Mode mode = new Hard();
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        Position hydraPos = new Position(5, 5);
        Hydra hydra = new Hydra(hydraPos, mode.damageMultiplier(), player);

        assertTrue(game.getEntities(hydraPos).size() == 0);
        game.addEntity(hydra);
        assertTrue(game.getEntities(hydraPos).size() > 0);

        game.addEntity(new Wall(new Position(4, 3)));
        game.addEntity(new Wall(new Position(4, 4)));
        game.addEntity(new Wall(new Position(4, 5)));

        Position boulderPos = new Position(5, 4);
        Boulder boulder = new Boulder(boulderPos);
        game.addEntity(boulder);

        game.tick(null, Direction.NONE);

        // zombie should stay in its position, as it cannot move a boulder
        assertTrue(hydra.getPosition().equals(hydraPos));
    }

    @Test
    public void testHydraHealthInBattle() {
        // Test the head regrowing ability of of a hydra during battle by
        // conducting 1000 consecutive battles.
        // The battles are exactly the same but should result in different
        // player healths (as the hydra can grow/regrow its head).
        // There is still a small chance that the player will have the
        // same health after every battle - but this is very unlikely
        // and will mean that the hydra is not regrowing its head randomly.
        Set<Integer> playerHealthAfterBattle = new HashSet<>();

        for (int i = 0; i < 1000; i++) {
            Mode mode = new Hard();
            Game game = new Game(
                "game",
                TestHelpers.sevenBySevenWallBoundary(),
                new ExitCondition(),
                mode
            );

            Position playerPos = new Position(1, 1);
            Player player = new Player(playerPos, mode.initialHealth());
            game.addEntity(player);

            Position hydraPos = new Position(1, 2);
            Hydra hydra = new Hydra(hydraPos, mode.damageMultiplier(), player);
            game.addEntity(hydra);

            // block hydra so the only direction that it can move is up, into the player
            game.addEntity(new Wall(new Position(1, 3)));
            game.addEntity(new Wall(new Position(2, 1)));
            game.addEntity(new Wall(new Position(2, 2)));
            game.addEntity(new Wall(new Position(2, 3)));

            // battle should occur in next tick, and the player will be left with a certain
            // health
            game.tick(null, Direction.NONE);
            playerHealthAfterBattle.add(player.getHealth());
        }

        // out of 1000 battles, player must be at least different once
        assertTrue(playerHealthAfterBattle.size() > 1);
    }

    @Test
    public void testAndurilBattle() {
        // since the player has an anduril in their inventory,
        // every battle should result in the same outcome
        // i.e. player health is the same after every battle
        Set<Integer> playerHealthAfterBattle = new HashSet<>();

        for (int i = 0; i < 1000; i++) {
            Mode mode = new Hard();
            Game game = new Game(
                "game",
                TestHelpers.sevenBySevenWallBoundary(),
                new ExitCondition(),
                mode
            );

            Position playerPos = new Position(1, 1);
            Player player = new Player(playerPos, mode.initialHealth());
            game.addEntity(player);

            Position andurilPos = new Position(1, 2);
            Anduril anduril = new Anduril(andurilPos);
            game.addEntity(anduril);

            // move player downwards and pick up the anduril
            game.tick(null, Direction.DOWN);

            Position hydraPos = new Position(1, 3);
            Hydra hydra = new Hydra(hydraPos, mode.damageMultiplier(), player);
            game.addEntity(hydra);

            // block hydra so the only direction that it can move is up, into the player
            game.addEntity(new Wall(new Position(1, 4)));
            game.addEntity(new Wall(new Position(2, 1)));
            game.addEntity(new Wall(new Position(2, 2)));
            game.addEntity(new Wall(new Position(2, 3)));
            game.addEntity(new Wall(new Position(2, 4)));

            // battle should occur in next tick, and the player will be left with a certain
            // health
            game.tick(null, Direction.NONE);
            assertTrue(game.getEntities(hydraPos).size() == 0);
            assertTrue(game.getEntities(playerPos.translateBy(Direction.DOWN)).size() == 1);
            playerHealthAfterBattle.add(player.getHealth());
        }

        // hydra cannot regrow it's head, and so, every battle results in the same player health
        assertTrue(playerHealthAfterBattle.size() == 1);
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
            if (e.getType().startsWith(HYDRA)) {
                return e;
            }
        }

        return null;
    }
}
