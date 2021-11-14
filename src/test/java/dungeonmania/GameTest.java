package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
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
                .filter(e -> e.getType().equals("zombie_toast"))
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
                    .filter(e -> e.getType().equals("zombie_toast"))
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
                .filter(e -> e.getType().equals("zombie_toast"))
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
                .filter(e -> e.getType().equals("zombie_toast"))
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
                    .filter(e -> e.getType().equals("zombie_toast"))
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
                .filter(e -> e.getType().equals("zombie_toast"))
                .collect(Collectors.toList())
                .size()
        );
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
        assertEquals(
            0,
            responseOne
                .getEntities()
                .stream()
                .filter(e -> e.getType().equals("zombie_toast"))
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
                    .filter(e -> e.getType().equals("zombie_toast"))
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
                .filter(e -> e.getType().equals("zombie_toast"))
                .collect(Collectors.toList())
                .size()
        );
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
        assertEquals (4, cardinallyAdjacentEntities.size());

        newGame.tick(null, Direction.RIGHT);

        // player is only cardinally adjacent to boulder
        cardinallyAdjacentEntities = newGame.getCardinallyAdjacentEntities(gamePlayer.getPosition());
        assertEquals (1, cardinallyAdjacentEntities.size());
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

        // Bow is in inventory
        boolean builtBow = false;
        for (ItemResponse item: gamePlayer.getInventoryResponses()) {
            if (item.getType().contains("bow")) builtBow = true;
        }
        assertTrue(builtBow);
    }

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
    
    
    @Test
    public void testInteract() {
        Mode mode = new Standard();
        Game newGame = new Game(
            "advanced",
            EntityFactory.extractEntities("advanced", mode),
            null,
            mode
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

        for (int i = 0; i < 17; i++) {
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
    }
}
