/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.compiler.lexicon.type;

import com.ufes.compiler.lexicon.LexiconHandler;
import com.ufes.compiler.lexicon.delimiters.Quote;
import com.ufes.compiler.model.Token;

/**
 *
 * @author Lucas
 */
public class String extends LexiconHandler {

    public String(Token token) {
        super(token);
    }
    
    @Override
    public java.lang.String getLexicalErrors(Token token) {
        if(similarity(token.getSymbol(), "string") > 0.7)
            return "Token pode ser substituido por STRING ";
        else if(similarity(token.getSymbol(), "string") > 0.5)
            return "Token semelhante a STRING; "+ next.getLexicalErrors(token);
        
        return next.getLexicalErrors(token);
    }

    @Override
    public void execute(Token token) {
        if(token.getSymbol().equals("string")) 
            token.setCategory("STRING");
        else this.setNext(new Quote(token));
    }
}
