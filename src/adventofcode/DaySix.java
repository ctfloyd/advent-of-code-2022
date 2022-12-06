package adventofcode;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DaySix extends AbstractAdventOfCode {

    private List<String> input;

    @Override
    public void setup() throws Exception {
        input = readInputForDay(6);
    }

    @Override
    public Object solvePartOne() throws Exception {
        String data = input.get(0);
        int idx = -1;
        for (int i = 0; i < data.length() - 4; i++) {
            String candidate = data.substring(i, i + 4);
            Set<Integer> chars = candidate.chars().boxed().collect(Collectors.toSet());
            if (chars.size() == 4) {
                idx = i + 4;
                break;
            }
        }
        return idx;
    }

    @Override
    public Object solvePartTwo() throws Exception {
        String data = input.get(0);
        int idx = -1;
        for (int i = 0; i < data.length() - 14; i++) {
            String candidate = data.substring(i, i + 14);
            Set<Integer> chars = candidate.chars().boxed().collect(Collectors.toSet());
            if (chars.size() == 14) {
                idx = i + 14;
                break;
            }
        }
        return idx;
    }

    public static void main(String[] args) {
        new DaySix().execute();
    }
}
