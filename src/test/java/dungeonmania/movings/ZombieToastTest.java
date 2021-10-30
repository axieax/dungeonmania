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
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.Door;
import dungeonmania.model.entities.statics.Portal;
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

        Position expectedZombieSpawnTile = new Position(4, 5);
        // zombie should spawn in 20 ticks
        for(int i = 0; i < 20; i++ ) {
            assertTrue(dungeon.getEntitiesAtPosition(expectedZombieSpawnTile).size() == 0);
            player.move(dungeon, Direction.NONE);
        }
        
        List<Entity> entitesAtTileAdjacentToSpawner = dungeon.getEntitiesAtPosition(expectedZombieSpawnTile);
        assertTrue(entitesAtTileAdjacentToSpawner.size() > 0);
    }

    @Test
    public void testEdgeCornerMovement() {
        Dungeon dungeon = new Dungeon(7, 7);

        Position zombiePos = new Position(5, 5);
        ZombieToast zombie = new ZombieToast("zombie", zombiePos);

        assertTrue(dungeon.getEntitiesAtPosition(zombiePos).size() == 0);
        
        dungeon.addEntity(zombie);
        assertTrue(dungeon.getEntitiesAtPosition(zombiePos).size() > 0);
        
        dungeon.addEntity(new Wall("wall1", new Position(4, 4)));
        dungeon.addEntity(new Wall("wall2", new Position(5, 4)));
        dungeon.addEntity(new Wall("wall3", new Position(4, 5)));
        
        // zombie is trapped in the corner and should not move in further ticks
        zombie.tick(dungeon);
        assertTrue(dungeon.getEntitiesAtPosition(zombiePos).size() > 0);
    }
   
    @Test
    public void testZombieCannotWalkThroughClosedDoor() {
        Dungeon dungeon = new Dungeon(7, 7);

        Position zombiePos = new Position(5, 5);
        ZombieToast zombie = new ZombieToast("zombie", zombiePos);

        assertTrue(dungeon.getEntitiesAtPosition(zombiePos).size() == 0);
        
        dungeon.addEntity(zombie);
        assertTrue(dungeon.getEntitiesAtPosition(zombiePos).size() > 0);
        
        dungeon.addEntity(new Wall("wall1", new Position(4, 3)));
        dungeon.addEntity(new Wall("wall1", new Position(4, 4)));
        dungeon.addEntity(new Wall("wall3", new Position(4, 5)));
        dungeon.addEntity(new Wall("wall2", new Position(5, 4)));

        Position doorPos = new Position(5, 4);
        Position keyPos = new Position(1, 5);
        Key key = new Key("key", keyPos, 1);
        dungeon.addEntity(new Door("door", doorPos, 1));

        // zombie is trapped in the corner and should not move in further ticks
        for(int i = 0; i < 5; i ++) {
            zombie.tick(dungeon);
            assertTrue(dungeon.getEntitiesAtPosition(zombiePos).size() > 0);
        }
    }
    
    @Test
    public void testZombieCanWalkThroughOpenDoor() {

    }

    @Test
    public void testPortalNoEffect() {
        // portals have no effect on zombies
        Dungeon dungeon = new Dungeon(7, 7);

        Position zombiePos = new Position(5, 5);
        ZombieToast zombie = new ZombieToast("zombie", zombiePos);

        assertTrue(dungeon.getEntitiesAtPosition(zombiePos).size() == 0);
        
        dungeon.addEntity(zombie);
        assertTrue(dungeon.getEntitiesAtPosition(zombiePos).size() > 0);
        
        dungeon.addEntity(new Wall("wall1", new Position(4, 3)));
        dungeon.addEntity(new Wall("wall1", new Position(4, 4)));
        dungeon.addEntity(new Wall("wall3", new Position(4, 5)));
        dungeon.addEntity(new Wall("wall2", new Position(5, 4)));

        Position portalPos = new Position(5, 4);
        Portal portal = new Portal("portal", portalPos, "blue");
        dungeon.addEntity(portal);
        
        // the only option for the zombie is to move to the portal
        assertTrue(dungeon.getEntitiesAtPosition(portalPos).size() == 0);
        zombie.tick(dungeon);
        assertTrue(dungeon.getEntitiesAtPosition(portalPos).size() > 0); // portal has no effect
    }

    @Test
    public void testZombmieCannotMoveBoulder() {
        Dungeon dungeon = new Dungeon(7, 7);

        Position zombiePos = new Position(5, 5);
        ZombieToast zombie = new ZombieToast("zombie", zombiePos);

        assertTrue(dungeon.getEntitiesAtPosition(zombiePos).size() == 0);
        
        dungeon.addEntity(zombie);
        assertTrue(dungeon.getEntitiesAtPosition(zombiePos).size() > 0);
        
        dungeon.addEntity(new Wall("wall1", new Position(4, 3)));
        dungeon.addEntity(new Wall("wall1", new Position(4, 4)));
        dungeon.addEntity(new Wall("wall3", new Position(4, 5)));
        dungeon.addEntity(new Wall("wall2", new Position(5, 4)));

        Position boulderPos = new Position(5, 4);
        Boulder boulder = new Boulder("boulder", boulderPos);
        dungeon.addEntity(boulder);
        
        // zombie should stay in its position, as it cannot move a boulder
        assertTrue(dungeon.getEntitiesAtPosition(boulderPos).size() == 0);
        zombie.tick(dungeon);
        assertTrue(dungeon.getEntitiesAtPosition(boulderPos).size() == 0); // portal has no effect
        assertTrue(dungeon.getEntitiesAtPosition(zombiePos).size() == 1);
    }

}