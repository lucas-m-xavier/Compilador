/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.compiler.syntatic;

import com.ufes.compiler.model.ErrorCollection;
import com.ufes.compiler.model.Token;
import com.ufes.compiler.view.MainView;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Lucas
 */
public class Syntatic {
    private MainView view;
    private List<Token> originalTokens;
    private List<Token> tokens;            
    private JTree syntaticTree;
    private ArrayList<DefaultMutableTreeNode> nodeList;
    private ErrorCollection handlerErrors;
    private Token parsedToken;
    private ArrayList<Token> stackBlock;

    public Syntatic(MainView view) {
        this.view = view;
        nodeList = new ArrayList<>();
    }
    
    public ErrorCollection syntaxAnalisys(List<Token> tokens, ErrorCollection errors){
        this.tokens = new ArrayList<Token>();
        this.tokens.addAll(tokens);
        this.originalTokens = tokens;
        this.handlerErrors = errors;
        this.createSyntacticTree();
        stackBlock = new ArrayList<>();
        this.analyzeToken();
        if(!stackBlock.isEmpty()){
            this.msgErro("<}>");
        }
        for (int i = 0; i < syntaticTree.getRowCount(); i++) {
            syntaticTree.expandRow(i);
            syntaticTree.setShowsRootHandles(true);
        }
        return handlerErrors;
    }
    
    private void createSyntacticTree() {
        DefaultMutableTreeNode no = new DefaultMutableTreeNode("Program");
        DefaultTreeModel model = new DefaultTreeModel(no);
        LookAndFeel previousLF = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            syntaticTree = new JTree(model);
            UIManager.setLookAndFeel(previousLF);
            syntaticTree.putClientProperty("JTree.lineStyle", "Angled");
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(this.view,"Error: "+e.getMessage(), "Error creating syntax tree", JOptionPane.ERROR_MESSAGE);
        }
        nodeList.add(no);
        JScrollPane blocoArvoreSintatica = new JScrollPane(syntaticTree);
        blocoArvoreSintatica.setViewportView(syntaticTree);
        if(this.view.getTabPanelResultados().getTabCount() <= 1){
            this.view.getTabPanelResultados().add("Árvore de Análise Sintática", blocoArvoreSintatica);
        }else{
            this.view.getTabPanelResultados().removeTabAt(1);
            this.view.getTabPanelResultados().add("Árvore de Análise Sintática", blocoArvoreSintatica);
        }
        syntaticTree.setShowsRootHandles(true);
    }
    
    private DefaultMutableTreeNode insertNewNode(DefaultMutableTreeNode pai, String filho) {
        DefaultMutableTreeNode novo = new DefaultMutableTreeNode(filho);
        pai.add(novo);
        nodeList.add(novo);
        return novo;
    }
    
    private void analyzeToken(){
         try {
            if (!program()) {
                if (!instruction()) {
                    if (parsedToken.getSymbol().equals("}")) {
                        if (!stackBlock.isEmpty()) {
                            stackBlock.remove(0);
                            this.tokens.remove(0);
                        } else {
                            this.msgErro(null);
                            recuperarErro();
                        }
                    } else {
                        this.msgErro(null);
                        recuperarErro();
                    }
                    recuperarErro();
                    while (!this.tokens.isEmpty()) {
                        analyzeToken();
                    }
                } else {
                    while (!this.tokens.isEmpty()) {
                        analyzeToken();
                    }
                }
            } else {
                while (!this.tokens.isEmpty()) {
                    analyzeToken();
                }
            }
        } catch (Exception ex) {
            recuperarErro();
            while (!this.tokens.isEmpty()) {
                analyzeToken();
            }
        }
        if(!this.tokens.isEmpty()){
            analyzeToken();
        }
    }
    
    private boolean program() throws Exception {
        if (!this.tokens.isEmpty()) {
            if (tipo()) {
                if (id()) {
                    program2();
                } else {
                    this.msgErro("<identificador>");
                }
                return true;
            }
        }
        return false;
    }

    private void program2() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<programa2>");
            if (abreParentese()) {
                params();
                if (fechaParentese()) {
                    codeBlock();
                } else {
                    msgErro("<)>");
                }
            } else if(num()){
                if(!delimitadorInstrucaoPontoEVirgula()){
                    msgErro("<;>");
                }
            } else {
                idList();
            }
            nodeList.remove(nodeList.size() - 1);
            program();
        }
    }

    private void idList() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<listaID>");
            if (abreColchete()) {
                if (num()) {
                    if (fechaColchete()) {
                        param2Statement();
                    } else {
                        msgErro("<]>");
                    }
                } else {
                    msgErro("<NUM>");
                }
            } else {
                param2Statement();
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private void param2Statement() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<declaracaoParam2>");
            if (separadorVirgula()) {
                if (id()) {
                    idList();
                } else {
                    this.msgErro("<identificador>");
                }
            } else if (!delimitadorInstrucaoPontoEVirgula()) {
                msgErro("<,> ou <;>");
            }
            nodeList.remove(nodeList.size() - 1);
        }else{
            msgErro("<,> ou <;>");
        }
    }

    private boolean params() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<listaParametros>");
            if (tipo()) {
                if (id()) {
                    params2();
                    nodeList.remove(nodeList.size() - 1);
                    return true;
                } else {
                    msgErro("<identificador>");
                }
            }
            nodeList.remove(nodeList.size() - 1);
        }
        return false;
    }

    private void params2() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<listaParamRestante>");
            if (abreColchete()) {
                if (num()) {
                    if (fechaColchete()) {
                        paramList();
                    } else {
                        msgErro("<]>");
                    }
                } else {
                    msgErro("<NUM>");
                }
            } else {
                paramList();
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private void paramList() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<listaParametros>");
            if (separadorVirgula()) {
                if (!params()) {
                    this.msgErro("<tipo>");
                }
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private boolean codeBlock() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<bloco>");
            if (begin()) {
                blockContent();
                if (!end() || !endPonto()) {
                    this.msgErro("<}>");
                }
                nodeList.remove(nodeList.size() - 1);
                return true;
                
            } else if (!delimitadorInstrucaoPontoEVirgula()) {
                this.msgErro("<{> ou <;>");
            }
            nodeList.remove(nodeList.size() - 1);
        }
        return false;
    }

    private void blockContent() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<conjuntoInst>");
            if (instruction() || program()) {
                blockContent();
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private boolean instruction() throws Exception {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucoes>");
            if (id()) {
                instruction2();
                if (!delimitadorInstrucaoPontoEVirgula()) {
                    this.msgErro("<;>");
                }
                retorno = true;
            } else if (instrucaoWriteln()) {
                if (abreParentese()) {
                    idInstructionParameters2();
                    if (fechaParentese()) {
                        if (!delimitadorInstrucaoPontoEVirgula()) {
                            msgErro("<;>");
                        }
                    } else {
                        msgErro("<)>");
                    }
                } else {
                    msgErro("<(>");
                }
                retorno = true;
            } else if (instrucaoWrite()) {
                if (abreParentese()) {
                    idInstructionParameters2();
                    if (fechaParentese()) {
                        if (!delimitadorInstrucaoPontoEVirgula()) {
                            msgErro("<;>");
                        }
                    } else {
                        msgErro("<)>");
                    }
                } else {
                    msgErro("<(>");
                }
                retorno = true;
            } else if (var()) {
                retorno = true;
            } else if (instrucaoReadln()) {
                if (abreParentese()) {
                    idInstructionParameters2();
                    if (fechaParentese()) {
                        if (!delimitadorInstrucaoPontoEVirgula()) {
                            msgErro("<;>");
                        }
                    } else {
                        msgErro("<)>");
                    }
                } else {
                    msgErro("<(>");
                }
                retorno = true;
            } else if (programInstruction()) {
                retorno = true;
            } else if (instrucaoRead()) {
                if (abreParentese()) {
                    if (id()) {
                        if (fechaParentese()) {
                            if (!delimitadorInstrucaoPontoEVirgula()) {
                                msgErro("<;>");
                            }
                        } else {
                            msgErro("<)>");
                        }
                    } else {
                        msgErro("<identificador>");
                    }
                } else {
                    msgErro("<(>");
                }
                retorno = true;
            } else if (instrucaoIF()) {
                if (abreParentese()) {
                    parametrosIF();
                    if (fechaParentese()) {
                        continuaInstrucaoIF();
                    } else {
                        msgErro("<)>");
                    }
                } else {
                    msgErro("<(>");
                }
                retorno = true;
            }
            nodeList.remove(nodeList.size() - 1);
        }
        return retorno;
    }

    private void instruction2() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<expressao>");
            if (abreColchete()) {
                if (num()) {
                    if (fechaColchete()) {
                        if (operadorDeAtribuicao()) {
                            atribuicao();
                        } else {
                            this.msgErro("<atribuicao>");
                        }
                    } else {
                        this.msgErro("<]>");
                    }
                } else {
                    this.msgErro("<NUM>");
                }
            } else if (abreParentese()) {
                idInstructionParameters();
                if (!fechaParentese()) {
                    this.msgErro(")");
                }
            } else if (operadorDeAtribuicao()) {
                atribuicao();
            } else {
                this.msgErro("<atribuicao>, <[> ou <(>");
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private void idInstructionParameters() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucoes>");
            if (abreParentese()) {
                idInstructionParameters();
                if (!fechaParentese()) {
                    this.msgErro(")");
                }
            } else if (!expressaoUnaria()) {
                if (operando()) {
                    expressaoBinaria();
                }
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private void idInstructionParameters2() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucoes>");
            if (abreParentese()) {
                idInstructionParameters2();
                if (!fechaParentese()) {
                    this.msgErro(")");
                }
            } else if (!expressaoUnaria()) {
                if (operando()) {
                    expressaoBinaria();
                } else {
                    this.msgErro("<exprUnary>, <operador> ou <(>");
                }
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private void parametrosIF() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucoesIF>");
            if (abreParentese()) {
                parametrosIF();
                if (!fechaParentese()) {
                    this.msgErro("<)>");
                }
            } else if (!expressaoUnaria()) {
                if (operando()) {
                    expressaoBinaria();
                } else {
                    this.msgErro("<exprUnary>, <operador> ou <(>");
                }
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private void continuaInstrucaoIF() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucoesIF>");
            if (!program()) {
                if (!instruction()) {
                    if (codeBlock()) {
                        continuaBlocoInstrucaoIF();
                    } else {
                        this.msgErro("<programa>, <bloco> ou <instrucao>");
                    }
                } else {
                    continuaBlocoInstrucaoIF();
                }
            } else {
                continuaBlocoInstrucaoIF();
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private void continuaBlocoInstrucaoIF() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucoesIF>");
            if (instrucaoELSE()) {
                continuaInstrucaoELSE();
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private void continuaInstrucaoELSE() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucoesELSE>");
            if (!instruction()) {
                if (!program()) {
                    if (!codeBlock()) {
                        this.msgErro("<programa>, <bloco>, <instrucao>");
                    }
                }
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private void atribuicao() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<atribuicao>");
            if (abreParentese()) {
                atribuicao();
                if (!fechaParentese()) {
                    this.msgErro("<)>");
                }
            } else {
                if (!expressaoUnaria()) {
                    if (operando()) {
                        expressaoBinaria();
                    } else {
                        this.msgErro("<exprUnary>, <operador> ou <(>");
                    }
                }
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private void expressaoBinaria() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<expr>");
            if (operadorExpressaoBinaria()) {
                operandoExpressaoBinaria();
                expressaoBinaria();
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private void operandoExpressaoBinaria() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<expr>");
            if (!operando()) {
                if (!expressaoUnaria()) {
                    this.msgErro("<primary> ou <exprUnary>");
                }
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private boolean expressaoUnaria() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<exprUnary>");
            if ((operadorAritmeticoDeSoma()) || (operadorAritmeticoDeSubtracao())) {
                continuaExpressaoUnaria();
                nodeList.remove(nodeList.size() - 1);
                return true;
            }
        }
        return false;
    }

    private void continuaExpressaoUnaria() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<exprParenthesis>");
            if (abreParentese()) {
                continuaExpressaoUnariaParenteses();
                if (!fechaParentese()) {
                    this.msgErro(")");
                }
            } else {
                operando();
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private void continuaExpressaoUnariaParenteses() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<expr>");
            if (!expressaoUnaria()) {
                if (operando()) {
                    expressaoBinaria();
                } else {
                    this.msgErro("<exprUnary> ou <primary>");
                }
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private boolean operando() throws Exception {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<primary>");
            if (num()) {
                retorno = true;
            } else if (literal()) {
                retorno = true;
            } else {
                if (id()) {
                    retorno = true;
                    continuaOperandoID();
                }
            }
            nodeList.remove(nodeList.size() - 1);
        }
        return retorno;
    }

    private void continuaOperandoID() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<primaryID>");
            if (abreColchete()) {
                if (num()) {
                    if (!fechaColchete()) {
                        this.msgErro("<]>");
                    }
                } else {
                    this.msgErro("<NUM>");
                }
            } else if (abreParentese()) {
                listaDeExpressao();
                if (!fechaParentese()) {
                    this.msgErro("<)>");
                }
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private boolean listaDeExpressao() throws Exception {
        boolean retorno = false;
        insertNewNode(nodeList.get(nodeList.size() - 1), "<exprList>");
        if (abreParentese()) {
            listaDeExpressao();
            retorno = true;
            if (!fechaParentese()) {
                this.msgErro("<)>");
            }
        } else if (expressaoUnaria()) {
            continuaListaDeExpressao();
            retorno = true;
        } else if (operando()) {
            expressaoBinaria();
            continuaListaDeExpressao();
            retorno = true;
        }
        nodeList.remove(nodeList.size() - 1);
        return retorno;
    }

    private void continuaListaDeExpressao() throws Exception {
        if (!this.tokens.isEmpty()) {
            insertNewNode(nodeList.get(nodeList.size() - 1), "<exprListTail>");
            if (separadorVirgula()) {
                if (!listaDeExpressao()) {
                    this.msgErro("<exprUnary>, <primary>");
                }
            }
            nodeList.remove(nodeList.size() - 1);
        }
    }

    private boolean operadorExpressaoBinaria() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "+":
                case "-":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<exprPlus2>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
                case "*":
                case "/":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<exprMult2>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
                case "==":
                case "!=":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<exprEqual2>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
                case "<":
                case ">":
                case "<=":
                case ">=":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<exprRelational2>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
                case "or":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<exprOr>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
                case "and":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<exprAnd>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }

    private boolean operadorAritmeticoDeSoma() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "+":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<operadorAritmeticoDeSoma>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }

    private boolean operadorAritmeticoDeSubtracao() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "-":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<operadorAritmeticoDeSubtracao>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }

    private boolean operadorDeAtribuicao() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case ":=":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<operadorAtrib>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }

    private boolean literal() {
        boolean retorno = false;
        Token temp;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            if (parsedToken.getCategory().equals("ASPAS")) {
                this.tokens.remove(0);
                parsedToken = this.tokens.get(0);
                if (parsedToken.getCategory().equals("identificador")) {
                    this.tokens.remove(0);
                    temp = parsedToken;
                    parsedToken = this.tokens.get(0);
                    if (parsedToken.getCategory().equals("ASPAS")) {
                        this.tokens.remove(0);
                        insertNewNode(nodeList.get(nodeList.size() - 1), "<literal>");
                        insertNewNode(nodeList.get(nodeList.size() - 1), "'" + parsedToken.getSymbol()+ "'");
                        nodeList.remove(nodeList.size() - 1);
                        nodeList.remove(nodeList.size() - 1);
                        retorno = true;
                    }
                }
            }
        }
        return retorno;
    }

    private boolean instrucaoIF() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "if":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<IF>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }

    private boolean instrucaoELSE() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "else":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<ELSE>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }

    private boolean instrucaoWriteln() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "writeln":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucaoWriteln>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }
    
    private boolean instrucaoWrite() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "write":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucaoWrite>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }
    
    private boolean instrucaoRead() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "read":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucaoRead>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }

    private boolean instrucaoReadln() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "readln":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucaoReadln>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }
    
    private boolean var() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "var":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucaoVar>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }

    private boolean tipo() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "CHAR":
                case "REAL":
                case "DOUBLE":
                case "STRING":
                case "INT":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<tipo>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }

    private boolean delimitadorInstrucaoPontoEVirgula() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "PONTO_E_VIRGULA":
                    this.tokens.remove(0);
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<delimitadorPontoEVirgula>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    retorno = true;
                    break;
            }
        }
        return retorno;
    }

    private boolean separadorVirgula() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case ",":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<separadorVirgula>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }

    private boolean abreColchete() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "[":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucaoAbreColchete>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }

    private boolean fechaColchete() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "]":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucaoFechaConlchete>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }
    
    private boolean then() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "then":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucaoThen>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }

    private boolean abreParentese() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "(":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucaoAbreParensete>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }

    private boolean fechaParentese() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case ")":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucaoFechaParentese>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }

    private boolean begin() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "begin":
                    this.tokens.remove(0);
                    retorno = true;
                    stackBlock.add(parsedToken);
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucaoBegin>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }

    private boolean end() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "end":
                    this.tokens.remove(0);
                    retorno = true;
                    if (!stackBlock.isEmpty()) {
                        stackBlock.remove(0);
                    }
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucaoEnd>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }
    
    private boolean endPonto() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "end.":
                    this.tokens.remove(0);
                    retorno = true;
                    if (!stackBlock.isEmpty()) {
                        stackBlock.remove(0);
                    }
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucaoEnd>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }
    
    private boolean programInstruction() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "program":
                    this.tokens.remove(0);
                    retorno = true;
                    if (!stackBlock.isEmpty()) {
                        stackBlock.remove(0);
                    }
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<instrucaoProgram>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }

    private boolean id() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "identificador":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<identificador>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
            }
        }
        return retorno;
    }

    private boolean num() {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            switch (parsedToken.getCategory()) {
                case "digito":
                    this.tokens.remove(0);
                    retorno = true;
                    insertNewNode(nodeList.get(nodeList.size() - 1), "<NUM>");
                    insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol());
                    nodeList.remove(nodeList.size() - 1);
                    nodeList.remove(nodeList.size() - 1);
                    break;
                case "-":
                case "+":
                    if (!this.tokens.isEmpty()) {
                        Token temp = this.tokens.get(1);
                        if (temp.getCategory().equals("digito")) {
                            this.tokens.remove(0);
                            this.tokens.remove(0);
                            retorno = true;
                            insertNewNode(nodeList.get(nodeList.size() - 1), "<NUM>");
                            insertNewNode(nodeList.get(nodeList.size() - 1), parsedToken.getSymbol()+ temp.getSymbol());
                            nodeList.remove(nodeList.size() - 1);
                            nodeList.remove(nodeList.size() - 1);
                        }
                    }
                    break;
            }
        }
        return retorno;
    }

    private boolean CRLF(int pLinha) {
        boolean retorno = false;
        if (!this.tokens.isEmpty()) {
            parsedToken = this.tokens.get(0);
            if (parsedToken.getLine().getPosition()!= pLinha) {
                retorno = true;
                insertNewNode(nodeList.get(nodeList.size() - 1), "<CRLF>");
                insertNewNode(nodeList.get(nodeList.size() - 1), "\\n");
                nodeList.remove(nodeList.size() - 1);
                nodeList.remove(nodeList.size() - 1);
            }
        }
        return retorno;
    }

    private void recuperarErro() {
        if (!this.tokens.isEmpty()) {
            Token temp;
            temp = this.tokens.get(0);
            while ((!this.tokens.isEmpty()) && (!tokenSincronizador(temp))) {
                if (!this.tokens.isEmpty()) {
                    this.tokens.remove(0);
                }
                if (!this.tokens.isEmpty()) {
                    temp = this.tokens.get(0);
                }
            }
            if (!this.tokens.isEmpty()) {
                if (this.tokens.get(0).getSymbol().equals(";")) {
                    this.tokens.remove(0);
                }
            }
        }
        insertNewNode(nodeList.get(nodeList.size() - 1), "<ERRO>");
        nodeList.remove(nodeList.size() - 1);
    }

    private boolean tokenSincronizador(Token pToken) {
        boolean retorno = false;
        switch (pToken.getCategory()) {
            case "PONTO_E_VIRGULA":
            case "STRING":
            case "CHAR":
            case "REAL":
            case "DOUBLE":
            case "INT":
            case "writeln":
            case "readln":
            case "write":
            case "read":
            case "begin":
            case "end":
            case "end.":
            case "IF":
                retorno = true;
                break;
        }
        return retorno;
    }

    private void msgErro(String simboloEsperado)  {
        if (parsedToken != null && !parsedToken.getCategory().equals("error")) {
            if (simboloEsperado.equals("<;>") || simboloEsperado.equals("<,> ou <;>") || simboloEsperado.equals("<}>")) {
                this.handlerErrors.addError(parsedToken, "Erro sintático. Esperado '" + simboloEsperado + "'.");
            } else {
                if (!this.tokens.isEmpty()) {
                    this.handlerErrors.addError(parsedToken, "Erro sintático para o token '" + parsedToken.getSymbol()
                        + "'. Esperado " + simboloEsperado + ", encontrado '" + parsedToken.getCategory()+ "'.");
                }
            }
        } else {
            this.handlerErrors.addError(null, "Erro sintático: Alguma instrução está faltando. "
                        + ". Esperado " + simboloEsperado);
        }
    }
}
