package adventofcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaySeven extends AbstractAdventOfCode {

    private List<String> input;

    @Override
    public void setup() throws Exception {
        input = readInputForDay(7);
    }

    private Map<String, Integer> makeFileSystem() {
        Map<String, Integer> filePathAndSize = new HashMap<>();
        String currentDirectory = "";
        boolean inListFiles = false;
        for (String str : input) {
            String[] split = str.split(" ");
            if (split[0].equals("$")) {
                if (split[1].equals("cd")) {
                    if (split[2].equals("..")) {
                        currentDirectory = currentDirectory.substring(0, currentDirectory.lastIndexOf("/"));
                        if (currentDirectory.isEmpty()) {
                            currentDirectory = "/";
                        }
                    } else {
                        if (currentDirectory.endsWith("/") || currentDirectory.isEmpty()) {
                            currentDirectory += split[2];
                        } else {
                            currentDirectory += "/" + split[2];
                        }
                        filePathAndSize.put(currentDirectory, 0);
                    }
                }

                if (split[1].equals("ls")) {
                    inListFiles = true;
                }
            } else if (inListFiles) {
                if (!split[0].equals("dir")) {
                    try {
                        Integer sizeInBytes = Integer.parseInt(split[0]);
                        Integer currentValue = filePathAndSize.getOrDefault(currentDirectory, 0);
                        filePathAndSize.put(currentDirectory, currentValue + sizeInBytes);
                    } catch (NumberFormatException e) {
                        // not a number, swallow error and move on
                        inListFiles = false;
                    }
                }
            }
        }

        Map<String, Integer> actualFilePathAndSize = new HashMap<>();
        for (Map.Entry<String, Integer> entry : filePathAndSize.entrySet()) {
            String directory = entry.getKey();
            int size = filePathAndSize.get(directory);

            for (Map.Entry<String, Integer> entry2 : filePathAndSize.entrySet()) {
                String otherDirectory = entry2.getKey();
                int otherSize = entry2.getValue();
                if (otherDirectory.contains(directory) && !otherDirectory.equals(directory)) {
                    size += otherSize;
                }
            }

            actualFilePathAndSize.put(directory, size);
        }

        return actualFilePathAndSize;
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
