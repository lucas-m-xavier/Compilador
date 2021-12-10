/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.compiler.presenter;

import com.ufes.compiler.model.CodeLine;
import com.ufes.compiler.model.Token;
import com.ufes.compiler.view.MainView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Lucas
 */
public class MainPresenter {
    private MainView view;
    private List<Token> tokens;
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
        code = code.replaceAll("\\(\\*([\\s\\S]*)\\*\\)", "");
        code = code.replaceAll("\\{([\\s\\S]*)\\}", "");
        code = code.replaceAll("\\/\\/([\\s\\S]*)", "");
        code = code.replaceAll("\t", " ");
        code = code.replaceAll("\\[", " \\[ ");
        code = code.replaceAll("\\]", " \\] ");
        code = code.replaceAll("\\(", " \\( ");
        code = code.replaceAll("\\)", " \\) ");
        code = code.replaceAll("\\{", " \\{ ");
        code = code.replaceAll("\\}", " \\} ");
        code = code.replaceAll("\\ +", " ");
        code = code.replaceAll("\\ \\n", "\\\n");
        code = code.replaceAll(";", " ; ");
        code = code.replaceAll(",", " , ");
        code = code.replaceAll("\\'", " \\' ");
        code = code.replaceAll("\\\"", " \\\" ");
        code = code.replaceAll("\\&&", " \\&& ");
        code = code.replaceAll("\\|\\|", " \\|\\| ");
        
        return code;
    }
    
    private void compile(String sourceCode){
        int idToken = 1;
        int posLine = 1;
        this.lines = new ArrayList<CodeLine>();
        this.tokens = new ArrayList<>();

        sourceCode = processCode(sourceCode);

        for(String line : sourceCode.split("\n")){
            String aux = "";
            CodeLine newLine = new CodeLine(line, posLine++);
            lines.add(newLine);
            
            for(int flag = 0; flag < newLine.getContent().length(); flag++ ){
                if(newLine.getContent().charAt(flag) != ' '){
                    int end = newLine.getContent().indexOf(" ", flag);
                    
                    if(end > -1 ) aux = newLine.getContent().substring(flag,end);
                    else aux = newLine.getContent().substring(flag);
                    
                    Token newToken = new Token(idToken++, aux, newLine, "undefined", flag, end-1);
                    tokens.add(newToken);
                    flag += aux.length();
                }
            }
        }
        //this.tokens =  this.calcularPosicaoOriginal(tokens, codigoFonte);
        //this.tokens = this.chainAnaliseLexica(tokens);
        this.fillTable(tokens);
        //this.gerenciadorErro = new AnalisadorSintatico(view).analiseSintatica(tokens, gerenciadorErro);
        //this.preencheTabelaErro(tokens);
    }
    
    private void fillTable(List<Token> tokens){
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "Linha", "Lexema", "Token"}, 0) {
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        }; 
        
        for(Token token : tokens){
            tableModel.addRow(new Object[]{
                token.getId(),
                token.getLine().getPosition(),
                token.getSymbol(),
                token.getCategory()
            });
        }
        
        this.view.getTblLexica().setModel(tableModel);
    }
}
