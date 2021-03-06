package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.equipment.Sword;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class SwordTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Sword sword = new Sword(new Position(1, 1));
        game.addEntity(sword);
        String swordId = sword.getId();

        assertTrue(new Position(1, 1).equals(game.getEntity(swordId).getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Sword sword = new Sword(new Position(1, 1));
        game.addEntity(sword);

        Player player = new Player(new Position(0, 1), mode.initialHealth());
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));

        assertTrue(game.getEntity(sword.getId()) == null);
        assertTrue(player.getInventoryItem(sword.getId()).equals(sword));
    }

    /**
     * Test durability of Sword.
     */
    @Test
    public void durabilityTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Sword sword = new Sword(new Position(1, 1));
        game.addEntity(sword);

        Player player = new Player(new Position(0, 1), mode.initialHealth());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);

        // Durability of sword when picked up should be 5
        int initialDurability = 5;
        assertTrue(sword.getDurability() == initialDurability);

        ZombieToastSpawner spawner = new ZombieToastSpawner(
            new Position(3, 1),
            mode.damageMultiplier()
        );
        game.addEntity(spawner);

        player.move(game, Direction.RIGHT);

        // Player is now next to the zombie toast spawner and will proceed to destroy it with the sword
        // Durability of sword decreases by 1 each time it battles (within one tick)
        assertDoesNotThrow(() -> {
            game.interact(spawner.getId());
        });
        Entity item = player.findInventoryItem("sword");
        assertTrue(item == null || ((Sword) item).getDurability() != initialDurability);
    }

    /**
     * Test if Sword can be used in battles.
     */
    @Test
    public void battleTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Sword sword = new Sword(new Position(1, 1));
        game.addEntity(sword);

        Player player = new Player(new Position(0, 1), mode.initialHealth());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);

        // Durability of sword when picked up should be 5
        int initialDurability = 5;
        assertTrue(sword.getDurability() == initialDurability);

        // Player moves to attack the mercenaries with the sword
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

        // Since swords only have 5 durability, it is guaranteed to be removed from the player's inventory
        assertTrue(player.getInventoryItem(sword.getId()) == null);
    }
}
