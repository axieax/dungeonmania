package dungeonmania.model.entities.movings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class RandomZombieState implements ZombieState {
    ZombieToast zombie;

    public RandomZombieState(ZombieToast zombie) {
        this.zombie = zombie;
    }

    @Override
    public void move(Dungeon dungeon) {
        Position currPos = zombie.getPosition();
        Set<Direction> chosen = new HashSet<>();
        
        // choose a direction (other than none)
        List<Direction> possibleDirections = Arrays.asList(
            Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT
        );
        
        // choose a random direction
        while(chosen.size() != possibleDirections.size()) {
            Direction direction = possibleDirections.get((int) Math.random() % 4);
            chosen.add(direction);
            
            Position newPos = currPos.translateBy(direction);
            List<Entity> entitiesNewPos = dungeon.getEntitiesAtPosition(newPos);

            if(entitiesNewPos == null || zombie.canZombieMoveOntoPosition(entitiesNewPos)) {
                zombie.setPosition(newPos);
                return;
            }
        }

        // all 4 directions are blocked, do not move anywhere
    }
    
}