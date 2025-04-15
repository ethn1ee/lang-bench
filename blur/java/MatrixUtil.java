package blur.java;

class MatrixUtil {
    public static int[] flattenArray(int[][] arr) {
        if (arr.length == 0)
            throw new IllegalArgumentException("Array cannot be empty!");

        int height = arr.length;
        int width = arr[0].length;

        int[] flattened = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                flattened[width * y + x] = arr[y][x];
            }
        }

        return flattened;
    }

    public static int[][] unflattenArray(int[] arr, int width, int height) {
        if (arr.length != width * height)
            throw new IllegalArgumentException("Given dimension does not match the array!");

        int[][] unflattened = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                unflattened[y][x] = arr[width * y + x];
            }
        }

        return unflattened;
    }

    public static double[][] transpose(double[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;
        double[][] transposed = new double[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed[j][i] = mat[i][j];
            }
        }

        return transposed;
    }
}
