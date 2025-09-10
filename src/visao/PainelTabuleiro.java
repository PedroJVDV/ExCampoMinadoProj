package visao;

import modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;

// Painel que contém todos os botões do tabuleiro
public class PainelTabuleiro extends JPanel {

    // Construtor que cria o painel com base no tabuleiro
    public PainelTabuleiro(Tabuleiro tabuleiro) {
        setLayout(new GridLayout(
                tabuleiro.getLinhas(), tabuleiro.getColunas()));

        tabuleiro.paraCadaCampo(c -> add(new BotaoCampo(c)));

        tabuleiro.registrarObservador(e -> {
            SwingUtilities.invokeLater(() -> {
                String mensagem = e.isGanhou() ? "Você GANHOU! :D" : "VOCÊ PERDEU! :(";

                // Garante que o painel esteja repintado antes do JOptionPane
                this.revalidate();
                this.repaint();

                // Usa a janela pai em vez de "null"
                Component parent = SwingUtilities.getWindowAncestor(this);

                JOptionPane.showMessageDialog(
                        parent,  // importante para evitar o fundo preto
                        mensagem,
                        e.isGanhou() ? "Parabéns" : "Game Over",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Agenda a reinicialização após o diálogo fechar
                SwingUtilities.invokeLater(tabuleiro::reiniciar);
            });
        });
    }
}
