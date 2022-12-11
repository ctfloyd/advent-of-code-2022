package adventofcode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.StringJoiner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Function;
import java.util.function.Predicate;

public class DayEleven extends AbstractAdventOfCode {

    private List<String> input;

    private static class Monkey {
        private final Queue<BigInteger> items = new ArrayBlockingQueue<>(64);
        private final Predicate<BigInteger> test;
        private final Function<BigInteger, BigInteger> transformer;
        private final int testDivisor;
        private final int number;
        private final int ifTrue;
        private final int ifFalse;
        private int numberOfInspections = 0;

        public Monkey(int number, Function<BigInteger, BigInteger> transformer, Predicate<BigInteger> test, int ifTrue, int ifFalse, List<BigInteger> items, int testDivisor) {
            this.number = number;
            this.transformer = transformer;
            this.test = test;
            this.ifTrue = ifTrue;
            this.ifFalse = ifFalse;
            this.items.addAll(items);
            this.testDivisor = testDivisor;
        }

        public void takeTurn(List<Monkey> otherMonkies, boolean partOne, BigInteger modulo) {
            while (!items.isEmpty()) {
                numberOfInspections += 1;
                BigInteger currentItem = items.poll();
                BigInteger worryLevel = transformer.apply(currentItem);
                if (partOne) {
                    worryLevel = worryLevel.divide(BigInteger.valueOf(3));
                } else {
                    worryLevel = worryLevel.mod(modulo);
                }

                int otherMokeyIndex = test.test(worryLevel) ? ifTrue : ifFalse;
                Monkey other = otherMonkies.get(otherMokeyIndex);
                other.addItem(worryLevel);
            }
        }

        public int getTestDivisor() {
            return testDivisor;
        }

        public int getNumberOfInspections() {
            return numberOfInspections;
        }

        public void addItem(BigInteger item) {
            items.add(item);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Monkey.class.getSimpleName() + "[", "]")
                    .add("number=" + number)
                    .add("transformer=" + transformer)
                    .add("test=" + test)
                    .add("ifTrue=" + ifTrue)
                    .add("ifFalse=" + ifFalse)
                    .add("items=" + items)
                    .toString();
        }
    }

    @Override
    public void setup() throws Exception {
        input = readInputForDay(11);
    }

    private List<Monkey> parseInput() {
        List<Monkey> monkies = new ArrayList<>();
        List<BigInteger> items = new ArrayList<>();
        int monkey = 0;
        int ifTrue = -1;
        int ifFalse = -1;
        int testDivisor = -1;
        Function<BigInteger, BigInteger> transformer = null;
        Predicate<BigInteger> test = null;
        for (String line : input) {
            if (line.contains("Starting items: ")) {
                String parsedLine = line.replaceAll("Starting items: ", "").trim();
                String[] numbers = parsedLine.split(", ");
                for (String num : numbers) {
                    items.add(BigInteger.valueOf(Long.parseLong(num)));
                }
            }
            if (line.contains("Operation: ")) {
                String parsedLine = line.replaceAll("Operation: new = old ", "").trim();
                String[] parts = parsedLine.split(" ");
                char operation = parts[0].charAt(0);
                String numberStr = parts[1];

                Integer actualNumber = -1;
                try {
                    actualNumber = Integer.parseInt(numberStr);
                } catch (Exception ex) {
                    // swallow
                }

                if (operation == '+') {
                    if (numberStr.equals("old")) {
                        transformer = (number) -> number.add(number);
                    } else {
                        Integer finalActualNumber = actualNumber;
                        transformer = (number) -> number.add(BigInteger.valueOf(finalActualNumber));
                    }
                } else if (operation == '*') {
                    if (numberStr.equals("old")) {
                        transformer = (number) -> number.multiply(number);
                    } else {
                        Integer finalActualNumber = actualNumber;
                        transformer = (number) -> number.multiply(BigInteger.valueOf(finalActualNumber));
                    }
                }
            }
            if (line.contains("Test: ")) {
                String parsedLine = line.replace("Test: divisible by ", "").trim();
                Integer divisor = Integer.parseInt(parsedLine);
                testDivisor = divisor;
                test = (number) -> number.mod(BigInteger.valueOf(divisor)).equals(BigInteger.ZERO);
            }
            if (line.contains("If true: ")) {
                String parsedLine = line.trim().replace("If true: throw to monkey ", "").trim();
                ifTrue = Integer.parseInt(parsedLine);
            }
            if (line.contains("If false: ")) {
                String parsedLine = line.trim().replace("If false: throw to monkey ", "").trim();
                ifFalse = Integer.parseInt(parsedLine);

                monkies.add(new Monkey(monkey++, transformer, test, ifTrue, ifFalse, items, testDivisor));
                ifTrue = -1;
                ifFalse = -1;
                testDivisor = -1;
                transformer = null;
                test = null;
                items.clear();
            }
        }

        return monkies;
    }

    @Override
    public Object solvePartOne() throws Exception {
        List<Monkey> monkies = parseInput();
        for (int i = 0; i < 20; i++) {
            monkies.forEach(monkey -> monkey.takeTurn(monkies, true, BigInteger.ZERO));
        }
        return monkies.stream().map(Monkey::getNumberOfInspections).sorted(Comparator.reverseOrder()).limit(2).reduce((a, b) -> a * b);
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<Monkey> monkies = parseInput();
        BigInteger modulo = BigInteger.ONE;
        for (Monkey monkey : monkies) {
            modulo = modulo.multiply(BigInteger.valueOf(monkey.getTestDivisor()));
        }
        for (int i = 0; i < 10000; i++) {
            final BigInteger finalModulo = modulo;
            monkies.forEach(monkey -> monkey.takeTurn(monkies, false, finalModulo));
        }
        return monkies.stream().map(Monkey::getNumberOfInspections)
                .sorted(Comparator.reverseOrder())
                .limit(2)
                .map(BigInteger::valueOf)
                .reduce(BigInteger.ONE, BigInteger::multiply);
    }

    public static void main(String[] args) {
        new DayEleven().execute();
    }
}
