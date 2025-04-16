package benchmarks.blur.java;

import java.util.LinkedHashMap;
import java.util.Map;
import lib.java.Profiler;

class GaussianBlur {
    int kernelRadius;
    int kernelSize;
    int width;
    int height;
    double[] kernel;

    Map<String, Double> performance;

    public GaussianBlur(int kernelRadius) {
        this.kernelRadius = kernelRadius;
        this.kernelSize = 2 * kernelRadius + 1;
        this.performance = new LinkedHashMap<>();
    }

    /**
     * Create a 1D Gaussian kernel with kernelRadius
     * 
     * @return A 1D double array of the kernel
     */
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

    /**
     * Apply blur horizontally
     * 
     * @param padded Padded image
     * @return Resulting image array after blurring
     */
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
        System.out.print("\r\033[K");

        return hBlurred;
    }

    /**
     * Apply blur vertically
     * 
     * @param hBlurred Horizontally blurred image (should run horizontalBlur first)
     * @return Resulting image array after blurring
     */
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
        System.out.print("\r\033[K");

        return blurred;
    }

    /**
     * Performs Gaussian blur over the given image with given kernal radius. Using
     * Gaussian blur's separability, this method runs horizontal blur first and
     * applices vertical blur later, which is more efficient that applying a 2D
     * gaussian kernel.
     * 
     * @param path Path to the input image, relative from root
     * @return A dictionary of elapsed time for horizontal blur, vertical blur, and
     *         total time elapsed
     */
    public String blur(String path) {
        Profiler profiler = new Profiler();
        profiler.start("Total");

        int padSize = kernelRadius;
        createKernel();

        String inputPath = path + "/input.jpg";
        int[][] img = ImageUtil.loadImage(inputPath);
        double[][] padded = ImageUtil.padImage(img, padSize);

        this.height = img.length;
        this.width = img[0].length;

        profiler.start("Horizontal Blur");
        double[][] hBlurred = horizontalBlur(padded);
        profiler.end("Horizontal Blur");

        profiler.start("Vertical Blur");
        double[][] blurred = verticalBlur(hBlurred);
        profiler.end("Vertical Blur");

        String outputPath = path + "/output/java_gaussian.jpg";
        ImageUtil.saveImage(blurred, outputPath);

        profiler.end("Total");

        return profiler.getTable();
    }

}