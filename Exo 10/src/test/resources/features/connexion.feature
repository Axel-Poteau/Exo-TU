# language: fr
Fonctionnalité: Connexion
  En tant qu'utilisateur, je veux me connecter à mon compte pour accéder à l'application et passer des commandes.

  Contexte:
    Étant donné un compte "axel" avec le mot de passe "secret123"

  Scénario: Connexion réussie
    Quand l'utilisateur se connecte avec le nom "axel" et le mot de passe "secret123"
    Alors la connexion est acceptée
    Et l'utilisateur est redirigé vers la page "accueil"

  Scénario: Connexion échouée avec un mauvais mot de passe
    Quand l'utilisateur se connecte avec le nom "axel" et le mot de passe "mauvais"
    Alors la connexion est refusée
    Et le message d'erreur de connexion contient "incorrect"

  Scénario: Connexion échouée avec un utilisateur inconnu
    Quand l'utilisateur se connecte avec le nom "marie" et le mot de passe "secret123"
    Alors la connexion est refusée
    Et le message d'erreur de connexion contient "incorrect"
