package org.EndiJean.exception;

public class PedidoNaoEncontradoException extends RuntimeException {

    public PedidoNaoEncontradoException() {
        super("Pedido Ñ Encontrado");
    }
}
