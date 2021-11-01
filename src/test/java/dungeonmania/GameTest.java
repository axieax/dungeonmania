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
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;  

public class GameTest {
    /**
     * Test zombie spawn rate in Standard Game Mode
     */
    @Test
    public void testStandardGameMode() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("zombie", "Standard"));

        DungeonResponse responseOne = controller.tick (null, Direction.NONE);
        assertEquals (responseOne.getEntities().stream().filter(e -> e.getClass().getSimpleName().toLowerCase().equals("zombie_toast")).collect (Collectors.toList()).size(), 0);


        responseOne.getEntities();
        for (int i = 0; i < 18; i++) {
            DungeonResponse responseTwo = controller.tick (null, Direction.NONE); 
            assert (responseTwo.getEntities().stream().filter(e -> e.getClass().getSimpleName().toLowerCase().equals("zombie_toast")).collect (Collectors.toList()).size() == 0);       
        }

        // 20 ticks have passed
        DungeonResponse responseThree= controller.tick (null, Direction.NONE);
        assert (responseThree.getEntities().stream().filter(e -> e.getClass().getSimpleName().toLowerCase().equals("zombie_toast")).collect (Collectors.toList()).size() == 1);
    }
    /**
     * This tests Zombie Spawn Rate in Peaceful Game Mode
     */
    @Test
    public void testPeacefulGameMode() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.newGame ("advanced", "Peaceful"));

        DungeonResponse responseOne = controller.tick (null, Direction.NONE);
        assert (responseOne.getEntities().stream().filter(e -> e.getClass().getSimpleName().toLowerCase().equals("zombie_toast")).collect (Collectors.toList()).size() == 0);


        responseOne.getEntities();
        for (int i = 0; i < 18; i++) {
            DungeonResponse responseTwo = controller.tick (null, Direction.NONE); 
            assert (responseTwo.getEntities().stream().filter(e -> e.getClass().getSimpleName().toLowerCase().equals("zombie_toast")).collect (Collectors.toList()).size() == 0);       
        }

        // 20 ticks have passed
        DungeonResponse responseThree= controller.tick (null, Direction.NONE);
        assert (responseThree.getEntities().stream().filter(e -> e.getClass().getSimpleName().toLowerCase().equals("zombie_toast")).collect (Collectors.toList()).size() == 1);
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
        assert (responseOne.getEntities().stream().filter(e -> e.getClass().getSimpleName().toLowerCase().equals("zombie_toast")).collect (Collectors.toList()).size() == 0);


        responseOne.getEntities();
        for (int i = 0; i < 13; i++) {
            DungeonResponse responseTwo = controller.tick (null, Direction.NONE); 
            assert (responseTwo.getEntities().stream().filter(e -> e.getClass().getSimpleName().toLowerCase().equals("zombie_toast")).collect (Collectors.toList()).size() == 0);       
        }

        // 15 ticks have passed
        DungeonResponse responseThree= controller.tick (null, Direction.NONE);
        assert (responseThree.getEntities().stream().filter(e -> e.getClass().getSimpleName().toLowerCase().equals("zombie_toast")).collect (Collectors.toList()).size() == 1);
    }



    @Test 
    public void testMoveablePositions () {
        Mode gameMode = new Standard();
        Game newGame = new Game ("advanced", EntityFactory.extractEntities("advanced", gameMode), null, gameMode);

        Player gamePlayer = null;
        for (Entity entity: newGame.getEntities()) {
            if (entity.getClass().getSimpleName().toLowerCase().equals ("player")) {
                gamePlayer = (Player) entity;
            }
        }

        List<Position> possiblePositions = newGame.getMoveablePositions (gamePlayer, gamePlayer.getPosition());
        assert (possiblePositions.size() == 2);

        newGame.tick (null, Direction.DOWN);

        possiblePositions = newGame.getMoveablePositions (gamePlayer, gamePlayer.getPosition());
        assert (possiblePositions.size() == 3);

        newGame.tick (null, Direction.DOWN);
        newGame.tick (null, Direction.DOWN);
        newGame.tick (null, Direction.DOWN);
        newGame.tick (null, Direction.RIGHT);

        possiblePositions = newGame.getMoveablePositions (gamePlayer, gamePlayer.getPosition());
        assert (possiblePositions.size() == 4);

    }

    @Test
    public void testCardinallyAdjacentEntitiesInAdvancedDungeon () {
        Mode gameMode = new Standard();
        Game newGame = new Game ("advanced", EntityFactory.extractEntities("advanced", gameMode), null, gameMode);

        Player gamePlayer = null;
        for (Entity entity: newGame.getEntities()) {
            if (entity.getClass().getSimpleName().toLowerCase().equals ("player")) {
                gamePlayer = (Player) entity;
            }
        }

        List<Entity> cardinallyAdjacentEntities = newGame.getAdjacentEntities(gamePlayer.getPosition());
        assert (cardinallyAdjacentEntities.size() == 2);

        newGame.tick (null, Direction.DOWN);

        cardinallyAdjacentEntities = newGame.getAdjacentEntities(gamePlayer.getPosition());
        assert (cardinallyAdjacentEntities.size() == 1);

        newGame.tick (null, Direction.DOWN);
        newGame.tick (null, Direction.DOWN);
        newGame.tick (null, Direction.DOWN);
        newGame.tick (null, Direction.RIGHT);

        cardinallyAdjacentEntities = newGame.getAdjacentEntities(gamePlayer.getPosition());
        assert (cardinallyAdjacentEntities.size() == 0);

    }

    @Test 
    public void testCardinallyAdjacentEntities () {
        Mode gameMode = new Standard();
        Game newGame = new Game ("advanced", EntityFactory.extractEntities("boulders", gameMode), null, gameMode);

        Player gamePlayer = null;
        for (Entity entity: newGame.getEntities()) {
            if (entity.getClass().getSimpleName().toLowerCase().equals ("player")) {
                gamePlayer = (Player) entity;
            }
        }
        // player is surrounded on all sides
        List<Entity> cardinallyAdjacentEntities = newGame.getAdjacentEntities(gamePlayer.getPosition());
        assert (cardinallyAdjacentEntities.size() == 4);

        newGame.tick (null, Direction.RIGHT);

        // player is only cardinally adjacent to boulder
        cardinallyAdjacentEntities = newGame.getAdjacentEntities(gamePlayer.getPosition());
        assert (cardinallyAdjacentEntities.size() == 1);
    }

    @Test
    public void testBuild() {
        Mode gameMode = new Standard();
        Game newGame = new Game ("advanced", EntityFactory.extractEntities("advanced", gameMode), null, gameMode);

        Player gamePlayer = null;
        for (Entity entity: newGame.getEntities()) {
            if (entity.getClass().getSimpleName().toLowerCase().equals ("player")) {
                gamePlayer = (Player) entity;
            }
        }

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

        assertDoesNotThrow(() -> newGame.build("bow"));

        // bow is in inventory
        assert(gamePlayer.getInventory().getInventoryResponses().size() == 1);
    }

    @Test
    public void testInteract () {
        Mode gameMode = new Standard();
        Game newGame = new Game ("advanced", EntityFactory.extractEntities("advanced", gameMode), null, gameMode);

        Player gamePlayer = null;
        Mercenary mercenary = null;
        

        for (Entity entity: newGame.getEntities()) {
            if (entity.getClass().getSimpleName().toLowerCase().equals ("player")) {
                gamePlayer = (Player) entity;
            }
        }

        for (Entity entity: newGame.getEntities()) {
            if (entity.getClass().getSimpleName().toLowerCase().equals ("mercenary")) {
                mercenary = (Mercenary) entity;
            }
        }

        String mercenaryId = mercenary.getId();

        for (int i = 0; i < 15; i++) {
            newGame.tick (null, Direction.RIGHT);            
        }

        for (int i = 0; i < 9; i++) {
            newGame.tick (null, Direction.DOWN);            
        }

        for (int i = 0; i < 9; i++) {
            newGame.tick (null, Direction.LEFT);            
        }

        for (int i = 0; i < 3; i++) {
            newGame.tick (null, Direction.UP);            
        } 
        
        assertDoesNotThrow(()->newGame.interact(mercenaryId));
        assert (gamePlayer.getAllies().size() == 1);
    }

}
