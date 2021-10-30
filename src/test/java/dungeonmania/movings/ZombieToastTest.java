package dungeonmania.movings;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import dungeonmania.DungeonManiaController;
import dungeonmania.model.Dungeon;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

@TestInstance(value = Lifecycle.PER_CLASS)
public class ZombieToastTest {
    final static String SPIDER_1 = "spider_1";
    final static String DUNGEON_NAME = "standard";
    final static String GAME_MODE = "peaceful";
    
    @Test
    public void testZombieSpawnRateNormalModes() {
        Dungeon dungeon = new Dungeon(7, 7);
        Player player = new Player("player", new Position(1, 1));
        dungeon.addEntity(player);
        
        ZombieToastSpawner spawner = new ZombieToastSpawner("spawner", new Position(5, 5));
        dungeon.addEntity(spawner);
        
        int numEntities = dungeon.getAllEntities().size();

        // Zombie should spawn
        for(int i = 0; i < 20; i++) {
            player.move(dungeon, Direction.NONE);
        }

        assertTrue(dungeon.getAllEntities().size() > numEntities);
    }
    
    @Test
    public void testBasicMovement() {
        Dungeon dungeon = new Dungeon(7, 7);
        Player player = new Player("player", new Position(1, 1));
        dungeon.addEntity(player);

        Position zombiePos =  new Position(5, 5);
        ZombieToastSpawner spawner = new ZombieToastSpawner("spawner", zombiePos);
        dungeon.addEntity(spawner);

        
        player.move(dungeon, Direction.RIGHT);
        List<Entity> entitiesAtOldZombiePos = dungeon.getEntitiesAtPosition(zombiePos);
        
        // zombie should change position as there exists an open tile
        assertTrue(entitiesAtOldZombiePos.size() == 0);
    }

    @Test
    public void testWallBlockingMovement() {
        Dungeon dungeon = new Dungeon(7, 7);
        Player player = new Player("player", new Position(1, 1));
        dungeon.addEntity(player);
        
        ZombieToastSpawner spawner = new ZombieToastSpawner("spawner", new Position(5, 5));
        dungeon.addEntity(spawner);
        
        // surround zombie with a wall, leaving one tile adjacent to the spawner open
        dungeon.addEntity(new Wall("wall1", new Position(3, 5)));
        dungeon.addEntity(new Wall("wall2", new Position(3, 4)));
        dungeon.addEntity(new Wall("wall3", new Position(4, 4)));
        dungeon.addEntity(new Wall("wall4", new Position(5, 4)));

        // zombie should spawn in 20 ticks
        for(int i = 0; i < 20; i++ ) {
            player.move(dungeon, Direction.NONE);
        }
        
        Position expectedZombieSpawnTile = new Position(4, 5);
        assertTrue(dungeon.getEntitiesAtPosition(expectedZombieSpawnTile).size() == 0);
        List<Entity> entitesAtTileAdjacentToSpawner = dungeon.getEntitiesAtPosition(expectedZombieSpawnTile);
        assertTrue(entitesAtTileAdjacentToSpawner.size() > 0);
    }

    @Test
    public void testEdgeCornerMovement() {
        // wall placed along wall 
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