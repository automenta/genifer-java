/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genifer;

import clojure.lang.RT;
import genifer.Genifer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * interface to Genifer clojure
 *
 * @author me
 */
public class Genifer {

    public Genifer() {

        System.setProperty("file.encoding", "gb18030");

        try {
            RT.loadResourceScript("genifer/core.clj");
        } catch (IOException ex) {
            Logger.getLogger(Genifer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            RT.loadResourceScript("genifer/io.clj");
        } catch (IOException ex) {
            Logger.getLogger(Genifer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Object eval(String t) {
        return RT.var("genifer.core", "repl1").invoke(t);
    }

    public Object formularize(String t, String u) {
        return RT.var("genifer.io", "formularize").invoke(t, u);
    }

    public Object sendFormula(String t) {
        return RT.var("genifer.io", "send-formula").invoke(t);
    }
	
	public Object sendFormulaScala(String t) {
		return genifer3.Genifer3.showMyPower(10);
	}

    public Object getFormula(String t) {
        return RT.var("genifer.io", "get-formula").invoke(t);
    }

	//    public void induce();
//    public void abduce(String input);
//    public void backwardChain(String query);
//
//    public void setDebug(int level);
//
//    public Memory getMemory();
//    public static void main(String[] args) throws Exception {
//        System.out.println(new Genifer().eval("(+ 1 2)"));
//    }
}
