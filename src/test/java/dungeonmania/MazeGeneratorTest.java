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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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

        // Simulates a queue
        List<Position> q = new ArrayList<Position> ();
        Position end = maze.getEnd();
        q.add(maze.getStart());

        while (q.size() > 0) {
            Position v = q.remove(0);
            if (visited.get(v.getY()).get(v.getX())) continue;
            visited.get(v.getY()).set(v.getX(), true);

            if ((v.getX() == end.getX()) && (v.getY() == end.getY())) return true;
            
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
                }
            }
        }
        return false;
    }

    // check if a position is on the boundary
    public static Boolean isOnBoundary (int width, int height, Position currPos) {
        int x = currPos.getX();
        int y = currPos.getY();
        if (x <= 0 || x >= width - 1) return true;
        if (y <= 0 || y >= height - 1) return true;
        return false;
    }

    public static int [] data () {
        return new int [] {5,15,21,29};
    }
    
    @ParameterizedTest
    @MethodSource(value = "data")
    public void testCanFindPath(int data) {
        int height;
        int width;
        Maze maze = new Maze (data, data, new Position (1,1), new Position (data - 2, data -2));
        assertTrue (canFindPath(maze, height, width));
    }

    @ParameterizedTest
    @MethodSource(value = "data")
    public void testGoalIsCorrect(int data) {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse rsp = controller.generateDungeon (1,1,data,data, "standard");
        assertEquals(":exit(1)", rsp.getGoals());
    }
}
