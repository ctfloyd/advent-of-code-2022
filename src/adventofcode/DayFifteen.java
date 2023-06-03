package adventofcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DayFifteen extends AbstractAdventOfCode {

    List<String> input;

    @Override
    public void setup() throws Exception {
        input = readInputForDay(15);
    }

    @Override
    public Object solvePartOne() throws Exception {
        List<Sensor> sensors = parseInput();
        int y = 2000000;
        Set<Integer> beaconX = sensors.stream().filter(s -> s.beacon.y == y).map(s -> s.beacon.x).collect(Collectors.toSet());
        return getRangesForY(sensors, y).stream()
                .mapToInt(r -> r.getCount(beaconX))
                .sum();
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<Sensor> sensors = parseInput();
        int min = 0;
        int max = 4000000;
        for (int y = min; y < max; y++) {
            List<Range> ranges = getRangesForY(sensors, y);
            if (ranges.size() > 1) {
                for (Range range : ranges) {
                    int x = range.end + 1;
                    if (x > min && x < max) {
                        return ((long)x * 4000000) + (long)y;
                    }
                }
            }
        }
        return -1;
    }

    private List<Range> getRangesForY(List<Sensor> sensors, int y) {
        List<Range> ranges = sensors.stream().map(s -> s.pointsOnY(y)).filter(Objects::nonNull).toList();
        boolean didMerge;
        do {
            List<Range> mergedRanges = new ArrayList<>();
            List<Range> currentCandidates = new ArrayList<>(ranges);
            for (Range range : ranges) {
                for (Range candidiateRange : new ArrayList<>(currentCandidates)) {
                    if (range.canMerge(candidiateRange)) {
                        range.merge(candidiateRange);
                        currentCandidates.remove(candidiateRange);
                        if (!mergedRanges.contains(range)) {
                            mergedRanges.add(range);
                        }
                    }
                }
                if (currentCandidates.contains(range)) {
                    mergedRanges.add(range);
                }
            }
            didMerge = mergedRanges.size() != ranges.size();
            ranges = mergedRanges;
        } while (didMerge);

        return ranges;
    }

    public List<Sensor> parseInput() {
        Pattern pattern = Pattern.compile("x=(-?\\d+), y=(-?\\d+)");
        List<Sensor> sensors = new ArrayList<>();
        for (String line : input) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                Point sensor = new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
                if (matcher.find()) {
                    Point beacon = new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
                    sensors.add(new Sensor(sensor, beacon));
                }
            }

        }
        return sensors;
    }

    private class Range {
        int start, end;
        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }
        public int getCount(Set<Integer> excludePoints) {
            int count = (end - start) + 1;
            for (Integer excludePoint : excludePoints) {
                if (excludePoint >= start && excludePoint <= end) {
                    count -= 1;
                }
            }
            return count;
        }

        public boolean canMerge(Range range) {
            return start - 1 <= range.end && end + 1 >= range.start;
        }

        public void merge(Range range) {
            if (start > range.start) {
                start = range.start;
            }

            if (end < range.end) {
                end = range.end;
            }
        }
    }

    private class Sensor {
        Point location, beacon;
        public Sensor(Point location, Point beacon) {
            this.location = location;
            this.beacon = beacon;
        }

        public Range pointsOnY(int y) {
            int maxDistance = Math.abs(location.y - beacon.y) + Math.abs(location.x - beacon.x);
            int distanceToY = Math.abs(y - location.y);
            int range = maxDistance - distanceToY;
            if (range >= 0) {
                return new Range(location.x - range, location.x + range);
            }
            return null;
        }

    }

    private class Point {
        int x, y;
        Point (int x, int y) {
            this.x = x; this.y = y;
        }
    }

    public static void main(String[] args) {
        new DayFifteen().execute();
    }
}
