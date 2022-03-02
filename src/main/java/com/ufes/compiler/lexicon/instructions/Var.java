/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.compiler.lexicon.instructions;

import com.ufes.compiler.lexicon.LexiconHandler;
import com.ufes.compiler.lexicon.delimiters.Comma;
import static com.ufes.compiler.lexicon.LexiconHandler.similarity;
import com.ufes.compiler.model.Token;

/**
 *
 * @author Lucas
 */
public class Var extends LexiconHandler {
    public Var(Token token) {
        super(token);
    }
    
    @Override
    public String getLexicalErrors(Token token) {
        if(similarity(token.getSymbol(), "var") > 0.7)
            return "Token pode ser substituido pela var";
        else if(similarity(token.getSymbol(), "var") > 0.5)
            return "Token similar a var "+ next.getLexicalErrors(token);
        
        return next.getLexicalErrors(token);
    }
    
    @Override
    public void execute(Token token) {
        if (token.getSymbol().toLowerCase().compareTo("var") == 0) 
            token.setCategory("var");
        else this.setNext(new Comma(token));
    }
}
