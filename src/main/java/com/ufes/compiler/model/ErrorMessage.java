/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.compiler.model;

/**
 *
 * @author Lucas
 */
public class ErrorMessage {
    private Token token;
    private String errorMessage;

    public ErrorMessage(Token token, String errorMessage) {
        this.token = token;
        this.errorMessage = errorMessage;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
