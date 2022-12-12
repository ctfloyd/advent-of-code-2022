package adventofcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class DayTwelve extends AbstractAdventOfCode {

    char[][] input;

    @Override
    public void setup() throws Exception {
        input = read2dBoardForDayAsChars(12);
    }

    private static class Point {
        public int x;
        public int y;
        public int score;
        public Point(int x, int y, int score) {
            this.x = x;
            this.y = y;
            this.score = score;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private Point findChar(char ch) {
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                if (input[i][j] == ch) {
                    return new Point(j, i, 0);
                }
            }
        }
        return null;
    }

    private Integer solve(Point start, Point end) {
        Queue<Point> needToVisit = new ArrayBlockingQueue<>(1024);
        Map<Point, Integer> visited = new HashMap<>();
        needToVisit.add(start);

        while (!needToVisit.isEmpty()) {
            Point visiting = needToVisit.remove();
            if (visited.containsKey(visiting)) {
                if (visiting.score >= visited.get(visiting)) {
                    continue;
                }
            }

            visited.put(visiting, visiting.score);
            char currentChar = input[visiting.y][visiting.x];
            char nextChar;

            if (visiting.x + 1 < input[visiting.y].length) {
                nextChar = input[visiting.y][visiting.x + 1];
                if (canVisit(currentChar, nextChar)) {
                    needToVisit.add(new Point(visiting.x + 1, visiting.y, visiting.score + 1));
                }
            }

            if (visiting.x - 1 >= 0){
                nextChar = input[visiting.y][visiting.x - 1];
                if (canVisit(currentChar, nextChar)) {
                    needToVisit.add(new Point(visiting.x - 1, visiting.y, visiting.score + 1));
                }
            }

            if (visiting.y + 1 < input.length) {
                nextChar = input[visiting.y + 1][visiting.x];
                if (canVisit(currentChar, nextChar)) {
                    needToVisit.add(new Point(visiting.x, visiting.y + 1, visiting.score + 1));
                }
            }

            if (visiting.y - 1 >= 0) {
                nextChar = input[visiting.y - 1][visiting.x];
                if (canVisit(currentChar, nextChar)) {
                    needToVisit.add(new Point(visiting.x, visiting.y - 1, visiting.score + 1));
                }
            }
        }

        return visited.get(end) == null ? null : visited.get(end) - 2;
    }

    private boolean canVisit(char currentChar, char nextChar) {
        return (currentChar == 'S' && nextChar == 'a') || (currentChar == 'z' && nextChar == 'E') || (nextChar - currentChar <= 1 && nextChar != 'E');
    }

    @Override
    public Object solvePartOne() throws Exception {
        return solve(findChar('S'), findChar('E'));
    }

    @Override
    public Object solvePartTwo() throws Exception {
        Point end = findChar('E');
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input.length; j++) {
                if (input[i][j] == 'a') {
                    Integer solve = solve(new Point(j, i, 0), end);
                    if (solve != null) {
                        min = Math.min(min, solve);
                    }
                }
            }
        }
        return min;
    }

    public static void main(String[] args) {
        new DayTwelve().execute();
    }
}
