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
public class WriteLn extends LexiconHandler {

    public WriteLn(Token token) {
        super(token);
    }
    
    @Override
    public String getLexicalErrors(Token token) {
        if(similarity(token.getSymbol(), "writeln") > 0.7)
            return "Token pode ser substituido pela writeln";
        else if(similarity(token.getSymbol(), "writeln") > 0.5)
            return "Token similar a writeln "+ next.getLexicalErrors(token);
        
        return next.getLexicalErrors(token);
    }
    
    @Override
    public void execute(Token token) {
        if (token.getSymbol().toLowerCase().compareTo("writeln") == 0) 
            token.setCategory("writeln");
        else this.setNext(new Program(token));
    }    
    
}
