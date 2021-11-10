package dungeonmania.model.goal;

import dungeonmania.model.Game;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class GoalComposite implements Goal {

    private final List<Goal> goals = new ArrayList<>();

    /**
     * Gets the list of Goals
     *
     * @return list of Goals
     */
    public final List<Goal> getGoals() {
        return goals;
    }

    /**
     * Adds a Goal to a Composite Goal
     *
     * @param goal Goal to be added
     */
    public final void addGoal(Goal goal) {
        goals.add(goal);
    }

    public String getOperator() {
        return getClass().getSimpleName().replace("Composite", "").toUpperCase();
    }

    @Override
    public String toString(Game game) {
        if (isComplete(game)) return "";
        // only include incomplete goals
        String delimiter = String.format(" %s ", getOperator());
        return goals
            .stream()
            .filter(goal -> !goal.isComplete(game))
            .map(goal -> goal.toString(game))
            .collect(Collectors.joining(delimiter, "(", ")"));
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("goal", getOperator());
        JSONArray arr = new JSONArray();
        goals.stream().forEach(s -> arr.put(s.toJSON()));
        json.put("subgoals", arr);
        return json;
    }
}
