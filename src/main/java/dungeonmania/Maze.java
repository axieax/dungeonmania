package dungeonmania;

import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;

public class Maze {

    private int width;
    private int height;
    private Position start;
    private Position end;
    private List<List<Boolean>> maze;

    public Maze(int width, int height, Position start, Position end) {
        this.width = width;
        this.height = height;
        this.start = start;
        this.end = end;
        this.maze = generateRandomisedPrimsMaze();
    }

    /**
     * Get start position
     *
     * @return start Position
     */
    public Position getStart() {
        return start;
    }

    /**
     * Get end position
     *
     * @return end Position
     */
    public Position getEnd() {
        return end;
    }

    /**
     * Get width of the map
     *
     * @return map width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get height of the map
     *
     * @return map height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get maze position by coordinate
     *
     * @param x x-cordinate
     * @param y y-cordinate
     * @return true if the position is empty space, false if it is a wall
     */
    public boolean getMazePosition(int x, int y) {
        return maze.get(y).get(x);
    }

    /**
     * This function uses the pseudo code based on prims algorithm to randomly generate a maze
     *
     * @return a two dimensional array representing a maze with true as empty
     * space and false representing a wall
     */
    private List<List<Boolean>> generateRandomisedPrimsMaze() {
        // generating the map
        List<List<Boolean>> mazeMap = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            List<Boolean> rowInMap = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                rowInMap.add(false); // default false
            }
            mazeMap.add(rowInMap);
        }
        // set start as non empty
        mazeMap.get(start.getY()).set(start.getX(), true);

        // add to options all neighbours of 'start' not on boundary that are of
        // distance 2 away and are walls
        List<Position> options = secondNeighbours(start)
            .stream()
            .filter(e -> !mazeMap.get(e.getY()).get(e.getX()))
            .collect(Collectors.toList());

        while (options.size() > 0) { // while options is not empty
            // let next = remove random from options
            Random random = new Random();
            Position next = options.remove(random.nextInt(options.size()));

            // let neighbours = each neighbour of distance 2 from next not on boundary
            // that are empty
            List<Position> neighbours = secondNeighbours(next)
                .stream()
                .filter(e -> mazeMap.get(e.getY()).get(e.getX()))
                .collect(Collectors.toList());
            if (neighbours.size() != 0) { // if neighbours is not empty
                Position neighbour = neighbours.get(random.nextInt(neighbours.size()));
                mazeMap.get(next.getY()).set(next.getX(), true);
                // position in between next and neighbour = empty
                setBetweenPositionAsEmpty(mazeMap, next, neighbour);
                mazeMap.get(neighbour.getY()).set(neighbour.getX(), true);
            }

            // add to options all neighbours of 'next' not on boundary that are of
            // distance 2 away and are walls
            List<Position> nextNeighbours = secondNeighbours(next)
                .stream()
                .filter(e -> !mazeMap.get(e.getY()).get(e.getX()))
                .filter(e -> !options.contains(e))
                .collect(Collectors.toList());
            options.addAll(nextNeighbours);
        }

        // incase the end position is not connected to the map
        if (!mazeMap.get(end.getY()).get(end.getX())) {
            mazeMap.get(end.getY()).set(end.getX(), true); // maze[end] = empty
            // let neighbours = neighbours not on boundary of distance 1 from maze[end]
            List<Position> firstNeighbours = firstNeighbours(end);
            if (allWalls(firstNeighbours, mazeMap)) { // no cells in neighbours that are empty
                // let's connect it to the grid
                Random random = new Random();
                //connect random neighbour
                Position neighbour = firstNeighbours.get(random.nextInt(firstNeighbours.size()));
                mazeMap.get(neighbour.getY()).set(neighbour.getX(), true);
            }
        }
        return mazeMap;
    }

    /**
     * Checks if a position is on the boundary
     *
     * @param currPos current position
     *
     * @return true if on boundary, false if not
     */
    private Boolean isOnBoundary(Position currPos) {
        int x = currPos.getX();
        int y = currPos.getY();
        if (x <= 0 || x >= width - 1) return true;
        if (y <= 0 || y >= height - 1) return true;
        return false;
    }

    /**
     * Checks if a list of possible positions is all walls
     *
     * @param possiblePositions positions to check
     * @param mazeMap maze
     *
     * @return true if all positions are walls, false otherwise
     */
    private Boolean allWalls(List<Position> possiblePositions, List<List<Boolean>> mazeMap) {
        for (Position position : possiblePositions) {
            int x = position.getX();
            int y = position.getY();
            if (mazeMap.get(y).get(x)) return false;
        }
        return true;
    }

    /**
     * set a position between position one and position two on the maze map as empty
     *
     * @param mazeMap maze
     * @param one first position
     * @param two second position
     */
    private void setBetweenPositionAsEmpty(
        List<List<Boolean>> mazeMap,
        Position one,
        Position two
    ) {
        int x1 = one.getX();
        int y1 = one.getY();
        int x2 = two.getX();
        int y2 = two.getY();
        if (y1 == y2 - 2) {
            mazeMap.get(y1 + 1).set(x1, true);
        } else if (y1 == y2 + 2) {
            mazeMap.get(y1 - 1).set(x1, true);
        } else if (x1 == x2 - 2) {
            mazeMap.get(y1).set(x1 + 1, true);
        } else if (x1 == x2 + 2) {
            mazeMap.get(y1).set(x1 - 1, true);
        }
    }

    /**
     * Find neighbours one move from current position
     * @param currPos current position
     *
     * @return list of Positions which are one move away from the current position
     */
    public List<Position> firstNeighbours(Position currPos) {
        int x = currPos.getX();
        int y = currPos.getY();

        // find the positions that could be the possible first neighbours of  currPos
        List<Position> positions = new ArrayList<Position>(
            Arrays.asList(
                new Position(x, y + 1),
                new Position(x - 1, y),
                new Position(x + 1, y),
                new Position(x, y - 1)
            )
        );

        // remove positions on the boundary
        positions.removeIf(pos -> isOnBoundary(pos));
        return positions;
    }

    /**
     * Find neighbours two moves from current position
     * @param currPos current position
     *
     * @return list of Positions which are two moves away from the current position
     */
    public List<Position> secondNeighbours(Position currPos) {
        int x = currPos.getX();
        int y = currPos.getY();
        List<Position> positions = new ArrayList<Position>(
            Arrays.asList(
                new Position(x, y + 2),
                new Position(x - 2, y),
                new Position(x + 2, y),
                new Position(x, y - 2)
            )
        );

        // remove positions on the boundary
        positions.removeIf(pos -> isOnBoundary(pos));
        return positions;
    }

    /**
     * Makes a JSON object given information about the entity
     *
     * @param xPosition entity's x co-ordinate
     * @param yPosition entity's y co-ordinate
     * @param type type of entity
     * @return JSON object representing the entity
     */
    private JSONObject makeJSONEntity(int xPosition, int yPosition, String type) {
        JSONObject newEntity = new JSONObject();
        newEntity.put("x", xPosition);
        newEntity.put("y", yPosition);
        newEntity.put("type", type);
        return newEntity;
    }

    /**
     * Returns the JSONObject representation for the dungeon
     *
     * @return JSONObject for dungeon
     */
    public JSONObject toJSON() {
        JSONObject mazeJSON = new JSONObject();
        JSONArray entities = new JSONArray();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (start.getX() == j && start.getY() == i) {
                    // player starts here
                    entities.put(makeJSONEntity(j, i, "player"));
                } else if (end.getX() == j && end.getY() == i) {
                    // exit is here
                    entities.put(makeJSONEntity(j, i, "exit"));
                } else if (!maze.get(i).get(j)) {
                    // place wall
                    entities.put(makeJSONEntity(j, i, "wall"));
                }
            }
        }
        mazeJSON.put("entities", entities);

        // adds the goal of the dungeon
        JSONObject goal = new JSONObject();
        goal.put("goal", "exit");
        mazeJSON.put("goal-condition", goal);
        return mazeJSON;
    }
}
