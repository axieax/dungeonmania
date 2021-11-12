package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.equipment.Anduril;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class AndurilTest {

    @Test
    public void instanceTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Anduril anduril = new Anduril(new Position(1, 1));
        game.addEntity(anduril);
        String andurilId = anduril.getId();

        assertTrue(new Position(1, 1).equals(game.getEntity(andurilId).getPosition()));
    }

    @Test
    public void collectTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Anduril anduril = new Anduril(new Position(1, 1));
        game.addEntity(anduril);

        Player player = new Player(new Position(0, 1));
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));

        assertTrue(game.getEntity(anduril.getId()) == null);
        assertTrue(player.getInventoryItem(anduril.getId()).equals(anduril));
    }

    @Test
    public void durabilityTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Anduril anduril = new Anduril(new Position(1, 1));
        game.addEntity(anduril);

        Player player = new Player(new Position(0, 1));
        game.addEntity(player);

        // check initial durability
        int initialDurability = anduril.getDurability();
        assertTrue(anduril.getDurability() == initialDurability);

        ZombieToastSpawner spawner = new ZombieToastSpawner(
            new Position(2, 1),
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
}
