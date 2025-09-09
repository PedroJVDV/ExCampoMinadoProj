package visao;

import modelo.Campo;
import modelo.CampoEvento;
import modelo.CampoObservador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// Classe que representa visualmente um campo do jogo como um botão
public class BotaoCampo extends JButton implements CampoObservador, MouseListener {

    // Cores para os diferentes estados do botão
    private final Color BG_PADRAO = new Color(184, 184, 184);
    private final Color BG_MARCADO = new Color(8, 179, 247);
    private final Color BG_EXPLODIR = new Color(189, 66, 68);
    private final Color TEXTO_VERDE = new Color(0, 100, 0);

    // Campo do modelo que este botão representa
    private final Campo campo;

    // Construtor que inicializa o botão
    public BotaoCampo(Campo campo) {
        this.campo = campo;
        setBackground(BG_PADRAO);
        setOpaque(true);
        setBorder(BorderFactory.createBevelBorder(0));

        // Registra o botão como observador do campo
        campo.registrarObservador(this);
        // Registra o listener de mouse
        addMouseListener(this);
    }

    // Método da interface CampoObservador, chamado quando um evento ocorre no campo
    @Override
    public void eventoOcorreu(Campo campo, CampoEvento evento) {
        switch (evento) {
            case ABRIR:
                aplicarEstiloAbrir();
                break;
            case MARCAR:
                aplicarEstiloMarcar();
                break;
            case EXPLODIR:
                aplicarEstiloExplodir();
                break;
            default:
                aplicarEstiloPadrao();
        }

        // Força a atualização visual do componente
        SwingUtilities.invokeLater(() -> {
            repaint();
            validate();
        });
    }

    // Define o estilo visual para um campo aberto
    private void aplicarEstiloAbrir() {
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        if (campo.isMinado()) {
            setBackground(BG_EXPLODIR);
            setText("*");
            setBackground(Color.RED);
            return;
        }

        setBackground(BG_PADRAO);

        // Define a cor do texto baseado no número de minas vizinhas
        switch (campo.minasNaVizinhanca()) {
            case 1:
                setForeground(TEXTO_VERDE);
                break;
            case 2:
                setForeground(Color.BLUE);
                break;
            case 3:
                setForeground(Color.ORANGE);
                break;
            case 4:
            case 5:
            case 6:
                setForeground(Color.RED);
                break;
            default:
                setForeground(Color.PINK);
        }

        // Mostra o número de minas vizinhas, se houver
        String valor = !campo.vizinhancaSegura() ?
                campo.minasNaVizinhanca() + "" : "";
        setText(valor);
    }

    // Define o estilo visual para um campo marcado
    private void aplicarEstiloMarcar() {
        setBackground(BG_MARCADO);
        setForeground(Color.BLACK);
        setText("M");
    }

    // Define o estilo visual para um campo que explodiu
    private void aplicarEstiloExplodir() {
        setBackground(BG_EXPLODIR);
        setForeground(Color.RED);
        setText("X");
        setForeground(Color.RED);
    }

    // Define o estilo visual padrão
    private void aplicarEstiloPadrao() {
        setBackground(BG_PADRAO);
        setBorder(BorderFactory.createBevelBorder(0));
        setText("");
    }

    // Implementação do método da interface MouseListener
    @Override
    public void mousePressed(MouseEvent e) {
        // Botão esquerdo (1) abre o campo
        if (e.getButton() == 1) {
            campo.abrir();
            // Outros botões (geralmente o direito) alternam a marcação
        } else {
            campo.alternarMarcacao();
        }
    }

    // Métodos não utilizados da interface MouseListener
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
}
