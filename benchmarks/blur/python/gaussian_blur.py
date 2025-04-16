from typing import Optional
import math
from lib.python.profiler import Profiler
from benchmarks.blur.python.image_util import ImageUtil


class GaussianBlur:
    def __init__(self, kernel_radius: int):
        self.kernel_radius: int = kernel_radius
        self.kernel_size: int = 2 * kernel_radius + 1
        self.kernel: Optional[list[float]] = None

        self.width: Optional[int] = None
        self.height: Optional[int] = None

    def _create_kernel(self) -> list[float]:
        """
        Create a 1D Gaussian kernel with kernel_radius

        Returns:
            kernel: A 1D float array of the kernel
        """

        self.kernel = [0.0] * self.kernel_size
        std = self.kernel_size / 6

        self.kernel = [1 / (math.sqrt(2 * math.pi) * std) *
                       math.exp(-(i - self.kernel_radius)**2 / (2 * std**2)) for i in range(self.kernel_size)]

        return self.kernel

    def _horizontal_blur(self, padded: list[list[float]]) -> list[list[float]]:
        """
        Apply blur horizontally

        Args:
            padded: Padded image
        Returns:
            h_blurred: Resulting image array after blurring horizontally
        """

        pad_size = self.kernel_radius
        padded_height = len(padded)
        padded_width = len(padded[0])

        # clone padded first to preserve paddings
        h_blurred = [row[:] for row in padded]

        for y in range(padded_height):
            for x in range(pad_size * 3, padded_width - pad_size * 3, 3):
                r = 0
                g = 0
                b = 0

                for i in range(self.kernel_size):
                    r = r + self.kernel[i] * padded[y][x -
                                                       self.kernel_radius * 3 + i * 3]
                    g = g + self.kernel[i] * padded[y][x -
                                                       self.kernel_radius * 3 + i * 3 + 1]
                    b = b + self.kernel[i] * padded[y][x -
                                                       self.kernel_radius * 3 + i * 3 + 2]

                h_blurred[y][x] = r
                h_blurred[y][x + 1] = g
                h_blurred[y][x + 2] = b

            print(
                f"\rHorizontal blur: {100 * y / padded_height:.2f}%", end="", flush=True)

        print("\r\033[k", end="", flush=True)

        return h_blurred

    def _vertical_blur(self, h_blurred: list[list[float]]) -> list[list[float]]:
        """
        Apply blur vertically

        Args:
            h_blurred Horizontally blurred image (therefore should run horizontal_blur first)
        Returns:
            blurred: Resulting image array after blurring
        """

        pad_size = self.kernel_radius
        padded_height = len(h_blurred)
        padded_width = len(h_blurred[0])

        blurred = [[0.0] * self.width] * self.height

        for x in range(pad_size * 3, padded_width - pad_size * 3):
            for y in range(pad_size, padded_height - pad_size):
                c = sum(self.kernel[i] * h_blurred[y - self.kernel_radius + i][x]
                        for i in range(self.kernel_size))

                blurred[y - pad_size][x-pad_size * 3] = c

            print(
                f"\rVertical blur: {100 * (x - pad_size * 3) / self.width:.2f}%", end="", flush=True)

        print("\r\033[k", end="", flush=True)

        return blurred

    def blur(self, input_path: str, output_path: str) -> str:
        """
        Performs Gaussian blur over the given image with given kernal radius. Using Gaussian blur's separability, this method runs horizontal blur first and applies vertical blur later, which is more efficient than applying a 2D Gaussian kernel.

        Args:
            input_path: Path to the input image relative to root
            output_path: Desired path of the output image relative to root.
        Returns:
            A table (string) of memory used and elapsed time for horizontal blur, vertical blur, and total time elapsed
        """

        profiler = Profiler()
        profiler.start("Total")

        pad_size = self.kernel_radius
        self._create_kernel()

        img = ImageUtil.load_image(input_path)
        padded = ImageUtil.pad_image(img, pad_size)

        self.height = len(img)
        self.width = len(img[0])

        profiler.start("Horizontal blur")
        h_blurred = self._horizontal_blur(padded)
        profiler.end("Horizontal blur")

        profiler.start("Vertical blur")
        blurred = self._vertical_blur(h_blurred)
        profiler.end("Vertical blur")

        ImageUtil.save_image(blurred, output_path)

        profiler.end("Total")

        return profiler.get_table()
