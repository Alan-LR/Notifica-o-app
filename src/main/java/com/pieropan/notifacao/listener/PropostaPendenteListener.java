package com.pieropan.notifacao.listener;

import com.pieropan.notifacao.constante.MensagemConstante;
import com.pieropan.notifacao.domain.Proposta;
import com.pieropan.notifacao.service.NotificacaoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PropostaPendenteListener {

    private NotificacaoService notificacaoService;

    public PropostaPendenteListener(NotificacaoService notificacaoService){
        this.notificacaoService = notificacaoService;
    }

    //Essa anotação indica que esse método vai ser um consumidor de mensagens da fila informada
    @RabbitListener(queues = "${rabbitmq.queue.proposta.pendente}")
    public void propostaPendente(Proposta proposta){
        if (Objects.isNull(proposta) || Objects.isNull(proposta.getUsuario()) || Objects.isNull(proposta.getUsuario().getNome())) {
            System.err.println("Proposta ou usuário nulo: " + proposta);
            return;
        }

        String mensagem = String.format(MensagemConstante.PROPOSTA_EM_ANALISE, proposta.getUsuario().getNome());
        notificacaoService.notificar(mensagem);
    }

}
