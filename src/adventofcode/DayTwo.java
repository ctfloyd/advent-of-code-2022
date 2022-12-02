package adventofcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayTwo extends AbstractAdventOfCode {

    private static final int DRAW_POINTS = 3;
    private static final int WIN_POINTS = 6;

    private static final HashMap<String, Integer> SCORES = new HashMap<>() {{
        put("X", 1);
        put("Y", 2);
        put("Z", 3);
    }};

    Map<String, String> DRAW = new HashMap<>() {{
        put("A", "X");
        put("B", "Y");
        put("C", "Z");
    }};

    Map<String, String> WIN = new HashMap<>(){{
        put("A", "Y");
        put("B", "Z");
        put("C", "X");
    }};

    Map<String, String> LOSS = new HashMap<>() {{
        put("A", "Z");
        put("B", "X");
        put("C", "Y");
    }};

    @Override
    public Object solvePartOne() throws Exception {
        List<String> input = readInputForDay(2);
        int total = 0;
        for (String str : input) {
            String[] parts = str.split(" ");
            String opponent = parts[0];
            String me = parts[1];
            if (DRAW.get(opponent).equals(me)) {
                total += DRAW_POINTS;
            } else if (WIN.get(opponent).equals(me)) {
                total += WIN_POINTS;
            }
            total += SCORES.get(me);
        }
        return total;
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<String> input = readInputForDay(2);

        int total = 0;
        for (String str : input) {
            String[] parts = str.split(" ");
            String opponent = parts[0];
            String me = parts[1];

            String shape;
            if (me.equals("Y")) {
                total += DRAW_POINTS;
                shape = DRAW.get(opponent);
            } else if (me.equals("Z")) {
                total += WIN_POINTS;
                shape = WIN.get(opponent);
            } else {
                shape = LOSS.get(opponent);
            }
            total += SCORES.get(shape);
        }
        return total;
    }

    public static void main(String[] args) {
        new DayTwo().execute();
    }
}
