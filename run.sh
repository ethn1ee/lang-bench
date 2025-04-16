#!/bin/bash

make_separator() {
    local label="$1"
    local width=$(tput cols)
    local sep="="
    local label_len=${#label}
    local sep_len=$((width - label_len - 1))
    if (( sep_len < 0 )); then sep_len=0; fi
    printf "\n\033[90m%s %s \n\033[0m" "$label" "$(printf '%*s' "$sep_len" | tr ' ' "$sep")"
}

alert_language_not_installed() {
    local language="$1"
    echo -e "\033[31m$language NOT INSTALLED. SKIPPING $language BENCHMARK.\033[0m"
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
        echo -ne "\033[0mENTER KERNEL RADIUS (e.g. 10, 50, 100): \033[33m"
        read kernel_radius

        if ! [[ "$kernel_radius" =~ ^[0-9]+$ ]]; then
            echo -e "\033[31mERROR: KERNEL SIZE MUST BE A NONNEGATIVE INTEGER.\033[0m"
            exit 1
        fi

        # Java
        if command -v javac >/dev/null 2>&1 && command -v java >/dev/null 2>&1; then
            make_separator "JAVA"
            javac $path/java/*.java
            java $path/java/RunBenchmark $kernel_radius $path
            rm -f $path/java/*.class
            rm -f lib/java/*.class
        else
            alert_language_not_installed "JAVA"
        fi

        # Python
        if command -v python3 > /dev/null 2>&1; then
            make_separator "PYTHON"
            python3 -m benchmarks.blur.python.run_benchmark --kernel_radius=$kernel_radius --path=$path
        else
            alert_language_not_installed "PYTHON"
        fi
        ;;

        # JavaScript
    2)
        benchmark_type="fibonacci"
        ;;
    *)
        echo -e "\033[31mINVALID CHOICE. EXCITING.\033[0m"
        exit 1
        ;;
esac