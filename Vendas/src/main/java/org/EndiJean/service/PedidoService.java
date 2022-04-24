package org.EndiJean.service;

import org.EndiJean.entity.Pedido;
import org.EndiJean.enums.StatusPedido;
import org.EndiJean.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar (PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizaStatus(Integer id, StatusPedido statusPedido);
}
