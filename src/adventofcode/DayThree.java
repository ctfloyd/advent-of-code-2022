package adventofcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DayThree extends AbstractAdventOfCode {

    public List<String> createSmallerRucksack(String combinedRucksack, int partition) {
        int size = combinedRucksack.length();
        int chunkSize = size / partition;
        List<String> rucksacks = new ArrayList<>();
        for (int i = 0; i < size; i += chunkSize) {
            rucksacks.add(combinedRucksack.substring(i, i + chunkSize));
        }
        return rucksacks;
    }

    private char findOverlappingCharacter(List<String> strs) {
        List<Set<Integer>> charactersInRucksack = new ArrayList<>();
        for (String rucksack : strs) {
            charactersInRucksack.add(rucksack.chars().boxed().collect(Collectors.toSet()));
        }

        // intersect all sets
        Set<Integer> firstSet = charactersInRucksack.get(0);
        for (int i = 1; i < charactersInRucksack.size(); i++) {
            firstSet.retainAll(charactersInRucksack.get(i));
        }

        return (char) firstSet.iterator().next().intValue();
    }

    private int scoreChar(char ch) {
        return Character.isUpperCase(ch) ? ch - 38 : ch - 96;
    }


    @Override
    public Object solvePartOne() throws Exception {
        return readInputForDay(3).stream()
                .map(l -> createSmallerRucksack(l, 2))
                .mapToInt(s -> scoreChar(findOverlappingCharacter(s)))
                .sum();
    }

    @Override
    public Object solvePartTwo() throws Exception {
        List<String> input =  readInputForDay(3);
        List<List<String>> splitLines = new ArrayList<>();
        List<String> split = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            split.add(input.get(i));
            if ((i + 1) % 3 == 0) {
                splitLines.add(new ArrayList<>(split));
                split.clear();
            }
        }
        return splitLines.stream()
                .mapToInt(l -> scoreChar(findOverlappingCharacter(l)))
                .sum();
    }

    public static void main(String[] args) {
        new DayThree().execute();
    }
}
