
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
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;  

public class DungeonTest {
    ////////////////////////////////////////////////////////////////////////////
    // Test mode
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Test the mercenary is vanquished in standard games
     */
    @Test
    public void testStandardGameMode() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        DungeonResponse vanquished = controller.tick (null, Direction.DOWN);
        assertEquals(vanquished.getGoals(), "treasure");
    }
    /**
     * This tests that enemies can still be vanquished in peaceful mode
     */
    @Test
    public void testPeacefulGameMode() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Peaceful"));
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        
        // Merc should still be in place
        // Merc appears to stay in one place --> must clarify later
        DungeonResponse before = controller.tick (null, Direction.RIGHT);
        assertEquals(before.getGoals(), "enemies and treasure");

        // Merc should be vanquished
        DungeonResponse vanquished = controller.tick (null, Direction.RIGHT);
        assertEquals(vanquished.getGoals(), "treasure");
    }

    /**
     * This test ensures the game adopts the features of a hard game mode.
     * Such as zombies spawning every 15 ticks
     * Players having less health points
     * Invincibility potions have no effect
     */
    @Test
    public void testHardGameMode() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Hard"));
        // how many health points do players have to begin with?
        // how many helath points do enemies have to begin with?

        // cannot test zombie spawn rate because no dungeon with zombie

        // cannot test invincibility .. need to ask about player entity
        // can player have an is invincible boolean?
    }

    ////////////////////////////////////////////////////////////////////////////
    // Test Dungeon
    ////////////////////////////////////////////////////////////////////////////
    
    



    ////////////////////////////////////////////////////////////////////////////
    // Test Goal
    ////////////////////////////////////////////////////////////////////////////
    
    /**
     * This tests the completion of goals in the advanced dungeon
     */
    @Test
    public void testAdvancedGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Standard"));
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        DungeonResponse vanquished = controller.tick (null, Direction.DOWN);
        assertEquals(vanquished.getGoals(), "treasure");

        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        
        DungeonResponse secondState = controller.tick (null, Direction.DOWN);
        assertEquals(secondState.getGoals(), "treasure");
        
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN); //Got treasure
    
        DungeonResponse thirdState = controller.tick (null, Direction.DOWN);
        assertEquals(thirdState.getGoals(), "");
    }

    /**
     * This tests the completion of boulders goal in the boulders dungeon
     */
    @Test 
    public void testBouldersGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("boulder", "Standard"));
    

        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);

        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);

        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.DOWN);

        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);

        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);

        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.UP);

        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);

        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.LEFT);

        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.DOWN);

        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.DOWN);

        controller.tick (null, Direction.LEFT);
        controller.tick (null, Direction.LEFT);

        DungeonResponse stateOne = controller.tick (null, Direction.UP);
        assertEquals(stateOne.getGoals(), "boulders");    


        DungeonResponse stateTwo = controller.tick (null, Direction.RIGHT);
        assertEquals(stateTwo.getGoals(), "boulders"); // game is won   
    }

    /**
     * Checks the exit goal is satisfied in the maze dungeon
     */
    @Test 
    public void testMazeGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("maze", "Standard"));

        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);

        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);

        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.RIGHT);
    
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);

        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);

        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);

        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);

        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
        controller.tick (null, Direction.UP);
    
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);
        controller.tick (null, Direction.RIGHT);

        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        controller.tick (null, Direction.DOWN);
        DungeonResponse stateOne = controller.tick (null, Direction.DOWN);
        assertEquals(stateOne.getGoals(), "exit");  

        DungeonResponse stateTwo = controller.tick (null, Direction.DOWN);
        assertEquals(stateTwo.getGoals(), "");  
    }

    /**
     * Test that Portals does not have a goal
     */
    @Test
    public void testPortalsGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("portals", "Standard"));
        DungeonResponse stateOne = controller.tick (null, Direction.NONE);
        assertEquals(stateOne.getGoals(), "");  
    }
  
}
