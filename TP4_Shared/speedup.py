"""
Speedup module for Monte Carlo simulation
"""

import matplotlib.pyplot as plt
import subprocess
import os


def compile_java_file(java_file):
    abs_path = os.path.abspath(java_file)
    dir_path = os.path.dirname(abs_path)
    root_dir = os.path.dirname(os.path.dirname(dir_path))  # Go up to TP4_Shared

    try:
        print(f"Compiling {abs_path}...")
        compile_result = subprocess.run(
            ["javac", "-d", root_dir, abs_path],  # Compile to root directory
            capture_output=True,
            text=True,
        )
        if compile_result.returncode != 0:
            raise Exception(f"Compilation error: {compile_result.stderr}")

    except FileNotFoundError:
        print("Error: Java compiler not found. Is Java installed?")
    except Exception as e:
        print(f"Error: {str(e)}")


def run_java_file(java_file, n, processus):
    # if not os.path.exists(java_file):
    #     raise FileNotFoundError(f"Java file not found: {java_file}")

    if not isinstance(n, str) or not isinstance(processus, str):
        n, processus = str(n), str(processus)

    # Setup paths
    current_dir = os.getcwd()
    parent_dir = os.path.dirname(current_dir)
    main_class = "TP4_Shared."
    if java_file == "Pi":
        main_class += "Pi.Pi"
    elif java_file == "Assignment102":
        main_class += "Assignment.Assignment102"
    # main_class = "TP4_Shared." + java_file + "." + java_file  # Full package path

    try:
        print(f"Running {main_class} with n={n}, processus={processus}")
        result = subprocess.run(
            ["java", "-cp", parent_dir, main_class, str(n), str(processus)],
            capture_output=True,
            text=True,
            cwd=current_dir,
        )

        if result.returncode != 0:
            raise Exception(f"Execution failed: {result.stderr}")

        return result.stdout if result.stdout else None

    except Exception as e:
        print(f"Error running Java program: {str(e)}")
        return None


class Result:
    """Class of result"""

    err: float = 0.0
    tot: int = 0
    processors: int = 0
    time: int = 0


def read_file(file_path: str) -> list:
    """Read a file and return a list of Result objects

    Args:
        file_path (str): data path

    Returns:
        list: list of Result
    """
    list_results = []
    with open(file_path, "r", encoding="utf-8") as file:
        lines = file.readlines()
        for line in lines:
            parts = line.split(" ")
            result = Result()
            result.err = float(parts[0].strip())
            result.tot = int(parts[1].strip())
            result.processors = int(parts[2].strip())
            result.time = int(parts[3].strip())
            list_results.append(result)
    return list_results


def calculate_speedup(resultsPi):
    # Plot results
    plt.figure(figsize=(12, 12))
    plt.plot([1, 12], [1, 12], "r--", label="Perfect Speedup")

    for total in resultsPi.keys():
        # Group by processors
        processor_groups = {}
        for result in resultsPi[total]:
            if result.processors not in processor_groups:
                processor_groups[result.processors] = []
            processor_groups[result.processors].append(result)

        # Calculate average times and speedups
        processors = sorted(processor_groups.keys())
        avg_speedups = []

        # Get T1 (sequential time with 1 processor)
        T1_results = processor_groups[1]
        T1 = sum(r.time for r in T1_results) / len(T1_results)

        for p in processors:
            results = processor_groups[p]
            Tp = sum(r.time for r in results) / len(results)
            speedup = T1 / Tp
            avg_speedups.append(speedup)

        plt.plot(
            processors,
            avg_speedups,
            marker="o",
            linestyle="-",
            label="Speedup " + str(total),
        )

    plt.xlabel("Processors")
    plt.ylabel("Sp")
    plt.title("Average Speedup by Number of Processors")
    plt.legend()
    # Set integer ticks
    plt.xticks(range(1, 13))
    plt.yticks(range(1, 13))

    # Force aspect ratio and starting point
    plt.axis("equal")
    plt.axis([0.5, 12.5, 0.5, 12.5])

    plt.grid(True)
    plt.show()


if __name__ == "__main__":
    resultsPi = dict()
    compile_java_file("Pi/Pi.java")
    for total in [120000000, 1200000, 1200000000]:
        file = "data\\out_Pi_G26_4c_" + str(total) + ".txt"
        if os.path.exists(file):
            os.remove(file)
        for process in [1, 2, 3, 4, 5, 6, 8, 10, 12]:
            for i in range(10):
                run_java_file("Pi", int(total / process), process)

        resultsPi[total] = read_file(file)

    resultsAssigment = dict()
    compile_java_file("Assignment/Assignment102.java")
    for total in [120000000, 1200000, 1200000000]:
        file = "data\\out_Assignment102_G26_4c_" + str(total) + ".txt"
        if os.path.exists(file):
            os.remove(file)
        for process in [1, 2, 3, 4, 5, 6, 8, 10, 12]:
            for i in range(10):
                run_java_file("Assignment102", total, process)

        resultsAssigment[total] = read_file(file)

    calculate_speedup(resultsPi)
    calculate_speedup(resultsAssigment)
