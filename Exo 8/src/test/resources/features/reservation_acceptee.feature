# language: fr
Fonctionnalité: Réservation de salle acceptée

  Contexte:
    Étant donné une salle "S1" nommée "Réunion A" avec une capacité de 10

  Scénario: Réservation acceptée
    Quand "user@mail.com" réserve la salle "S1" pour 5 participants du "2026-01-01T10:00" au "2026-01-01T11:00"
    Alors la réservation est acceptée
    Et une confirmation est envoyée

  Scénario: Réservation acceptée à capacité maximale
    Quand "user@mail.com" réserve la salle "S1" pour 10 participants du "2026-01-01T10:00" au "2026-01-01T11:00"
    Alors la réservation est acceptée
    Et une confirmation est envoyée

  Scénario: Réservation acceptée si le créneau commence après une réservation existante
    Étant donné une réservation existante sur la salle "S1" du "2026-01-01T09:00" au "2026-01-01T10:00"
    Quand "user@mail.com" réserve la salle "S1" pour 4 participants du "2026-01-01T10:00" au "2026-01-01T11:00"
    Alors la réservation est acceptée
    Et une confirmation est envoyée
