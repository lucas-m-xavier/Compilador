/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.compiler.lexicon.Identifier;

import com.ufes.compiler.lexicon.LexiconHandler;
import com.ufes.compiler.model.Token;
import java.util.regex.Pattern;

/**
 *
 * @author Lucas
 */
public class Digit extends LexiconHandler {
    public Digit(Token token) {
        super(token);
    }
    
    @Override
    public String getLexicalErrors(Token token) {
        return next.getLexicalErrors(token);
    }
    
    @Override
    public void execute(Token token) {
        if (token.getSymbol().length() <= LexiconHandler.maxSize && Pattern.matches("[0-9]*", token.getSymbol()))
            token.setCategory("digito");
        else if (token.getSymbol().length() <= LexiconHandler.maxSize && Pattern.matches("[0-9].*", token.getSymbol())){
            token.setCategory("digito");
        } else this.setNext(new Identifier(token));
    }
}
