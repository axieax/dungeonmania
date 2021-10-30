package dungeonmania.model.goal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class GoalComposite implements Goal {

    private List<Goal> goals = new ArrayList<>();

    public List<Goal> getGoals() {
        return goals;
    }

    public String getOperator() {
        return getClass().getSimpleName().replace("Composite", "").toUpperCase();
    }

    @Override
    public String toString() {
        String delimiter = String.format(" %s ", getOperator());
        return getIncompleteGoals()
            .map(Goal::toString)
            .collect(Collectors.joining(delimiter, "(", ")"));
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("goal", getOperator());
        JSONArray subgoals = new JSONArray(getIncompleteGoals().map(Goal::toJSON));
        json.put("subgoals", subgoals);
        return json;
    }

    private Stream<Goal> getIncompleteGoals() {
        return getGoals().stream().filter(goal -> !goal.evaluate());
    }
}
