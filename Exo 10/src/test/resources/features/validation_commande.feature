# language: fr
Fonctionnalité: Validation de commande
  En tant qu'utilisateur, je veux valider une commande.

  Scénario: Validation d'une commande
    Étant donné une commande en cours numéro "C1"
    Et que la commande "C1" contient déjà 1 exemplaire du produit "Clavier sans fil"
    Quand l'utilisateur valide la commande "C1"
    Alors la commande est confirmée

  Scénario: Validation d'une commande qui n'existe pas
    Quand l'utilisateur valide la commande "C9"
    Alors une erreur de commande introuvable est renvoyée
