# Modèles et paradigmes de programmation parallèle

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
AtomicInteger est un objet qui protège notre entier, c'est un moniteur.  

La fonction `getPi()` est la fonction qui effectue la méthode de Monte Carlo.  

`ExecutorService` est un support de thread et `Thread` est un support de tâche.  
`Executors.newWorkStealingPool()` permet de créer un pool (un groupe) de threads et quand un thread termine, il n'est pas détruit mais réutilisé.  

## Pi - PiMonteCarlo

Le code suit un paradigme `Master-Worker`.  
`Callable` est une fonction paramétré qui retourne une valeur précisée, contrairement à `Runnable` qui retourne `void`.  

Cette fois ci, on utilise `Executors.newFixedThreadPool(numWorkers)` qui permet de créer un groupe de threads de la taille précisée.  
On stocke les résultats obtenu dans une liste de `Future<Long>`. Cette liste est une liste de résultats futurs. Derrière on récupère les résultats avec `future.get()`. Il récupère les résultats d'un thread, on ne sait pas quand est-ce qu'on va les récupérer.  

## Conclusion
