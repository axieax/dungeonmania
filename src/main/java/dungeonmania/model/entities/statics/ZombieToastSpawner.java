package dungeonmania.model.entities.statics;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.Tickable;
import dungeonmania.model.entities.movings.SubjectPlayer;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Position;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ZombieToastSpawner extends Entity implements Tickable {

    private final int TICK_RATE;
    private int currTick;

    public ZombieToastSpawner(Position position, int tickRate) {
        super("zombie_toast_spawner", position, true, false);
        this.TICK_RATE = tickRate;
        this.currTick = 0;
    }

    @Override
    public void interact(Game game, Entity character) throws InvalidActionException {
        // If the Player interacts with the ZombieToastSpawner with a weapon equipped,
        // the player destroys the spawner and the weapon loses durability.
        if (!(character instanceof Player)) return;
        Player player = (Player) character;

        // Check if the player is cardinally adjacent to the spawner
        game
            .getCardinallyAdjacentEntities(player.getPosition())
            .stream()
            .filter(e -> e.equals(this))
            .findFirst()
            .orElseThrow(() ->
                new InvalidActionException("Player is not cardinally adjacent to the spawner")
            );

        if (player.hasWeapon()) {
            Equipment weapon = player.getWeapon();
            weapon.useEquipment(player, this);
            game.removeEntity(this);
        } else {
            throw new InvalidActionException(
                "You need to have a weapon to destroy a zombie toast spawner"
            );
        }
    }

    @Override
    public void tick(Game game) {
        if (++currTick % TICK_RATE != 0) return;

        int x = this.getX();
        int y = this.getY();
        List<Position> positions = Arrays.asList(
            new Position(x, y + 1),
            new Position(x - 1, y),
            new Position(x + 1, y),
            new Position(x, y - 1)
        );

        // find empty positions
        List<Position> openSquares = positions
            .stream()
            .filter(position -> game.getEntities(position).isEmpty())
            .collect(Collectors.toList());
        if (openSquares.isEmpty()) return;

        // spawn in a random position
        Random rand = new Random();
        Position randPosition = openSquares.get(rand.nextInt(openSquares.size()));
        game.addEntity(
            new ZombieToast(
                randPosition,
                game.getMode().damageMultiplier(),
                (SubjectPlayer) game.getPlayer()
            )
        );
    }
}
