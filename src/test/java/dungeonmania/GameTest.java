package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.List;
import org.junit.jupiter.api.Test;

public class GameTest {

    /**
     * Test zombie spawn rate in Standard Game Mode
     */
    @Test
    public void testStandardGameMode() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("zombie", "Standard"));

        DungeonResponse responseOne = controller.tick(null, Direction.NONE);
        assertNull(TestHelpers.findFirstInstance(responseOne.getEntities(), "zombie_toast"));

        for (int i = 0; i < 18; i++) {
            DungeonResponse responseTwo = controller.tick(null, Direction.NONE);
            assertNull(TestHelpers.findFirstInstance(responseTwo.getEntities(), "zombie_toast"));
        }

        // 20 ticks have passed
        DungeonResponse responseThree = controller.tick(null, Direction.NONE);
        assertNotNull(TestHelpers.findFirstInstance(responseThree.getEntities(), "zombie_toast"));
    }

    /**
     * This tests Zombie Spawn Rate in Peaceful Game Mode
     */
    @Test
    public void testPeacefulGameMode() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("zombie", "Peaceful"));

        DungeonResponse responseOne = controller.tick(null, Direction.NONE);
        assertNull(TestHelpers.findFirstInstance(responseOne.getEntities(), "zombie_toast"));

        responseOne.getEntities();
        for (int i = 0; i < 18; i++) {
            DungeonResponse responseTwo = controller.tick(null, Direction.NONE);
            assertNull(TestHelpers.findFirstInstance(responseTwo.getEntities(), "zombie_toast"));
        }

        // 20 ticks have passed
        DungeonResponse responseThree = controller.tick(null, Direction.NONE);
        assertNotNull(TestHelpers.findFirstInstance(responseThree.getEntities(), "zombie_toast"));
    }

    /**
     * This test ensures the game adopts the features of a hard game mode.
     * Such as zombies spawning every 15 ticks
     */
    @Test
    public void testHardGameMode() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("zombie", "Hard"));
        DungeonResponse responseOne = controller.tick(null, Direction.NONE);
        assertNull(TestHelpers.findFirstInstance(responseOne.getEntities(), "zombie_toast"));

        responseOne.getEntities();
        for (int i = 0; i < 13; i++) {
            DungeonResponse responseTwo = controller.tick(null, Direction.NONE);
            assertNull(TestHelpers.findFirstInstance(responseTwo.getEntities(), "zombie_toast"));
        }

        DungeonResponse responseThree = controller.tick(null, Direction.NONE);
        // 15 ticks have passed
        assertNotNull(TestHelpers.findFirstInstance(responseThree.getEntities(), "zombie_toast"));
    }

    /**
     * Tests that game produces the correct number of cardinally adjacent entities
     * in the advanced dungeon
     */
    @Test
    public void testCardinallyAdjacentEntitiesInAdvancedDungeon() {
        Mode mode = new Standard();
        Game newGame = new Game(
            "advanced",
            EntityFactory.extractEntities("advanced", mode),
            null,
            mode
        );

        Player gamePlayer = newGame.getCharacter();

        List<Entity> cardinallyAdjacentEntities = newGame.getCardinallyAdjacentEntities(
            gamePlayer.getPosition()
        );
        assertEquals(2, cardinallyAdjacentEntities.size());

        newGame.tick(null, Direction.DOWN);

        cardinallyAdjacentEntities =
            newGame.getCardinallyAdjacentEntities(gamePlayer.getPosition());
        assertEquals(1, cardinallyAdjacentEntities.size());

        TestHelpers.gameTickMovement(newGame, Direction.DOWN, 3);
        newGame.tick(null, Direction.RIGHT);

        cardinallyAdjacentEntities =
            newGame.getCardinallyAdjacentEntities(gamePlayer.getPosition());
        assertEquals(0, cardinallyAdjacentEntities.size());
    }

    /**
     * This tests that the correct number of cardinally adjacent entities are produced in the boulders dungeon
     */
    @Test
    public void testCardinallyAdjacentEntities() {
        Mode mode = new Standard();
        Game newGame = new Game(
            "advanced",
            EntityFactory.extractEntities("boulders", mode),
            null,
            mode
        );

        Player gamePlayer = null;
        for (Entity entity : newGame.getEntities()) {
            if (entity.getClass().getSimpleName().toLowerCase().equals("player")) {
                gamePlayer = (Player) entity;
            }
        }
        // player is surrounded on all sides
        List<Entity> cardinallyAdjacentEntities = newGame.getCardinallyAdjacentEntities(
            gamePlayer.getPosition()
        );
        assertEquals(4, cardinallyAdjacentEntities.size());

        newGame.tick(null, Direction.RIGHT);

        // player is only cardinally adjacent to boulder
        cardinallyAdjacentEntities =
            newGame.getCardinallyAdjacentEntities(gamePlayer.getPosition());
        assertEquals(1, cardinallyAdjacentEntities.size());
    }

    /**
     * This tests that a buildable equipment (bow) can be crafted if sufficient materials are present
     */
    @Test
    public void testBuild() {
        Mode mode = new Standard();
        Game newGame = new Game(
            "advanced",
            EntityFactory.extractEntities("advanced", mode),
            null,
            mode
        );

        Player gamePlayer = newGame.getCharacter();

        TestHelpers.gameTickMovement(newGame, Direction.DOWN, 13);
        TestHelpers.gameTickMovement(newGame, Direction.RIGHT, 12);
        TestHelpers.gameTickMovement(newGame, Direction.LEFT, 2);

        DungeonResponse gameResponseOne = newGame.tick(null, Direction.UP);
        assertTrue(gameResponseOne.getBuildables().contains("bow"));

        assertDoesNotThrow(() -> newGame.build("bow"));

        // Bow is in inventory
        assertNotNull(TestHelpers.getItemFromInventory(gamePlayer.getInventoryResponses(), "bow"));
    }

    /**
     * This tests that the player's spawn location is correct regardless of where the player is
     */
    @Test
    public void testPlayerSpawnLocation() {
        Mode mode = new Standard();
        Player player = new Player(new Position(1, 1), mode.initialHealth());
        Game newGame = new Game(
            "advanced",
            EntityFactory.extractEntities("advanced", mode),
            null,
            mode
        );

        newGame.addEntity(player);
        assertTrue(new Position(1, 1).equals(newGame.getPlayerSpawnLocation()));

        // Check that the player's spawn location is the same even if they move
        newGame.tick(null, Direction.NONE);
        assertTrue(new Position(1, 1).equals(newGame.getPlayerSpawnLocation()));
        newGame.tick(null, Direction.UP);
        assertTrue(new Position(1, 1).equals(newGame.getPlayerSpawnLocation()));
        newGame.tick(null, Direction.RIGHT);
        assertTrue(new Position(1, 1).equals(newGame.getPlayerSpawnLocation()));
        newGame.tick(null, Direction.DOWN);
        assertTrue(new Position(1, 1).equals(newGame.getPlayerSpawnLocation()));
        newGame.tick(null, Direction.LEFT);
        assertTrue(new Position(1, 1).equals(newGame.getPlayerSpawnLocation()));
    }
    
    /**
     * This tests that the player interacts as intended
     */
    @Test
    public void testInteract() {
        Mode mode = new Standard();
        Game newGame = new Game(
            "advanced",
            EntityFactory.extractEntities("advanced", mode),
            null,
            mode
        );

        Player gamePlayer = newGame.getCharacter();
        Mercenary mercenary = null;

        for (Entity entity : newGame.getEntities()) {
            if (entity.getClass().getSimpleName().toLowerCase().equals("mercenary")) {
                mercenary = (Mercenary) entity;
            }
        }

        String mercenaryId = mercenary.getId();

        TestHelpers.gameTickMovement(newGame, Direction.RIGHT, 17);
        TestHelpers.gameTickMovement(newGame, Direction.DOWN, 9);
        TestHelpers.gameTickMovement(newGame, Direction.LEFT, 9);
        TestHelpers.gameTickMovement(newGame, Direction.UP, 3);

        assertDoesNotThrow(() -> newGame.interact(mercenaryId));
        assertEquals(1, gamePlayer.getAllies().size());
    }

    /**
     * This tests that the game ticks as intended
     */
    @Test
    public void testTick() {
        Mode mode = new Standard();
        Game newGame = new Game(
            "advanced",
            EntityFactory.extractEntities("advanced", mode),
            null,
            mode
        );

        Player gamePlayer = null;

        for (Entity entity : newGame.getEntities()) {
            if (entity.getClass().getSimpleName().toLowerCase().equals("player")) {
                gamePlayer = (Player) entity;
            }
        }

        assertThrows(IllegalArgumentException.class, () -> newGame.tick("", Direction.NONE));
        
        assertTrue(newGame.getEntity("non-existent-item") == null);
        assertThrows(InvalidActionException.class, () -> newGame.tick("non-existent-item", Direction.NONE));

        // Pickup sword
        TestHelpers.gameTickMovement(newGame, Direction.RIGHT, 5);

        // Sword is not a valid item to use
        String swordId = gamePlayer.findInventoryItem("sword").getId();
        assertThrows(IllegalArgumentException.class, () -> newGame.tick(swordId, Direction.NONE));

        // Pickup bomb
        TestHelpers.gameTickMovement(newGame, Direction.RIGHT, 5);
        TestHelpers.gameTickMovement(newGame, Direction.DOWN, 3);
        TestHelpers.gameTickMovement(newGame, Direction.RIGHT, 2);

        // Bomb is a valid item to use
        String bombId = gamePlayer.findInventoryItem("bomb").getId();
        assertDoesNotThrow(() -> newGame.tick(bombId, Direction.NONE));

        // If we try to use bomb again, it does not exist in player's inventory
        assertThrows(InvalidActionException.class, () -> newGame.tick(bombId, Direction.NONE));
    }
}
