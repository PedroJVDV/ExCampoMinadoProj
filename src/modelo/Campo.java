package modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {

    // Coordenadas do campo no tabuleiro
    private final int linha;
    private final int coluna;

    // Estados do campo
    private boolean aberto = false;    // Indica se o campo foi revelado
    private boolean minado = false;    // Indica se o campo contém uma mina
    private boolean marcado = false;   // Indica se o campo foi marcado pelo jogador (bandeira)

    // Lista de campos vizinhos adjacentes
    private List<Campo> vizinhos = new ArrayList<>();
    // Lista de observadores para o padrão Observer (notificação de eventos)
    private List<CampoObservador> observadores = new ArrayList<>();

    // Construtor que recebe as coordenadas do campo
    Campo(int linha, int coluna){
        this.linha = linha;
        this.coluna = coluna;
    }

    // Registra um observador para receber notificações de eventos
    public void registrarObservador (CampoObservador observador){
        observadores.add(observador);
    }

    // Notifica todos os observadores sobre um evento ocorrido neste campo
    private void notificarObservadores(CampoEvento evento){
        observadores.stream()
                .forEach(observador ->
                        observador.eventoOcorreu(this, evento));
    }

    // Adiciona um campo vizinho se ele estiver adjacente (horizontal, vertical ou diagonal)
    boolean adicionarVizinho (Campo vizinho){
        boolean linhaDiferente = linha != vizinho.linha;
        boolean colunaDiferente = coluna != vizinho.coluna;
        boolean diagonal = linhaDiferente && colunaDiferente;

        int deltaLinha = Math.abs(linha - vizinho.linha);    // Distância entre as linhas
        int deltaColuna = Math.abs(coluna - vizinho.coluna); // Distância entre as colunas
        int deltaGeral = deltaColuna + deltaLinha;           // Soma das distâncias

        // Verifica se é um vizinho adjacente na horizontal ou vertical
        if (deltaGeral == 1 && !diagonal){
            vizinhos.add(vizinho);
            return true;
        }
        // Verifica se é um vizinho na diagonal
        else if (deltaGeral == 2 && diagonal){
            vizinhos.add(vizinho);
            return true;
        }else{
            return false;
        }
    }

    // Alterna o estado de marcação (bandeira) do campo
    public void alternarMarcacao(){
        if (!aberto){
            marcado = !marcado;

            if (marcado){
                notificarObservadores(CampoEvento.MARCAR);
            }else{
                notificarObservadores(CampoEvento.DESMARCAR);
            }
        }
    }

    // Abre o campo se ele não estiver aberto nem marcado
    public boolean abrir() {
        if (!aberto && !marcado) {
            aberto = true;

            // Se o campo tiver uma mina, notifica explosão
            if (minado) {
                notificarObservadores(CampoEvento.EXPLODIR);
                return true;
            }
            setAberto(true);

            // Se não houver minas vizinhas, abre todos os vizinhos recursivamente
            if (vizinhancaSegura()) {
                vizinhos.forEach(v -> v.abrir());
            }
            return true;
        } else {
            return false;
        }
    }

    // Verifica se nenhum dos vizinhos está minado
    public boolean vizinhancaSegura(){
        return vizinhos.stream().noneMatch(v -> v.minado);
    }

    // Coloca uma mina no campo
    void minar(){
        minado = true;
    }

    // Retorna se o campo está marcado com bandeira
    public boolean isMarcado(){
        return marcado;
    }

    // Define o estado de abertura do campo e notifica observadores
    void setAberto(boolean aberto) {
        this.aberto = aberto;

        if (aberto){
            notificarObservadores(CampoEvento.ABRIR);
        }
    }

    // Retorna se o campo está aberto
    public boolean isAberto(){
        return aberto;
    }

    // Retorna se o campo está fechado (não aberto)
    public boolean isFechado(){
        return !isAberto();
    }

    // Retorna se o campo contém uma mina
    public boolean isMinado(){
        return minado;
    }

    // Retorna a coluna do campo
    public int getColuna() {
        return coluna;
    }

    // Retorna a linha do campo
    public int getLinha() {
        return linha;
    }

    // Verifica se o objetivo do jogo foi alcançado para este campo
    // (campo sem mina e aberto OU campo com mina e marcado)
    boolean objetivoAlcancado(){
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }

    // Conta quantas minas existem na vizinhança deste campo
    public int minasNaVizinhanca(){
        return (int)vizinhos.stream().filter(v -> v.minado).count();
    }

    // Reinicia o campo para o estado inicial
    boolean reiniciar(){
        aberto = false;
        minado = false;
        marcado = false;
        notificarObservadores(CampoEvento.REINICIAR);
        return false;
    }
}
