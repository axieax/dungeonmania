package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.equipment.Armour;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class ArmourTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Armour armour = new Armour(new Position(1, 1));
        game.addEntity(armour);

        assertTrue(new Position(1, 1).equals(game.getEntity(armour.getId()).getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Armour armour = new Armour(new Position(1, 1));
        game.addEntity(armour);

        Player player = new Player(new Position(0, 1));
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));

        assertTrue(game.getEntity(armour.getId()) == null);
        assertTrue(player.getInventoryItem(armour.getId()).equals(armour));
    }

    /**
     * Test durability of Armour.
     */
    @Test
    public void durabilityTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Armour armour = new Armour(new Position(1, 1));
        game.addEntity(armour);

        Player player = new Player(new Position(0, 1));
        game.addEntity(player);
        player.move(game, Direction.RIGHT);

        // Durability of armour when picked up should be 5
        int initialDurability = 5;
        assertTrue(armour.getDurability() == initialDurability);

        Mercenary mercenary = new Mercenary(new Position(2, 1), mode.damageMultiplier(), player);
        game.addEntity(mercenary);

        // Player moves to attack the mercenary
        // Durability of armour decreases by 1 each time it battles (within one tick)
        player.move(game, Direction.RIGHT);
        Entity item = player.findInventoryItem("armour");
        assertTrue(item == null || ((Armour) item).getDurability() != initialDurability);
    }
}
