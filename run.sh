#!/bin/bash
echo
echo "SELECT BENCHMARK TYPE:"
echo
echo -e "\033[36m1. blur\033[0m"
# echo "2: FIBONACCI"

echo
read -p "ENTER YOUR CHOICE: " choice

case $choice in
    1)
        benchmark_type="blur"

        echo
        read -p "ENTER KERNEL SIZE (e.g. 10, 50, 100): " kernel_size
        echo

        javac blur/java/*.java
        java blur/java/RunBenchmark $kernel_size
        rm -f blur/java/*.class
        ;;
    # 2)
    #     benchmark_type="fibonacci"
    #     ;;
    *)
        echo "INVALID CHOICE. EXCITING."
        exit 1
        ;;
esac