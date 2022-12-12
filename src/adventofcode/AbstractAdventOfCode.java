package adventofcode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractAdventOfCode {

    private static final String DEFAULT_INPUT_PATH_TEMPLATE = "input/day%d/data.txt";
    private static final String DEFAULT_EXAMPLE_INPUT_PATH_TEMPLATE = "input/day%d/example.txt";

    public List<String> readInputForDay(int day) throws Exception {
        return readInput(String.format(DEFAULT_INPUT_PATH_TEMPLATE, day));
    }

    public List<String> readExampleInputForDay(int day) throws Exception {
        return readInput(String.format(DEFAULT_EXAMPLE_INPUT_PATH_TEMPLATE, day));
    }

    public List<String> readInput(String path) throws Exception {
        return Files.lines(Path.of(path)).collect(Collectors.toList());
    }

    public List<String> readInputAsCsvForDay(int day) throws Exception {
        return readInputAsCsv(String.format(DEFAULT_INPUT_PATH_TEMPLATE, day));
    }

    public List<String> readExampleInputAsCsvForDay(int day) throws Exception {
        return readInputAsCsv(String.format(DEFAULT_EXAMPLE_INPUT_PATH_TEMPLATE, day));
    }

    public List<String> readInputAsCsv(String path) throws Exception {
        return Files.lines(Path.of(path)).map(l -> l.split(",")).flatMap(Stream::of).collect(Collectors.toList());
    }

    public List<Integer> readInputAsIntsForDay(int day) throws Exception {
        return readInputAsInts(String.format(DEFAULT_INPUT_PATH_TEMPLATE, day));
    }

    public List<Integer> readExampleInputAsIntsForDay(int day) throws Exception {
        return readInputAsInts(String.format(DEFAULT_EXAMPLE_INPUT_PATH_TEMPLATE, day));
    }

    public List<Integer> readInputAsInts(String path) throws Exception {
        return readInputAsInts(path, 10);
    }

    public List<Integer> readInputAsInts(String path, int radix) throws Exception {
        return readInput(path).stream().map(i -> Integer.parseInt(i, radix)).collect(Collectors.toList());
    }

    public List<Integer> readInputAsCsvIntsForDay(int day) throws Exception {
        return readInputAsCsvInts(String.format(DEFAULT_INPUT_PATH_TEMPLATE, day));
    }

    public List<Integer> readExampleInputAsCsvIntsForDay(int day) throws Exception {
        return readInputAsCsvInts(String.format(DEFAULT_EXAMPLE_INPUT_PATH_TEMPLATE, day));
    }

    public List<Integer> readInputAsCsvInts(String path, int radix) throws Exception {
        return readInputAsCsv(path).stream().map(i -> Integer.parseInt(i, radix)).collect(Collectors.toList());
    }

    public List<Integer> readInputAsCsvInts(String path) throws Exception {
        return readInputAsCsvInts(path, 10);
    }

    public int[][] read2dBoardForDay(int day) throws Exception {
        return read2dBoard(String.format(DEFAULT_INPUT_PATH_TEMPLATE, day));
    }

    public int[][] readExample2DBoardForDay(int day) throws Exception {
        return read2dBoard(String.format(DEFAULT_EXAMPLE_INPUT_PATH_TEMPLATE, day));
    }

    public char[][] read2dBoardForDayAsChars(int day) throws Exception {
        return read2dBoardAsChars(String.format(DEFAULT_INPUT_PATH_TEMPLATE, day));
    }

    public char[][] readExample2DBoardForDayAsChars(int day) throws Exception {
        return read2dBoardAsChars(String.format(DEFAULT_EXAMPLE_INPUT_PATH_TEMPLATE, day));
    }

    public char[][] read2dBoardAsChars(String path) throws Exception {
        List<String> lines = readInput(path);
        char[][] board = new char[lines.size()][lines.get(0).split("").length];
        for (int i = 0;  i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = line.charAt(j);
            }
        }
        return board;
    }

    public int[][] read2dBoard(String path) throws Exception {
        List<String> lines = readInput(path);
        int[][] board = new int[lines.size()][lines.get(0).split("").length];
        for (int i = 0;  i < lines.size(); i++) {
            String[] nums = lines.get(i).split("");
            for (int j = 0; j < nums.length; j++) {
                board[i][j] = Integer.parseInt(nums[j]);
            }
        }
        return board;
    }

    public abstract Object solvePartOne() throws Exception;
    public abstract Object solvePartTwo() throws Exception;
    public void setup() throws Exception {
        // nothing
    }

    public void execute() {
        try {
            setup();
            long start = System.nanoTime();
            Object answer = solvePartOne();
            float delta = (System.nanoTime() - start) / 1000000f;
            System.out.println(String.format("(1) %6.2f ms - %s", delta, answer));

            start = System.nanoTime();
            answer = solvePartTwo();
            delta = (System.nanoTime() - start) / 1000000f;
            System.out.println(String.format("(2) %6.2f ms - %s", delta, answer));
        } catch (Exception ex) {
            System.err.println("An error occurred while solving advent of code problem!");
            ex.printStackTrace();
        }
    }
}
