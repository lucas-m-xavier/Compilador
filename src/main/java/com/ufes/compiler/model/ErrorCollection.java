/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.compiler.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lucas
 */
public class ErrorCollection {
    private List<ErrorMessage> collection;

    public ErrorCollection(){
        collection = new ArrayList<>();
    }
    
    public String getMessage(Token token) {
        for(ErrorMessage element : collection){
            if(element.getToken().equals(token)) return element.getErrorMessage();
        }
        return null;
    }
    
    public void addError(Token token, String mensagemErro){
        collection.add(new ErrorMessage(token, mensagemErro));
    }

    public List<ErrorMessage> getErrorList() {
        return collection;
    }
}
