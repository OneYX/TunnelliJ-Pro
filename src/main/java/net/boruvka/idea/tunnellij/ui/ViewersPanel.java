package net.boruvka.idea.tunnellij.ui;

import com.intellij.ui.EditorTextField;
import com.intellij.ui.Gray;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import net.boruvka.idea.tunnellij.net.Call;

import javax.swing.*;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.io.ByteArrayOutputStream;

/**
 * @author pingd
 */
public class ViewersPanel extends JPanel {

    private JTextArea left;

    private JTextArea right;

    public ViewersPanel() {
        initComponents();
    }

    protected void initComponents() {
        setLayout(new BorderLayout());

        setBackground(UIManager.getColor("Tree.textBackground"));

        left = new JBTextArea();
        right = new JBTextArea();

        left.setEditable(true);
        right.setEditable(true);

        left.setBackground(UIManager.getColor("Tree.textBackground"));
        right.setBackground(UIManager.getColor("Tree.textBackground"));

        JScrollPane leftScroll = new JBScrollPane(left);
        JScrollPane rightScroll = new JBScrollPane(right);

        leftScroll.setBorder(null);
        rightScroll.setBorder(null);

        JSplitPane splitPaneLeftRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPaneLeftRight.setBorder(null);
        SplitPaneUI ui = splitPaneLeftRight.getUI();
        if (ui instanceof BasicSplitPaneUI) {
            ((BasicSplitPaneUI) ui).getDivider().setBorder(BorderFactory.createLineBorder(Gray._201));
        }
        splitPaneLeftRight.setDividerLocation(0.50d);
        splitPaneLeftRight.setResizeWeight(0.50d);
        splitPaneLeftRight.setDividerSize(TunnelPanel.DIVIDER_SIZE);

        splitPaneLeftRight.add(leftScroll, JSplitPane.LEFT);
        splitPaneLeftRight.add(rightScroll, JSplitPane.RIGHT);

        add(splitPaneLeftRight, BorderLayout.CENTER);
    }

    public void view(Call call) {
        if (call == null) {
            return;
        }

        ByteArrayOutputStream leftBaos = (ByteArrayOutputStream) call
                .getOutputLogger();
        if (leftBaos == null) {
            return;
        }

        left.setText("");
        left.setText(leftBaos.toString());
        left.setCaretPosition(0);

        ByteArrayOutputStream rightBaos = ((ByteArrayOutputStream) call
                .getInputLogger());
        if (rightBaos == null) {
            return;
        }
        right.setText(rightBaos.toString());
        right.setCaretPosition(0);
    }

    public void wrap() {
        left.setLineWrap(true);
        left.setWrapStyleWord(true);
        right.setLineWrap(true);
        right.setWrapStyleWord(true);
    }

    public void unwrap() {
        left.setLineWrap(false);
        right.setLineWrap(false);
    }

    public void clear() {
        left.setText("");
        right.setText("");
    }

}