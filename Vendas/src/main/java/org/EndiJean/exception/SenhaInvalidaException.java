package org.EndiJean.exception;

public class SenhaInvalidaException extends RuntimeException {

    public SenhaInvalidaException(){
        super("Senha invalida");
    }

}
