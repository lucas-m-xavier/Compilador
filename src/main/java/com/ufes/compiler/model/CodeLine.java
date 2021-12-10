/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.compiler.model;

/**
 *
 * @author Lucas
 */
public class CodeLine {
    private String content;
    private int position;

    public CodeLine(String content, int position) {
        this.content = content;
        this.position = position;
    }
    
    public CodeLine removeComment(){
        int posicaoBarras = content.indexOf("//");
        if( posicaoBarras >= 0 )
            content = content.substring(0, posicaoBarras);
        
        return this;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
