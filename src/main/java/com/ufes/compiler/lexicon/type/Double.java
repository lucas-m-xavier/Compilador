package com.ufes.compiler.lexicon.type;

import com.ufes.compiler.lexicon.LexiconHandler;
import static com.ufes.compiler.lexicon.LexiconHandler.similarity;
import com.ufes.compiler.model.Token;

/**
 *
 * @author 55289
 */
public class Double extends LexiconHandler{
   public Double(Token token) {
        super(token);
    }

     @Override
    public java.lang.String getLexicalErrors(Token token) {
        if(similarity(token.getSymbol(), "double") > 0.7) 
            return "Token substituido para DOUBLE";
        else if(similarity(token.getSymbol(), "double") > 0.5) 
            return "Token similar a DOUBLE; "+ next.getLexicalErrors(token);
        
        return next.getLexicalErrors(token);
    }

    @Override
    public void execute(Token token) {
        if(token.getSymbol().equals("double"))
            token.setCategory("DOUBLE");
        else 
            this.setNext(new Integer(token));
    }
 
}
