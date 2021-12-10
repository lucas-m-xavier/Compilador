/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.compiler.presenter;

import com.ufes.compiler.model.CodeLine;
import com.ufes.compiler.view.MainView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lucas
 */
public class MainPresenter {
    private MainView view;
    private List<CodeLine> lines;

    public MainPresenter() {
        this.view = new MainView();
        this.view.setVisible(true);
        
        this.view.getBtn().addActionListener(
                new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    compile(view.getTa().getText());
                }
            }
        );
    }
    
    private String processCode(String code){
        //Remove comentários
        code = code.replaceAll("\\(\\*([\\s\\S]*)\\*\\)", "");
        code = code.replaceAll("\\{([\\s\\S]*)\\}", "");
        code = code.replaceAll("\\/\\/([\\s\\S]*)", "");
        
        return code;
    }
    
    private void compile(String sourceCode){

        this.lines = new ArrayList<CodeLine>();
        int posicaoLinha = 1;

        sourceCode = processCode(sourceCode);

        for(String linha : sourceCode.split("\n")){
            lines.add( new CodeLine(linha, posicaoLinha++));
        }

        System.out.println(sourceCode);
    }
}
