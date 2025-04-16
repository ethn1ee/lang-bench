package benchmarks.blur.java;

import lib.java.Profiler;

class GaussianBlur {
    int kernelRadius;
    int kernelSize;
    double[] kernel;

    int width;
    int height;

    public GaussianBlur(int kernelRadius) {
        this.kernelRadius = kernelRadius;
        this.kernelSize = 2 * kernelRadius + 1;
    }

    /**
     * Create a 1D Gaussian kernel with kernelRadius
     * 
     * @return A 1D double array of the kernel
     */
    private double[] createKernel() {
        this.kernel = new double[kernelSize];
        double std = this.kernelSize / 6.0;

        for (int i = 0; i < kernelSize; i++) {
            int xFromCenter = i - this.kernelRadius;
            kernel[i] = 1 / (Math.sqrt(2 * Math.PI) * std) * Math.exp(-(xFromCenter * xFromCenter) / (2 * std * std));
        }

        return kernel;
    }

    /**
     * Apply blur horizontally
     * 
     * @param padded Padded image
     * @return Resulting image array after blurring horizontally
     */
    private double[][] horizontalBlur(double[][] padded) {
        int padSize = this.kernelRadius;
        int paddedHeight = padded.length;
        int paddedWidth = padded[0].length;

        double[][] hBlurred = new double[paddedHeight][paddedWidth];

        // Clone padded first to preserve paddings
        for (int y = 0; y < paddedHeight; y++) {
            for (int x = 0; x < paddedWidth; x++) {
                hBlurred[y][x] = padded[y][x];
            }
        }

        for (int y = 0; y < paddedHeight; y++) {
            for (int x = padSize * 3; x < paddedWidth - padSize * 3; x += 3) {
                double r = 0, g = 0, b = 0;

                for (int i = 0; i < this.kernelSize; i++) {
                    r += this.kernel[i] * padded[y][x - this.kernelRadius * 3 + i * 3];
                    g += this.kernel[i] * padded[y][x - this.kernelRadius * 3 + i * 3 + 1];
                    b += this.kernel[i] * padded[y][x - this.kernelRadius * 3 + i * 3 + 2];
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
     * @param hBlurred Horizontally blurred image (therefore should run horizontalBlur first)
     * @return Resulting image array after blurring
     */
    private double[][] verticalBlur(double[][] hBlurred) {
        int padSize = this.kernelRadius;
        int paddedHeight = hBlurred.length;
        int paddedWidth = hBlurred[0].length;

        double[][] blurred = new double[height][width]; // same size and type as original image

        for (int x = padSize * 3; x < paddedWidth - padSize * 3; x++) {
            for (int y = padSize; y < paddedHeight - padSize; y++) {
                double c = 0;

                for (int i = 0; i < this.kernelSize; i++) {
                    c += this.kernel[i] * hBlurred[y - this.kernelRadius + i][x];
                }

                blurred[y - padSize][x - padSize * 3] = c;
            }
            System.out.print("\rVertical blur: " + String.format("%.2f", 100.0 * (x - padSize * 3) / this.width) + "%");
        }
        System.out.print("\r\033[K");

        return blurred;
    }

    /**
     * Performs Gaussian blur over the given image with given kernal radius. Using
     * Gaussian blur's separability, this method runs horizontal blur first and
     * applices vertical blur later, which is more efficient thab applying a 2D
     * Gaussian kernel.
     * 
     * @param inputPath Path to the input image relative to root
     * @param outputPath Desired path of the output image relative to root
     * @return A table (string) of memory used and elapsed time for horizontal blur, vertical blur, and
     *         total time elapsed
     */
    public String blur(String inputPath, String outputPath) {
        Profiler profiler = new Profiler();
        profiler.start("Total");

        int padSize = kernelRadius;
        createKernel();

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

        ImageUtil.saveImage(blurred, outputPath);

        profiler.end("Total");

        return profiler.getTable();
    }

}