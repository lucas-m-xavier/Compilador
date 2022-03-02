/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.compiler.lexicon.instructions;

import com.ufes.compiler.lexicon.LexiconHandler;
import static com.ufes.compiler.lexicon.LexiconHandler.similarity;
import com.ufes.compiler.model.Token;

/**
 *
 * @author L
 */
public class ReadLn extends LexiconHandler {

    public ReadLn(Token token) {
        super(token);
    }
    
    @Override
    public String getLexicalErrors(Token token) {
        if(similarity(token.getSymbol(), "readln") > 0.7)
            return "Token pode ser substituido pela readln";
        else if(similarity(token.getSymbol(), "readln") > 0.5)
            return "Token similar a readln "+ next.getLexicalErrors(token);
        
        return next.getLexicalErrors(token);
    }
    
    @Override
    public void execute(Token token) {
        if (token.getSymbol().toLowerCase().compareTo("readln") == 0) 
            token.setCategory("readln");
        else this.setNext(new WriteLn(token));
    }    
    
}
