# language: fr
Fonctionnalité: Réservation de salle refusée

  Scénario: Réservation refusée si la salle est inconnue
    Étant donné aucune salle pour le code "INCONNUE"
    Quand "user@mail.com" réserve la salle "INCONNUE" pour 3 participants du "2026-01-01T10:00" au "2026-01-01T11:00"
    Alors la réservation est refusée
    Et le message de refus contient "Salle inconnue"
    Et aucune confirmation n'est envoyée

  Scénario: Réservation refusée si la capacité est insuffisante
    Étant donné une salle "S1" nommée "Réunion A" avec une capacité de 10
    Quand "user@mail.com" réserve la salle "S1" pour 11 participants du "2026-01-01T10:00" au "2026-01-01T11:00"
    Alors la réservation est refusée
    Et le message de refus contient "Capacité insuffisante"
    Et aucune confirmation n'est envoyée

  Scénario: Réservation refusée si la période est invalide
    Étant donné une salle "S1" nommée "Réunion A" avec une capacité de 10
    Quand "user@mail.com" réserve la salle "S1" pour 5 participants du "2026-01-01T11:00" au "2026-01-01T10:00"
    Alors la réservation est refusée
    Et le message de refus contient "Période invalide"
    Et aucune confirmation n'est envoyée

  Scénario: Réservation refusée si le créneau est déjà occupé
    Étant donné une salle "S1" nommée "Réunion A" avec une capacité de 10
    Et une réservation existante sur la salle "S1" du "2026-01-01T10:00" au "2026-01-01T12:00"
    Quand "user@mail.com" réserve la salle "S1" pour 5 participants du "2026-01-01T11:00" au "2026-01-01T13:00"
    Alors la réservation est refusée
    Et le message de refus contient "Conflit de réservation"
    Et aucune confirmation n'est envoyée
