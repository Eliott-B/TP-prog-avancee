"""
Speedup module for Monte Carlo simulation
"""

import matplotlib.pyplot as plt
import subprocess
import os

import numpy as np


def compile_java_file(java_file: str):
    """Compile a Java file

    Args:
        java_file (str): Path to the Java file

    Raises:
        Exception: Compilation error
    """
    abs_path = os.path.abspath(java_file)
    dir_path = os.path.dirname(abs_path)
    root_dir = os.path.dirname(os.path.dirname(dir_path))

    try:
        print(f"Compiling {abs_path}...")
        compile_result = subprocess.run(
            [
                "javac",
                "-cp",
                root_dir,
                "-d",
                root_dir,
                abs_path,
            ],
            capture_output=True,
            text=True,
        )
        if compile_result.returncode != 0:
            raise Exception(f"Compilation error: {compile_result.stderr}")

    except FileNotFoundError:
        print("Error: Java compiler not found. Is Java installed?")
    except Exception as e:
        print(f"Error: {str(e)}")


def run_java_file(java_file: str, n: int, processus: int, fileSubName: str) -> str:
    """Run a Java file

    Args:
        java_file (str): Path to the Java file
        n (int): Number of iterations
        processus (int): Number of processors
        fileSubName (str): Subname of the output file

    Raises:
        Exception: Execution error

    Returns:
        str: Output of the Java program
    """
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
            [
                "java",
                "-cp",
                parent_dir,
                main_class,
                str(n),
                str(processus),
                fileSubName,
            ],
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


def calculate_speedup(resultsPi: dict) -> plt:
    """Calculate strong scaling efficiency and plot it

    Args:
        resultsPi (dict): Results of the Pi simulation

    Returns:
        plt: Matplotlib plot
    """
    plt.figure(figsize=(12, 12))
    plt.plot([1, 12], [1, 12], "r--", label="Perfect Speedup")

    for total in resultsPi.keys():
        processor_groups = {}
        for result in resultsPi[total]:
            if result.processors not in processor_groups:
                processor_groups[result.processors] = []
            processor_groups[result.processors].append(result)

        processors = sorted(processor_groups.keys())
        avg_speedups = []

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
            label=f"Speedup $10^{{{int(np.log10(total))}}}$",
        )

    plt.xlabel("Processors")
    plt.ylabel("Sp")
    plt.title("Average Speedup by Number of Processors")
    plt.legend()

    plt.xticks(range(1, 13))
    plt.yticks(range(1, 13))

    plt.axis("equal")
    plt.axis([0.5, 12.5, 0.5, 12.5])

    plt.grid(True)
    return plt


def calculate_weak_speedup(resultsPi: dict) -> plt:
    """Calculate weak scaling efficiency and plot it

    Args:
        resultsPi (dict): Results of the Pi simulation

    Returns:
        plt: Matplotlib plot
    """
    plt.figure(figsize=(12, 6))
    plt.plot([1, 12], [1, 1], "r--", label="Perfect Efficiency")

    for total in resultsPi.keys():
        processor_groups = {}
        for result in resultsPi[total]:
            if result.processors not in processor_groups:
                processor_groups[result.processors] = []
            processor_groups[result.processors].append(result)

        processors = sorted(processor_groups.keys())
        avg_speedups = []

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
            label=f"Speedup $10^{{{int(np.log10(total))}}}$",
        )

    plt.xlabel("Processors")
    plt.ylabel("Sp")
    plt.title("Weak Scaling Efficiency")
    plt.legend()

    plt.yticks(range(0, 7))
    plt.xticks(range(0, 13))

    plt.axis("equal")
    plt.axis([0, 12.5, 0, 6.5])

    plt.grid(True)
    return plt


def draw_error(resultsPi: dict) -> plt:
    """Draw error distribution by number of iterations

    Args:
        resultsPi (dict): Results of the Pi simulation

    Returns:
        plt: Matplotlib plot
    """
    plt.figure(figsize=(12, 12))

    for total in resultsPi.keys():
        iterations = []
        errors = []
        for result in resultsPi[total]:
            iterations.append(result.tot)
            errors.append(result.err)

        plt.scatter(
            iterations,
            errors,
            alpha=0.5,
            label=f"Error p=$10^{{{int(np.log10(total))}}}$",
        )

    plt.xlabel("Number of iterations")
    plt.ylabel("Relative Error")
    plt.title("Error Distribution by Number of Iterations")
    plt.legend()

    plt.xscale("log", base=10)
    plt.yscale("log", base=10)

    ax = plt.gca()
    ax.xaxis.set_major_formatter(
        plt.FuncFormatter(lambda x, p: f"$10^{{{int(np.log10(x))}}}$")
    )
    ax.yaxis.set_major_formatter(
        plt.FuncFormatter(lambda y, p: f"$10^{{{int(np.log10(y))}}}$")
    )

    plt.grid(True)
    return plt


if __name__ == "__main__":
    resultsPi = dict()
    compile_java_file("Pi/Worker.java")
    compile_java_file("Pi/Master.java")
    compile_java_file("Pi/Pi.java")
    for total in [120000000, 1200000, 1200000000]:
        file = "data/out_Pi_G26_4c_" + str(total) + ".txt"
        if os.path.exists(file):
            os.remove(file)
        for process in [1, 2, 3, 4, 5, 6, 8, 10, 12]:
            for i in range(10):
                run_java_file("Pi", int(total / process), process, str(total))

        resultsPi[total] = read_file(file)

    resultsAssigment = dict()
    compile_java_file("Assignment/Assignment102.java")
    for total in [120000000, 1200000]:
        file = "data/out_Assignment102_G26_4c_" + str(total) + ".txt"
        if os.path.exists(file):
            os.remove(file)
        for process in [1, 2, 3, 4, 5, 6, 8, 10, 12]:
            for i in range(10):
                run_java_file("Assignment102", total, process, str(total))

        resultsAssigment[total] = read_file(file)

    resultsPiWeak = dict()
    for total in [120000000, 1200000]:
        file = "data/out_Pi_G26_4c_weak_" + str(total) + ".txt"
        if os.path.exists(file):
            os.remove(file)
        for process in [1, 2, 3, 4, 5, 6, 8, 10, 12]:
            for i in range(10):
                run_java_file("Pi", total, process, "weak_" + str(total))

        resultsPiWeak[total] = read_file(file)

    pltPi = calculate_speedup(resultsPi)
    pltPi.savefig("data\\speedup_pi.png")
    pltWPi = calculate_weak_speedup(resultsPiWeak)
    pltWPi.savefig("data\\weak_speedup_pi.png")
    pltA102 = calculate_speedup(resultsAssigment)
    pltA102.savefig("data\\speedup_assigment102.png")
    # pltWA102 = calculate_weak_speedup(resultsAssigment)
    # pltWA102.savefig("data\\weak_speedup_assigment102.png")
    pltErrorPi = draw_error(resultsPi)
    pltErrorPi.savefig("data/error_pi.png")
    pltErrorPiWeak = draw_error(resultsPiWeak)
    pltErrorPiWeak.savefig("data/error_pi_weak.png")
    pltErrorA102 = draw_error(resultsAssigment)
    pltErrorA102.savefig("data/error_assigment102.png")

    pltPi.show()
    pltWPi.show()
    pltA102.show()
    # pltWA102.show()
    pltErrorPi.show()
    pltErrorPiWeak.show()
    pltErrorA102.show()
