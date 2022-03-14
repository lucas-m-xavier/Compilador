/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.compiler.lexicon.instructions;

import com.ufes.compiler.lexicon.LexiconHandler;
import com.ufes.compiler.lexicon.arithmetic.Division;
import static com.ufes.compiler.lexicon.LexiconHandler.similarity;
import com.ufes.compiler.model.Token;

/**
 *
 * @author fs1609
 */
public class Else  extends LexiconHandler {
    public Else(Token token) {
        super(token);
    }
    
    @Override
    public String getLexicalErrors(Token token) {
        if(similarity(token.getSymbol(), "else") > 0.7)
            return "Token pode ser substituido pela else";
        else if(similarity(token.getSymbol(), "else") > 0.5)
            return "Token similar a else "+ next.getLexicalErrors(token);
        
        return next.getLexicalErrors(token);
    }
    
    @Override
    public void execute(Token token) {
        if (token.getSymbol().toLowerCase().compareTo("else") == 0) 
            token.setCategory("else");
        else this.setNext(new If(token));
    }    
}
