package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;  

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;

public class ControllerTest {
    /**
     * Given a dungeonResponse returns the id of an object within the player's inventory
     * @param resp
     * @param objectType
     * @return
     */
    public static String getInventoryId (DungeonResponse resp, String objectType) {
        for (ItemResponse item: resp.getInventory()) {
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
    public static String getEntityId (DungeonResponse resp, String objectType) {
        for (EntityResponse entity : resp.getEntities()) {
            if (objectType.equals(entity.getType())) return entity.getId();
        }
        return null;
    }
    
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
        assertThrows(IllegalArgumentException.class, () -> controller.newGame ("advanced", "Non-existent"));
    }

    /**
     * This test ensures that an illegalArgumentException is thrown when an invalid
     * Dungeon name is given
     */
    @Test
    public void testInCorrectDungeonName() {
        DungeonManiaController controller = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> controller.newGame ("Non-existent", "Standard"));
    }

    /**
     * This test ensures a game is created when new game is called
     */
    @Test
    public void testGameIsCreated() {
        DungeonManiaController controller = new DungeonManiaController();    
        DungeonResponse gameResponse = controller.newGame ("advanced", "Standard");
        assertEquals(gameResponse.getDungeonName(), "advanced");

    }

    /**
     * This test ensures a game is created when new Hard game is made
     */
    @Test
    public void testHardGameIsCreated() {
        DungeonManiaController controller = new DungeonManiaController();    
        DungeonResponse gameResponse = controller.newGame ("advanced", "Hard");
        assertEquals(gameResponse.getDungeonName(), "advanced");

    }

    /**
     * This test ensures a game is created when new Peaceful game is made
     */
    @Test
    public void testPeacefulGameIsCreated() {
        DungeonManiaController controller = new DungeonManiaController();    
        DungeonResponse gameResponse = controller.newGame ("advanced", "Peaceful");
        assertEquals(gameResponse.getDungeonName(), "advanced");

    }


    //////////////////
    /// Test Save Game
    //////////////////
    /**
     * Ensures saveGame throws error if string is empty??
     */
    @Test
    public void testSaveGame () {
        DungeonManiaController controller = new DungeonManiaController();  
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));
        assertThrows(IllegalArgumentException.class, () -> controller.saveGame(""));
    }


    //////////////////
    /// Test Load Game
    //////////////////
    /**
     * Ensures LoadGame throws error if string is empty??
     */
    @Test
    public void testLoadGame () {
        DungeonManiaController controller = new DungeonManiaController();  
        assertThrows(IllegalArgumentException.class, () -> controller.loadGame(""));
    }


    /**
     * Ensures game is not loaded if name is invalid
     */
    @Test
    public void testInvalidLoadGameName () {
        DungeonManiaController controller = new DungeonManiaController();  
        assertThrows(IllegalArgumentException.class, () -> controller.loadGame("no_game_exists"));       
    }

    /**
     * Test a game persists and can be loaded.
     */
    @Test
    public void testLoadGameWorks() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));

        // Player moves to battle mercenary
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        DungeonResponse currGame = controller.tick (null, Direction.RIGHT);

        // Find current position of the player
        Position playerPosition = new Position(0, 0);
        for (EntityResponse entity: currGame.getEntities()) {
            if (entity.getType().startsWith("player")) playerPosition = entity.getPosition();
        }

        // Save the current game
        assertDoesNotThrow(() -> controller.saveGame ("GameOne"));

        // New controller is made
        DungeonManiaController controllerNew = new DungeonManiaController();
        DungeonResponse loadedGame = controllerNew.loadGame("GameOne");  
        // There should be only one goal left
        assertEquals (":enemies(1) AND :treasure(1)", loadedGame.getGoals());

        // Player should be in the same position as when the game was saved
        for (EntityResponse entity: loadedGame.getEntities()) {
            if (entity.getType().equals ("player")) {
                assertEquals(entity.getPosition(), playerPosition);
            }
        }
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
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));
        int numSavedGames = controller.allGames().size();
        
        String gameName = LocalTime.now().toString();
        // Game is saved
        assertDoesNotThrow(() -> controller.saveGame (gameName));

        // All games should contain the saved game
        assertEquals (numSavedGames + 1, controller.allGames().size());
        assertTrue (controller.allGames().contains(gameName));
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
        DungeonResponse resp = controller.newGame ("advanced", "standard");
        for (int i = 0; i < 5; i++) {
            controller.tick (null, Direction.RIGHT);            
        }
        assertThrows (IllegalArgumentException.class, () ->controller.tick (ControllerTest.getEntityId(resp, "sword"), Direction.NONE));    
    }


    /**
     * Test an InvalidActionException is thrown if item used is not in inventory
     */
    @Test
    public void testItemNotInInventory () {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse resp = controller.newGame ("bombs", "Standard");
        // Bomb is not in inventory
        assertThrows (InvalidActionException.class, () ->controller.tick (ControllerTest.getEntityId(resp, "bomb"), Direction.NONE));    
    }


    /**
     * Test an InvalidActionException is thrown if non-null item used is not bomb,
     * invincibility potion, invisibility potion or health potion
     */
    @Test
    public void testItemNotValid() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "standard"));  
        assertThrows (InvalidActionException.class, () ->controller.tick ("id-does-not-exist", Direction.NONE));    
    }
    
    /**
     * Test potions can be used in a game
     */
    @Test 
    public void testUsePotion() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("potions", "Standard"));
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        DungeonResponse resp = controller.tick (null, Direction.RIGHT);
        String health_id = ControllerTest.getInventoryId(resp, "health_potion");
        String invisibility_id = ControllerTest.getInventoryId(resp, "invisibility_potion");
        String invincibility_id = ControllerTest.getInventoryId(resp, "invincibility_potion");
        assertDoesNotThrow(() -> controller.tick (health_id, Direction.NONE));
        assertDoesNotThrow(() -> controller.tick (invisibility_id, Direction.NONE));
        assertDoesNotThrow(() -> controller.tick (invincibility_id, Direction.NONE));
    }

    /**
     * Test bombs can be used and placed in a game
     */
    @Test 
    public void testUseBomb() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("bombs", "Standard"));
        DungeonResponse resp = controller.tick (null, Direction.RIGHT);
        String bomb_id = ControllerTest.getInventoryId(resp, "bomb");
        assertDoesNotThrow(() -> controller.tick (bomb_id, Direction.NONE));
    }

    /**
     * Test exception is thrown if id provided is insufficient (too short)
     */
    @Test 
    public void testInsufficientId() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("bombs", "Standard"));
        assertThrows(IllegalArgumentException.class, () -> controller.tick ("", Direction.NONE));
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
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));  
        assertThrows (IllegalArgumentException.class, () -> controller.build ("bad-item"));    
    }

    /**
     * Test invalid action exception is thrown when player does not have sufficient
     * items to craft buildable
     */
    
    @Test
    public void testInsufficientMaterialsToBuild() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));  
        assertThrows (InvalidActionException.class, () -> controller.build ("bow"));  
        assertThrows (InvalidActionException.class, () -> controller.build ("shield"));      
    } 

    /**
     *  Test can craft buildable
     */
    @Test
    public void testCanBuildObject() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));

        for (int i = 0; i < 13; i++) {
            controller.tick (null, Direction.DOWN);            
        }

        for (int i = 0; i < 12; i++) {
            controller.tick (null, Direction.RIGHT);            
        } 

        for (int i = 0; i < 2; i++) {
            controller.tick (null, Direction.LEFT);           
        }

        DungeonResponse gameResponseOne = controller.tick (null, Direction.UP);
        assert(gameResponseOne.getBuildables().contains ("bow")); 

        for (int i = 0; i < 3; i++) {
            controller.tick (null, Direction.UP);            
        }
        
        for (int i = 0; i < 4; i++) {
            controller.tick (null, Direction.LEFT);            
        } 

        DungeonResponse gameResponseTwo = controller.tick (null, Direction.UP);
        assert(gameResponseTwo.getBuildables().contains ("bow"));
        assert(gameResponseTwo.getBuildables().contains ("shield")); 
        assertDoesNotThrow(() ->controller.build("bow"));
    }

    /**
     * This ensures that you can build shields if you have sufficient materials
     */
    @Test
    public void testCanBuildShield() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));

        for (int i = 0; i < 13; i++) {
            controller.tick (null, Direction.DOWN);            
        }

        for (int i = 0; i < 12; i++) {
            controller.tick (null, Direction.RIGHT);            
        } 

        for (int i = 0; i < 2; i++) {
            controller.tick (null, Direction.LEFT);           
        }

        for (int i = 0; i < 4; i++) {
            controller.tick (null, Direction.UP);            
        }
        
        for (int i = 0; i < 4; i++) {
            controller.tick (null, Direction.LEFT);            
        } 
        assertDoesNotThrow(() ->controller.build("shield"));
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
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));  
        assertThrows (IllegalArgumentException.class, () ->controller.interact("ID-not-exist"));    
    }

    /**
     * Test Cannot Bribe Mercenary without money and more than two cardinal tiles away
     */

    @Test
    public void testTooFarFromMercenary() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse gameResponse = controller.newGame ("advanced", "Standard");
        EntityResponse mercenary =  gameResponse.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null);
        String mercenaryId = mercenary != null ?  mercenary.getId() : "";
        assertThrows (InvalidActionException.class, ()->controller.interact(mercenaryId));
    }

    /**
     * Test a player cannot bribe a mercenary if not have gold
     */
    @Test
    public void testNoGoldBribe() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse gameResponse = controller.newGame ("advanced", "Standard");  
        EntityResponse mercenary =  gameResponse.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null);
        String mercenaryId = mercenary != null ?  mercenary.getId() : "";
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        assertThrows (InvalidActionException.class, ()->controller.interact(mercenaryId));           
    }


    //////////////////
    /// Test Given Functions
    //////////////////
    /**
     * Test the given functions provide the expected output
     */
    @Test
    public void testGivenFunction() {
        DungeonManiaController controller = new DungeonManiaController();
        assertEquals ("default", controller.getSkin());
        assertEquals ("en_US", controller.getLocalisation());
        List<String> gameModes = controller.getGameModes();
        assertEquals ("standard", gameModes.get(0));
        assertEquals ("hard", gameModes.get(2));
    }    
}
