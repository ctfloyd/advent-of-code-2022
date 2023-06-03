package adventofcode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class DayFourteen extends AbstractAdventOfCode {

    private static final int GRID_SIZE = 659;
    private static final int START_X = 500;
    private static final int START_Y = 0;

    private List<String> input;

    @Override
    public void setup() throws Exception {
        input = readInputForDay(14);
    }

    @Override
    public Object solvePartOne() throws Exception {
        List<Range> occupiedRanges = parseInput(input);
        int minX = Integer.MAX_VALUE;
        for (Range range : occupiedRanges) {
            minX = Math.min(minX, range.start.x);
        }
        int finalMinX = minX;
        Point min = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
        return simulate(occupiedRanges, new Range(min, min), (currentX, unused) -> currentX >= finalMinX);
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<Range> occupiedRanges = parseInput(input);
        int maxY = Integer.MIN_VALUE;
        for (Range range : occupiedRanges) {
            maxY = Math.max(maxY, range.end.y);
        }
        Range limitRange = new Range(new Point(Integer.MIN_VALUE, maxY + 2), new Point(Integer.MAX_VALUE, maxY + 2));
        return simulate(occupiedRanges, limitRange, (unused, points) -> !points[START_X][START_Y]);
    }

    private int simulate(List<Range> occupiedRanges, Range limitRange, BiFunction<Integer, boolean[][], Boolean> predicate) {
        int sand = 0;

        boolean[][] occupiedPoints = new boolean[GRID_SIZE][GRID_SIZE];
        for (Range range : occupiedRanges) {
            for (int x = range.start.x; x <= range.end.x; x++) {
                for (int y = range.start.y; y <= range.end.y; y++) {
                    occupiedPoints[x][y] = true;
                }
            }
        }

        int[] xOffsets = new int[] { 0, -1, 1 };
        int currentX = START_X;
        int currentY = START_Y;
        while (predicate.apply(currentX, occupiedPoints)) {
            boolean moved = false;
            for (int xOffset : xOffsets) {
                if (!moved && !occupiedPoints[currentX + xOffset][currentY + 1] && !limitRange.intersects(currentX + xOffset, currentY + 1)) {
                    currentX += xOffset;
                    currentY += 1;
                    moved = true;
                }
            }
            if (!moved) {
                occupiedPoints[currentX][currentY] = true;
                currentX = START_X;
                currentY = START_Y;
                sand += 1;
            }
        }

        return sand;
    }

    private List<Range> parseInput(List<String> lines) {
        List<Range> ranges = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(" ");
            for (int i = 0; i < parts.length - 1; i += 2) {
                String[] pair1 = parts[i].split(",");
                String[] pair2 = parts[i + 2].split(",");
                ranges.add(new Range(
                            new Point(Integer.parseInt(pair1[0]), Integer.parseInt(pair1[1])),
                            new Point(Integer.parseInt(pair2[0]), Integer.parseInt(pair2[1]))
                        )
                );
            }
        }
        return ranges;
    }

    private static class Point {
        int x, y;
        public Point(int x, int y) {
            this.x = x; this.y = y;
        }
    }

    private class Range {
        Point start, end;
        public Range(Point start, Point end) {
            if (start.x > end.x || start.y > end.y)  {
                this.end = start; this.start = end;
            } else {
                this.start = start; this.end = end;
            }
        }
        public boolean intersects(int x, int y) {
            return x >= start.x && x <= end.x && y >= start.y && y <= end.y;
        }
    }

    public static void main(String[] args) {
        new DayFourteen().execute();
    }
}
