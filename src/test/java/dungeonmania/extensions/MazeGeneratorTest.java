package dungeonmania.extensions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.DungeonManiaController;
import dungeonmania.Maze;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class MazeGeneratorTest {

    public static Boolean canFindPath(Maze maze, int height, int width) {
        // create a visited array and set all the positions to false
        ArrayList<ArrayList<Boolean>> visited = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            ArrayList<Boolean> rowInMap = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                rowInMap.add(false); // default false
            }
            visited.add(rowInMap);
        }

        // Simulates a queue
        List<Position> q = new ArrayList<Position>();
        Position end = maze.getEnd();
        q.add(maze.getStart());

        while (q.size() > 0) { // while there are still more places to visit
            Position v = q.remove(0);
            if (visited.get(v.getY()).get(v.getX())) continue;
            // set the current position as visited
            visited.get(v.getY()).set(v.getX(), true);

            // is v is the end position a path is found
            if ((v.getX() == end.getX()) && (v.getY() == end.getY())) return true;

            //get all the neighbours of the current position
            List<Position> positions = new ArrayList<>();
            positions.add(new Position(v.getX(), v.getY() + 1));
            positions.add(new Position(v.getX() - 1, v.getY()));
            positions.add(new Position(v.getX() + 1, v.getY()));
            positions.add(new Position(v.getX(), v.getY() - 1));

            for (Position position : positions) {
                // check that the neighbours are not on the boundary, not visited and
                // not a wall
                if (
                    !isOnBoundary(width, height, position) &&
                    maze.getMazePosition(position.getX(), position.getY()) &&
                    !visited.get(position.getY()).get(position.getX())
                ) {
                    q.add(position);
                }
            }
        }
        return false;
    }

    // check if a position is on the boundary
    public static Boolean isOnBoundary(int width, int height, Position currPos) {
        int x = currPos.getX();
        int y = currPos.getY();
        if (x <= 0 || x >= width - 1) return true;
        if (y <= 0 || y >= height - 1) return true;
        return false;
    }

    public static int[] data() {
        return new int[] { 5, 15, 21, 29 };
    }

    /**
     * Uses BFS to ensure that there is a path through the randomly generated maze
     * @param data
     */
    @ParameterizedTest
    @MethodSource(value = "data")
    public void testCanFindPath(int data) {
        Maze maze = new Maze(data, data, new Position(1, 1), new Position(data - 2, data - 2));
        assertTrue(canFindPath(maze, maze.getHeight(), maze.getWidth()));
    }

    /**
     * Ensure that the goal is to exit the maze
     * @param data
     */
    @ParameterizedTest
    @MethodSource(value = "data")
    public void testGoalIsCorrect(int data) {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse rsp = controller.generateDungeon(1, 1, data, data, "standard");
        assertEquals(":exit(1)", rsp.getGoals());
    }

    /**
     * Ensure that there are never more than 4 cardinal neighbours to a position
     */
    @Test
    public void testFirstNeighbours() {
        Maze maze = new Maze(27, 27, new Position(1, 1), new Position(10, 10));
        assertTrue(maze.firstNeighbours(new Position(1, 1)).size() <= 4);
    }
}
