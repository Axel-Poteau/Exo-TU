# language: fr
Fonctionnalité: Commande refusée

  Scénario: Commande refusée si le produit est inconnu
    Étant donné aucun produit pour la référence "REF-INCONNUE"
    Quand le client "client@mail.com" commande 1 unités de "REF-INCONNUE" avec le profil "STANDARD"
    Alors la commande est refusée
    Et le message de refus contient "Produit inconnu"

  Scénario: Commande refusée si le stock est insuffisant
    Étant donné un produit "REF-2" nommé "Souris" au prix de 50.0 avec un stock de 3
    Quand le client "client@mail.com" commande 5 unités de "REF-2" avec le profil "STANDARD"
    Alors la commande est refusée
    Et le message de refus contient "Stock insuffisant"
