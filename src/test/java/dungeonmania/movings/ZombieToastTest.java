package dungeonmania.movings;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.TestHelpers;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.collectables.potion.InvincibilityPotion;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.movings.movement.RunMovementState;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.movings.player.PlayerInvincibleState;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.Door;
import dungeonmania.model.entities.statics.Portal;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(value = Lifecycle.PER_CLASS)
public class ZombieToastTest {

    @Test
    public void testZombieSpawnRateNormalModes() {
        Mode mode = new Standard();
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(5, 5), mode.tickRate());
        game.addEntity(spawner);

        int numEntities = game.getEntities().size();

        // Zombie should spawn
        TestHelpers.gameTickMovement(game, Direction.NONE, 20);
        assertTrue(game.getEntities().size() > numEntities);
    }

    @Test
    public void testBasicMovement() {
        Mode mode = new Standard();

        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        Position zombiePos = new Position(5, 5);
        ZombieToast zombie = new ZombieToast(zombiePos, mode.damageMultiplier(), player);
        game.addEntity(zombie);

        game.tick(null, Direction.RIGHT);
        List<Entity> entitiesAtOldZombiePos = game.getEntities(zombiePos);

        // Zombie should change position as there exists an open tile
        assertTrue(entitiesAtOldZombiePos.size() == 0);
    }

    @Test
    public void testWallBlockingMovement() {
        Mode mode = new Standard();

        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(5, 5), mode.tickRate());
        game.addEntity(spawner);

        // Surround zombie with a wall, leaving one tile adjacent to the spawner open
        game.addEntity(new Wall(new Position(3, 5)));
        game.addEntity(new Wall(new Position(3, 4)));
        game.addEntity(new Wall(new Position(4, 4)));
        game.addEntity(new Wall(new Position(5, 4)));

        Position expectedZombieSpawnTile = new Position(4, 5);
        // Zombie should spawn in 20 ticks
        for (int i = 0; i < 20; i++) {
            assertTrue(game.getEntities(expectedZombieSpawnTile).size() == 0);
            game.tick(null, Direction.NONE);
        }

        List<Entity> entitesAtTileAdjacentToSpawner = game.getEntities(expectedZombieSpawnTile);
        assertTrue(entitesAtTileAdjacentToSpawner.size() > 0);
    }

    @Test
    public void testEdgeCornerMovement() {
        Mode mode = new Standard();
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        Position zombiePos = new Position(5, 5);
        ZombieToast zombie = new ZombieToast(zombiePos, mode.damageMultiplier(), player);

        assertTrue(game.getEntities(zombiePos).size() == 0);

        game.addEntity(zombie);
        assertTrue(game.getEntities(zombiePos).size() > 0);

        game.addEntity(new Wall(new Position(4, 4)));
        game.addEntity(new Wall(new Position(5, 4)));
        game.addEntity(new Wall(new Position(4, 5)));

        // Zombie is trapped in the corner and should not move in further ticks
        zombie.tick(game);
        assertTrue(game.getEntities(zombiePos).size() > 0);
    }

    @Test
    public void testZombieCannotWalkThroughClosedDoor() {
        Mode mode = new Standard();
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        Position zombiePos = new Position(5, 5);
        ZombieToast zombie = new ZombieToast(zombiePos, mode.damageMultiplier(), player);

        assertTrue(game.getEntities(zombiePos).size() == 0);

        game.addEntity(zombie);
        assertTrue(game.getEntities(zombiePos).size() > 0);

        game.addEntity(new Wall(new Position(4, 3)));
        game.addEntity(new Wall(new Position(4, 4)));
        game.addEntity(new Wall(new Position(4, 5)));

        Position doorPos = new Position(5, 4);
        Position keyPos = new Position(1, 5);
        Key key = new Key(keyPos, 1);
        game.addEntity(new Door(doorPos, 1));
        game.addEntity(key);

        // Zombie is trapped in the corner and should not move in further ticks
        for (int i = 0; i < 5; i++) {
            zombie.tick(game);
            assertTrue(game.getEntities(zombiePos).size() > 0);
        }
    }

    @Test
    public void testZombieCanWalkThroughOpenDoor() {
        Mode mode = new Standard();

        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(4, 2), mode.initialHealth());
        game.addEntity(player);
        game.addEntity(new Key(new Position(4, 3), 1));
        Door door = new Door(new Position(4, 4), 1);
        game.addEntity(door);
        game.tick(null, Direction.DOWN);

        assertTrue(player.hasItemQuantity("key", 1));
        game.tick(null, Direction.DOWN);
        assertTrue(door.getPosition().equals(player.getPosition()));
        assertTrue(door.isOpen());

        game.tick(null, Direction.UP);

        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(5, 5), mode.tickRate());
        game.addEntity(spawner);

        // Surround zombie with a wall, leaving one tile adjacent to the spawner open
        game.addEntity(new Wall(new Position(3, 5)));
        game.addEntity(new Wall(new Position(3, 4)));
        game.addEntity(new Wall(new Position(5, 4)));

        Position expectedZombieSpawnTile = new Position(4, 5);
        // Zombie should spawn in 20 ticks
        for (int i = 0; i < mode.tickRate(); i++) {
            // Remove any other moving entities that have spawned e.g. spiders
            List<Entity> toRemove = new ArrayList<>();
            for (Entity e : game.getEntities()) {
                if (e instanceof Player || e instanceof ZombieToast) continue; else if (
                    e instanceof MovingEntity
                ) toRemove.add(e);
            }

            for (Entity e : toRemove) game.removeEntity(e);

            game.tick(null, Direction.NONE);
        }

        List<Entity> entitesAtTileAdjacentToSpawner = game.getEntities(expectedZombieSpawnTile);
        assertTrue(entitesAtTileAdjacentToSpawner.size() == 1);

        game.tick(null, Direction.NONE);
        assertTrue(door.getPosition().equals(entitesAtTileAdjacentToSpawner.get(0).getPosition()));
    }

    @Test
    public void testPortalNoEffect() {
        // Portals have no effect on zombies
        Mode mode = new Standard();
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        Position zombiePos = new Position(5, 5);
        ZombieToast zombie = new ZombieToast(zombiePos, mode.damageMultiplier(), player);

        assertTrue(game.getEntities(zombiePos).size() == 0);

        game.addEntity(zombie);
        assertTrue(game.getEntities(zombiePos).size() > 0);

        game.addEntity(new Wall(new Position(4, 3)));
        game.addEntity(new Wall(new Position(4, 4)));
        game.addEntity(new Wall(new Position(4, 5)));
        game.addEntity(new Wall(new Position(5, 3)));

        Position portalPos = new Position(5, 4);
        Portal portal = new Portal(portalPos, "blue");
        game.addEntity(portal);
        game.tick(null, Direction.NONE);

        // The only option for the zombie is to move to the portal which cant pass through
        assertTrue(zombie.getPosition().equals(zombiePos));
    }

    @Test
    public void testZombmieCannotMoveBoulder() {
        Mode mode = new Standard();
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
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

        game.tick(null, Direction.NONE);

        // Zombie should stay in its position, as it cannot move a boulder
        assertTrue(zombie.getPosition().equals(zombiePos));
    }

    @Test
    public void testZombieRunAway() {
        Mode mode = new Standard();
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        InvincibilityPotion invinc = new InvincibilityPotion(new Position(1, 2));
        game.addEntity(invinc);

        // Collect and drink invincibility potion
        game.tick(null, Direction.DOWN);
        game.tick(invinc.getId(), Direction.NONE);

        assertTrue(player.getState() instanceof PlayerInvincibleState);

        // Spawn zombie next to player
        Position zombiePos = new Position(1, 1);
        ZombieToast zombie = new ZombieToast(zombiePos, mode.damageMultiplier(), player);
        assertTrue(game.getEntities(zombiePos).size() == 0);

        int entitiesBeforeZombie = game.getEntities().size();
        game.addEntity(zombie);
        assertTrue(game.getEntities(zombiePos).size() > 0);

        // Zombie should now run away
        game.tick(null, Direction.NONE);
        assertTrue(zombie.getHealth() > 0);
        assertTrue(player.getState() instanceof PlayerInvincibleState);
        assertTrue(zombie.getMovementState() instanceof RunMovementState);
        assertTrue(
            game.getCardinallyAdjacentEntities(player.getPosition()).size() < entitiesBeforeZombie
        );
    }

    @Test
    public void testZombieInteractIdempotence() {
        Mode mode = new Standard();
        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(1, 1), mode.initialHealth());
        game.addEntity(player);

        // Spawn zombie next to player
        Position zombiePos = new Position(1, 1);
        ZombieToast zombie = new ZombieToast(zombiePos, mode.damageMultiplier(), player);

        game.addEntity(zombie);
        int numEntitesAtZombiePos = game.getEntities(zombiePos).size();
        assertTrue(game.getEntities(zombiePos).size() > 0);

        assertDoesNotThrow(() -> {
            zombie.interact(game, player);
            assertEquals(numEntitesAtZombiePos, game.getEntities(zombiePos).size());
        });
    }

    @Test
    public void testZombieSpawnWithArmourIntermittently() {
        // Zombies have a 20% chance to spawn with armour
        Mode mode = new Standard();

        Game game = new Game(
            "game",
            TestHelpers.sevenBySevenWallBoundary(),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(3, 1), mode.initialHealth());
        game.addEntity(player);

        game.addEntity(new Wall(new Position(2, 1)));
        game.addEntity(new Wall(new Position(2, 2)));
        game.addEntity(new Wall(new Position(2, 3)));

        game.addEntity(new Wall(new Position(3, 3)));

        game.addEntity(new Wall(new Position(4, 1)));
        game.addEntity(new Wall(new Position(4, 2)));
        game.addEntity(new Wall(new Position(4, 3)));

        // The chance of no zombies dropping armour is 0.8^100 = 0.00000002%
        boolean hasArmour = false;
        for (int i = 0; i < 100; i++) {
            game.addEntity(new ZombieToast(new Position(3, 2), mode.damageMultiplier(), player));
            game.tick(null, Direction.NONE);

            for (ItemResponse item : player.getInventoryResponses()) {
                if (item.getType().equals("armour")) {
                    hasArmour = true;
                    break;
                }
            }

            // Remove any other moving entities that have spawned
            List<Entity> toRemove = new ArrayList<>();
            for (Entity e : game.getEntities()) {
                if (
                    e instanceof MovingEntity &&
                    !(e instanceof Player) &&
                    !(e instanceof ZombieToast)
                ) {
                    toRemove.add(e);
                }
            }

            for (Entity e : toRemove) {
                game.removeEntity(e);
            }

            // Regenerate player health
            player.setHealth(player.getMaxCharacterHealth());
        }

        assertTrue(player.isAlive());
        assertTrue(hasArmour);
    }
}
