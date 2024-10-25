# Programmation parallèle sur machine à mémoire partagée

> Eliott Barker  
> INF3-FA  

## Conception

![Diagramme de classes](./assets/mobile.jpg)  
**Figure 1** : Diagramme de classes  

## Affichage d'un mobile

```java
Container leConteneur = getContentPane();
sonMobile = new UnMobile(LARG, HAUT);
leConteneur.add(sonMobile);
setSize(LARG, HAUT);
setVisible(true);
```

Ce code permet d'afficher un mobile dans une fenêtre en précisant sa largeur et sa hauteur.  
Il faut bien le rendre visible pour qu'il s'affiche.  

## Start d'un thread

```java
Thread laTache= new Thread(sonMobile);
laTache.start();
```

Pour lancer un thread, il faut l'instancier avec un objet `Runnable` et appeler ensuite la méthode `start`.

## Modification du comportement du mobile

Pour modifier le comportement du mobile, il faut bien modifier la méthode `run` de la classe `UnMobile`.  
C'est cette méthode qui est appelée lorsqu'on lance le thread et qui donc exécute le processus du mobile.

## Avoir plusieurs mobiles sur la même fenêtre

On peut utiliser un `GridLayout` pour indiquer le nombre de lignes et de colonnes qu'on veut sur notre fenêtre.

```java
Container leConteneur = getContentPane();
leConteneur.setLayout (new GridLayout(NBRLIG, NBRCOL));
```

## Intégration des semaphores dans le mobile

Pour intégrer les semaphores dans le mobile, il faut faire 6 boucles `for` pour définir les 6 sections de la fenêtre :

- 0 -> 1/3 : allé
- 1/3 -> 2/3 : section critique allé
- 2/3 -> 3/3 : fin de l'allé
- 3/3 -> 2/3 : retour
- 2/3 -> 1/3 : section critique retour
- 1/3 -> 0 : fin du retour

La boucle qui va de 1/3 à 2/3 est une section critique avec la boucle qui va de 2/3 à 1/3.  
La ressource critique est `JPanel`.  

Pour voir l'application du semaphore, on peut rendre aléatoire le temps des mobiles.

```java
Random random = new Random();
sonTemps = random.nextInt(60 + 10) + 10;
```

## Semaphore Naire

Le semaphore naire est un semaphore qui peut être pris par plusieurs threads en même temps.  
Il faut simplement créer une classe qui hérite de `semaphore`. On ne peut pas directement utiliser la classe `semaphore` car elle est abstraite.  
Ensuite dans le mobile, on peut indiquer le nombre de threads qui peuvent prendre le semaphore en même temps.
