package dungeonmania.movings;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.TestHelpers;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.collectables.SunStone;
import dungeonmania.model.entities.collectables.TheOneRing;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.collectables.Wood;
import dungeonmania.model.entities.movings.Assassin;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.Door;
import dungeonmania.model.entities.statics.Exit;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(value = Lifecycle.PER_CLASS)
public class AssassinTest {

    public static final String ASSASSIN = "assassin";
    public static final String CHARACTER_TYPE = "player";
    public static final String DUNGEON_NAME = "advanced";
    public static final String GAME_MODE = "Peaceful";

    @Test
    public void testDoesNotSpawnWithNoEnemies() {
        Mode mode = new Standard();
        // Assassins only spawn in dungeons with at least one enemy
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );
        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        assertTrue(game.getAllEnemies().size() == 0);

        int numEntities = game.getEntities().size();
        for (int i = 0; i < 18; i++) {
            game.tick(null, Direction.NONE);
            assertTrue(game.getAllEnemies().size() == 0);
            assertTrue(game.getEntities().size() == numEntities);
        }
    }

    @Test
    public void testSpawnAssassin() {
        Mode mode = new Standard();
        List<Entity> entities = TestHelpers.sevenBySevenWallBoundary();
        Player player = new Player(new Position(1, 1), mode.initialHealth());
        entities.add(player);

        Game game = new Game("game", entities, new ExitCondition(), mode);

        // Move player away from spawning location (otherwise mercenary will immediately die after spawning)
        TestHelpers.gameTickMovement(game, Direction.RIGHT, 3);

        // Check that assassins will spawn eventually
        // Note that there will be spiders in the dungeon (which means there are enemies in the dungeon)
        // Since there's a 30% chance assassins will spawn instead of a mercenary, this means that
        // the chance of this not happening in 600 ticks (20 chances to spawn) is 0.7^20 = 0.08%
        Assassin assassin = null;
        for (int i = 0; i < 3000; i++) {
            game.tick(null, Direction.NONE);
            for (Entity entity : game.getEntities()) {
                if (entity.getType().startsWith(ASSASSIN)) {
                    assassin = (Assassin) entity;
                    assertTrue(assassin != null);
                    // Also ensure that it spawns at the player's initial spawning location
                    assertTrue(assassin.getPosition().equals(new Position(1, 1)));
                    return;
                }
            }
        }
    }

    @Test
    public void testAssassinSpawnWithArmourIntermittently() {
        // Assassins have a 25% chance to spawn with armour
        Mode mode = new Standard();

        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(3, 1), mode.initialHealth());
        game.addEntity(player);

        game.addEntity(new Wall(new Position(2, 1)));
        game.addEntity(new Wall(new Position(2, 2)));
        game.addEntity(new Wall(new Position(2, 3)));

        game.addEntity(new Wall(new Position(3, 3)));

        game.addEntity(new Wall(new Position(4, 1)));
        game.addEntity(new Wall(new Position(4, 2)));
        game.addEntity(new Wall(new Position(4, 3)));

        // The chance of no assassin dropping armour is 0.75^100 = 0.00000000003%
        boolean hasArmour = false;
        for (int i = 0; i < 100; i++) {
            game.addEntity(new Assassin(new Position(3, 2), mode.damageMultiplier(), player));
            game.tick(null, Direction.NONE);

            for (ItemResponse item : player.getInventoryResponses()) {
                if (item.getType().equals("armour")) {
                    hasArmour = true;
                    break;
                }
            }

            // Remove any other moving entities that have spawned
            List<Entity> toRemove = new ArrayList<>();
            for (Entity e : game.getEntities()) {
                if (
                    e instanceof MovingEntity && !(e instanceof Player) && !(e instanceof Assassin)
                ) toRemove.add(e);
            }

            for (Entity e : toRemove) game.removeEntity(e);

            // Regenerate player health
            player.setHealth(player.getMaxCharacterHealth());
        }

        assertTrue(player.isAlive());
        assertTrue(hasArmour);
    }

    @Test
    public void testSimpleMovement() {
        Mode mode = new Standard();
        // Distance between the assassin and player should decrease per tick/movement
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        Assassin assassin = new Assassin(new Position(3, 3), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        game.tick(null, Direction.RIGHT);

        // Assassin should move to the left or upwards
        assertTrue(
            (assassin.getX() == 2 && assassin.getY() == 3) ||
            (assassin.getX() == 3 && assassin.getY() == 2)
        );
    }

    @Test
    public void testSimpleMovementFollowFar() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        Assassin assassin = new Assassin(new Position(1, 10), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        game.tick(null, Direction.NONE);

        // Assassin should move to the left to follow the player
        assertTrue(assassin.getPosition().equals(new Position(1, 9)));

        game.tick(null, Direction.NONE);

        // Assassin should move to the left to follow the player
        assertTrue(assassin.getPosition().equals(new Position(1, 8)));
    }

    @Test
    public void testSimpleMovementFollowBattle() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        Assassin assassin = new Assassin(new Position(1, 2), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        game.tick(null, Direction.NONE);

        // Assassin should battle player
        assertTrue(!game.getEntities().contains(assassin) || !game.getEntities().contains(player));
    }

    @Test
    public void testAssassinSimpleWall() {
        Mode mode = new Standard();
        // Wall with 1 gap exists and assassin should go directly to player, and not move
        // outside/go through the gap
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        // Create horizontal wall with 1 gap near the right game border between the player and assassin
        for (int i = 0; i < 4; i++) {
            game.addEntity(new Wall(new Position(i + 1, 2)));
        }

        Assassin assassin = new Assassin(new Position(4, 1), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        // Assassin now at same horizontal level as player and any further ticks reduce the horizontal distance
        game.tick(null, Direction.NONE);
        assertTrue(assassin.getX() == 3);
        game.tick(null, Direction.NONE);
        assertTrue(assassin.getX() == 2);
        game.tick(null, Direction.NONE);
        // Same position as player but assassin should be killed
        assertTrue(assassin.getX() == 1);
    }

    @Test
    public void testAssassinSimpleBoulder() {
        Mode mode = new Standard();
        // Boulder with 1 gap exists and assassin should go directly to player, and not move
        // outside/go through the gap
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        // Create horizontal boulder with 1 gap near the right game border between the player and assassin
        for (int i = 0; i < 4; i++) {
            game.addEntity(new Boulder(new Position(i + 1, 2)));
        }

        Assassin assassin = new Assassin(new Position(4, 1), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        // Assassin now at same horizontal level as player and any further ticks reduce the horizontal distance
        game.tick(null, Direction.NONE);
        assertTrue(assassin.getX() == 3);
        game.tick(null, Direction.NONE);
        assertTrue(assassin.getX() == 2);
        game.tick(null, Direction.NONE);
        // Same position as player but assassin should be killed
        assertTrue(assassin.getX() == 1);
    }

    @Test
    public void testBribeWithoutOneRing() {
        Mode mode = new Standard();
        // Character attemps to bribe assassin without TheOneRing should throw an exception
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        game.addEntity(new Treasure(new Position(1, 2)));

        // Player collects coin
        player.move(game, Direction.DOWN);

        Assassin assassin = new Assassin(new Position(2, 1), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        // Assassin in adjacent tile, so attempt bribe
        assertThrows(InvalidActionException.class, () -> game.interact(assassin.getId()));
    }

    @Test
    public void testBribeWithoutTreasure() {
        Mode mode = new Standard();
        // Character attemps to bribe assassin without treasure should throw an exception
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        game.addEntity(new TheOneRing(new Position(1, 2)));

        // Player collects TheOneRing
        player.move(game, Direction.DOWN);

        Assassin assassin = new Assassin(new Position(2, 1), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        // Assassin in adjacent tile, so attempt bribe
        assertThrows(InvalidActionException.class, () -> game.interact(assassin.getId()));
    }

    @Test
    public void testInteractAssassinNotAdjacent() {
        // InvalidActionException if the player is not within 2 cardinal
        // tiles to the assassin and they are bribing
        Mode mode = new Standard();

        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Position playerPos = new Position(1, 1);
        Player player = new Player(playerPos, mode.initialHealth());
        game.addEntity(player);

        game.addEntity(new Treasure(new Position(1, 2)));
        game.addEntity(new TheOneRing(new Position(1, 3)));

        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);

        Position assassinPos = new Position(5, 5);
        Assassin assassin = new Assassin(assassinPos, mode.damageMultiplier(), player);
        game.addEntity(assassin);

        // Assassin too far away from character
        assertThrows(InvalidActionException.class, () -> game.interact(assassin.getId()));
    }

    @Test
    public void testBribedAssassinDoesNotAttack() {
        Mode mode = new Standard();
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        Assassin assassin = new Assassin(new Position(5, 1), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        game.addEntity(new Treasure(new Position(1, 2)));
        game.addEntity(new Treasure(new Position(1, 3)));
        game.addEntity(new Treasure(new Position(1, 4)));

        // Make player collect all 3 coins
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);

        game.addEntity(new TheOneRing(new Position(2, 4)));

        // Make player collect TheOneRing
        player.move(game, Direction.RIGHT);

        Position updatedPlayerPos = new Position(2, 4);

        while (!game.getCardinallyAdjacentEntities(player.getPosition()).contains(assassin)) {
            game.tick(null, Direction.NONE);
        }

        int playerHealth = player.getHealth();

        // Assassin in adjacent tile, so bribe (player stil at tile)
        game.interact(assassin.getId());
        assertTrue(game.getEntities(updatedPlayerPos).size() == 1);

        // Assassin will not attack the player
        game.tick(null, Direction.NONE);
        assertTrue(player.getHealth() == playerHealth);
        game.tick(null, Direction.NONE);
        assertTrue(player.getHealth() == playerHealth);
        game.tick(null, Direction.NONE);
        assertTrue(player.getHealth() == playerHealth);
    }

    @Test
    public void testBribedMovement() {
        Mode mode = new Standard();
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        Assassin assassin = new Assassin(new Position(5, 1), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        game.addEntity(new Treasure(new Position(1, 2)));
        game.addEntity(new Treasure(new Position(1, 3)));
        game.addEntity(new Treasure(new Position(1, 4)));

        // Make player collect all 3 coins
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);

        game.addEntity(new TheOneRing(new Position(2, 4)));
        game.addEntity(new TheOneRing(new Position(3, 4)));

        // Make player collect TheOneRing
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.RIGHT);

        Position updatedPlayerPos = new Position(3, 4);

        while (!game.getCardinallyAdjacentEntities(player.getPosition()).contains(assassin)) {
            game.tick(null, Direction.NONE);
        }

        // Assassin in adjacent tile, so bribe (player still at original tile)
        game.interact(assassin.getId());
        assertTrue(game.getEntities(updatedPlayerPos).size() == 1);

        // Nothing should happen if the assassin gets bribed again
        game.interact(assassin.getId());

        // Assassin stays either next to or on top of the player regardless of where the latter moves
        // Since assassin is bribed, it will not engage in battle with the player
        List<Direction> possibleDirections = Arrays.asList(
            Direction.UP,
            Direction.RIGHT,
            Direction.LEFT,
            Direction.DOWN
        );
        Random rand = new Random(5);
        for (int i = 0; i < 100; i++) {
            int index = rand.nextInt(100) % 4;
            Direction movementDirection = possibleDirections.get(index);

            game.tick(null, movementDirection);

            // Exit the loop if the player or assassin has died
            if (
                game.getEntity(player.getId()) == null || game.getEntity(assassin.getId()) == null
            ) {
                break;
            }

            List<Entity> adjacentEntites = game.getCardinallyAdjacentEntities(player.getPosition());
            int numEntitesAtPlayerPos = game.getEntities(player.getPosition()).size();

            // Assassin will always be adjacent to or at the same position as the player since it will always follow it
            // Note that we have the number of entities at the player position is >= 2 since spiders may spawn
            assertTrue(adjacentEntites.contains(assassin) || numEntitesAtPlayerPos >= 2);
        }
    }

    @Test
    public void testCannotMoveThroughExit() {
        Mode mode = new Standard();
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Position playerPos = new Position(1, 1);
        Player player = new Player(playerPos, mode.initialHealth());
        game.addEntity(player);

        Assassin assassin = new Assassin(new Position(1, 1), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        Position exitPos = new Position(1, 2);
        Exit exit = new Exit(exitPos);
        game.addEntity(exit);

        assertTrue(game.getEntities(exitPos).size() == 1);
        assassin.setPosition(exitPos);
        assertTrue(game.getEntities(exitPos).size() == 2);
    }

    @Test
    public void testCannotMoveThroughClosedDoor() {
        Mode mode = new Standard();
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Position playerPos = new Position(5, 5);
        Player player = new Player(playerPos, mode.initialHealth());
        game.addEntity(player);

        Assassin assassin = new Assassin(new Position(1, 1), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        // Surround assassin and door with wall
        game.addEntity(new Wall(new Position(1, 2)));
        game.addEntity(new Wall(new Position(2, 2)));
        game.addEntity(new Wall(new Position(3, 2)));
        game.addEntity(new Wall(new Position(3, 1)));

        Position doorPos = new Position(2, 1);
        Door door = new Door(doorPos, 0);
        game.addEntity(door);

        assertTrue(game.getEntities(doorPos).size() == 1);

        // Assassin should not be able to go in the door position
        for (int i = 0; i < 100; i++) {
            game.tick(null, Direction.NONE);

            // Exit loop if either the player or assassin has died
            if (
                game.getEntity(player.getId()) == null || game.getEntity(assassin.getId()) == null
            ) {
                break;
            }

            assertTrue(!game.getEntity(assassin.getId()).getPosition().equals(doorPos));
        }
    }

    @Test
    public void testSimpleFight() {
        Mode mode = new Standard();
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Position playerPos = new Position(1, 1);
        Player player = new Player(playerPos, mode.initialHealth());
        game.addEntity(player);

        Position assassinPos = new Position(2, 1);
        Assassin assassin = new Assassin(assassinPos, mode.damageMultiplier(), player);
        game.addEntity(assassin);

        assertTrue(game.getEntities(playerPos).size() == 1);
        assertTrue(game.getEntities(assassinPos).size() == 1);
        game.tick(null, Direction.NONE);

        // Assassin should move towards player, the two should fight and character should win
        assertTrue(game.getEntities(playerPos).size() == 1);
        assertTrue(game.getEntity(assassin.getId()) == null);
    }

    @Test
    public void testMindControlledMovementAndAttack() {
        Mode mode = new Standard();
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        Assassin assassin = new Assassin(new Position(5, 1), mode.damageMultiplier(), player);
        game.addEntity(assassin);

        game.addEntity(new Wood(new Position(2, 1)));
        game.addEntity(new Key(new Position(2, 2), 1));
        game.addEntity(new SunStone(new Position(1, 2)));

        // Player collects all items and builds a sceptre
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.LEFT);

        game.build("sceptre");

        // Player uses the sceptre to mind control the assassin
        Position updatedPlayerPos = new Position(1, 2);
        game.interact(assassin.getId());

        // The distance between the player and the assassin should decrease
        int distance = assassin.getDistanceToPlayer(game, updatedPlayerPos);
        for (int i = 0; i < 10; i++) {
            game.tick(null, Direction.NONE);
            assertTrue(assassin.getDistanceToPlayer(game, updatedPlayerPos) <= distance);
            distance = assassin.getDistanceToPlayer(game, updatedPlayerPos);
        }

        // After 10 ticks, the assassin will no longer be mind controlled
        // It will battle with the player and will consequently die
        game.tick(null, Direction.NONE);

        assertTrue(game.getEntity(assassin.getId()) == null);
    }
}
