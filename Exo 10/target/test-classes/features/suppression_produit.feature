# language: fr
Fonctionnalité: Suppression de produit d'une commande
  En tant qu'utilisateur, je veux supprimer des produits de ma commande.

  Contexte:
    Étant donné une commande en cours numéro "C1"

  Scénario: Diminution de la quantité quand elle est supérieure à 1
    Étant donné que la commande "C1" contient déjà 2 exemplaires du produit "Souris gamer"
    Quand l'utilisateur supprime le produit "Souris gamer" de la commande "C1"
    Alors la commande "C1" contient 1 exemplaire du produit "Souris gamer"

  Scénario: Retrait du produit quand la quantité est égale à 1
    Étant donné que la commande "C1" contient déjà 1 exemplaire du produit "Souris gamer"
    Quand l'utilisateur supprime le produit "Souris gamer" de la commande "C1"
    Alors la commande "C1" ne contient plus le produit "Souris gamer"

  Scénario: Suppression d'un produit absent de la commande
    Quand l'utilisateur supprime le produit "Souris gamer" de la commande "C1"
    Alors une erreur de produit absent est renvoyée

  Scénario: Suppression sur une commande qui n'existe pas
    Quand l'utilisateur supprime le produit "Souris gamer" de la commande "C9"
    Alors une erreur de commande introuvable est renvoyée
