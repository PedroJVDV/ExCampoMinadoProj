package modelo;

// Enumeração que define os possíveis eventos que podem ocorrer em um campo
public enum CampoEvento {
    ABRIR,      // Evento quando um campo é aberto
    MARCAR,     // Evento quando um campo é marcado com bandeira
    DESMARCAR,  // Evento quando um campo é desmarcado (bandeira removida)
    EXPLODIR,   // Evento quando um campo com mina é aberto (explosão)
    REINICIAR   // Evento quando um campo é reiniciado para o estado inicial
}
