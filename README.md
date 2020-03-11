# FreeBds
FreeBds est une application web dont l'objet est de gérer une collection privée de bandes dessinées.

L'application se décompose en trois parties fonctionnelles :
- Une fonction Référentiel Bds.
- Une fonction Gestion de la collection privée.
- Une fonction Gestion des utilisateurs qui accèdent à la collection.

## Fonction Référentiel Bds
L'objet de cette fonction est de présenter la liste de l'intégralité des bandes dessinées répertoriées par le site de référence http://www.bedetheque.com.

L'utilisateur peut :
- Parcourir la liste des séries existantes.
- Si une série est sélectionnée, parcourir les albums existants de cette série.
- Si un album est sélectionné, associer ce dernier à notre collection privée.

## Fonction Gestion de la collection privée
Cette fonction permet à l'utilisateur de :
- Visualiser les informations des séries associées à la collection.
- Visualiser les informations des albums associés à la collection.
- Visualiser les informations des auteurs associés à la collection.
- Saisir un avis sur une série ou un album.

## Fonction Gestion des utilisateurs
L'accès à l'application est limité aux utilisateurs possédant un compte.

A chaque compte est associé un rôle, qui permet de limiter les actions possibles de chaque utilisateur dans l'application :
- **ADMIN** : l'utilisateur peut associer / dissocier des albums dans la collection privée.
- **USER** : l'utilisateur peut consulter le contenu de la collection privée et saisir des avis mais n'a pas le droit d'associer / dissocier des albums dans cette dernière.
