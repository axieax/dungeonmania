package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.DungeonManiaController;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.equipment.Sword;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class ZombieToastSpawnerTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Game game = new Game(3, 3);
        game.addEntity(new ZombieToastSpawner("zombietoastspawner1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(game.getEntity("zombietoastspawner1").getPosition()));
    }

    /**
     * Test whether a ZombieToast spawns every 20 ticks in an open square cardinally adjacent to the spawner.
     */
    @Test
    public void zombieToastSpawnEveryCycle() {
      
        DungeonManiaController controller = new DungeonManiaController();
        Game game = new Game(3, 3);
        game.addEntity(new ZombieToastSpawner("zombietoastspawner1", new Position(1, 1)));
        
        // Ticks the game 20 times
        for (int i = 0; i < 20; i++) {
            controller.tick(null, Direction.NONE);
        }

        // Check that only one zombie toast has spawned
        int count = 0;
        for (Entity entity : game.getAllEntities()) {
            if (entity instanceof ZombieToast) {
                ZombieToast zombieToast = (ZombieToast) entity;
                if (zombieToast.getPosition().equals(new Position(1, 0))) count++;
                if (zombieToast.getPosition().equals(new Position(1, 2))) count++;
                if (zombieToast.getPosition().equals(new Position(0, 1))) count++;
                if (zombieToast.getPosition().equals(new Position(2, 1))) count++;
            }
        }

        assertTrue(count == 1);
    }

    /**
     * Test that a ZombieToast cannot spawn as it is completely surrounded by walls.
     */
    @Test
    public void zombieToastSurroundedByWalls() {
        Game game = new Game(3, 3);
        game.addEntity(new ZombieToastSpawner("zombietoastspawner1", new Position(1, 1)));

        // The zombie toast spawner is surrounded by either walls or boulders
        game.addEntity(new Wall("wall1", new Position(0, 0)));
        game.addEntity(new Wall("wall2", new Position(0, 1)));
        game.addEntity(new Wall("wall3", new Position(0, 2)));
        game.addEntity(new Wall("wall4", new Position(1, 0)));
        game.addEntity(new Wall("wall5", new Position(1, 2)));
        game.addEntity(new Wall("wall6", new Position(2, 0)));
        game.addEntity(new Wall("wall7", new Position(2, 1)));
        game.addEntity(new Wall("wall8", new Position(2, 2)));

        // Check that there are no zombie toasts spawned
        for (Entity entity : game.getAllEntities()) {
            if (entity instanceof ZombieToast) {
                fail("ZombieToast spawned even though there are no open squares");
            }
        }
    }

    /**
     * Test that a ZombieToast cannot spawn as there is no open square cardinally adjacent to the spawner.
     */
    @Test
    public void zombieToastCannotSpawn() {
        Game game = new Game(3, 3);
        game.addEntity(new ZombieToastSpawner("zombietoastspawner1", new Position(1, 1)));

        // The zombie toast spawner is blocked by boulders in the four cardinal directions
        game.addEntity(new Boulder("boulder1", new Position(0, 1)));
        game.addEntity(new Boulder("boulder2", new Position(1, 0)));
        game.addEntity(new Boulder("boulder3", new Position(1, 2)));
        game.addEntity(new Boulder("boulder4", new Position(2, 1)));

        // Check that there are no zombie toasts spawned
        for (Entity entity : game.getAllEntities()) {
            if (entity instanceof ZombieToast) {
                fail("ZombieToast spawned even though there are no open squares");
            }
        }
    }
    
    /**
     * Test if player can destroy spawner if they have a weapon and are cardinally adjacent to the spawner.
     */
    @Test
    public void zombieToastDestroySpawner() {
        Game game = new Game(3, 3);
        game.addEntity(new ZombieToastSpawner("zombietoastspawner1", new Position(1, 1)));
        game.addEntity(new Sword("sword", new Position(3, 1)));

        Player player = new Player("player1", new Position(3, 2));
        
        // Player picks up sword
        player.move(game, Direction.UP);

        // Player moves to the right of the zombie toast spawner
        // They will interact with it, destroying the zombie toast spawner with the sword
        player.move(game, Direction.LEFT);
        
        // Check that the zombie toast spawner has been destroyed
        assertTrue(game.getEntity("zombietoastspawner1") == null);
    }

}
