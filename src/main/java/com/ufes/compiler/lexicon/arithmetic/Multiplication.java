/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.compiler.lexicon.arithmetic;

import com.ufes.compiler.lexicon.LexiconHandler;
import static com.ufes.compiler.lexicon.LexiconHandler.similarity;
import com.ufes.compiler.lexicon.delimiters.CloseParenthesis;
import com.ufes.compiler.model.Token;

/**
 *
 * @author L
 */
public class Multiplication extends LexiconHandler {
    public Multiplication(Token token) {
        super(token);
    }
    
    @Override
    public String getLexicalErrors(Token token) {
        if(similarity(token.getSymbol(), "*") > 0.7)
            return "Token pode ser substituido pela *";
        else if(similarity(token.getSymbol(), "*") > 0.5)
            return "Token similar a * "+ next.getLexicalErrors(token);
        
        return next.getLexicalErrors(token);
    }
    
    @Override
    public void execute(Token token) {
        if (token.getSymbol().toLowerCase().compareTo("*") == 0) 
            token.setCategory("*");
        else this.setNext(new Subtraction(token));
    }    
    
}