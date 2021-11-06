package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.equipment.Sword;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class ZombieToastSpawnerTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(1, 1), mode.tickRate());
        game.addEntity(spawner);

        assertTrue(new Position(1, 1).equals(game.getEntity(spawner.getId()).getPosition()));
    }

    /**
     * Test whether a ZombieToast spawns every 20 ticks in an open square cardinally adjacent to the spawner.
     */
    @Test
    public void zombieToastSpawnEveryCycle() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(1, 1), mode.tickRate());
        game.addEntity(spawner);
        game.addEntity(new Player(new Position(10, 10)));
        // Ticks the game 20 times
        for (int i = 0; i < 20; i++) {
            game.tick("", Direction.NONE);
        }

        // Check that only one zombie toast has spawned
        int count = 0;
        for (Entity entity : game.getEntities()) {
            if (entity instanceof ZombieToast) {
                ZombieToast zombieToast = (ZombieToast) entity;
                if (zombieToast.getPosition().equals(new Position(1, 0))) count++;
                if (zombieToast.getPosition().equals(new Position(1, 2))) count++;
                if (zombieToast.getPosition().equals(new Position(0, 1))) count++;
                if (zombieToast.getPosition().equals(new Position(2, 1))) count++;
            }
        }

        // count = player + spawner + zombie
        assertTrue(count == 1);
    }

    /**
     * Test that a ZombieToast cannot spawn as it is completely surrounded by walls.
     */
    @Test
    public void zombieToastSurroundedByWalls() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(1, 1), mode.tickRate());
        game.addEntity(spawner);

        // The zombie toast spawner is surrounded by either walls or boulders
        game.addEntity(new Wall(new Position(0, 0)));
        game.addEntity(new Wall(new Position(0, 1)));
        game.addEntity(new Wall(new Position(0, 2)));
        game.addEntity(new Wall(new Position(1, 0)));
        game.addEntity(new Wall(new Position(1, 2)));
        game.addEntity(new Wall(new Position(2, 0)));
        game.addEntity(new Wall(new Position(2, 1)));
        game.addEntity(new Wall(new Position(2, 2)));

        // Check that there are no zombie toasts spawned
        for (Entity entity : game.getEntities()) {
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
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(1, 1), mode.tickRate());
        game.addEntity(spawner);

        // The zombie toast spawner is blocked by boulders in the four cardinal directions
        game.addEntity(new Boulder(new Position(0, 1)));
        game.addEntity(new Boulder(new Position(1, 0)));
        game.addEntity(new Boulder(new Position(1, 2)));
        game.addEntity(new Boulder(new Position(2, 1)));

        // Check that there are no zombie toasts spawned
        for (Entity entity : game.getEntities()) {
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
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(1, 1), mode.tickRate());
        game.addEntity(spawner);
        
        game.addEntity(new Sword(new Position(2, 1)));

        Player player = new Player(new Position(2, 2));
        game.addEntity(player);
        
        // Player picks up sword
        player.move(game, Direction.UP);

        // Player moves to the right of the zombie toast spawner
        // They will interact with it, destroying the zombie toast spawner with the sword
        player.move(game, Direction.LEFT);
        
        // Check that the zombie toast spawner has been destroyed
        assertTrue(game.getEntity(spawner.getId()) == null);
    }

    /**
     * Interact with a zombie spawner without a weapon - should raise exception.
     */
    @Test
    public void zombieToastDestroySpawnerWithoutWeapon() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(1, 1), mode.tickRate());
        game.addEntity(spawner);
        
        Player player = new Player(new Position(1, 0));
        game.addEntity(player);
        
        assertThrows(InvalidActionException.class, () -> game.interact(spawner.getId()));
    }

}
