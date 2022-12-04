package adventofcode;

import java.util.ArrayList;
import java.util.List;

public class DayFour extends AbstractAdventOfCode {

    private List<String> input;

    @Override
    public void setup() throws Exception {
        input = readInputForDay(4);
    }

    @Override
    public Object solvePartOne() throws Exception {
        int contains = 0;
        for (String str : input) {
            String[] rangePair = str.split(",");
            String[] aRange = rangePair[0].split("-");
            String[] otherRange = rangePair[1].split("-");
            int v1 = Integer.parseInt(aRange[0]);
            int v2 = Integer.parseInt(aRange[1]);
            int v3 = Integer.parseInt(otherRange[0]);
            int v4 = Integer.parseInt(otherRange[1]);

            if (v1 <= v3 && v2 >= v4 || v3 <= v1 && v4 >= v2) {
                contains++;
            }
        }
        return contains;
    }

    @Override
    public Object solvePartTwo() throws Exception {
        int overlap = 0;
        for (String str : input) {
            String[] rangePair = str.split(",");
            String[] aRange = rangePair[0].split("-");
            String[] otherRange = rangePair[1].split("-");
            int v1 = Integer.parseInt(aRange[0]);
            int v2 = Integer.parseInt(aRange[1]);
            int v3 = Integer.parseInt(otherRange[0]);
            int v4 = Integer.parseInt(otherRange[1]);

            if (v1 <= v3 && v2 >= v3 || v3 <= v1 && v4 >= v1) {
                overlap++;
            }
       }
        return overlap;
    }

    public static void main(String[] args) {
        new DayFour().execute();
    }
}
