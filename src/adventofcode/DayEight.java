package adventofcode;

public class DayEight extends AbstractAdventOfCode {

    private int[][] input;

    @Override
    public void setup() throws Exception {
        input = read2dBoardForDay(8);
    }

    @Override
    public Object solvePartOne() throws Exception {
        int count = 2 * (input.length + input[0].length) - 4;
        for (int i = 1; i < input.length - 1; i++) {
            for (int j = 1; j < input[i].length - 1; j++){
                int height = input[i][j];
                boolean visible = true;
                for (int k = 0; k < input.length ; k++) {
                    if (k == j) {
                        if (visible) {
                            break;
                        }
                        visible = true;
                        continue;
                    }
                    if (input[i][k] >= height)  {
                        visible = false;
                    }
                }
                if (visible) {
                    count++;
                    continue;
                }
                visible = true;
                for (int k = 0; k < input[i].length; k++) {
                    if (k == i) {
                        if (visible) {
                            break;
                        }
                        visible = true;
                        continue;
                    }
                    if (input[k][j] >= height) {
                        visible = false;
                    }
                }
                if (visible) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public Object solvePartTwo() throws Exception {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                int height = input[i][j];

                int up = 0;
                for (int k = i - 1; k >= 0; k--) {
                    up++;
                    if (input[k][j] >= height) {
                        break;
                    }
                }
                int down = 0;
                for (int k = i + 1; k < input.length; k++) {
                    down++;
                    if (input[k][j] >= height) {
                        break;
                    }
                }

                int left = 0;
                for (int k = j - 1; k >= 0; k--) {
                    left++;
                    if (input[i][k] >= height) {
                        break;
                    }
                }

                int right = 0;
                for (int k = j + 1; k < input[i].length; k++) {
                    right++;
                    if (input[i][k] >= height) {
                        break;
                    }
                }
                max = Math.max(max, left * right * up * down);
            }
        }
        return max;
    }

    public static void main(String[] args) {
        new DayEight().execute();
    }

}
