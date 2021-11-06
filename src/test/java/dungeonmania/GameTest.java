package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.List;
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
        assertDoesNotThrow(() -> controller.newGame("zombie", "Peaceful"));

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
        assertDoesNotThrow(() -> controller.newGame("zombie", "Hard"));
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

    @Test
    public void testCardinallyAdjacentEntitiesInAdvancedDungeon() {
        Mode gameMode = new Standard();
        Game newGame = new Game(
            "advanced",
            EntityFactory.extractEntities("advanced", gameMode),
            null,
            gameMode
        );

        Player gamePlayer = null;
        for (Entity entity : newGame.getEntities()) {
            if (entity.getClass().getSimpleName().toLowerCase().equals("player")) {
                gamePlayer = (Player) entity;
            }
        }

        List<Entity> cardinallyAdjacentEntities = newGame.getCardinallyAdjacentEntities(
            gamePlayer.getPosition()
        );
        assertEquals (2, cardinallyAdjacentEntities.size());

        newGame.tick(null, Direction.DOWN);

        cardinallyAdjacentEntities = newGame.getCardinallyAdjacentEntities(gamePlayer.getPosition());
        assertEquals (1, cardinallyAdjacentEntities.size());

        newGame.tick(null, Direction.DOWN);
        newGame.tick(null, Direction.DOWN);
        newGame.tick(null, Direction.DOWN);
        newGame.tick(null, Direction.RIGHT);

        cardinallyAdjacentEntities = newGame.getCardinallyAdjacentEntities(gamePlayer.getPosition());
        assertEquals (0, cardinallyAdjacentEntities.size());
    }

    @Test
    public void testCardinallyAdjacentEntities() {
        Mode gameMode = new Standard();
        Game newGame = new Game(
            "advanced",
            EntityFactory.extractEntities("boulders", gameMode),
            null,
            gameMode
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
        assertEquals (4, cardinallyAdjacentEntities.size());

        newGame.tick(null, Direction.RIGHT);

        // player is only cardinally adjacent to boulder
        cardinallyAdjacentEntities = newGame.getCardinallyAdjacentEntities(gamePlayer.getPosition());
        assertEquals (1, cardinallyAdjacentEntities.size());
    }

    @Test
    public void testBuild() {
        Mode gameMode = new Standard();
        Game newGame = new Game(
            "advanced",
            EntityFactory.extractEntities("advanced", gameMode),
            null,
            gameMode
        );

        Player gamePlayer = null;
        for (Entity entity : newGame.getEntities()) {
            if (entity.getClass().getSimpleName().toLowerCase().equals("player")) {
                gamePlayer = (Player) entity;
            }
        }

        for (int i = 0; i < 13; i++) {
            newGame.tick(null, Direction.DOWN);
        }

        for (int i = 0; i < 12; i++) {
            newGame.tick(null, Direction.RIGHT);
        }

        for (int i = 0; i < 2; i++) {
            newGame.tick(null, Direction.LEFT);
        }

        DungeonResponse gameResponseOne = newGame.tick(null, Direction.UP);
        assertTrue(gameResponseOne.getBuildables().contains("bow"));

        assertDoesNotThrow(() -> newGame.build("bow"));

        // bow is in inventory
        assertEquals (1, gamePlayer.getInventoryResponses().size());
    }
    
    /*
    @Test
    public void testInteract() {
        Mode gameMode = new Standard();
        Game newGame = new Game(
            "advanced",
            EntityFactory.extractEntities("advanced", gameMode),
            null,
            gameMode
        );

        Player gamePlayer = null;
        Mercenary mercenary = null;

        for (Entity entity : newGame.getEntities()) {
            if (entity.getClass().getSimpleName().toLowerCase().equals("player")) {
                gamePlayer = (Player) entity;
            }
        }

        for (Entity entity : newGame.getEntities()) {
            if (entity.getClass().getSimpleName().toLowerCase().equals("mercenary")) {
                mercenary = (Mercenary) entity;
            }
        }

        String mercenaryId = mercenary.getId();

        for (int i = 0; i < 15; i++) {
            newGame.tick(null, Direction.RIGHT);
        }

        for (int i = 0; i < 9; i++) {
            newGame.tick(null, Direction.DOWN);
        }

        for (int i = 0; i < 9; i++) {
            newGame.tick(null, Direction.LEFT);
        }

        for (int i = 0; i < 3; i++) {
            newGame.tick(null, Direction.UP);
        }

        assertDoesNotThrow(() -> newGame.interact(mercenaryId));
        assertEquals (1, gamePlayer.getAllies().size());
    } */
}
