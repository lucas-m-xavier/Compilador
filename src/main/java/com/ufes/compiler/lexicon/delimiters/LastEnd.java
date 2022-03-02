/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.compiler.lexicon.delimiters;

import com.ufes.compiler.lexicon.instructions.Var;
import com.ufes.compiler.lexicon.LexiconHandler;
import static com.ufes.compiler.lexicon.LexiconHandler.similarity;
import com.ufes.compiler.model.Token;

/**
 *
 * @author fs1609
 */
public class LastEnd extends LexiconHandler {

    public LastEnd(Token token) {
        super(token);
    }     
    @Override
    public String getLexicalErrors(Token token) {
        if(similarity(token.getSymbol(), "end.") > 0.7)
            return "Token pode ser substituido pela end.";
        else if(similarity(token.getSymbol(), "end.") > 0.5)
            return "Token similar a end. "+ next.getLexicalErrors(token);
        
        return next.getLexicalErrors(token);
    }
    @Override//verificar end.
    public void execute(Token token) {
        if (token.getSymbol().toLowerCase().compareTo("end.") == 0) 
            token.setCategory("end.");
        else this.setNext(new Var(token));
    } 
}
