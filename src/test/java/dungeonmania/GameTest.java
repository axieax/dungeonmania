package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;  

public class GameTest {
    /**
     * Test zombie spawn rate in Standard Game Mode
     */
    @Test
    public void testStandardGameMode() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("zombie", "Standard"));

        DungeonResponse responseOne = controller.tick (null, Direction.NONE);
        assert (responseOne.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).collect (Collectors.toList()).size() == 0);


        responseOne.getEntities();
        for (int i = 0; i < 18; i++) {
            DungeonResponse responseTwo = controller.tick (null, Direction.NONE); 
            assert (responseTwo.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).collect (Collectors.toList()).size() == 0);       
        }

        // 20 ticks have passed
        DungeonResponse responseThree= controller.tick (null, Direction.NONE);
        assert (responseThree.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).collect (Collectors.toList()).size() == 1);
    }
    /**
     * This tests Zombie Spawn Rate in Peaceful Game Mode
     */
    @Test
    public void testPeacefulGameMode() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Peaceful"));

        DungeonResponse responseOne = controller.tick (null, Direction.NONE);
        assert (responseOne.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).collect (Collectors.toList()).size() == 0);


        responseOne.getEntities();
        for (int i = 0; i < 18; i++) {
            DungeonResponse responseTwo = controller.tick (null, Direction.NONE); 
            assert (responseTwo.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).collect (Collectors.toList()).size() == 0);       
        }

        // 20 ticks have passed
        DungeonResponse responseThree= controller.tick (null, Direction.NONE);
        assert (responseThree.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).collect (Collectors.toList()).size() == 1);
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
        DungeonResponse responseOne = controller.tick (null, Direction.NONE);
        assert (responseOne.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).collect (Collectors.toList()).size() == 0);


        responseOne.getEntities();
        for (int i = 0; i < 13; i++) {
            DungeonResponse responseTwo = controller.tick (null, Direction.NONE); 
            assert (responseTwo.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).collect (Collectors.toList()).size() == 0);       
        }

        // 15 ticks have passed
        DungeonResponse responseThree= controller.tick (null, Direction.NONE);
        assert (responseThree.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).collect (Collectors.toList()).size() == 1);
    }
}
