package br.com.ropalon.tarefas.exception;

public class RoleInvalidaException extends RuntimeException {

    private static final long serialVersionUID = -2283575032099469992L;

    public RoleInvalidaException() {
        super();
    }

    public RoleInvalidaException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RoleInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleInvalidaException(String message) {
        super(message);
    }

    public RoleInvalidaException(Throwable cause) {
        super(cause);
    }


}