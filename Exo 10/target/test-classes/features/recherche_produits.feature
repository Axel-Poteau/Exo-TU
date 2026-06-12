# language: fr
Fonctionnalité: Recherche de produits
  En tant qu'utilisateur, je veux rechercher des produits pour trouver rapidement ce dont j'ai besoin.

  Contexte:
    Étant donné un catalogue avec les produits suivants:
      | nom              | categorie    | prix  |
      | Clavier sans fil | Informatique | 39.99 |
      | Souris gamer     | Informatique | 59.99 |
      | Tapis de souris  | Informatique | 9.99  |
      | Cafetière        | Cuisine      | 49.99 |

  Scénario: Recherche par mot-clé
    Quand l'utilisateur recherche "souris"
    Alors les résultats contiennent "Souris gamer"
    Et les résultats contiennent "Tapis de souris"
    Et les résultats ne contiennent pas "Cafetière"

  Scénario: Recherche sans résultat
    Quand l'utilisateur recherche "aspirateur"
    Alors la liste de résultats est vide

  Scénario: Recherche par prix maximum
    Quand l'utilisateur recherche les produits à moins de 40 euros
    Alors les résultats contiennent "Clavier sans fil"
    Et les résultats contiennent "Tapis de souris"
    Et les résultats ne contiennent pas "Souris gamer"
