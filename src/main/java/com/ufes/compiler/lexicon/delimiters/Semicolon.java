/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.compiler.lexicon.delimiters;

import com.ufes.compiler.lexicon.LexiconHandler;
import static com.ufes.compiler.lexicon.LexiconHandler.similarity;
import com.ufes.compiler.model.Token;

/**
 *
 * @author Lucas
 */
public class Semicolon extends LexiconHandler {
    
    public Semicolon(Token token) {
        super(token);
    }
    
    @Override
    public String getLexicalErrors(Token token) {
        if(similarity(token.getSymbol(), ";") > 0.7)
            return "Token pode ser substituido pelas PONTO_E_VIRGULA";
        else if(similarity(token.getSymbol(), ";") > 0.5)
            return "Token similar ao PONTO_E_VIRGULA "+ next.getLexicalErrors(token);
        
        return next.getLexicalErrors(token);
    }
    
    @Override
    public void execute(Token token) {
        if (token.getSymbol().toLowerCase().compareTo(";") == 0) 
            token.setCategory("PONTO_E_VIRGULA");
        else this.setNext(new Begin(token));
    }
}
