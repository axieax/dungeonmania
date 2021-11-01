package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import java.util.stream.Collectors;
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
        assertEquals(
            0,
            responseOne
                .getEntities()
                .stream()
                .filter(e -> e.getPrefix().equals("zombie_toast"))
                .collect(Collectors.toList())
                .size()
        );

        responseOne.getEntities();
        for (int i = 0; i < 18; i++) {
            DungeonResponse responseTwo = controller.tick(null, Direction.NONE);
            assertEquals(
                0,
                responseTwo
                    .getEntities()
                    .stream()
                    .filter(e -> e.getPrefix().equals("zombie_toast"))
                    .collect(Collectors.toList())
                    .size()
            );
        }

        // 20 ticks have passed
        DungeonResponse responseThree = controller.tick(null, Direction.NONE);
        assertEquals(
            1,
            responseThree
                .getEntities()
                .stream()
                .filter(e -> e.getPrefix().equals("zombie_toast"))
                .collect(Collectors.toList())
                .size()
        );
    }

    /**
     * This tests Zombie Spawn Rate in Peaceful Game Mode
     */
    @Test
    public void testPeacefulGameMode() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame("advanced", "Peaceful"));

        DungeonResponse responseOne = controller.tick(null, Direction.NONE);
        assertEquals(
            0,
            responseOne
                .getEntities()
                .stream()
                .filter(e -> e.getPrefix().equals("zombie_toast"))
                .collect(Collectors.toList())
                .size()
        );

        responseOne.getEntities();
        for (int i = 0; i < 18; i++) {
            DungeonResponse responseTwo = controller.tick(null, Direction.NONE);
            assertEquals(
                0,
                responseTwo
                    .getEntities()
                    .stream()
                    .filter(e -> e.getPrefix().equals("zombie_toast"))
                    .collect(Collectors.toList())
                    .size()
            );
        }

        // 20 ticks have passed
        DungeonResponse responseThree = controller.tick(null, Direction.NONE);
        assertEquals(
            1,
            responseThree
                .getEntities()
                .stream()
                .filter(e -> e.getPrefix().equals("zombie_toast"))
                .collect(Collectors.toList())
                .size()
        );
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
        assertDoesNotThrow(() -> controller.newGame("advanced", "Hard"));
        DungeonResponse responseOne = controller.tick(null, Direction.NONE);
        assertEquals(
            0,
            responseOne
                .getEntities()
                .stream()
                .filter(e -> e.getPrefix().equals("zombie_toast"))
                .collect(Collectors.toList())
                .size()
        );

        responseOne.getEntities();
        for (int i = 0; i < 13; i++) {
            DungeonResponse responseTwo = controller.tick(null, Direction.NONE);
            assertEquals(
                0,
                responseTwo
                    .getEntities()
                    .stream()
                    .filter(e -> e.getPrefix().equals("zombie_toast"))
                    .collect(Collectors.toList())
                    .size()
            );
        }

        // 15 ticks have passed
        DungeonResponse responseThree = controller.tick(null, Direction.NONE);
        assertEquals(
            1,
            responseThree
                .getEntities()
                .stream()
                .filter(e -> e.getPrefix().equals("zombie_toast"))
                .collect(Collectors.toList())
                .size()
        );
    }
}
