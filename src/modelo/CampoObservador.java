package modelo;

// Interface que define o contrato para observadores de campos
// Implementa o padrão Observer para notificar sobre eventos ocorridos no campo
public interface CampoObservador {

    // Método chamado quando um evento ocorre em um campo específico
    // @param campo - O campo onde o evento ocorreu
    // @param evento - O tipo de evento que ocorreu
    void eventoOcorreu(Campo campo, CampoEvento evento);
}
