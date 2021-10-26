package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;

//import static dungeonmania.TestHelpers.assertListAreEqualIgnoringOrder;

public class ControllerTest {
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
     * Ensures saveGame throws error if string is empty??
     */
    @Test
    public void testSaveGame () {
        DungeonManiaController controller = new DungeonManiaController();  
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));
        assertThrows(IllegalArgumentException.class, () -> controller.saveGame(""));
    }

    /**
     * Ensures LoadGame throws error if string is empty??
     */
    @Test
    public void testLoadGame () {
        DungeonManiaController controller = new DungeonManiaController();  
        assertThrows(IllegalArgumentException.class, () -> controller.loadGame(""));
    }
    

    /**
     * This function tests the persistence of games in dungeon mania
     */
    @Test
    public void testSaveGameWorks() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));
        assertEquals(controller.allGames().size(), 0);
        
        assertDoesNotThrow(() -> controller.saveGame ("GameOne"));
        
        List<String> gamesList = new ArrayList <String> ();
        gamesList.add("GameOne");
        // assertArrayEqualsIgnoringOrder (controller.allGames(), gamesList);
        assertEquals (controller.allGames().size(), 1);
        assertTrue (controller.allGames().contains("GameOne"));
    }

    /**
     * Test an InvalidActionException is thrown if item used is not in inventory
     */
    @Test
    public void testItemNotInInventory () {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));  
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
     * Test a player cannot bribe a mercenary if not adjacent
     */
    @Test
    public void testNoGoldBribe() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));  
        assertThrows (InvalidActionException.class, () ->controller.interact("1"));            
    }

    /**
     * Player does not have sufficient items to craft the buildable
     */
    @Test
    public void testCannotBuild() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));  
        assertThrows (InvalidActionException.class, () ->controller.interact("bow"));         
    }

    /**
     * The buildable is not an invalid type (not bow or shield)
     */
    @Test
    public void testInvalidBuildableType() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));  
        assertThrows (IllegalArgumentException.class, () ->controller.interact("invalid-type"));            
    }


}
