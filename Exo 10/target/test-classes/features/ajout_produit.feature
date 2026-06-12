# language: fr
Fonctionnalité: Ajout de produit à une commande
  En tant qu'utilisateur, je veux ajouter des produits à ma commande.

  Contexte:
    Étant donné une commande en cours numéro "C1"

  Scénario: Ajout d'un produit à la commande
    Quand l'utilisateur ajoute le produit "Clavier sans fil" à la commande "C1"
    Alors l'ajout est confirmé
    Et la commande "C1" contient 1 exemplaire du produit "Clavier sans fil"

  Scénario: Ajout d'un produit déjà présent dans la commande
    Étant donné que la commande "C1" contient déjà 2 exemplaires du produit "Clavier sans fil"
    Quand l'utilisateur ajoute le produit "Clavier sans fil" à la commande "C1"
    Alors l'ajout est confirmé
    Et la commande "C1" contient 3 exemplaires du produit "Clavier sans fil"

  Scénario: Ajout sur une commande qui n'existe pas
    Quand l'utilisateur ajoute le produit "Clavier sans fil" à la commande "C9"
    Alors une erreur de commande introuvable est renvoyée
