package visao;

import modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;

// Janela principal do jogo Campo Minado
public class TelaPrincipal extends JFrame {

    // Construtor que configura e exibe a janela
    public TelaPrincipal() {
        // Cria um tabuleiro com 16 linhas, 30 colunas e 50 minas
        Tabuleiro tabuleiro = new Tabuleiro(16, 30, 50);

        // Adiciona o painel do tabuleiro à janela
        add(new PainelTabuleiro(tabuleiro));

        // Configura as propriedades da janela
        setTitle("Campo Minado");
        setSize(690, 438);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // Método principal que inicia a aplicação
    public static void main(String[] args) {
        // Configura o look and feel para parecer com o sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Garante que a criação da janela ocorra na Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TelaPrincipal();
            }
        });
    }
}
