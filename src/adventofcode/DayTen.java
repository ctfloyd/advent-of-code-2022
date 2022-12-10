package adventofcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class DayTen extends AbstractAdventOfCode {

    private static class Instruction {
        private final String type;
        private final int value;
        private int startCycle = 0;

        public Instruction(String type, int value) {
            this.type = type;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public int getValue() {
            return value;
        }

        public void setStartCycle(int startCycle) {
            this.startCycle = startCycle;
        }

        public int getStartCycle() {
            return startCycle;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Instruction.class.getSimpleName() + "[", "]")
                    .add("type='" + type + "'")
                    .add("value=" + value)
                    .add("startCycle=" + startCycle)
                    .toString();
        }
    }

    private static class CPU {

        private static final boolean DEBUG = false;
        private static final Map<String, Integer> CYCLES_PER_INSTRUCTION = new HashMap<>() {{
            put("noop", 1);
            put("addx", 2);
        }};

        private boolean render;
        private int cycleCount = 1;
        private int registerX = 1;
        private Instruction executingInstruction;

        public CPU(boolean render) {
            this.render = render;
        }

        private void tick() {
            if (DEBUG) {
                System.out.println("[CC: " + cycleCount + "] [RX: " + registerX + "] Executing instruction: " + executingInstruction);
            }
            if (render) {
                render();
            }
            this.cycleCount += 1;
            if (executingInstruction != null) {
                String type = executingInstruction.getType();
                if (cycleCount - executingInstruction.getStartCycle() >= CYCLES_PER_INSTRUCTION.get(type)) {
                    if (type.equals("addx"))  {
                        registerX += executingInstruction.getValue();
                    }
                    executingInstruction = null;
                }
            }
        }

        private void render() {
            if ((cycleCount - 1) % 40 == 0) {
                System.out.println();
            }
            int renderPosition = cycleCount % 40 - 1;
            if (registerX >= renderPosition - 1 && registerX <= renderPosition + 1) {
                System.out.print("#");
            } else {
                System.out.print(".");
            }
        }

        private void enqueue(Instruction instruction) {
            instruction.setStartCycle(cycleCount);
            executingInstruction = instruction;
        }

        public boolean hasInstruction() {
            return executingInstruction != null;
        }

        public int getCycleCount() {
            return cycleCount;
        }

        public int getRegisterX() {
            return registerX;
        }
    }

    private List<String> input;

    @Override
    public void setup() throws Exception {
        input = readInputForDay(10);
    }

    private List<Instruction> parseInput() {
        List<Instruction> instructions = new ArrayList<>();
        for (String str : input) {
            String[] parts = str.split(" ");
            String insn = parts[0];
            int amount = 0;
            if (parts.length > 1) {
                amount = Integer.parseInt(parts[1]);
            }
            Instruction instruction = new Instruction(insn, amount);
            instructions.add(instruction);
        }
        return instructions;
    }

    @Override
    public Object solvePartOne() throws Exception {
        CPU cpu = new CPU(false);
        List<Instruction> instructions = parseInput();
        int count = 0;
        for (Instruction instruction : instructions) {
            cpu.enqueue(instruction);
            while (cpu.hasInstruction()) {
                cpu.tick();
                if ((cpu.getCycleCount() + 20) % 40 == 0) {
                    count += cpu.getCycleCount() * cpu.getRegisterX();
                }
            }

        }
        return count;
    }

    @Override
    public Object solvePartTwo() throws Exception {
        CPU cpu = new CPU(true);
        List<Instruction> instructions = parseInput();
        for (Instruction instruction : instructions) {
            cpu.enqueue(instruction);
            while (cpu.hasInstruction()) {
                cpu.tick();
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        new DayTen().execute();
    }
}
