package adventofcode;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DayFive extends AbstractAdventOfCode {

    private List<String> input;

    @Override
    public void setup() throws Exception {
        input = readInputForDay(5);
    }

    private Map<Integer, Stack<Character>> generateCargoStacksFromInput(List<String> input, AtomicInteger firstLineOfInstructionsIndex) {
        Map<Integer, Stack<Character>> cargoStacks = new HashMap<>();

        int bottomOfStackIndex = -1;
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).contains("1")) {
                bottomOfStackIndex = i - 1;
                firstLineOfInstructionsIndex.set(i + 2);
                break;
            }
        }

        int currentCargoStack = 0;
        for (int i = bottomOfStackIndex; i >= 0; i--) {
            String str = input.get(i);
            for (int j = 0; j < str.length(); j++) {
                char ch = str.charAt(j);
                if (j % 4 == 0) {
                    currentCargoStack += 1;
                } else if (Character.isLetter(ch)) {
                    cargoStacks.computeIfAbsent(currentCargoStack, (unused) -> new Stack<>()).add(ch);
                }
            }
            currentCargoStack = 0;
        }

        return cargoStacks;
    }

    private String solve(Map<Integer, Stack<Character>> cargoStacks, int instructionIndex, boolean craneMover9001) {
        for (int i = instructionIndex; i < input.size(); i++) {
            String rawInstruction = input.get(i);
            String parsedInstruction = rawInstruction.replaceAll("move ", "")
                    .replaceAll("from ", "")
                    .replaceAll("to ", "");
            String[] rawNumbers = parsedInstruction.split(" ");

            int amount = Integer.parseInt(rawNumbers[0]);
            int from = Integer.parseInt(rawNumbers[1]);
            int to = Integer.parseInt(rawNumbers[2]);

            Stack<Character> fromStack = cargoStacks.get(from);
            Stack<Character> toStack = cargoStacks.get(to);

            if (craneMover9001) {
                List<Character> tmp = new ArrayList<>();
                for (int j = 0; j < amount; j++) {
                    tmp.add(fromStack.pop());
                }

                for (int j = tmp.size() - 1; j >= 0; j--) {
                    toStack.add(tmp.get(j));
                }
            } else {
                for (int j = 0; j < amount; j++) {
                    toStack.add(fromStack.pop());
                }
            }
        }

        String output = "";
        List<Map.Entry<Integer, Stack<Character>>> orderedEntries = cargoStacks.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());

        for (Map.Entry<Integer, Stack<Character>> entry : orderedEntries) {
            output += entry.getValue().peek();
        }

        return output;
    }

    @Override
    public Object solvePartOne() throws Exception {
        AtomicInteger instructionIndex = new AtomicInteger(-1);
        Map<Integer, Stack<Character>> cargoStacks = generateCargoStacksFromInput(input, instructionIndex);
        return solve(cargoStacks, instructionIndex.intValue(), false);
    }

    @Override
    public Object solvePartTwo() throws Exception {
        AtomicInteger instructionIndex = new AtomicInteger(-1);
        Map<Integer, Stack<Character>> cargoStacks = generateCargoStacksFromInput(input, instructionIndex);
        return solve(cargoStacks, instructionIndex.intValue(), true);
    }

    public static void main(String[] args) {
        new DayFive().execute();
    }
}
