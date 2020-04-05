package net.boruvka.idea.tunnellij.util;


import javax.swing.*;

/**
 * @author pingd
 */
public class PortNumberVerifier extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText();
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }
}
