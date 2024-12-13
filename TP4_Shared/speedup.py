"""
Speedup module for Monte Carlo simulation
"""    

import matplotlib.pyplot as plt

class Result:
    """Class of result
    """
    err:float = 0.0
    tot:int = 0
    processors:int = 0
    time:int = 0


def read_file(file_path: str) -> list:
    """Read a file and return a list of Result objects

    Args:
        file_path (str): data path

    Returns:
        list: list of Result
    """
    list_results = []
    with open(file_path, 'r', encoding='utf-8') as file:
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

if __name__ == "__main__":
    resultsPi = read_file("data/out_Pi_G26_4c.txt")
    resultsAssigment102 = read_file("data/out_Assigment102_G26_4c.txt")

    processors = [result.processors for result in resultsAssigment102]
    res = [(result.tot*result.time)/(result.tot/result.processors*result.time)
           for result in resultsAssigment102]

    plt.figure(figsize=(10, 6))
    plt.plot(processors, res, marker='o', linestyle='-', color='b', label='Speedup')
    plt.xlabel('Processors')
    plt.ylabel('Sp')
    plt.title('Speedup')
    plt.legend()
    plt.grid(True)
    plt.show()
