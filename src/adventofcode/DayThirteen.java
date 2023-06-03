package adventofcode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DayThirteen extends AbstractAdventOfCode {

    List<String> input;

    @Override
    public void setup() throws Exception {
        input = readInputForDay(13);
    }

    @Override
    public Object solvePartOne() throws Exception {
        List<Packet> packets = parseInput(input);
        return IntStream.range(0, packets.size())
                .filter(x -> x % 2 == 0)
                .filter(i -> packets.get(i).compareTo(packets.get(i + 1)) < 0)
                .map(i -> i / 2 + 1)
                .sum();
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<Packet> packets = parseInput(input);
        packets.addAll(Arrays.asList(parsePacket("[[2]]"), parsePacket("[[6]]")));
        Collections.sort(packets);
        return IntStream.range(0, packets.size())
                .filter(i -> packets.get(i).isMarkerPacket(2) || packets.get(i).isMarkerPacket(6))
                .map(i -> i + 1)
                .reduce((x, y) -> x * y)
                .orElse(-1);
    }

    private List<Packet> parseInput(List<String> lines)  {
        return lines.stream()
                .filter(line -> !line.isBlank())
                .map(this::parsePacket)
                .collect(Collectors.toList());
    }

    private Packet parsePacket(String line) {
        StringBuilder buffer = new StringBuilder(4);
        Stack<Packet> packetStack = new Stack<>();
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (Character.isDigit(ch)) {
                buffer.append(ch);
            } else {
                if (ch == '[') {
                    Packet newPacket = Packet.asList();
                    if (!packetStack.isEmpty()) {
                        packetStack.peek().addValue(newPacket);
                    }
                    packetStack.push(newPacket);
                }
                if (ch == ']') {
                    Packet currentPacket = packetStack.size() == 1 ? packetStack.peek() : packetStack.pop();
                    if (!buffer.isEmpty()) {
                        currentPacket.addValue(buffer.toString());
                    }
                }
                if (ch == ',' && !buffer.isEmpty()) {
                    packetStack.peek().addValue(buffer.toString());
                }
                buffer = new StringBuilder();
            }
        }
        return packetStack.pop();
    }

    private static final class Packet implements Comparable<Packet> {
        public static final String INTEGER = "INTEGER";
        public static final String LIST = "LIST";

        private final String type;
        private Integer integerValue = null;
        private List<Packet> packets = null;

        public static Packet asList(Packet... packets) {
            return new Packet(Arrays.stream(packets).collect(Collectors.toList()));
        }

        private Packet(int intValue) {
            this.type = INTEGER;
            this.integerValue = intValue;
        }

        private Packet(List<Packet> values) {
            this.type = LIST;
            this.packets = values;
        }

        public void addValue(String value) {
            addValue(new Packet(Integer.parseInt(value)));
        }

        public void addValue(Packet packet) {
            assert(isList());
            this.packets.add(packet);
        }

        public boolean isInteger() {
            return this.type.equals(INTEGER);
        }

        public boolean isList() {
            return this.type.equals(LIST);
        }

        public int getIntegerValue() {
            return integerValue;
        }

        public Packet getPacket(int index) {
            return isList() ? this.packets.get(index) : null;
        }

        public Packet getFirstPacket() {
            return getPacket(0);
        }

        private boolean isListOfSizeOne() {
            return isList() && size() == 1;
        }

        public int size() {
            return isList() ? this.packets.size() : -1;
        }

        public boolean isMarkerPacket(int marker) {
            return isListOfSizeOne() &&
                    getFirstPacket().isListOfSizeOne() &&
                    getFirstPacket().getFirstPacket().isInteger() &&
                    getFirstPacket().getFirstPacket().getIntegerValue() == marker;
        }


        @Override
        public String toString() {
            if (type.equals(INTEGER)) {
                return integerValue.toString();
            } else {
                return "[" + packets.stream().map(String::valueOf).collect(Collectors.joining(",")) + "]";
            }
        }

        @Override
        public int compareTo(Packet other) {
            if (isInteger() && other.isInteger()) {
                return getIntegerValue() - other.getIntegerValue();
            }

            if (isList() && other.isList()) {
                return compareLists(other);
            }

            if (isInteger()) {
                return Packet.asList(this).compareTo(other);
            }

            return this.compareTo(Packet.asList(other));
        }

        private int compareLists(Packet other) {
            int minSize = Math.min(size(), other.size());
            for (int i = 0; i < minSize; i++) {
                int comparison = getPacket(i).compareTo(other.getPacket(i));
                if (comparison != 0) {
                    return comparison;
                }
            }
            return size() - other.size();
        }
    }

    public static void main(String[] args) {
        new DayThirteen().execute();
    }

}
