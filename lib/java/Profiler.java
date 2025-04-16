package lib.java;

import java.util.HashMap;
import java.util.Map;

public class Profiler {
    private static Runtime runtime = Runtime.getRuntime();

    private static Map<String, Process> processes = new HashMap<>();

    public Process createProcess(String id) {
        processes.put(id, new Process());
        return processes.get(id);
    }

    public void start(String id) {
        Process current = createProcess(id);
        runtime.gc();
        current.startMemory = runtime.totalMemory() - runtime.freeMemory();
        current.startTime = System.currentTimeMillis();
    }

    public void end(String id) {
        if (!processes.containsKey(id))
            throw new IllegalArgumentException("No process found with id: " + id);

        Process current = processes.get(id);
        runtime.gc();
        current.endMemory = runtime.totalMemory() - runtime.freeMemory();
        current.endTime = System.currentTimeMillis();
    }

    public String getTable() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%-20s | %-20s | %-20s\n", "Stage", "Memory (KB)", "Time (s)"));
        sb.append("---------------------|----------------------|---------------------\n");

        for (Map.Entry<String, Process> entry : processes.entrySet()) {
            Process current = entry.getValue();
            sb.append(String.format("%-20s | %-20.3f | %-20.3f\n", entry.getKey(), current.getMemoryUsed(),
                    current.getTimeElapsed()));
        }

        return sb.toString();
    }
}

class Process {
    public long startMemory = -1;
    public long endMemory = -1;
    public long startTime = -1;
    public long endTime = -1;

    public double getMemoryUsed() {
        if (this.endMemory == -1)
            throw new IllegalArgumentException("The process has not ended!");

        return (this.endMemory - this.startMemory) / 1000.0;
    }

    public double getTimeElapsed() {
        if (this.endTime == -1)
            throw new IllegalArgumentException("The process has not ended!");

        return (this.endTime - this.startTime) / 1000.0;
    }
}