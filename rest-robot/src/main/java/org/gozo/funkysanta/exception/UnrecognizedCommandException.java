package org.gozo.funkysanta.exception;

public class UnrecognizedCommandException extends RuntimeException{

    public UnrecognizedCommandException(){
    }

    public UnrecognizedCommandException(String message, Exception ex) {
        super(message,ex);
    }

    public UnrecognizedCommandException(String message){
        super(message);
    }

    public UnrecognizedCommandException(String message, String code){
        super(message);
    }

    public UnrecognizedCommandException(Exception ex){
        super(ex);
    }
}
