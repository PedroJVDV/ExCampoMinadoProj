package visao;

import modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;

// Painel que contém todos os botões do tabuleiro
public class PainelTabuleiro extends JPanel {

    // Construtor que cria o painel com base no tabuleiro
    public PainelTabuleiro(Tabuleiro tabuleiro) {
        // Define o layout como grid com base nas dimensões do tabuleiro
        setLayout(new GridLayout(
                tabuleiro.getLinhas(), tabuleiro.getColunas()));

        // Adiciona um botão para cada campo do tabuleiro
        tabuleiro.paraCadaCampo(c -> add(new BotaoCampo(c)));

        // CÓDIGO CORRIGIDO
        tabuleiro.registrarObservador(e -> {
            // Coloca a exibição do diálogo na thread de eventos (EDT)
            SwingUtilities.invokeLater(() -> {
                String mensagem = e.isGanhou() ? "Você GANHOU!!!" : "Você PERDEU!!";
                JOptionPane.showMessageDialog(null, mensagem);

                // AGORA, A PARTE IMPORTANTE:
                // Agenda a reinicialização como uma NOVA tarefa na EDT.
                // Isso dá tempo para o JOptionPane fechar e a tela se repintar
                // antes que o trabalho pesado de reiniciar comece.
                SwingUtilities.invokeLater(() -> {
                    tabuleiro.reiniciar();
                });
            });
        });
    }
}
