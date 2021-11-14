package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.TestHelpers;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.equipment.Anduril;
import dungeonmania.model.entities.movings.Assassin;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.model.entities.movings.Hydra;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Hard;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Peaceful;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class AndurilTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Anduril anduril = new Anduril(new Position(1, 1));
        game.addEntity(anduril);
        String andurilId = anduril.getId();

        assertTrue(new Position(1, 1).equals(game.getEntity(andurilId).getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Anduril anduril = new Anduril(new Position(1, 1));
        game.addEntity(anduril);

        Player player = new Player(new Position(0, 1), mode.initialHealth());
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));

        assertTrue(game.getEntity(anduril.getId()) == null);
        assertTrue(player.getInventoryItem(anduril.getId()).equals(anduril));
    }

    /**
     * Test durability of Anduril.
     */
    @Test
    public void durabilityTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Anduril anduril = new Anduril(new Position(1, 1));
        game.addEntity(anduril);

        Player player = new Player(new Position(0, 1), mode.initialHealth());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);

        // Durability of anduril when picked up should be 5
        int initialDurability = 5;
        assertTrue(anduril.getDurability() == initialDurability);

        ZombieToastSpawner spawner = new ZombieToastSpawner(
            new Position(3, 1),
            mode.damageMultiplier()
        );
        game.addEntity(spawner);

        player.move(game, Direction.RIGHT);

        // Player is now next to the zombie toast spawner and will proceed to destroy it with the anduril
        // Durability of anudril decreases by 1 each time it battles (within one tick)
        assertDoesNotThrow(() -> {
            game.interact(spawner.getId());
        });
        Entity item = player.getInventoryItem(anduril.getId());
        assertTrue(item == null || ((Anduril) item).getDurability() != initialDurability);
    }

    /**
     * Test if Anduril can be used in battles.
     */
    @Test
    public void battleTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Anduril anduril = new Anduril(new Position(1, 1));
        game.addEntity(anduril);

        Player player = new Player(new Position(0, 1), mode.initialHealth());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);

        int initialDurability = anduril.getDurability();
        assertTrue(anduril.getDurability() == initialDurability);

        // Player moves to attack the mercenaries with the anduril
        for (int i = 0; i < 5; i++) {
            Mercenary mercenary = new Mercenary(
                new Position(1, 2 + i),
                mode.damageMultiplier(),
                player
            );
            game.addEntity(mercenary);
            player.move(game, Direction.DOWN);

            // Mercenary should die upon battle, but restore the health of the player
            assertTrue(game.getEntity(mercenary.getId()) == null);
            player.setHealth(100);
        }

        // Since andurils only have 5 durability, it is guaranteed to be removed from the player's inventory
        assertTrue(player.getInventoryItem(anduril.getId()) == null);
    }
    
    /**
     * Test if Anduril deals triple damage to bosses.
     */
    @Test
    public void testTripleDamage() {
        Mode mode = new Hard();
        Player player = new Player(new Position(1, 2), mode.initialHealth());
        
        List<Enemy> enemies = Arrays.asList(
            new Hydra(new Position(1, 3), mode.damageMultiplier(), player),
            new Assassin(new Position(1, 3), mode.damageMultiplier(), player)
            );

        Game game = new Game("game", TestHelpers.sevenBySevenWallBoundary(), new ExitCondition(), mode);

        game.addEntity(new Wall(new Position(2, 1)));
        game.addEntity(new Wall(new Position(2, 2)));
        game.addEntity(new Wall(new Position(2, 3)));
        game.addEntity(new Wall(new Position(2, 4)));
        game.addEntity(new Wall(new Position(1, 4)));
        
        game.addEntity(player);
            
        for (Enemy e : enemies) {
            // Spawn enemy and fight
            game.addEntity(e);
            game.tick(null, Direction.NONE);

            // Find health remaining without anduril;
            int enemyHealthWithoutAnduril = e.getHealth();

            game.removeEntity(e);
            game.removeEntity(player);
            
            // Find health remaining with anduril
            player.setHealth(player.getMaxCharacterHealth());
            if (e instanceof Hydra) {
                Hydra hydra = (Hydra) e;
                hydra.setHealth(Hydra.MAX_HYDRA_HEALTH);
            } else if (e instanceof Assassin) {
                Assassin assassin = (Assassin) e;
                assassin.setHealth(Assassin.MAX_ASSASSIN_HEALTH);
            }
            
            player.addInventoryItem(new Anduril(new Position(0, 0)));
            
            game.addEntity(player);

            // Spawn enemy and fight
            game.addEntity(e);
            game.tick(null, Direction.NONE);
            
            int enemyHealthWithAnduril = e.getHealth();
            assertTrue(enemyHealthWithAnduril < enemyHealthWithoutAnduril);
        }
    }

    /**
     * Test Anduril drop rate after winning a battle.
     */
    @Test
    public void dropRateTest() {
        Mode mode = new Peaceful();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);

        // Spawn mercenaries next to the player - upon ticking, the mercenary would move to the player
        // Since this is peaceful mode, the player's health will not change, so mercenaries will always die
        for (int i = 0; i < 50; i++) {
            Mercenary mercenary = new Mercenary(new Position(1, 2), mode.damageMultiplier(), player);
            game.addEntity(mercenary);
            game.tick(null, Direction.NONE);

            if (player.findInventoryItem("anduril") != null) break;
        }

        assertTrue(player.getHealth() == 100);
        // Check that Anduril is in the player's inventory
        assertTrue(player.findInventoryItem("anduril") != null);
    }
}
