/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.compiler.lexicon.instructions;

import com.ufes.compiler.lexicon.LexiconHandler;
import static com.ufes.compiler.lexicon.LexiconHandler.similarity;
import com.ufes.compiler.lexicon.arithmetic.Division;
import com.ufes.compiler.model.Token;

/**
 *
 * @author L
 */
public class If extends LexiconHandler{
    public If(Token token) {
        super(token);
    }
    
    @Override
    public String getLexicalErrors(Token token) {
        if(similarity(token.getSymbol(), "if") > 0.7)
            return "Token pode ser substituido pela else";
        else if(similarity(token.getSymbol(), "if") > 0.5)
            return "Token similar a else "+ next.getLexicalErrors(token);
        
        return next.getLexicalErrors(token);
    }
    
    @Override
    public void execute(Token token) {
        if (token.getSymbol().toLowerCase().compareTo("if") == 0) 
            token.setCategory("if");
        else this.setNext(new Division(token));
    }
}
