# language: fr
Fonctionnalité: Création de compte
  En tant qu'utilisateur, je veux créer un compte pour pouvoir passer des commandes.

  Scénario: Inscription réussie
    Étant donné aucun compte existant pour le nom d'utilisateur "axel"
    Quand l'utilisateur s'inscrit avec l'email "axel@mail.com", le nom d'utilisateur "axel" et le mot de passe "secret123"
    Alors l'inscription est confirmée
    Et le compte est enregistré

  Scénario: Inscription refusée avec un identifiant déjà existant
    Étant donné un compte existant pour le nom d'utilisateur "axel"
    Quand l'utilisateur s'inscrit avec l'email "axel@mail.com", le nom d'utilisateur "axel" et le mot de passe "secret123"
    Alors l'inscription est refusée
    Et le message d'erreur d'inscription contient "existe déjà"
    Et aucun compte n'est enregistré
