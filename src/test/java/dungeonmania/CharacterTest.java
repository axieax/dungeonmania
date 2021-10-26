package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

@TestInstance(value = Lifecycle.PER_CLASS)
public class CharacterTest {
    final static String CHARACTER_TYPE = "player";
    final static String DUNGEON_NAME = "advanced";
    final static String GAME_MODE = "peaceful";

    @Test
    public void testMovementIdempotence() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame(DUNGEON_NAME, GAME_MODE);

        // a tick without movement
        DungeonResponse response = controller.tick(null, Direction.NONE);

        List<EntityResponse> entities = response.getEntities();
        assertTrue(entities.size() > 0);

        Position characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(1, 1).toString(), characterPos.toString());

        // the response above should be equal to a new game as no entities have moved (gamemode is peaceful)
        DungeonResponse newResponse = controller.newGame(DUNGEON_NAME, GAME_MODE);

        assertTrue(response.equals(newResponse));
    }
    
    @Test
    public void testBasicMovement() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_NAME, GAME_MODE);

        // initial position 1, 1
        List<EntityResponse> entities = response.getEntities();
        assertTrue(entities.size() > 0);

        Position characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(1, 1).toString(), characterPos.toString());
        
        // move character down
        response = controller.tick(null, Direction.DOWN);
        
        entities = response.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(2, 1).toString(), characterPos.toString());

        // move character right
        response = controller.tick(null, Direction.RIGHT);
        
        entities = response.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(2, 2).toString(), characterPos.toString());

        // move character up
        response = controller.tick(null, Direction.UP);
        
        entities = response.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(1, 2).toString(), characterPos.toString());

        // move character left
        response = controller.tick(null, Direction.LEFT);
        
        entities = response.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(1, 1).toString(), characterPos.toString());

    }
    
    @Test
    public void testTopLeftCornerMovement() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame(DUNGEON_NAME, GAME_MODE);

        // moving character left or up should not change position as character is already in the top left corner
        DungeonResponse response = controller.tick(null, Direction.LEFT);
        
        List<EntityResponse> entities = response.getEntities();
        assertTrue(entities.size() > 0);

        Position characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(1, 1).toString(), characterPos.toString());


        response = controller.tick(null, Direction.UP);
        
        entities = response.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(1, 1).toString(), characterPos.toString());
    }

    @Test
    public void testNonBorderWallMovement() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame(DUNGEON_NAME, GAME_MODE);

        DungeonResponse response = controller.tick(null, Direction.DOWN);
        
        // Place character halfway down the left wall and ensure that it cannot move left
        for(int i = 0; i < 8; i++) {
            response = controller.tick(null, Direction.DOWN);
        }

        // Confirm character position
        List<EntityResponse> entities = response.getEntities();
        assertTrue(entities.size() > 0);

        Position characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(8, 1).toString(), characterPos.toString());

        // Ensure character cannot move left into dungeon wall
        response = controller.tick(null, Direction.LEFT);

        entities = response.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(8, 1).toString(), characterPos.toString());

        // Ensure character cannot move into dungeon wall placed in middle of map
        response = controller.tick(null, Direction.RIGHT);
        entities = response.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(8, 2).toString(), characterPos.toString());

        // further movement attempts to the right result it the character staying in
        // the same position due to a non border wall
        for(int i = 0; i < 8; i++) {
            response = controller.tick(null, Direction.RIGHT);
            entities = response.getEntities();
            assertTrue(entities.size() > 0);

            characterPos = getCharacterPosition(entities);
            assertNotNull(characterPos);
            assertEquals(new Position(8, 2).toString(), characterPos.toString());
        }
    }

    @Test
    public void testMovementIntoSpaceWithEntity() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_NAME, GAME_MODE);

        // initial position is 1, 1
        
        List<EntityResponse> entities = response.getEntities();
        assertTrue(entities.size() > 0);

        Position characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(1, 1).toString(), characterPos.toString());

        
        // move the character 4 blocks to the right
        // in this position a sword entity will be to the right
        for(int i = 0; i < 4; i++) {
            response = controller.tick(null, Direction.RIGHT);
            int y = i + 2;
            
            // ensure character in correct position
            entities = response.getEntities();
            assertTrue(entities.size() > 0);

            characterPos = getCharacterPosition(entities);
            assertNotNull(characterPos);
            assertEquals(new Position(1, y).toString(), characterPos.toString());
        }

        // one more movement to the right will result in character being on top of/using that entity
        assertDoesNotThrow(() -> controller.tick(null, Direction.RIGHT));

        // ensure character in correct position
        entities = response.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(1, 6).toString(), characterPos.toString());
    }

    @Test
    public void testMovementDoesNotAffectDungeonState() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_NAME, GAME_MODE);

        // initial position 1, 1 - original response
        List<EntityResponse> entities = response.getEntities();
        assertTrue(entities.size() > 0);

        Position characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(1, 1).toString(), characterPos.toString());

        // Check that as the character moves without interacting with any entities,
        // the dungeon response stays the same (except the position of the character)

        // character moves down - a new response is created
        DungeonResponse updatedResponse = controller.tick(null, Direction.DOWN);
    
        entities = updatedResponse.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(2, 1).toString(), characterPos.toString());

        // compares two DungeonResponses and returns true if they are the same
        // NOTE: Comparison is only done on the actual state of the dungeon, and not
        //       on fields like dungeonId, dungeonName etc. The player position is also disregarded.

        // ensure entities are equal (except player)
        List<EntityResponse> responseEntities =  response.getEntities();
        List<EntityResponse> updatedEntities = updatedResponse.getEntities();
        assertTrue(updatedEntities.size() > 0);
        assertEquals(responseEntities.size(), updatedEntities.size());

        for(int i = 0; i < updatedEntities.size(); i++) {
            // don't compare players as their position is different
            if(updatedEntities.get(i).getType() == CHARACTER_TYPE) {
                continue;
            }
            
            assertTrue(responseEntities.get(i).equals(updatedEntities.get(i)));
        }

        // ensure inventory remains unchanged
        List<ItemResponse> responseInventory = response.getInventory();
        List<ItemResponse> updatedInventory = updatedResponse.getInventory();
        assertTrue(updatedInventory.size() > 0);
        assertEquals(responseInventory.size(), updatedInventory.size());

        for(int i = 0; i < updatedInventory.size(); i++) {
            assertTrue(responseInventory.get(i).equals(updatedInventory.get(i)));
        }

        // ensure buildables are the same
        List<String> responseBuildables = response.getBuildables();
        List<String> updatedBuildables = updatedResponse.getBuildables();

        assertEquals(responseBuildables.size(), updatedBuildables.size());
        for(int i = 0; i < updatedBuildables.size(); i++) {
            assertTrue(responseBuildables.get(i).equals(updatedBuildables.get(i)));
        }

        // ensure goals remain the same
        String responseGoals = response.getGoals();
        String updatedGoals = updatedResponse.getGoals();
        assertEquals(responseGoals, updatedGoals);
    }

    @Test
    public void testSavingAndLoadingGameRetainsOriginalPosition() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_NAME, GAME_MODE);

        // initial position 1, 1
        List<EntityResponse> entities = response.getEntities();
        assertTrue(entities.size() > 0);

        Position characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(1, 1).toString(), characterPos.toString());
        
        // move character to 6, 4
        for(int i = 0; i < 6; i++) {
            response = controller.tick(null, Direction.DOWN);
        }

        for(int i = 0; i < 4; i++) {
            response = controller.tick(null, Direction.RIGHT);
        }

        entities = response.getEntities();
        assertTrue(entities.size() > 0);

        characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(6, 4).toString(), characterPos.toString());

        // save the game
        assertDoesNotThrow(() -> controller.saveGame("1"));

        // load the game
        final DungeonResponse innerResponse = response;
        assertDoesNotThrow(() -> {
            DungeonResponse loadedResponse = controller.loadGame("1");

            List<EntityResponse> responseEntities =  innerResponse.getEntities();
            List<EntityResponse> loadedEntities = loadedResponse.getEntities();
            assertTrue(loadedEntities.size() > 0);
            assertEquals(responseEntities.size(), loadedEntities.size());
            for(int i = 0; i < loadedEntities.size(); i++) {
                if(loadedEntities.get(i).getType() == CHARACTER_TYPE) {
                    // ensure player position has not changed
                    assertTrue(responseEntities.get(i).equals(loadedEntities.get(i)));
                    break;
                }
            }
        });
    }

    @Test
    public void testSavingAndLoadingGameMovement() {
        // saving a game and then loading it does not affect movement

        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_NAME, GAME_MODE);

        // initial position 1, 1
        List<EntityResponse> entities = response.getEntities();
        assertTrue(entities.size() > 0);

        Position characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(1, 1).toString(), characterPos.toString());

        // save the game
        assertDoesNotThrow(() -> controller.saveGame("1"));

        // load the game
        final DungeonManiaController innerController = controller;
        assertDoesNotThrow(() -> {
            // move down
            DungeonResponse loadedResponse = innerController.tick(null, Direction.DOWN);

            List<EntityResponse> loadedEntities = loadedResponse.getEntities();
            assertTrue(loadedEntities.size() > 0);

            Position loadedCharacterPos = getCharacterPosition(loadedEntities);
            assertNotNull(loadedCharacterPos);
            assertEquals(new Position(2, 1).toString(), loadedCharacterPos.toString());

            // move right
            loadedResponse = innerController.tick(null, Direction.RIGHT);

            loadedEntities = loadedResponse.getEntities();
            assertTrue(loadedEntities.size() > 0);

            loadedCharacterPos = getCharacterPosition(loadedEntities);
            assertNotNull(loadedCharacterPos);
            assertEquals(new Position(2, 2).toString(), loadedCharacterPos.toString());

            // move up
            loadedResponse = innerController.tick(null, Direction.UP);

            loadedEntities = loadedResponse.getEntities();
            assertTrue(loadedEntities.size() > 0);

            loadedCharacterPos = getCharacterPosition(loadedEntities);
            assertNotNull(loadedCharacterPos);
            assertEquals(new Position(1, 2).toString(), loadedCharacterPos.toString());

            // move left
            // move up
            loadedResponse = innerController.tick(null, Direction.LEFT);

            loadedEntities = loadedResponse.getEntities();
            assertTrue(loadedEntities.size() > 0);

            loadedCharacterPos = getCharacterPosition(loadedEntities);
            assertNotNull(loadedCharacterPos);
            assertEquals(new Position(1, 1).toString(), loadedCharacterPos.toString());
        });
    }

    @Test
    public void testBuildDoesNotChangeCharacterPosition() {
        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_NAME, GAME_MODE);

        // initial position 1, 1
        List<EntityResponse> entities = response.getEntities();
        assertTrue(entities.size() > 0);

        Position characterPos = getCharacterPosition(entities);
        assertNotNull(characterPos);
        assertEquals(new Position(1, 1).toString(), characterPos.toString());


        for(int i = 0; i < 12; i++) {
            response = controller.tick(null, Direction.DOWN);
        }

        assertEquals(new Position(13, 1).toString(), characterPos.toString());
        
        for(int i = 0; i < 10; i++) {
            response = controller.tick(null, Direction.DOWN);
        }

        assertEquals(new Position(13, 11).toString(), characterPos.toString());

        response = controller.tick(null, Direction.UP); // collect arrow
        response = controller.tick(null, Direction.DOWN);
        response = controller.tick(null, Direction.RIGHT); // collect arrow
        response = controller.tick(null, Direction.RIGHT); // collect wood

        assertEquals(new Position(13, 13).toString(), characterPos.toString());
        
        // build bow - player has sufficient items
        response = assertDoesNotThrow(() -> controller.build("bow"));
        
        // position after building should be the same
        assertEquals(new Position(13, 13).toString(), characterPos.toString());
    }
    
    // @Test
    // public void testMovementDoesNotAffectHealth() {
        // // Create a new controller
        // DungeonManiaController controller = new DungeonManiaController();
        // controller.newGame(DUNGEON_NAME, GAME_MODE);

        // // initial position 1, 1
        // DungeonResponse response = controller.tick(null, Direction.NONE);

        // List<EntityResponse> entities = response.getEntities();
        // assertTrue(entities.size() > 0);

        // Position characterPos = getCharacterPosition(entities);
        // assertNotNull(characterPos);
        // assertEquals(new Position(1, 1).toString(), characterPos.toString());
    // }


    public Position getCharacterPosition(List<EntityResponse> entities) throws IllegalArgumentException, InvalidActionException {
        for(EntityResponse e: entities) {
            if(e.getType() == CHARACTER_TYPE) {
                return e.getPosition();
            }
        }

        return null;
    }
}