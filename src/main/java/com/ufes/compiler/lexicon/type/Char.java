/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.compiler.lexicon.type;

import com.ufes.compiler.lexicon.LexiconHandler;
import com.ufes.compiler.model.Token;

/**
 *
 * @author Lucas
 */
public class Char extends LexiconHandler{

    public Char(Token token) {
        super(token);
    }

    @Override
    public java.lang.String getLexicalErrors(Token token) {
        if(similarity(token.getSymbol(), "char") > 0.7) 
            return "Token substituido para CHAR";
        else if(similarity(token.getSymbol(), "char") > 0.5) 
            return "Token similar a CHAR; "+ next.getLexicalErrors(token);
        
        return next.getLexicalErrors(token);
    }

    @Override
    public void execute(Token token) {
        if(token.getSymbol().equals("char"))
            token.setCategory("CHAR");
        else 
            this.setNext(new Integer(token));
    }
}
