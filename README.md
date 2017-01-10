# SoundCloudIsALie

Cette application à pour but de fournir une alternative aux lecteurs de musiques du type iTunes, ou les applications web Spotify ou Deezer ; en s'appuyant sur les vidéos YouTube.<br />
L'application permet de lire de manière complète et en partie automatique les vidéos, permettant d'écouter le flux des vidéos Related YouTube comme on écoute une chaine Radio, mais aussi de gérer sa propre liste de lecture.<br />
L'application se veut hybride dans le sens ou elle fonctionne essentiellement comme un lecteur audio, mais il suffit d'appuyer sur un bouton pour passer en mode cinema, et regarder cette fois ci le flux YouTube comme on regarderait une chaine télévisée.
Enfin, l'application propose diverses options de gestion de son compte et de ses vidéos (Upload, visualisation de ses données, aimer des vidéos, les commenter...) et propose la possibilité de télécharger les vidéos directement sur le disque dur.
<br />
<br />
Vidéo : https://www.youtube.com/watch?v=nkBBYvPxIBM<br />


## Installation

Installer javafx dans votre IDE<br />
Utiliser JAVA 8<br />
Importer les sources en tant que projet Maven<br />
Décompressez le zip default_assets.zip dans son propre dossier<br />
Mettre vos clefs de test<br />
Pour démarrer l'application, configurez le Run avec comme projet le projet importé et comme classe "app.App"

## Compilation et tests

Les tests doivent être lancé à partir de la classe "SoundCloudIsALie.SCiaL.AppTest".<br />
La compilation s'effectue de cette manière :<br />
mvn package<br />
Si maven retourne une erreur des tests, compilez en sautant les tests.

## Lancement du .jar

Pensez à entrer vos clefs d'api dans le dossier asset/ qui doit se trouver dans le même répertoire que le fichier .jar (voir dans RunnableJar/).<br />
Les clefs actuelles sont fausses.

## Changelog

### Lundi 12/12/2016

Mise en place de la fenêtre de base, implémentation des boutons personnalisés pour l'utilisation de fenêtre borderless (fermer, réduire, maximiser, et déplacer la fenêtre).<br />
Implémentation de la gestion des vues (système de génération des vues dynamiquement avec utilisation de templates HTML et communiquation JS pour mise à jour dynamique de la vue tout en ayant un fonctionnement JAVA).<br />
Recherche basique de vidéos via sdk YouTube data Api, affichage sous forme de jaquettes.<br />
Lecture très basique des vidéos/musiques, début d'implémentation d'un player personnalisé (bare de lecture avec pourcentage, timing, miniature de la video)<br />
#### Back
Mise en place de l'écriture et lecture des commentaires<br />
Mise en place de l'upload de vidéo<br />

### Mardi 12/12/2016

Mise en place d'un volet détails permettant d'afficher les commentaires de la vidéo en cours de lecture.<br />
Amélioration du player avec l'implémentation des boutons play, pause, affichage en plein écran de la vidéo, déplacement dans la vidéo.<br />
Mise en place d'un système d'historique affichant jusque 100 vidéos lues, avec possibilité de revenir à une vidéo lue.<br />
Possibilité d'ajouter une vidéo en début ou fin d'une liste de "à lire ensuite". <br />
Possibilité de télécharger une vidéo en cours de lecture dans un dossier à choisir.<br />
#### Back
Mise en place de sauvegarde en dur de données comme vidéos favorites, notations internes...<br />

### Mercredi 14/12/2016
Connexion obligatoire au compte google au démarrage.<br />
Mise en place d'un menu lateral<br />
Upload de vidéo sur la chaine par défault de l'utilisateur.<br />
Afficher mes vidéos uploadées.<br />
Affichage des informations de base de l'utilisateur.<br />
Mise en place du like/dislike<br />
Implémentation des commentaires avec ajout, modification, réponse.<br />
Ajout des autres informations sur la vidéo dans l'onglet détail.<br />
Possibilité de supprimer des éléments de la liste de lecture.<br />
Possibilité de se logout.<br />
Téléchargement de vidéos simultanément avec affichage des pourcentages et possibilité d'annuler les téléchargements (avec suppression / nettoyage de la vidéo téléchargée sur le disque).<br />
Page de paramètres avec choix du dossier d'enregistrement des vidéos téléchargées<br />
Enregistrement de la dernière vidéo lue pour la remettre au démarrage de l'application<br />

#### Back
Recherche de vidéos par chaine<br />

## Calendrier

### Jeudi 15/12/2016
Ajout de scripts de test<br />
Tests pousés de l'application <br />
Pouvoir lire les vidéos partout <br />
Ne pas afficher les vidéos illisibles <br />
Implémentation des commentaires avec ajout, modification, réponse.<br />
Ne pas afficher les vidéos non lisibles<br />
Connexion google in-app (ne pas sortir de l'application pour ça)<br />
Améliorer les graphismes des pages créées mercredi (upload, paramètres, menus)<br />
Debugage en fin de journée des bugs mineurs <br />

### Vendredi 16/12/2016
Vidéo de présentation de l'application <br />
Propreté du code <br />
Optimisation du code <br />
Développement des options si temps<br />
Finitions<br />

## Optionnel

Commentaire avec position<br />
Regroupement de parties de vidéos<br />
Amélioration des recherches<br />
Réseaux sociaux<br />
Thèmes de musiques<br />
<br />
Mode musique / vidéo -> Fait mardi<br />
Mode hors ligne -> Remplacé par la gestion des téléchargements<br />
Download de vidéos -> Fait mardi<br />
Gestion de sa propre chaîne -> Commencé mercredi<br />

## Bug connus à résoudre

Clic sur la barre de temps de la vidéo au niveau des boutons de droite non fonctionnel sur le Player.<br />
Vidéo suivante automatique ne prend pas parmis les vidéos Related de la vidéo courante.<br />
Erreur graphique lors de la mise en plein écran des vidéo alors que la barre de droite est ouverte.<br />
Des vidéos non lisibles (restreint à la visualisation sur le site YouTube) sont affichés dans la recherche, il faudrait les supprimer.<br />
Animation d'ouverture du mode cinéma/plein écran sacadé.<br />
