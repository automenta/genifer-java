/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genifer.gui;

import genifer.Genifer;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Panel representing an interface to Genifer
 * @author me
 */
public class GeniferPanel extends JPanel {

    public final Genifer genifer;
    private final JTextField inputText;
    private final JTextArea outputText;

    public GeniferPanel(Genifer g) {
        super(new BorderLayout());
        this.genifer = g;

        outputText = new JTextArea();
        add(outputText, BorderLayout.CENTER);

        inputText = new JTextField();
        add(inputText, BorderLayout.SOUTH);

        inputText.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enter();
                }
            }

        });

    }
    public void enter() {
        String t = inputText.getText();

        Object result = genifer.eval(t);
        outputText.append("> " + t + "\n" + result.toString() + "\n\n");

        inputText.setText("");
    }

    public static void main(String[] args) {
        Genifer g = new Genifer();

        JFrame window = new JFrame("Genifer");
        window.add(new GeniferPanel(g));
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}
