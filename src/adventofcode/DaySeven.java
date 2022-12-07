package adventofcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class DaySeven extends AbstractAdventOfCode {

    private List<String> input;

    @Override
    public void setup() throws Exception {
        input = readInputForDay(7);
    }

    private String getCurrentDirectoryString(Stack<String> stack)  {
        String s = "/";
        for (String str : stack) {
            if (!str.equals("/")) {
                if (s.equals("/")) {
                    s += str;
                } else {
                    s += "/" + str;
                }
            }
        }
        return s;
    }

    private void walkPathAndIncrement(Map<String, Integer> filePathAndSize, Stack<String> currentDirectory, int sizeInBytes) {
        String path = "/";
        for (String st : currentDirectory) {
            if (path.equals("/")) {
                if (!st.equals("/")) {
                    path += st;
                }
            } else {
                path += "/" + st;
            }
            filePathAndSize.put(path, filePathAndSize.getOrDefault(path, 0) + sizeInBytes);
        }
    }

    private Map<String, Integer> makeFileSystem() {
        Map<String, Integer> filePathAndSize = new HashMap<>();

        Stack<String> currentDirectory = new Stack<>();
        for (String str : input) {
            String[] split = str.split(" ");
            if (split[0].equals("$")) {
                if (split[1].equals("cd")) {
                    if (split[2].equals("..")) {
                        currentDirectory.pop();
                    } else {
                        currentDirectory.push(split[2]);
                        String currentDirectoryStr = getCurrentDirectoryString(currentDirectory);
                        filePathAndSize.put(currentDirectoryStr, 0);;
                    }
                }
            } else {
                if (!split[0].equals("dir")) {
                    try {
                        walkPathAndIncrement(filePathAndSize, currentDirectory, Integer.parseInt(split[0]));
                    } catch (NumberFormatException e) {
                        // not a number, swallow error and move on
                    }
                }
            }
        }

        return filePathAndSize;
    }



    @Override
    public Object solvePartOne() throws Exception {
        return makeFileSystem().values().stream().filter(i -> i <= 100000).mapToInt(Integer::intValue).sum();
    }

    @Override
    public Object solvePartTwo() throws Exception {
        Map<String, Integer> fileSystem = makeFileSystem();
        int totalSize = 70000000;
        int desiredSize = 30000000;
        int currentUsedSpace = fileSystem.get("/");
        int currentFreeSpace = totalSize - currentUsedSpace;
        int minimumDirectorySize = desiredSize - currentFreeSpace;

        int answer = Integer.MAX_VALUE;
        for (Integer fileSize : fileSystem.values()) {
            if (fileSize >= minimumDirectorySize) {
                answer = Math.min(answer, fileSize);
            }
        }

        return answer;
    }

    public static void main(String[] args) {
        new DaySeven().execute();
    }

}
