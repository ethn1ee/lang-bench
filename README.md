# Lang Bench

This is a multi-language benchmarking suite for evaluating the performance of various programming languages. It can also be used to benchmark performances of different machines. This project is WIP, and currently includes benchmarks in the following languages:

- Java

I plan to add more languages in the future, including C++, Go, JavaScript, Rust, Zig, and Python.

## How to Run

To run the benchmarks, execute the following command:

```bash
bash run.sh
```

Then you will be prompted with a menu to select the benchmark you want to run. The available benchmarks are:

1. Blur

You can type the corresponding number to run the benchmark.

### 1. Blur

The Blur benchmark is a simple program that blurs an image using a Gaussian filter. The benchmark measures the time taken to process the image and the memory usage during the operation.
Upon selecting the blur option, you will be prompted to enter the following parameters:

- **Kernel radius**
    The radius of the Gaussian kernel used for blurring. A larger radius will result in a more blurred image, and will take longer. A radius of 0 will result in no blur.
