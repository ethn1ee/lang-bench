package benchmarks.blur.java;

class MatrixUtil {
    /**
     * Flattens a 2D array into a 1D array.
     * 
     * @param arr 2D integer array
     * @return Flattened array
     */
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

    /**
     * Unflattens a 1D array into a 2D array with the given dimension.
     * 
     * @param arr    1D integer array to unflatten
     * @param width  Desired width of the 2D array
     * @param height Desired height of the 2D array
     * @return Unflattened array with the given dimensions
     * @throws IllegalArgumentException If the given dimensions do not match the
     *                                  array length
     */
    public static int[][] unflattenArray(int[] arr, int width, int height) {
        if (arr.length != width * height)
            throw new IllegalArgumentException("Given dimensions do not match the array!");

        int[][] unflattened = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                unflattened[y][x] = arr[width * y + x];
            }
        }

        return unflattened;
    }

}
