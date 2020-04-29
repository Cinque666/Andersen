package by.andersen.rz.loader.exception;

public class ClassLoaderException extends Exception {

    public ClassLoaderException() {
    }

    public ClassLoaderException(String message) {
        super(message);
    }

    public ClassLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassLoaderException(Throwable cause) {
        super(cause);
    }

    public ClassLoaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
