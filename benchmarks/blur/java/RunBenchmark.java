package benchmarks.blur.java;

class RunBenchmark {
    public static void main(String[] args) {
        int kernelRadius = Integer.parseInt(args[0]);
        String path = args[1]; // path to the blur benchmark folder relative to root
        String inputPath = path + "/input.jpg";
        String outputPath = path + "/output/java_gaussian.jpg";

        GaussianBlur gaussian = new GaussianBlur(kernelRadius);
        String result = gaussian.blur(inputPath, outputPath);
        System.out.println(result);
    }
}
