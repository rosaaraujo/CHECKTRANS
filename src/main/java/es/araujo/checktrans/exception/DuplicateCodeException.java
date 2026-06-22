package es.araujo.checktrans.exception;

public class DuplicateCodeException extends RuntimeException {

    public DuplicateCodeException(String code) {
        super("Checklist with code '" + code + "' already exists");
    }
}
