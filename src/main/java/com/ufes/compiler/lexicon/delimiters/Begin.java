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
public class Begin extends LexiconHandler {
    public Begin(Token token) {
        super(token);
    }
    
    @Override
    public String getLexicalErrors(Token token) {
        if(similarity(token.getSymbol(), "begin") > 0.7)
            return "Token pode ser substituido pela begin";
        else if(similarity(token.getSymbol(), "begin") > 0.5)
            return "Token similar ao begin "+ next.getLexicalErrors(token);
        
        return next.getLexicalErrors(token);
    }
    
    @Override
    public void execute(Token token) {
        if (token.getSymbol().toLowerCase().compareTo("begin") == 0) 
            token.setCategory("begin");
        else this.setNext(new End(token));
    }
}
