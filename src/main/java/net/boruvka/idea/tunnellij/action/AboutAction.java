package net.boruvka.idea.tunnellij.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.*;

import com.intellij.ui.components.JBScrollPane;
import net.boruvka.idea.tunnellij.ui.CommonDialog;
import net.boruvka.idea.tunnellij.ui.Icons;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author boruvka
 */
public class AboutAction extends AnAction {

    public AboutAction() {
        super("Show About Dialog", "Show About dialog", Icons.ICON_HELP);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(
                "readme.txt");
        if (is != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String s;
            try {
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
                JTextArea area = new JTextArea(20, 80);
                area.setBorder(null);
                JScrollPane areaSp = new JBScrollPane(area);
                area.setEditable(false);
                area.append(sb.toString());
                new CommonDialog("About", areaSp).show();
//                JOptionPane.showMessageDialog(null, areaSp, "About:", JOptionPane.PLAIN_MESSAGE);
                // Messages.showMessageDialog(sb.toString(), "About TunnelliJ",
                // Messages.getInformationIcon());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("stream is null!");
        }
    }
}