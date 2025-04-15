package blur.java;

import java.util.LinkedHashMap;
import java.util.Map;

class GaussianBlurBenchmark implements BlurBenchmark {
    int kernelRadius;
    int kernelSize;
    int width;
    int height;
    double[] kernel;

    long start;
    long end;
    long hStart;
    long hEnd;
    long vStart;
    long vEnd;

    public GaussianBlurBenchmark(int kernelRadius) {
        this.kernelRadius = kernelRadius;
        this.kernelSize = 2 * kernelRadius + 1;
    }

    private double[] createKernel() {
        double[] kernel = new double[kernelSize];
        double std = kernelSize / 6.0;

        for (int i = 0; i < kernelSize; i++) {
            int xFromCenter = i - kernelRadius;
            kernel[i] = 1 / (Math.sqrt(2 * Math.PI) * std) * Math.exp(-(xFromCenter * xFromCenter) / (2 * std * std));
        }

        this.kernel = kernel;

        return kernel;
    }

    private double[][] horizontalBlur(double[][] padded) {
        int padSize = kernelRadius;
        int paddedWidth = width + 2 * padSize * 3;
        int paddedHeight = height + 2 * padSize;
        double[][] hBlurred = new double[paddedHeight][paddedWidth];

        // Clone padded first to preserve paddings
        for (int y = 0; y < paddedHeight; y++) {
            for (int x = 0; x < paddedWidth; x++) {
                hBlurred[y][x] = padded[y][x];
            }
        }

        for (int y = 0; y < paddedHeight; y++) {
            for (int x = padSize * 3; x < width + padSize * 3; x += 3) {
                double r = 0, g = 0, b = 0;

                for (int i = 0; i < kernelSize; i++) {
                    r += kernel[i] * padded[y][x - kernelRadius * 3 + i * 3];
                    g += kernel[i] * padded[y][x - kernelRadius * 3 + i * 3 + 1];
                    b += kernel[i] * padded[y][x - kernelRadius * 3 + i * 3 + 2];
                }

                hBlurred[y][x] = r;
                hBlurred[y][x + 1] = g;
                hBlurred[y][x + 2] = b;
            }
            System.out.print("\rHorizontal blur: " + String.format("%.2f", 100.0 * y / paddedHeight) + "%");
        }
        System.out.print("\r                                   ");

        return hBlurred;
    }

    private double[][] verticalBlur(double[][] hBlurred) {
        int padSize = kernelRadius;
        double[][] blurred = new double[height][width]; // same size and type as original image

        for (int x = padSize * 3; x < width + padSize * 3; x++) {
            for (int y = padSize; y < padSize + height; y++) {
                double c = 0;

                for (int i = 0; i < kernelSize; i++) {
                    c += kernel[i] * hBlurred[y - kernelRadius + i][x];
                }

                blurred[y - padSize][x - padSize * 3] = c;
            }
            System.out.print("\rVertical blur: " + String.format("%.2f", 100.0 * (x - padSize * 3) / width) + "%");
        }
        System.out.print("\r                                   ");

        return blurred;
    }

    public Map<String, Long> blur(String path) {
        long start = System.currentTimeMillis();

        int padSize = kernelRadius;
        createKernel();

        int[][] img = ImageUtil.loadImage(path);
        double[][] padded = ImageUtil.padImage(img, padSize);

        this.height = img.length;
        this.width = img[0].length;

        this.hStart = System.currentTimeMillis();
        double[][] hBlurred = horizontalBlur(padded);
        this.hEnd = System.currentTimeMillis();

        this.vStart = System.currentTimeMillis();
        double[][] blurred = verticalBlur(hBlurred);
        this.vEnd = System.currentTimeMillis();

        String outputPath = "blur/output/java_gaussian.jpg";
        ImageUtil.saveImage(blurred, outputPath);

        long end = System.currentTimeMillis();

        Map<String, Long> result = new LinkedHashMap<>();
        result.put("Horizontal Blur", hEnd - hStart);
        result.put("Vertical Blur", vEnd - vStart);
        result.put("Total", end - start);
        return result;
    }

}