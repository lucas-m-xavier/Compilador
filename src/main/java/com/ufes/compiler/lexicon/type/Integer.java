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
public class Integer extends LexiconHandler {

    public Integer(Token token) {
        super(token);
    }
    
    @Override
    public java.lang.String getLexicalErrors(Token token) {
        if(similarity(token.getSymbol(), "int") > 0.7)
            return "Token pode ser substituido por INT ";
        else if(similarity(token.getSymbol(), "int") > 0.5)
            return "Token semelhante a INT; "+ next.getLexicalErrors(token);
        
        return next.getLexicalErrors(token);
    }

    @Override
    public void execute(Token token) {
        if(token.getSymbol().equals("int")) 
            token.setCategory("INT");
        else this.setNext(new Real(token));
    }
}
