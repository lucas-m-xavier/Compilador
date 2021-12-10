/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.compiler.lexicon.Error;

import com.ufes.compiler.lexicon.LexiconHandler;
import com.ufes.compiler.model.Token;
import java.util.regex.Pattern;

/**
 *
 * @author Lucas
 */
public class Error extends LexiconHandler {
    
    public Error(Token token) {
        super(token);
    }
    
    @Override
    public String getLexicalErrors(Token token) {
        return "Esse token é inválido";
    }
    
    @Override
    public void execute(Token token) {
        token.setCategory("error");
    }
}
