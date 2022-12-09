package adventofcode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

public class DayNine extends AbstractAdventOfCode {

    private class Point {
        public int x;
        public int y;

        public Point(int x, int y)  {
            this.x = x;
            this.y = y;
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

        @Override
        public String toString() {
            return new StringJoiner(", ", Point.class.getSimpleName() + "[", "]")
                    .add("x=" + x)
                    .add("y=" + y)
                    .toString();
        }
    }

    List<String> input;

    @Override
    public void setup() throws Exception {
        input = readInputForDay(9);
    }

    private void simulate(Point head, Point tail, String direction, boolean isTrueHead, boolean isTrueTail, Set<Point> tailPoints) {
        if (isTrueHead) {
            if (direction.equals("R")) {
                head.x += 1;
            } else if (direction.equals("L")) {
                head.x -= 1;
            } else if (direction.equals("U")) {
                head.y += 1;
            } else if (direction.equals("D")) {
                head.y -= 1;
            }
        }

        if (!isAdjacent(head, tail)) {
            int deltaX = head.x - tail.x;
            int deltaY = head.y - tail.y;
            int stepX = deltaX > 0 ? 1 : -1;
            int stepY = deltaY > 0 ? 1 : -1;
            if (deltaX != 0 && deltaY != 0) {
                tail.x += stepX;
                tail.y += stepY;
            } else if (deltaX != 0) {
                tail.x += stepX;
            } else {
                tail.y += stepY;
            }

            if (isTrueTail) {
                tailPoints.add(new Point(tail.x, tail.y));
            }
        }
    }

    private boolean isAdjacent(Point head, Point tail) {
        return Math.abs(head.x - tail.x) <= 1 && Math.abs(head.y - tail.y) <= 1;
    }

    @Override
    public Object solvePartOne() throws Exception {
        Set<Point> positions = new HashSet<>();
        positions.add(new Point(0, 0));

        Point head = new Point(0, 0);
        Point tail = new Point(0, 0);

        for (String str : input) {
            String[] split = str.split(" ");
            String direction = split[0];
            Integer amount = Integer.parseInt(split[1]);
            for (int i = 0; i < amount; i++) {
                simulate(head, tail, direction, true, true, positions);
            }
        }

        return positions.size();
    }

    @Override
    public Object solvePartTwo() throws Exception {
        Set<Point> positions = new HashSet<>();
        positions.add(new Point(0, 0));

        List<Point> knots = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            knots.add(new Point(0, 0));
        }

        for (String str : input) {
            String[] split = str.split(" ");
            String direction = split[0];
            Integer amount = Integer.parseInt(split[1]);
            for (int j = 0; j < amount; j++) {
                for (int i = 0; i < knots.size() - 1; i++) {
                    simulate(knots.get(i), knots.get(i + 1), direction, i == 0, i == 8, positions);
                }
            }
        }

        return positions.size();
    }

    public static void main(String[] args) {
        new DayNine().execute();
    }
}
