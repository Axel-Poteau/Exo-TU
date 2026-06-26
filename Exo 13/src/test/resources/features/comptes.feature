Feature: Gestion des comptes bancaires
  L'API permet de créer des comptes et d'effectuer des opérations bancaires.

  Scenario: Création d'un nouveau compte
    When je crée un compte "FR001" pour "Alice"
    Then la réponse a le statut HTTP 201
    And le solde du compte "FR001" est 0.0

  Scenario: Dépôt d'argent sur un compte
    Given un compte "FR010" pour "Bob"
    When je dépose 150.0 sur le compte "FR010"
    Then la réponse a le statut HTTP 200
    And le solde du compte "FR010" est 150.0

  Scenario: Retrait avec fonds suffisants
    Given un compte "FR020" pour "Carla"
    And le compte "FR020" a un solde de 200.0
    When je retire 50.0 du compte "FR020"
    Then la réponse a le statut HTTP 200
    And le solde du compte "FR020" est 150.0

  Scenario: Retrait avec fonds insuffisants
    Given un compte "FR030" pour "David"
    And le compte "FR030" a un solde de 30.0
    When je retire 100.0 du compte "FR030"
    Then la réponse a le statut HTTP 409
    And le solde du compte "FR030" est 30.0

  Scenario: Virement entre deux comptes
    Given un compte "FR040" pour "Emma"
    And le compte "FR040" a un solde de 300.0
    And un compte "FR041" pour "Felix"
    When je vire 100.0 du compte "FR040" vers le compte "FR041"
    Then la réponse a le statut HTTP 200
    And le solde du compte "FR040" est 200.0
    And le solde du compte "FR041" est 100.0

  Scenario: Virement refusé pour solde insuffisant
    Given un compte "FR050" pour "Gina"
    And le compte "FR050" a un solde de 50.0
    And un compte "FR051" pour "Hugo"
    When je vire 100.0 du compte "FR050" vers le compte "FR051"
    Then la réponse a le statut HTTP 409
    And le solde du compte "FR050" est 50.0
