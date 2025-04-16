import argparse
from benchmarks.blur.python.gaussian_blur import GaussianBlur


class RunBenchmark:
    def main(self):
        parser = argparse.ArgumentParser()
        parser.add_argument("--kernel_radius",
                            type=int,
                            default=2,
                            help="radius of the Gaussian kernel")
        parser.add_argument("--path",
                            default="benchmarks/blur",
                            help="path to the blur benchmark folder relative to root")

        args = parser.parse_args()

        input_path = args.path + "/input.jpg"
        output_path = args.path + "/output/python_gaussian.jpg"

        gaussian = GaussianBlur(args.kernel_radius)
        result = gaussian.blur(input_path, output_path)
        print(result)


if __name__ == "__main__":
    RunBenchmark().main()
