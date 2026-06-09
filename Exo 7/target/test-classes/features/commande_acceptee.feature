# language: fr
Fonctionnalité: Commande acceptée selon le profil client

  Contexte:
    Étant donné un produit "REF-1" nommé "Clavier" au prix de 100.0 avec un stock de 10

  Scénario: Commande acceptée pour un client STANDARD
    Quand le client "standard@mail.com" commande 2 unités de "REF-1" avec le profil "STANDARD"
    Alors la commande est acceptée
    Et le montant total est 200.0
    Et le reçu concerne la référence "REF-1" avec la quantité 2
    Et le reçu contient un message de confirmation

  Scénario: Commande acceptée pour un client PREMIUM
    Quand le client "premium@mail.com" commande 2 unités de "REF-1" avec le profil "PREMIUM"
    Alors la commande est acceptée
    Et le montant total est 180.0

  Scénario: Commande acceptée pour un client VIP
    Quand le client "vip@mail.com" commande 2 unités de "REF-1" avec le profil "VIP"
    Alors la commande est acceptée
    Et le montant total est 160.0
