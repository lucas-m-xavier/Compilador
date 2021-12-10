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
public class Token {
    private int id;
    private String symbol;
    private CodeLine line;
    private String category;
    private int begin;
    private int end;

    public Token(int id, String symbol, CodeLine line, String category, int begin, int end) {
        this.id = id;
        this.symbol = symbol;
        this.line = line;
        this.category = category;
        this.begin = begin;
        this.end = end;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public CodeLine getLine() {
        return line;
    }

    public void setLine(CodeLine line) {
        this.line = line;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
