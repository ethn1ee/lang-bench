package blur.java;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class ImageUtil {
    public static int[][] loadImage(String path) {
        // System.out.println("Loading image...");

        int img[][] = null;

        try {
            BufferedImage bi = ImageIO.read(new File(path));
            Raster r = bi.getData();
            int width = r.getWidth();
            int height = r.getHeight();
            int[] pixels = r.getPixels(0, 0, width, height, (int[]) null);
            img = MatrixUtil.unflattenArray(pixels, width * 3, height); // width * 3 to account for rgb channel for each
                                                                        // pixel
        } catch (IOException e) {
            System.err.println(e);
        }

        return img;
    }

    public static double[][] padImage(int[][] img, int padSize) {
        // System.out.println("Padding image...");

        int height = img.length;
        int width = img[0].length;
        int paddedWidth = width + 2 * padSize * 3;
        int paddedHeight = height + 2 * padSize;
        double[][] padded = new double[paddedHeight][paddedWidth];

        // Copy original image to center
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                padded[y + padSize][x + padSize * 3] = img[y][x];
            }
        }

        // Fill horizontal paddings using reflection (exclude corners)
        for (int y = 0; y < padSize; y++) {
            for (int x = 0; x < width; x++) {
                // Top padding
                padded[y][x + padSize * 3] = img[padSize - y][x];
                // Bottom padding
                padded[y + height + padSize][x + padSize * 3] = img[height - 1 - y][x];
            }
        }

        // Fill vertical paddings (with corners)
        for (int y = 0; y < paddedHeight; y++) {
            for (int x = 0; x < padSize * 3; x += 3) {
                // Left padding
                padded[y][x] = padded[y][(padSize + padSize) * 3 - x];
                padded[y][x + 1] = padded[y][(padSize + padSize) * 3 - x + 1];
                padded[y][x + 2] = padded[y][(padSize + padSize) * 3 - x + 2];

                // Right padding
                padded[y][x + width + padSize * 3] = padded[y][width + (padSize) * 3 - x - 3];
                padded[y][x + width + padSize * 3 + 1] = padded[y][width + padSize * 3 - x - 2];
                padded[y][x + width + padSize * 3 + 2] = padded[y][width + padSize * 3 - x - 1];
            }
        }

        return padded;
    }

    public static String suffixPath(String path, String suffix) {
        int lastDotIndex = path.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return path + suffix;
        }

        return path.substring(0, lastDotIndex) + "_" + suffix + path.substring(lastDotIndex);
    }

    public static BufferedImage saveImage(double[][] img, String outputPath) {
        // System.out.println("Saving image...");

        int height = img.length;
        int width = img[0].length / 3;

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = (int) Math.round(img[y][3 * x]);
                int g = (int) Math.round(img[y][3 * x + 1]);
                int b = (int) Math.round(img[y][3 * x + 2]);

                // Ensure values are in valid range
                r = Math.min(255, Math.max(0, r));
                g = Math.min(255, Math.max(0, g));
                b = Math.min(255, Math.max(0, b));

                // Combine into RGB pixel
                int rgb = (r << 16) | (g << 8) | b;

                // Set the pixel in the image
                bi.setRGB(x, y, rgb);
            }
        }

        try {
            File outputFile = new File(outputPath);
            ImageIO.write(bi, "jpg", outputFile);
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
        }

        return bi;
    }
}
