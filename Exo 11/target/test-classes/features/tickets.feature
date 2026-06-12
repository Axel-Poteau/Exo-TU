

Feature: Gestion des tickets de support
  L'API permet de créer des tickets de support et de suivre leur statut.

  Scenario: Création d'un ticket valide
    When je crée un ticket "Ecran bleu au demarrage" avec la priorité "HIGH"
    Then la réponse a le statut HTTP 201
    And le ticket retourné a le statut "OPEN"

  Scenario: Résolution d'un ticket
    Given un ticket "Imprimante en panne" avec la priorité "MEDIUM"
    When je passe ce ticket au statut "RESOLVED"
    Then la réponse a le statut HTTP 200
    And le ticket retourné a le statut "RESOLVED"

  Scenario: Refus de modification d'un ticket déjà résolu
    Given un ticket "Souris cassee" avec la priorité "LOW"
    And ce ticket est déjà au statut "RESOLVED"
    When je passe ce ticket au statut "IN_PROGRESS"
    Then la réponse a le statut HTTP 409

  Scenario: Consultation d'un ticket inexistant
    When je consulte le ticket 999
    Then la réponse a le statut HTTP 404