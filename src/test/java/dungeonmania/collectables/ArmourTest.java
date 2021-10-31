package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.equipment.Armour;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class ArmourTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Game game = new Game(3, 3);
        game.addEntity(new Armour(new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(game.getEntity("armour1").getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Game game = new Game(3, 3);

        Armour item = new Armour(new Position(1, 1));

        game.addEntity(item);

        Player player = new Player(new Position(0, 1));
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(game.getEntity(item.getId()) == null);
        assertTrue(player.getInventoryItem(item.getId()).equals(item));
    }

    /**
     * Test durability of Armour
     */
    @Test
    public void durabilityTest() {
        Game game = new Game(3, 3);

        String collectableId = "armour1";

        Armour item = new Armour(new Position(1, 1));

        game.addEntity(item);

        Player player = new Player(new Position(0, 1));
        player.move(game, Direction.RIGHT);

        // Durability of armour when picked up should be 5
        assertTrue(item.getDurability() == 5);

        Mercenary mercenary = new Mercenary(new Position(2, 1));
        game.addEntity(mercenary);

        // Player moves to attack (interact with) the mercenary
        // This will cause the durability of the armour to decrease by 1
        player.move(game, Direction.RIGHT);
        assertTrue(item.getDurability() == 4);
    }

    /**
     * Test armour protection for ZombieToast since zombies randomly spawn with armour.
     */
    @Test
    public void monsterArmourTest() {
        fail();
    }

    /**
     * Test if the Player can take the Armour from a defeated ZombieToast/Mercenary
     */
    @Test
    public void takeArmourTest() {
        fail();
    }
}