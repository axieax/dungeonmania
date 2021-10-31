package dungeonmania.movings;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

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
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

@TestInstance(value = Lifecycle.PER_CLASS)
public class ZombieToastTest {
    @Test
    public void testZombieSpawnRateNormalModes() {
        Mode mode = new Standard();

        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);
        
        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(5, 5), mode.tickRate());
        game.addEntity(spawner);
        
        int numEntities = game.getEntities().size();

        // Zombie should spawn
        for(int i = 0; i < 20; i++) {
            player.move(game, Direction.NONE);
        }

        assertTrue(game.getEntities().size() > numEntities);
    }
    
    @Test
    public void testBasicMovement() {
        Mode mode = new Standard();

        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        Position zombiePos =  new Position(5, 5);
        ZombieToastSpawner spawner = new ZombieToastSpawner(zombiePos, mode.tickRate());
        game.addEntity(spawner);

        
        player.move(game, Direction.RIGHT);
        List<Entity> entitiesAtOldZombiePos = game.getEntities(zombiePos);
        
        // zombie should change position as there exists an open tile
        assertTrue(entitiesAtOldZombiePos.size() == 0);
    }

    @Test
    public void testWallBlockingMovement() {
        Mode mode = new Standard();
        
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);
        
        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(5, 5), mode.tickRate());
        game.addEntity(spawner);
        
        // surround zombie with a wall, leaving one tile adjacent to the spawner open
        game.addEntity(new Wall(new Position(3, 5)));
        game.addEntity(new Wall(new Position(3, 4)));
        game.addEntity(new Wall(new Position(4, 4)));
        game.addEntity(new Wall(new Position(5, 4)));

        Position expectedZombieSpawnTile = new Position(4, 5);
        // zombie should spawn in 20 ticks
        for(int i = 0; i < 20; i++ ) {
            assertTrue(game.getEntities(expectedZombieSpawnTile).size() == 0);
            player.move(game, Direction.NONE);
        }
        
        List<Entity> entitesAtTileAdjacentToSpawner = game.getEntities(expectedZombieSpawnTile);
        assertTrue(entitesAtTileAdjacentToSpawner.size() > 0);
    }

    @Test
    public void testEdgeCornerMovement() {
        Mode mode = new Standard();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);
        
        Player player = new Player(new Position(1, 1));
        game.addEntity(player);
        
        Position zombiePos = new Position(5, 5);
        ZombieToast zombie = new ZombieToast(zombiePos, mode.damageMultiplier(), player);

        assertTrue(game.getEntities(zombiePos).size() == 0);
        
        game.addEntity(zombie);
        assertTrue(game.getEntities(zombiePos).size() > 0);
        
        game.addEntity(new Wall(new Position(4, 4)));
        game.addEntity(new Wall(new Position(5, 4)));
        game.addEntity(new Wall(new Position(4, 5)));
        
        // zombie is trapped in the corner and should not move in further ticks
        zombie.tick(game);
        assertTrue(game.getEntities(zombiePos).size() > 0);
    }
   
    @Test
    public void testZombieCannotWalkThroughClosedDoor() {
        Mode mode = new Standard();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);
        
        Position zombiePos = new Position(5, 5);
        ZombieToast zombie = new ZombieToast(zombiePos, mode.damageMultiplier(), player);

        assertTrue(game.getEntities(zombiePos).size() == 0);
        
        game.addEntity(zombie);
        assertTrue(game.getEntities(zombiePos).size() > 0);
        
        game.addEntity(new Wall(new Position(4, 3)));
        game.addEntity(new Wall(new Position(4, 4)));
        game.addEntity(new Wall(new Position(4, 5)));
        game.addEntity(new Wall(new Position(5, 4)));

        Position doorPos = new Position(5, 4);
        Position keyPos = new Position(1, 5);
        Key key = new Key(keyPos, 1);
        game.addEntity(new Door(doorPos, 1));
        game.addEntity(key);

        // zombie is trapped in the corner and should not move in further ticks
        for(int i = 0; i < 5; i ++) {
            zombie.tick(game);
            assertTrue(game.getEntities(zombiePos).size() > 0);
        }
    }
    
    @Test
    public void testZombieCanWalkThroughOpenDoor() {

    }

    @Test
    public void testPortalNoEffect() {
        // portals have no effect on zombies
        Mode mode = new Standard();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);
        
        Position zombiePos = new Position(5, 5);
        ZombieToast zombie = new ZombieToast(zombiePos, mode.damageMultiplier(), player);

        assertTrue(game.getEntities(zombiePos).size() == 0);
        
        game.addEntity(zombie);
        assertTrue(game.getEntities(zombiePos).size() > 0);
        
        game.addEntity(new Wall(new Position(4, 3)));
        game.addEntity(new Wall(new Position(4, 4)));
        game.addEntity(new Wall(new Position(4, 5)));
        game.addEntity(new Wall(new Position(5, 4)));

        Position portalPos = new Position(5, 4);
        Portal portal = new Portal(portalPos, "blue");
        game.addEntity(portal);
        
        // the only option for the zombie is to move to the portal
        assertTrue(game.getEntities(portalPos).size() == 0);
        zombie.tick(game);
        assertTrue(game.getEntities(portalPos).size() > 0); // portal has no effect
    }

    @Test
    public void testZombmieCannotMoveBoulder() {
        Mode mode = new Standard();
        Game game = new Game("game", sevenBySevenWallBoundary(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);
        
        Position zombiePos = new Position(5, 5);
        ZombieToast zombie = new ZombieToast(zombiePos, mode.damageMultiplier(), player);

        assertTrue(game.getEntities(zombiePos).size() == 0);
        
        game.addEntity(zombie);
        assertTrue(game.getEntities(zombiePos).size() > 0);
        
        game.addEntity(new Wall(new Position(4, 3)));
        game.addEntity(new Wall(new Position(4, 4)));
        game.addEntity(new Wall(new Position(4, 5)));
        game.addEntity(new Wall(new Position(5, 4)));

        Position boulderPos = new Position(5, 4);
        Boulder boulder = new Boulder(boulderPos);
        game.addEntity(boulder);
        
        // zombie should stay in its position, as it cannot move a boulder
        assertTrue(game.getEntities(boulderPos).size() == 0);
        zombie.tick(game);
        assertTrue(game.getEntities(boulderPos).size() == 0); // portal has no effect
        assertTrue(game.getEntities(zombiePos).size() == 1);
    }

    private List<Entity> sevenBySevenWallBoundary() {
        ArrayList<Entity> wallBorder = new ArrayList<>();
        
        // left border
        for(int i = 0; i < 7; i ++) {
            Wall wall = new Wall(new Position(0, i));
            wallBorder.add(wall);
        }
        
        // right border
        for(int i = 0; i < 7; i ++) {
            Wall wall = new Wall(new Position(6, i));
            wallBorder.add(wall);
        }

        // top border
        for(int i = 1; i < 6; i ++) {
            Wall wall = new Wall(new Position(i, 0));
            wallBorder.add(wall);
        }

        // bottom border
        for(int i = 1; i < 6; i ++) {
            Wall wall = new Wall(new Position(i, 6));
            wallBorder.add(wall);
        }

        return wallBorder;
    }

}