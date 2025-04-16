#!/bin/bash
echo
echo -e "SELECT BENCHMARK TYPE:"
echo
echo -e "\033[36m1. blur"
# echo "2: FIBONACCI"

echo
echo -ne "\033[0mENTER YOUR CHOICE: \033[33m"
read choice

case $choice in
    1)
        benchmark_type="blur"
        path="benchmarks/$benchmark_type"

        echo
        echo -ne "\033[0mENTER KERNEL SIZE (e.g. 10, 50, 100): \033[33m"
        read kernel_size
        echo

        if ! [[ "$kernel_size" =~ ^[0-9]+$ ]]; then
            echo -e "\033[31mERROR: KERNEL SIZE MUST BE A NONNEGATIVE INTEGER.\033[0m"
            exit 1
        fi

        echo -e "\033[90mJAVA ===================================\033[0m\n"
        javac $path/java/*.java
        java $path/java/RunBenchmark $kernel_size $path
        rm -f $path/java/*.class
        echo

        echo -e "\033[90mPYTHON =================================\033[0m\n"
        ;;
    # 2)
    #     benchmark_type="fibonacci"
    #     ;;
    *)
        echo -e "\033[31mINVALID CHOICE. EXCITING.\033[0m"
        exit 1
        ;;
esac