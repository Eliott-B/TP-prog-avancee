# Programmation parallèle de la méthode de Monte Carlo pour le calcul de Pi

> Eliott Barker  
> INF3-FA  
> 2024-2025  

## Introduction

## Conception

### Shared

#### Pi

![Diagramme de classes](./assets/Pi.jpg)  
**Figure 1** : Diagramme de classes de Pi  

#### Assignment

![Diagramme de classes](./assets/Assignment.jpg)
**Figure 2** : Diagramme de classes de Assignment

## Assignment102 - PiMonteCarlo

Dans `PiMonteCarlo`, notre `n_cible` du pseudo-code est `nAtomSuccess`.  
:warning: `worker` n'est pas un worker du paradigme `Master-Worker` mais seulement un runnable, donc une tâche.  

La fonction `getPi()` est la fonction qui effectue la méthode de Monte Carlo.  

`ExecutorService` est un support de thread et `Thread` est un support de tâche.  
`Executors.newWorkStealingPool()` permet de créer un pool (un groupe) de threads et quand un thread termine, il n'est pas détruit mais réutilisé.  

## Conclusion
