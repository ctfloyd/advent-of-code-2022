package adventofcode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DayOne extends AbstractAdventOfCode {

    private List<Integer> caloriesByElf(List<String> input) {
        List<Integer> calories = new ArrayList<>();
        int caloriesForElf = 0;
        for (String str : input) {
            if (!str.isEmpty()) {
                caloriesForElf += Integer.parseInt(str);
            } else {
                calories.add(caloriesForElf);
                caloriesForElf = 0;
            }
        }
        return calories;
    }

    @Override
    public Object solvePartOne() throws Exception {
        return caloriesByElf(readInputForDay(1)).stream().mapToInt(Integer::intValue).max().orElse(-1);
    }

    @Override
    public Object solvePartTwo() throws Exception {
        return caloriesByElf(readInputForDay(1)).stream().sorted(Comparator.reverseOrder()).limit(3).mapToInt(Integer::intValue).sum();
    }

    public static void main(String[] args) {
        new DayOne().execute();
    }

}
