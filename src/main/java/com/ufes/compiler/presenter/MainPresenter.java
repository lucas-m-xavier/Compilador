/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.compiler.presenter;

import com.ufes.compiler.view.MainView;

/**
 *
 * @author Lucas
 */
public class MainPresenter {
    private MainView view;

    public MainPresenter() {
        this.view = new MainView();
        this.view.setVisible(true);
    }
    
    private String processCode(String code){
        //Remove comentários
        code = code.replaceAll("\\(\\*([\\s\\S]*)\\*\\)", "");
        code = code.replaceAll("\\{([\\s\\S]*)\\}", "");
        code = code.replaceAll("\\/\\/([\\s\\S]*)", "");
        
        code = code.replaceAll("\\[", " \\[ ");
        code = code.replaceAll("\\]", " \\] ");
        code = code.replaceAll("\\> = ", " \\>.= ");
        code = code.replaceAll("\\>", " \\> ");
        code = code.replaceAll("\\> \\.=", "\\>=");
        code = code.replaceAll("\\+ = ", " \\+.= ");
        code = code.replaceAll("\\(", " \\( ");
        code = code.replaceAll("\\)", " \\) ");
        code = code.replaceAll(",", " , ");
        code = code.replaceAll(";", " ; ");
        code = code.replaceAll("\\{", " \\{ ");
        code = code.replaceAll("\\}", " \\} ");
        code = code.replaceAll("\\'", " \\' ");
        code = code.replaceAll("\\\"", " \\\" ");
        code = code.replaceAll("\\&&", " \\&& ");
        code = code.replaceAll("\\|\\|", " \\|\\| ");
        code = code.replaceAll("\\==", " \\=.= ");
        code = code.replaceAll("\\=", " \\= ");
        code = code.replaceAll("\\= \\. \\=", "\\==");
        code = code.replaceAll("\\! = ", " \\!.= ");
        code = code.replaceAll("\\!", " \\! ");
        code = code.replaceAll("\\%", " \\% ");
        code = code.replaceAll("\\% \\.=", "\\%=");
        code = code.replaceAll("\t", " ");
        code = code.replaceAll("\\ +", " ");
        code = code.replaceAll("\\ \\n", "\\\n");
        code = code.replaceAll("\\! \\.=", "\\!=");
        code = code.replaceAll("\\< = ", " \\<.= ");
        code = code.replaceAll("\\<", " \\< ");
        code = code.replaceAll("\\< \\.=", "\\<=");
        code = code.replaceAll("\\+", " \\+ ");
        code = code.replaceAll("\\+ \\.=", "\\+=");
        code = code.replaceAll("\\- = ", " \\-.= ");
        code = code.replaceAll("\\-", " \\- ");
        code = code.replaceAll("\\- \\.=", "\\-=");
        code = code.replaceAll("\\* = ", " \\*.= ");
        code = code.replaceAll("\\*", " \\* ");
        code = code.replaceAll("\\* \\.=", "\\*=");
        code = code.replaceAll("\\% = ", " \\%.= ");
        
        return code;
    }
}
