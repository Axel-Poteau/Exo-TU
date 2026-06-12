Feature: Réservation de salles de réunion
  L'API permet de réserver une salle de réunion sur un créneau libre.

  Scenario: Réservation acceptée quand la salle existe et que le créneau est libre
    Given une salle "Salle A" avec une capacité de 8
    When je réserve cette salle pour "Axel" du "2026-06-15T10:00:00" au "2026-06-15T11:00:00"
    Then la réponse a le statut HTTP 201
    And la réservation retournée a le statut "CONFIRMEE"

  Scenario: Réservation refusée quand la salle n'existe pas
    When je réserve la salle 999 pour "Axel" du "2026-06-15T10:00:00" au "2026-06-15T11:00:00"
    Then la réponse a le statut HTTP 404

  Scenario: Réservation refusée quand le créneau chevauche une réservation existante
    Given une salle "Salle A" avec une capacité de 8
    And une réservation existante pour "Marie" du "2026-06-15T10:00:00" au "2026-06-15T11:00:00"
    When je réserve cette salle pour "Axel" du "2026-06-15T10:30:00" au "2026-06-15T11:30:00"
    Then la réponse a le statut HTTP 409
