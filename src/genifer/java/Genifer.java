/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genifer.java;

import clojure.lang.RT;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * interface to Genifer clojure
 * @author me
 */
public class Genifer {

    public Genifer() {
        try {
            RT.loadResourceScript("genifer/core.clj");
        } catch (IOException ex) {
            Logger.getLogger(Genifer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    Object eval(String t) {       
        return RT.var("genifer.core", "repl1").invoke(t);
    }

    
//    public void induce();
//    public void abduce(String input);
//    public void backwardChain(String query);
//    
//    public void setDebug(int level);
//    
//    public Memory getMemory();    
    
    public static void main(String[] args) throws Exception {
        System.out.println(new Genifer().eval("(+ 1 2)"));
    }
}
