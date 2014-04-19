package net.mosstest.servercore;

/**
 * This exception is thrown in case of an unrecoverable error in Mosstest.
 * At each level where a feature needs a clean shutdown, this exception must be caught, the shutdown performed, and the exception rethrown.
 */
public class MosstestFatalDeathException extends RuntimeException{
    public MosstestFatalDeathException() {
        super();
    }

    public MosstestFatalDeathException(String message) {
        super(message);
    }

    public MosstestFatalDeathException(String message, Throwable cause) {
        super(message, cause);
    }

    public MosstestFatalDeathException(Throwable cause) {
        super(cause);
    }
}
