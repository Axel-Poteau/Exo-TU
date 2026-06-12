# language: fr
Fonctionnalité: Navigation par catégorie
  En tant qu'utilisateur, je veux naviguer par catégorie de produits pour découvrir ce qui est disponible.

  Contexte:
    Étant donné un catalogue avec les produits suivants:
      | nom              | categorie    | prix  |
      | Clavier sans fil | Informatique | 39.99 |
      | Cafetière        | Cuisine      | 49.99 |
      | Grille-pain      | Cuisine      | 29.99 |

  Scénario: Sélection d'une catégorie
    Quand l'utilisateur sélectionne la catégorie "Cuisine"
    Alors les résultats contiennent "Cafetière"
    Et les résultats contiennent "Grille-pain"
    Et les résultats ne contiennent pas "Clavier sans fil"

  Scénario: Catégorie sans produit
    Quand l'utilisateur sélectionne la catégorie "Jardin"
    Alors la liste de résultats est vide
