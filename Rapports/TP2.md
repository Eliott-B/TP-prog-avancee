# Programmation parallèle sur machine à mémoire partagée

> Eliott Barker  
> INF3-FA  

## Conception

![Diagramme de classes](./assets/semaphore.jpg)  
**Figure 1** : Diagramme de classes  

## Analyse de la ressource critiques et section critique

Les threads ne doivent pas écrire en même temps sur le terminal. La ressource critique est donc `System.out`.  
Il y a une boucle et il ne faut pas que les threads se superposent. La section critique est donc la boucle.  

Pour définir une section critique en Java, on utilise le mot-clé `synchronized`.  

```java
static Exclusion exclusionMutuelle = new Exclusion();

// ...

synchronized (exclusionMutuelle) //section critique
{
    //code
}
```

Si on a trouvé la ressource critique on peut aussi faire comme ça :

```java
synchronized (System.out) //section critique
{
    //code
}
```

La méthode `synchronized` permet d'avoir qu'un seul thread qui peut accéder à la section critique à la fois.  

## Semaphores

Les semaphores fonctionnent avec 2 états : `wait` et `signal`.  
`wait` permet de bloquer un thread si la ressource critique est déjà utilisée.  
`signal` permet de débloquer un thread qui attend la ressource critique.  

```java
static semaphoreBinaire sem = new semaphoreBinaire(1);

// ...

sem.wait(); //section critique

// CODE

sem.signal(); //fin de la section critique
```

La première ressource qui passe par `sem.wait()` va pouvoir accéder à la section critique. Les autres attendent que la ressource soit libérée. Quand la ressource passe par `sem.signal()`, un thread qui attend la ressource va pouvoir accéder à la section critique.  

## semaphore - Classe

Dans la classe `semaphore`, l'incrément et le décrément indique le nombre de ressources disponibles.  
Par défaut il y a `x` ressources disponibles, quand une ressource utilise la section critique, le nombre de ressources disponibles est décrémenté. Quand cette ressource a fini d'utiliser la section critique, le nombre de ressources disponibles est incrémenté.  

## semaphoreBinaire

Pour afficher les indications suivantes :

```text
j’entre en section critique
je sors de la section critique
```

Il faut ajouter un `System.out.println()` dans la méthode `wait()` et `signal()` de `semaphoreBinaire` (il faut donc refaire une méthode hérité de `signal()` de `semaphore` dans `semaphoreBinaire`).  
Pour le `System.out.println("j’entre en section critique");`, il faut le mettre après le `sem.wait();` et pour le `System.out.println("je sors de la section critique");`, il faut le mettre avant le `sem.signal();`.  
Pour le premier cas, il faut le mettre après parce qu'il boucle dans `sem.wait()` et donc si il passe cette instruction c'est qu'il est entré dans la section critique.  
Pour le deuxième cas, il faut le mettre avant parce que quand il donne le signal, il sort et une autre ressource peut rentrer directement dans la section critique. Notre message risque de s'afficher en même temps que le message de la ressource qui rentre dans la section critique.  
