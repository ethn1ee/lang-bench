from PIL import Image
import numpy as np
from matrix_util import MatrixUtil


class ImageUtil:
    @staticmethod
    def load_image(path: str) -> list[list[int]]:
        """
        Returns a 2D integer Array extracted from the given image

        Args:
            path: Path to image, relative from the root
        Returns:
            img: 2D integer array with each pixel represented with 3 numbers for RGB channels
        """

        print("Loading image...", end="", flush=True)

        img: list[list[int]] = []

        with Image.open(path) as im:
            pixels = np.array(im).flatten()
            img = MatrixUtil.unflatten_array(
                pixels, im.width * 3, im.height)

        print("\r\033[K", end="")

        return img

    @staticmethod
    def pad_image(img: list[list[int]], pad_size: int) -> list[list[float]]:
        """
        Add reflection padding to image and also convert into a 2D float array

        Args:
            img: 2D integer array of an image
            pad_size: Desired size of the padding
        Returns:
            padded: Padded image converted to a 2D float array with width and height increased by 2 * padSize
        """

        print("Padding image...", end="", flush=True)

        height = len(img)
        width = len(img[0])
        padded_width = width + 2 * pad_size * 3
        padded_height = height + 2 * pad_size

        padded: list[list[float]] = [
            [0.0 for _ in range(padded_width)] for _ in range(padded_height)]

        # Copy original image to center
        for y in range(height):
            for x in range(width):
                padded[pad_size + y][pad_size * 3 + x] = img[y][x]

        # Fill horizontal paddings using reflection (exclude corners)
        for y in range(pad_size):
            for x in range(width):
                # Top padding
                padded[y][pad_size * 3 + x] = img[pad_size - y][x]
                # Bottom padding
                padded[pad_size + height + y][pad_size *
                                              3 + x] = img[height - 1 - y][x]

        # Fill vertical paddings (with corners)
        for y in range(padded_height):
            for x in range(0, pad_size * 3, 3):
                # Left padding
                padded[y][x] = padded[y][(pad_size + pad_size) * 3 - x]
                padded[y][x + 1] = padded[y][(pad_size + pad_size) * 3 - x + 1]
                padded[y][x + 2] = padded[y][(pad_size + pad_size) * 3 - x + 2]

                # Right padding
                padded[y][x + width + pad_size *
                          3] = padded[y][width + pad_size * 3 - x - 3]
                padded[y][x + width + pad_size * 3 +
                          1] = padded[y][width + pad_size * 3 - x - 2]
                padded[y][x + width + pad_size * 3 +
                          2] = padded[y][width + pad_size * 3 - x - 1]

        print("\r\033[K", end="")

        return padded

    def save_image(img: list[list[float]], output_path: str):
        """
        Convert 2D float array into a jpg image

        Args:
            img: 2D float array of an image
            output_path: desired path of the output jpg file
        """

        print("Saving image...", end="", flush=True)

        height = len(img)
        width = len(img[0]) // 3

        img_3d = np.array(img, dtype=np.float32).reshape((height, width, 3))
        img_3d = np.clip(img_3d, 0, 255).astype(np.uint8)

        im = Image.fromarray(img_3d, mode="RGB")
        im.save(output_path, format="JPEG")

        print("\r\033[K", end="")
