package dungeonmania.movings;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;

@TestInstance(value = Lifecycle.PER_CLASS)
public class ZombieToastTest {
    final static String SPIDER_1 = "spider_1";
    final static String DUNGEON_NAME = "standard";
    final static String GAME_MODE = "peaceful";
    
    @Test
    public void testZombieSpawnRateNormalModes() {
        // zombies spawn every 20 ticks
    }
    
    @Test
    public void testBasicMovement() {
        // at least one spider must always spawn

        // Create a new controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(DUNGEON_NAME, GAME_MODE);
    }

    @Test
    public void testWallBlockingMovement() {
        // zombies can't move through walls
    }

    @Test
    public void testEdgeCornerMovement() {

    }
   
    @Test
    public void testZombieCannotWalkThroughClosedDoor() {
        
    }
    
    @Test
    public void testZombieCanWalkThroughOpenDoor() {
        // since zombie has same constraints as character
    }

    @Test
    public void testPortalNoEffect() {
        // portals have no effect on zombies
    }

    @Test
    public void testMovementIntoSpaceWithEntity() {

    }

}