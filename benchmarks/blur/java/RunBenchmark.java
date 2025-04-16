package benchmarks.blur.java;

class RunBenchmark {
    public static void main(String[] args) {
        int kernelRadius = Integer.parseInt(args[0]);
        String path = args[1];

        GaussianBlur gaussian = new GaussianBlur(kernelRadius);
        String result = gaussian.blur(path);
        System.out.println(result);
    }
}
