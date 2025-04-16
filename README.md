# LangBench

This is a multi-language benchmarking suite for evaluating the performance of various programming languages. It can also be used to benchmark performances of different machines. This project is WIP, and I plan to add more languages in the future, including C++, Go, JavaScript, Rust, Zig, etc.

## Table of Contents

- [LangBench](#langbench)
  - [Table of Contents](#table-of-contents)
  - [Available Benchmarks \& Languages](#available-benchmarks--languages)
  - [How to Run](#how-to-run)
    - [1. Blur](#1-blur)
      - [Parameters](#parameters)
    - [2. Fibonacci](#2-fibonacci)
      - [Parameters](#parameters-1)

## Available Benchmarks & Languages

| Language | Blur | Fibonacci |
|----------|------|-----------|
| Java     |  ✅  |         |
| Python   |  ✅  |         |

## How to Run

To run the benchmarks, execute the following command:

```bash
bash run.sh
```

Then you will be prompted with a menu to select the benchmark you want to run. You can type the corresponding number to run the benchmark

---

### 1. Blur

The Blur benchmark is a simple program that blurs an image using a Gaussian filter. The benchmark measures the time taken to process the image and the memory usage during the operation.

#### Parameters

Upon selecting the blur option, you will be prompted to enter the following parameters:

- **Kernel radius**
    The radius of the Gaussian kernel used for blurring. A larger radius will result in a more blurred image, and will take longer. A radius of 0 will result in no blur.

### 2. Fibonacci

WIP

#### Parameters

WIP
