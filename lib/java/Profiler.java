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

    /**
     * Creates a new process with the given unique ID.
     * 
     * @param id ID of the new process. Has to be unique.
     */
    public void start(String id) {
        if (processes.containsKey(id))
            throw new IllegalArgumentException("The ID " + id + " already exists.");
        Process current = createProcess(id);
        runtime.gc();
        current.startMemory = runtime.totalMemory() - runtime.freeMemory();
        current.startTime = System.currentTimeMillis();
    }

    /**
     * Ends the process with the given ID.
     * 
     * @param id ID of the new process. Has to exist among the running processes.
     */
    public void end(String id) {
        if (!processes.containsKey(id))
            throw new IllegalArgumentException("No process found with id: " + id);

        Process current = processes.get(id);
        runtime.gc();
        current.endMemory = runtime.totalMemory() - runtime.freeMemory();
        current.endTime = System.currentTimeMillis();
    }

    /**
     * Formats the performance of all processes into a table.
     * 
     * @return String of the table
     */
    public String getTable() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%-20s | %-20s | %-20s\n", "Stage", "Memory (KB)", "Time (s)"));
        sb.append("---------------------|----------------------|---------------------\n");

        for (Map.Entry<String, Process> entry : processes.entrySet()) {
            Process current = entry.getValue();
            sb.append(String.format("%-20s | %-20.3f | %-20.3f\n", entry.getKey(), current.getMemoryUsed(),
                    current.getTimeElapsed()));
        }

        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }
}

class Process {
    public long startMemory = -1;
    public long endMemory = -1;
    public long startTime = -1;
    public long endTime = -1;

    /**
     * @return the memory used by the process with the given ID
     * @throws IllegalArgumentException if the processes has not ended
     */
    public double getMemoryUsed() {
        if (this.endMemory == -1)
            throw new IllegalArgumentException("The process has not ended!");

        return (this.endMemory - this.startMemory) / 1000.0;
    }

    /**
     * @return the time taken by the process with the given ID
     * @throws IllegalArgumentException if the process has not ended
     */
    public double getTimeElapsed() {
        if (this.endTime == -1)
            throw new IllegalArgumentException("The process has not ended!");

        return (this.endTime - this.startTime) / 1000.0;
    }
}