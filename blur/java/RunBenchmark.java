package blur.java;

import java.util.Map;

class RunBenchmark {
    public static void main(String[] args) {
        int kernelRadius;

        try {
            kernelRadius = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Error: kernelRadius must be an integer.");
            System.exit(1);
            return;
        }

        GaussianBlurBenchmark gaussian = new GaussianBlurBenchmark(kernelRadius);
        Map<String, Long> res = gaussian.blur("blur/sample.jpg");
        printResult(res);
    }

    private static void printResult(Map<String, Long> res) {
        System.out.printf("\n%-20s | %-10s%n", "Stage", "Time (s)");
        System.out.println("---------------------|-----------");
        for (Map.Entry<String, Long> entry : res.entrySet()) {
            System.out.printf("%-20s | %-10.3f%n", entry.getKey(), entry.getValue() / 1000.0);
        }
    }
}
