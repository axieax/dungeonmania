
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
        assertEquals(vanquished.getGoals(), ":treasure(1)");
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
        assertEquals(before.getGoals(), ":treasure(1)");

        // Merc should be vanquished
        DungeonResponse vanquished = controller.tick (null, Direction.RIGHT);
        assertEquals(vanquished.getGoals(), ":treasure(1)");
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
}
