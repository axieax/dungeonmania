package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.spi.MarkerFactoryBinder;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;  

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;

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
            if (entity.getPrefix().equals ("player")) playerPosition = entity.getPosition();
        }

        // Save the current game
        assertDoesNotThrow(() -> controller.saveGame ("GameOne"));

        // New controller is made
        DungeonManiaController controllerNew = new DungeonManiaController();
        DungeonResponse loadedGame = controllerNew.loadGame("GameOne");  
        // There should be only one goal left
        assertEquals (loadedGame.getGoals(), "treasure");

        // Player should be in the same position as when the game was saved
        for (EntityResponse entity: loadedGame.getEntities()) {
            if (entity.getPrefix().equals ("player")) {
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
        
        // Game is not saved yet, all games should contain zero games
        assertEquals(controller.allGames().size(), 0);
        
        // Game is saved
        assertDoesNotThrow(() -> controller.saveGame ("GameOne"));

        // All games should contain the saved game
        assertEquals (controller.allGames().size(), 1);
        assertTrue (controller.allGames().contains("GameOne"));
    }




    //////////////////
    /// Test Tick
    //////////////////

    /**
     * Test an InvalidActionException is thrown if item used is not in inventory
     */
    @Test
    public void testItemNotInInventory () {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));  
        // Invisibility poition is not in inventory
        assertThrows (InvalidActionException.class, () ->controller.tick ("invisibility_potion", Direction.NONE));    
    }

    /**
     * Test an IllegalArgumentException is thrown if item used is not bomb,
     * invincibility potion, invisibility potion or health potion
     */
    @Test
    public void testItemNotValid() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));  
        assertThrows (IllegalArgumentException.class, () ->controller.tick ("shield", Direction.NONE));    
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
        EntityResponse mercenary =  gameResponse.getEntities().stream().filter(e -> e.getPrefix().equals("mercenary")).findFirst().orElse(null);
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
        EntityResponse mercenary =  gameResponse.getEntities().stream().filter(e -> e.getPrefix().equals("mercenary")).findFirst().orElse(null);
        String mercenaryId = mercenary != null ?  mercenary.getId() : "";
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        assertThrows (InvalidActionException.class, ()->controller.interact(mercenaryId));           
    }
}
