/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.compiler.lexicon;

import com.ufes.compiler.model.Token;

/**
 *
 * @author Lucas
 */
public abstract class LexiconHandler {
    protected LexiconHandler next;
    protected static int maxSize = 15;

    public LexiconHandler(Token token) {
        executar(token);
    }
    
    public static void setTamanhoMaximoId(int tamanho){
        LexiconHandler.maxSize = tamanho;
    }
    
    public abstract String recuperarErrosLexico(Token token);
    
    public void setProximo(LexiconHandler proximo) {
        this.next = proximo;
    }
    
    public abstract void executar(Token token);
}
