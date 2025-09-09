package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

// Classe que representa o tabuleiro do jogo Campo Minado
// Implementa a interface CampoObservador para receber notificações dos campos
public class Tabuleiro implements CampoObservador {

    // Dimensões do tabuleiro e quantidade de minas
    private final int linhas;
    private final int colunas;
    private final int minas;

    // Lista que armazena todos os campos do tabuleiro
    private final List<Campo> campos = new ArrayList<>();
    // Lista de observadores que serão notificados sobre o resultado do jogo
    private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

    // Construtor que inicializa o tabuleiro com suas dimensões e quantidade de minas
    public Tabuleiro(int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;

        // Inicializa a estrutura do tabuleiro
        gerarCampos();
        associarVizinhos();
        sortearMinas();
    }

    // Aplica uma função para cada campo do tabuleiro
    public void paraCadaCampo(Consumer<Campo> funcao){
        campos.forEach(funcao);
    }

    // Retorna o número de linhas do tabuleiro
    public int getLinhas() {
        return linhas;
    }

    // Retorna o número de colunas do tabuleiro
    public int getColunas() {
        return colunas;
    }

    // Registra um observador para receber notificações de resultado do jogo
    public void registrarObservador(Consumer<ResultadoEvento> observador) {
        observadores.add(observador);
        System.out.println("Observador registrado: " + observador);
        System.out.println("Total de observadores: " + observadores.size());
    }

    // Notifica todos os observadores sobre o resultado do jogo (vitória ou derrota)
    private void notificarObservadores(Boolean resultado){
        observadores.stream()
                .forEach(observador -> observador.accept(new ResultadoEvento(resultado)));
    }

    // Abre um campo específico baseado em suas coordenadas
    public void abrir (int linha, int coluna){
        campos.parallelStream()
                .filter(c -> c.getLinha() == linha && c.getColuna()== coluna)
                .findFirst()
                .ifPresent(c -> c.abrir());
    }

    // Alterna a marcação (bandeira) de um campo específico
    public void alternarMarcacao (int linha, int coluna){
        campos.parallelStream()
                .filter(c -> c.getLinha() == linha && c.getColuna()== coluna)
                .findFirst()
                .ifPresent(c -> c.alternarMarcacao());
    }

    // Distribui aleatoriamente as minas pelo tabuleiro
    private void sortearMinas() {
        long minasArmadas = 0;
        Predicate<Campo> minado = c -> c.isMinado();

        do {
            int aleatorio = (int) (Math.random() * campos.size());
            campos.get(aleatorio).minar();
            minasArmadas = campos.stream().filter(minado).count();
        } while (minasArmadas < minas);
    }

    // Verifica se o objetivo do jogo foi alcançado (todos os campos sem minas abertos)
    public boolean objetivoAlcancado(){
        return campos.stream().allMatch(c -> c.objetivoAlcancado());
    }

    // Reinicia o jogo, revertendo todos os campos ao estado inicial e redistribuindo as minas
    public void reiniciar(){
        campos.stream().forEach(c -> c.reiniciar());
        sortearMinas();
    }

    // Estabelece as relações de vizinhança entre os campos
    private void associarVizinhos() {
        for (Campo c1:  campos){
            for (Campo c2 : campos){
                c1.adicionarVizinho(c2);
            }
        }
    }

    // Cria todos os campos do tabuleiro
    private void gerarCampos() {
        for (int linha = 0; linha < linhas ; linha++) {
            for (int coluna = 0; coluna < colunas ; coluna++) {
                // Cria um novo campo com as coordenadas especificadas
                Campo campo = new Campo(linha, coluna);
                // Registra o tabuleiro como observador do campo
                campo.registrarObservador(this);
                // Adiciona o campo à lista de campos do tabuleiro
                campos.add(campo);
            }
        }
    }

    // Implementação do método da interface CampoObservador
    // É chamado quando ocorre um evento em qualquer campo do tabuleiro
    @Override
    public void eventoOcorreu(Campo campo, CampoEvento evento) {
        if (evento == CampoEvento.EXPLODIR){
            // Se uma mina explodiu, mostra todas as minas e notifica derrota
            mostrarMinas();
            notificarObservadores(false);
        } else if (objetivoAlcancado()) {
            // Se o objetivo foi alcançado, notifica vitória
            notificarObservadores(true);
        }
    }

    // Revela todas as minas do tabuleiro (chamado quando o jogador perde)
    private void mostrarMinas(){
        campos.stream()
                .filter(c-> c.isMinado())
//                .filter(c-> !c.isMinado())  // Comentário deixado no código original
                .forEach(c-> c.setAberto(true));
    }
}
