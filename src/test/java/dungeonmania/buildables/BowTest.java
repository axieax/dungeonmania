package dungeonmania.buildables;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import dungeonmania.model.Game;
import dungeonmania.model.entities.buildables.Bow;
import dungeonmania.model.entities.collectables.Arrow;
import dungeonmania.model.entities.collectables.Wood;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class BowTest {
    /**
     * Test whether the buildable entity can be built by the Player.
     */
    @Test
    public void buildTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        // To build a bow, we need 1 wood and 3 arrows
        Wood wood = new Wood(new Position(1, 0));
        Arrow arrow1 = new Arrow(new Position(1, 1));
        Arrow arrow2 = new Arrow(new Position(1, 2));
        Arrow arrow3 = new Arrow(new Position(1, 3));

        game.addEntity(wood);
        game.addEntity(arrow1);
        game.addEntity(arrow2);
        game.addEntity(arrow3);

        // Player picks up the wood and arrows
        Player player = new Player(new Position(0, 0), mode.damageMultiplier());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);

        assertTrue(player.findInventoryItem("bow") == null);

        // Player builds a bow
        game.build("bow");

        // Check that the player has a bow
        assertTrue(player.hasItemQuantity("bow", 1));

        // Check that the player has no wood or arrows
        assertTrue(player.getInventoryItem(wood.getId()) == null);
        assertTrue(player.findInventoryItem("arrow") == null);
    }

    /**
     * Test durability of Bow.
     */
    @Test
    public void durabilityTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Wood wood = new Wood(new Position(1, 0));
        Arrow arrow1 = new Arrow(new Position(1, 1));
        Arrow arrow2 = new Arrow(new Position(1, 2));
        Arrow arrow3 = new Arrow(new Position(1, 3));

        game.addEntity(wood);
        game.addEntity(arrow1);
        game.addEntity(arrow2);
        game.addEntity(arrow3);

        Player player = new Player(new Position(0, 0), mode.damageMultiplier());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);

        game.build("bow");

        // Durability of bow when built should be 5
        int initialDurability = 5;
        Bow bow = (Bow) player.findInventoryItem("bow");
        assertTrue(bow.getDurability() == initialDurability);

        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(3, 3), mode.damageMultiplier());
        game.addEntity(spawner);

        player.move(game, Direction.RIGHT);

        // Player is now next to the zombie toast spawner and will proceed to destroy it with the bow
        // Durability of bow decreases by 1
        game.interact(spawner.getId());
        assertTrue(bow.getDurability() == initialDurability - 1);
    }

    /**
     * Test if Bow can be used in battles.
     */
    @Test
    public void battleTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Wood wood = new Wood(new Position(1, 0));
        Arrow arrow1 = new Arrow(new Position(1, 1));
        Arrow arrow2 = new Arrow(new Position(1, 2));
        Arrow arrow3 = new Arrow(new Position(1, 3));

        game.addEntity(wood);
        game.addEntity(arrow1);
        game.addEntity(arrow2);
        game.addEntity(arrow3);

        Player player = new Player(new Position(0, 0), mode.damageMultiplier());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);

        game.build("bow");

        int initialDurability = 5;
        Bow bow = (Bow) player.findInventoryItem("bow");
        assertTrue(bow.getDurability() == initialDurability);

        Mercenary mercenary = new Mercenary(new Position(2, 3), mode.damageMultiplier(), player);
        game.addEntity(mercenary);

        // Player moves to attack the mercenary with the bow
        player.move(game, Direction.RIGHT);

        // Either the player or the mercenary should be dead
        // Durability of bow decreases by 1 each time it battles (within one tick)
        assertTrue((game.getEntity(mercenary.getId()) == null) || (game.getEntity(player.getId()) == null));
        assertTrue(bow == null || bow.getDurability() != initialDurability);
    }
}
