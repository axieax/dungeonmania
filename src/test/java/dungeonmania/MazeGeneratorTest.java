package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class MazeGeneratorTest {
    public static Boolean canFindPath (Maze maze, int height, int width) {
        // visited
        ArrayList<ArrayList<Boolean>> visited = new ArrayList<> ();
        for (int i = 0; i < height; i++) {
            ArrayList<Boolean> rowInMap = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                rowInMap.add(false); // default false
            }
            visited.add(rowInMap);
        }
        // pred
        ArrayList<ArrayList<Position>> pred = new ArrayList<> ();
        for (int i = 0; i < height; i++) {
            ArrayList<Position> rowInMap = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                rowInMap.add(new Position(-1,-1)); // default negative 
            }
            pred.add(rowInMap);
        }

        List<Position> q = new ArrayList<Position> ();
        q.add(maze.getStart());

        Boolean found = false;
        while (!found && q.size() > 0) {
            Position v = q.remove(0);
            if (visited.get(v.getY()).get(v.getX())) continue;
            visited.get(v.getY()).set(v.getX(), true);

            if (MazeVisit(m,v)) {
                found = true;
                break;
            }
            

            List<Position> positions = new ArrayList<>();
            positions.add(new Position(v.getX(), v.getY() + 1));
            positions.add(new Position(v.getX() - 1, v.getY()));
            positions.add(new Position(v.getX() + 1, v.getY()));
            positions.add(new Position(v.getX(), v.getY() - 1));

            for (Position position: positions) {
                if (!isOnBoundary(width, height, position) && 
                    maze.get(position.getY()).get(position.getX()) && 
                    !visited.get(position.getY()).get(position.getX())) {
                        q.add (position);
                        pred.get(position.getY()).set(position.getX(), v);
                }
            }
        }
        return found;
    }

    // check if a position is on the boundary
    public static Boolean isOnBoundary (int width, int height, Position currPos) {
        int x = currPos.getX();
        int y = currPos.getY();
        if (x <= 0 || x >= width - 1) return true;
        if (y <= 0 || y >= height - 1) return true;
        return false;
    }

    public boolean mazeVisit (Maze maze, Position p) {
        int x = p.getX();
        int y = p.getY();
        if (!maze.get(y).get(x)) return false;
        s
    }

    @Test
    public void testCanFindPath() {
        Maze maze = new Maze ()
    }
    
}
