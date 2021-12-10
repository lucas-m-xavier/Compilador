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
        execute(token);
    }
    
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
          int lastValue = i;
          for (int j = 0; j <= s2.length(); j++) {
            if (i == 0)
              costs[j] = j;
            else {
              if (j > 0) {
                int newValue = costs[j - 1];
                if (s1.charAt(i - 1) != s2.charAt(j - 1))
                  newValue = Math.min(Math.min(newValue, lastValue),
                      costs[j]) + 1;
                costs[j - 1] = lastValue;
                lastValue = newValue;
              }
            }
          }
          if (i > 0)
            costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
    
    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) {
          longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {return 1.0;}
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }
    
    public abstract String getLexicalErrors(Token token);
    public abstract void execute(Token token);

    public void setNext(LexiconHandler next) {
        this.next = next;
    }

    public static void setMaxSize(int maxSize) {
        LexiconHandler.maxSize = maxSize;
    }
}
