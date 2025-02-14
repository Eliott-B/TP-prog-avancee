"""
Speedup module for Monte Carlo simulation
"""

import matplotlib.pyplot as plt
import subprocess
import os
import time

import numpy as np


def compile_java_file(java_file):
    abs_path = os.path.abspath(java_file)
    dir_path = os.path.dirname(abs_path)
    root_dir = os.path.dirname(os.path.dirname(dir_path))

    try:
        print(f"Compiling {abs_path}...")
        compile_result = subprocess.run(
            ["javac", "-d", root_dir, abs_path],
            capture_output=True,
            text=True,
        )
        if compile_result.returncode != 0:
            raise Exception(f"Compilation error: {compile_result.stderr}")

    except FileNotFoundError:
        print("Error: Java compiler not found. Is Java installed?")
    except Exception as e:
        print(f"Error: {str(e)}")


def run_java_file(java_file, n, processus, subFileName):
    if not isinstance(n, str) or not isinstance(processus, str):
        n, processus = str(n), str(processus)

    current_dir = os.getcwd()
    parent_dir = os.path.dirname(current_dir) + "\\TP4_Suite"
    main_class = "TP4_Suite." + java_file + "." + java_file

    try:
        print(f"Running {main_class} with n={n}, processus={processus}")
        result = subprocess.run(
            [
                "java",
                "TP4_Suite\\MasterSocket.java",
                str(n),
                str(processus),
                subFileName,
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
    parent = os.path.dirname(os.getcwd())
    print(parent)
    os.chdir(parent)
    compile_java_file("TP4_Shared/Pi/Pi.java")
    compile_java_file("TP4_Suite/MasterSocket.java")
    compile_java_file("TP4_Suite/WorkerSocket.java")

    resultsPi = dict()
    for total in [120000000, 1200000, 1200000000]:
        file = "TP4_Suite\\data\\out_MasterSocket_G26_4c_" + str(total) + ".txt"
        if os.path.exists(file):
            os.remove(file)
        for process in [1, 2, 3, 4, 5, 6, 8, 10, 12]:
            for i in range(10):
                for k in range(process):
                    subprocess.Popen(
                        [
                            "java",
                            "TP4_Suite/WorkerSocket.java",
                            str(25545 + k),
                        ]
                    )
                time.sleep(1)
                run_java_file("MasterSocket", int(total / process), process, str(total))

        resultsPi[total] = read_file(file)

    resultsPiW = dict()
    for total in [120000000, 1200000]:
        file = "TP4_Suite\\data\\out_MasterSocket_G26_4c_" + str(total) + "_weak.txt"
        if os.path.exists(file):
            os.remove(file)
        for process in [1, 2, 3, 4, 5, 6, 8, 10, 12]:
            for i in range(10):
                for k in range(process):
                    subprocess.Popen(
                        [
                            "java",
                            "TP4_Suite/WorkerSocket.java",
                            str(25545 + k),
                        ]
                    )
                time.sleep(1)
                run_java_file("MasterSocket", total, process, str(total) + "_weak")

        resultsPiW[total] = read_file(file)

    PltPi = calculate_speedup(resultsPi)
    PltPiW = calculate_speedup(resultsPiW)
    PltErr = draw_error(resultsPi)
    PltErrW = draw_error(resultsPiW)

    PltPi.savefig("TP4_Suite\\data\\speedup.png")
    PltPiW.savefig("TP4_Suite\\data\\weak_speedup.png")
    PltErr.savefig("TP4_Suite\\data\\error.png")
    PltErrW.savefig("TP4_Suite\\data\\error_weak.png")

    PltPi.show()
    PltPiW.show()
    PltErr.show()
    PltErrW.show()
