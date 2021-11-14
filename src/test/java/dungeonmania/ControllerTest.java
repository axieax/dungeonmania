package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

public class ControllerTest {

    //////////////////
    /// Test New Game
    //////////////////
    /**
     * This test ensures that an exception is thrown when an invalid game mode
     * is given
     */
    @Test
    public void testInCorrectGameMode() {
        DungeonManiaController controller = new DungeonManiaController();
        assertThrows(
            IllegalArgumentException.class,
            () -> controller.newGame("advanced", "Non-existent")
        );
    }

    /**
     * This test ensures that an illegalArgumentException is thrown when an invalid
     * Dungeon name is given
     */
    @Test
    public void testInCorrectDungeonName() {
        DungeonManiaController controller = new DungeonManiaController();
        assertThrows(
            IllegalArgumentException.class,
            () -> controller.newGame("Non-existent", "Standard")
        );
    }

    /**
     * This test ensures a game is created when new game is called
     */
    @Test
    public void testGameIsCreated() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse gameResponse = controller.newGame("advanced", "Standard");
        assertEquals(gameResponse.getDungeonName(), "advanced");
    }

    /**
     * This test ensures a game is created when new Hard game is made
     */
    @Test
    public void testHardGameIsCreated() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse gameResponse = controller.newGame("advanced", "Hard");
        assertEquals(gameResponse.getDungeonName(), "advanced");
    }

    /**
     * This test ensures a game is created when new Peaceful game is made
     */
    @Test
    public void testPeacefulGameIsCreated() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse gameResponse = controller.newGame("advanced", "Peaceful");
        assertEquals(gameResponse.getDungeonName(), "advanced");
    }

    //////////////////
    /// Test Save Game
    //////////////////
    /**
     * Ensures saveGame throws error if string is empty??
     */
    @Test
    public void testSaveGame() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("advanced", "Standard"));
        assertThrows(IllegalArgumentException.class, () -> controller.saveGame(""));
    }

    //////////////////
    /// Test Load Game
    //////////////////
    /**
     * Ensures LoadGame throws error if string is empty??
     */
    @Test
    public void testLoadGame() {
        DungeonManiaController controller = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> controller.loadGame(""));
    }

    /**
     * Ensures game is not loaded if name is invalid
     */
    @Test
    public void testInvalidLoadGameName() {
        DungeonManiaController controller = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> controller.loadGame("no_game_exists"));
    }

    /**
     * Test a game persists and can be loaded.
     */
    @Test
    public void testLoadGameWorks() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("advanced", "Standard"));

        // Player moves to battle mercenary
        DungeonResponse currGame = TestHelpers.tickMovement(controller, Direction.RIGHT, 3);

        // Find current position of the player
        Position playerPosition = TestHelpers.getPlayerPosition(currGame.getEntities());

        // Save the current game
        assertDoesNotThrow(() -> controller.saveGame("GameOne"));

        // New controller is made
        DungeonManiaController controllerNew = new DungeonManiaController();
        DungeonResponse loadedGame = controllerNew.loadGame("GameOne");
        // There should be only one goal left
        assertEquals(":enemies(1) AND :treasure(1)", loadedGame.getGoals());

        // Player should be in the same position as when the game was saved
        assertEquals(TestHelpers.getPlayerPosition(loadedGame.getEntities()), playerPosition);
    }

    //////////////////
    /// Test All Games
    //////////////////
    /**
     * This function tests the persistence of games in dungeon mania
     */
    @Test
    public void testSaveGameWorks() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("advanced", "Standard"));
        int numSavedGames = controller.allGames().size();

        String gameName = LocalTime.now().toString();
        // Game is saved
        assertDoesNotThrow(() -> controller.saveGame(gameName));

        // All games should contain the saved game
        assertEquals(numSavedGames + 1, controller.allGames().size());
        assertTrue(controller.allGames().contains(gameName));
    }

    //////////////////
    /// Test Tick
    //////////////////

    /**
     * Test an IllegalArgumentException is thrown if non-null item used is not bomb,
     * invincibility potion, invisibility potion or health potion
     */
    @Test
    public void testItemIllegal() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse resp = controller.newGame("advanced", "standard");

        TestHelpers.tickMovement(controller, Direction.RIGHT, 5);

        assertThrows(
            IllegalArgumentException.class,
            () -> controller.tick(TestHelpers.getEntityId(resp, "sword"), Direction.NONE)
        );
    }

    /**
     * Test an InvalidActionException is thrown if item used is not in inventory
     */
    @Test
    public void testItemNotInInventory() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse resp = controller.newGame("bombs", "Standard");
        // Bomb is not in inventory
        assertThrows(
            InvalidActionException.class,
            () -> controller.tick(TestHelpers.getEntityId(resp, "bomb"), Direction.NONE)
        );
    }

    /**
     * Test an InvalidActionException is thrown if non-null item used is not bomb,
     * invincibility potion, invisibility potion or health potion
     */
    @Test
    public void testItemNotValid() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("advanced", "standard"));
        assertThrows(
            InvalidActionException.class,
            () -> controller.tick("id-does-not-exist", Direction.NONE)
        );
    }

    /**
     * Test potions can be used in a game
     */
    @Test
    public void testUsePotion() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("potions", "Standard"));
        DungeonResponse resp = TestHelpers.tickMovement(controller, Direction.RIGHT, 3);
        String health_id = TestHelpers.getInventoryId(resp, "health_potion");
        String invisibility_id = TestHelpers.getInventoryId(resp, "invisibility_potion");
        String invincibility_id = TestHelpers.getInventoryId(resp, "invincibility_potion");
        assertDoesNotThrow(() -> controller.tick(health_id, Direction.NONE));
        assertDoesNotThrow(() -> controller.tick(invisibility_id, Direction.NONE));
        assertDoesNotThrow(() -> controller.tick(invincibility_id, Direction.NONE));
    }

    /**
     * Test bombs can be used and placed in a game
     */
    @Test
    public void testUseBomb() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("bombs", "Standard"));
        DungeonResponse resp = controller.tick(null, Direction.RIGHT);
        String bomb_id = TestHelpers.getInventoryId(resp, "bomb");
        assertDoesNotThrow(() -> controller.tick(bomb_id, Direction.NONE));
    }

    /**
     * Test exception is thrown if id provided is insufficient (too short)
     */
    @Test
    public void testInsufficientId() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("bombs", "Standard"));
        assertThrows(IllegalArgumentException.class, () -> controller.tick("", Direction.NONE));
    }

    //////////////////
    /// Test Buildable
    //////////////////
    /**
     * Tests that an item that is not a bow or shield is not buildable
     */
    @Test
    public void testItemNotBuildable() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("advanced", "Standard"));
        assertThrows(IllegalArgumentException.class, () -> controller.build("bad-item"));
    }

    /**
     * Test invalid action exception is thrown when player does not have sufficient
     * items to craft buildable
     */

    @Test
    public void testInsufficientMaterialsToBuild() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("advanced", "Standard"));
        assertThrows(InvalidActionException.class, () -> controller.build("bow"));
        assertThrows(InvalidActionException.class, () -> controller.build("shield"));
    }

    /**
     *  Test can craft buildable
     */
    @Test
    public void testCanBuildObject() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("advanced", "Standard"));

        TestHelpers.tickMovement(controller, Direction.DOWN, 13);
        TestHelpers.tickMovement(controller, Direction.RIGHT, 12);
        TestHelpers.tickMovement(controller, Direction.LEFT, 2);

        DungeonResponse gameResponseOne = controller.tick(null, Direction.UP);
        assert (gameResponseOne.getBuildables().contains("bow"));

        TestHelpers.tickMovement(controller, Direction.UP, 3);
        TestHelpers.tickMovement(controller, Direction.LEFT, 4);

        DungeonResponse gameResponseTwo = controller.tick(null, Direction.UP);
        assert (gameResponseTwo.getBuildables().contains("bow"));
        assert (gameResponseTwo.getBuildables().contains("shield"));
        assertDoesNotThrow(() -> controller.build("bow"));
    }

    /**
     * This ensures that you can build shields if you have sufficient materials
     */
    @Test
    public void testCanBuildShield() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("advanced", "Standard"));

        TestHelpers.tickMovement(controller, Direction.DOWN, 13);
        TestHelpers.tickMovement(controller, Direction.RIGHT, 12);
        TestHelpers.tickMovement(controller, Direction.LEFT, 2);
        TestHelpers.tickMovement(controller, Direction.UP, 4);
        TestHelpers.tickMovement(controller, Direction.LEFT, 4);

        assertDoesNotThrow(() -> controller.build("shield"));
    }

    //////////////////
    /// Test Interact
    //////////////////

    /**
     * Test an IllegalArgumentException is thrown if item used is not bomb,
     * invincibility potion, invisibility potion or health potion
     */
    @Test
    public void testInvalidEntityId() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("advanced", "Standard"));
        assertThrows(IllegalArgumentException.class, () -> controller.interact("ID-not-exist"));
    }

    /**
     * Test Cannot Bribe Mercenary without money and more than two cardinal tiles away
     */

    @Test
    public void testTooFarFromMercenary() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse gameResponse = controller.newGame("advanced", "Standard");
        EntityResponse mercenary = TestHelpers.findFirstInstance(
            gameResponse.getEntities(),
            "mercenary"
        );
        assertNotNull(mercenary);
        assertThrows(InvalidActionException.class, () -> controller.interact(mercenary.getId()));
    }

    /**
     * Test a player cannot bribe a mercenary if not have gold
     */
    @Test
    public void testNoGoldBribe() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse gameResponse = controller.newGame("advanced", "Standard");
        EntityResponse mercenary = TestHelpers.findFirstInstance(
            gameResponse.getEntities(),
            "mercenary"
        );
        assertNotNull(mercenary);
        TestHelpers.tickMovement(controller, Direction.RIGHT, 2);
        assertThrows(InvalidActionException.class, () -> controller.interact(mercenary.getId()));
    }
}
