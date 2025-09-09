package modelo;

// Classe que representa o resultado de um jogo (vitória ou derrota)
public class ResultadoEvento {

    // Indica se o jogador ganhou ou perdeu
    private final boolean ganhou;

    // Construtor que define o resultado
    public ResultadoEvento(boolean ganhou) {
        this.ganhou = ganhou;
    }

    // Retorna se o jogador ganhou
    public boolean isGanhou() {
        return ganhou;
    }
}
