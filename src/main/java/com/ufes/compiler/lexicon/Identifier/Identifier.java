/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.compiler.lexicon.Identifier;

import com.ufes.compiler.lexicon.LexiconHandler;
import static com.ufes.compiler.lexicon.LexiconHandler.similarity;
import com.ufes.compiler.model.Token;
import java.util.regex.Pattern;
import com.ufes.compiler.lexicon.Error.Error;

/**
 *
 * @author Lucas
 */
public class Identifier extends LexiconHandler {
    public Identifier(Token token) {
        super(token);
    }
    
    @Override
    public String getLexicalErrors(Token token) {
        return next.getLexicalErrors(token);
    }
    
    @Override
    public void execute(Token token) {
        if (token.getSymbol().length() <= LexiconHandler.maxSize && Pattern.matches("[[A-Za-z]+|[_]+][[A-Za-z0-9]+|[_]+]*", token.getSymbol())){
            token.setCategory("identificador");
        } else {
            this.setNext(new Error(token));
        }
    }
}
