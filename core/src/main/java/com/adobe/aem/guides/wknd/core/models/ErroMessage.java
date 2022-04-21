package com.adobe.aem.guides.wknd.core.models;

public class ErroMessage {

    private String errorMessage;
    private int errorCode;
    private String retorno;
    private String input;

    public ErroMessage() {
    }

    public ErroMessage(String errorMessage, int errorCode, String retorno) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.retorno = retorno;
    }

    public ErroMessage(String errorMessage, int errorCode, String retorno, String input) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.retorno = retorno;
        this.input = input;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
