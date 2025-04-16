class MatrixUtil:
    @staticmethod
    def flatten_array(arr: list[list[int]]) -> list[int]:
        """
        Flattens a 2D array into a 1D array.
        Args:
            arr: The 2D array to flatten.
        Returns:
            The flattened 1D array.
        """
        return [item for sublist in arr for item in sublist]

    @staticmethod
    def unflatten_array(arr: list[int], width: int, height: int) -> list[list[int]]:
        """
        Unflattens a 1D array into a 2D array with the given dimension
        Args:
            arr: 1D integer array to unflatten
            width: Desired width of the 2D array
            height: Desired height of the 2D array
        Returns:
            Unflattened array with the given dimensions
        Raises:
        Raises:
            ValueError: If the given dimensions do not match the array length. 
        """
        if len(arr) != width * height:
            raise ValueError("Given dimensions do not match the array!")

        unflattened: list[list[int]] = []

        for y in range(height):
            row = []
            for x in range(width):
                row.append(arr[width * y + x])
            unflattened.append(row)

        return unflattened
