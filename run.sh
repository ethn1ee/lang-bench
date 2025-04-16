#!/bin/bash

make_separator() {
    local label="$1"
    local width=$(tput cols)
    local sep="="
    local label_len=${#label}
    local sep_len=$((width - label_len - 1))
    if (( sep_len < 0 )); then sep_len=0; fi
    printf "\033[90m%s %s\033[0m" "$label" "$(printf '%*s' "$sep_len" | tr ' ' "$sep")"
}

# Benchmark selector
echo -e "\n\033[0mAVAILABLE BENCHMARKS"
echo -e "\033[36m"
echo "1: BLUR"
echo "2: FIBONACCI"
echo -e "\033[0m"

echo -ne "\033[0mENTER YOUR CHOICE: \033[33m"
read choice

case $choice in
    1)
        echo -e "\033[32m=> SELECTED BLUR"

        benchmark_type="blur"
        path="benchmarks/$benchmark_type"

        # Kernel radius input
        echo
        echo -ne "\033[0mENTER KERNEL SIZE (e.g. 10, 50, 100): \033[33m"
        read kernel_size

        if ! [[ "$kernel_size" =~ ^[0-9]+$ ]]; then
            echo -e "\033[31mERROR: KERNEL SIZE MUST BE A NONNEGATIVE INTEGER.\033[0m"
            exit 1
        fi

        # Java
        echo -e "\n$(make_separator "JAVA")\n"
        javac $path/java/*.java
        java $path/java/RunBenchmark $kernel_size $path
        rm -f $path/java/*.class
        rm -f benchmarks/lib/java/*.class

        # Python
        echo -e "\n$(make_separator "PYTHON")\n"
        ;;
    2)
        benchmark_type="fibonacci"
        ;;
    *)
        echo -e "\033[31mINVALID CHOICE. EXCITING.\033[0m"
        exit 1
        ;;
esac