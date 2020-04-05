package net.boruvka.idea.tunnellij.ui;

import com.intellij.openapi.ui.Messages;
import com.intellij.ui.Gray;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import net.boruvka.idea.tunnellij.net.Call;
import net.boruvka.idea.tunnellij.net.Tunnel;
import net.boruvka.idea.tunnellij.net.TunnelException;
import net.boruvka.idea.tunnellij.net.TunnelListener;

import javax.swing.*;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author boruvka
 */
public class TunnelPanel extends JPanel implements TunnelListener {

    public static final int DIVIDER_SIZE = 1;

    private JBList<Call> list;

    private DefaultListModel<Call> model;

    private ViewersPanel viewers;

    private PropertiesPanel propertiesPanel = new PropertiesPanel();

    private Tunnel tunnel;

    public TunnelPanel() {
        model = new DefaultListModel<>();
        list = new JBList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        viewers = new ViewersPanel();
        viewers.setBorder(null);
        list.addListSelectionListener(e -> {
            JBList list = (JBList) e.getSource();
            Call call = (Call) list.getSelectedValue();

            if (call != null) {
                viewers.view(call);
            } else {
                viewers.clear();
            }
        });

        list.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    clearSelected();
                }
            }
        });

        setLayout(new BorderLayout());
        initComponents();
    }

    protected void initComponents() {
        list.setBackground(UIManager.getColor("Tree.textBackground"));
        list.setVisibleRowCount(3);

        JPanel topPanel = new JPanel(new BorderLayout());
        JScrollPane jScrollPane = new JBScrollPane(list);
        jScrollPane.setBorder(null);
        topPanel.add(jScrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(viewers, BorderLayout.CENTER);

        JSplitPane splitPaneTopBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        SplitPaneUI ui = splitPaneTopBottom.getUI();
        if (ui instanceof BasicSplitPaneUI) {
            ((BasicSplitPaneUI) ui).getDivider().setBorder(BorderFactory.createLineBorder(Gray._201));
        }
        splitPaneTopBottom.setDividerLocation(0.20d);
        splitPaneTopBottom.setResizeWeight(0.20d);
        splitPaneTopBottom.setDividerSize(DIVIDER_SIZE);

        splitPaneTopBottom.add(topPanel, JSplitPane.TOP);
        splitPaneTopBottom.add(bottomPanel, JSplitPane.BOTTOM);

        topPanel.setBorder(null);
        bottomPanel.setBorder(null);
        splitPaneTopBottom.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Gray._201));

        add(splitPaneTopBottom, BorderLayout.CENTER);
    }

    public void start() {
        new Thread(() -> {
            tunnel = new Tunnel(propertiesPanel.getSrcPort(),
                    propertiesPanel.getDestPort(), propertiesPanel.getDestHost());
            try {
                tunnel.addTunnelListener(this);
                tunnel.addTunnelListener(propertiesPanel);
                tunnel.start();
            } catch (TunnelException e) {
                Messages.showMessageDialog("Error when starting server: "
                        + e.getMessage(), "Error", Messages.getErrorIcon());
                e.printStackTrace();
            }
        }).start();
    }

    public void stop() {
        new Thread(() -> {
            tunnel.stop();
        }).start();
        repaint();
    }

    @Override
    public synchronized void newCall(Call call) {
        model.addElement(call);
    }

    @Override
    public synchronized void endCall(Call call) {
        if (list.isVisible()) {
            list.repaint();
            viewers.repaint();
        }

    }

    public void wrap() {
        viewers.wrap();
    }

    public void unwrap() {
        viewers.unwrap();
    }

    public synchronized void clear() {
        model.clear();
    }

    public synchronized void clearSelected() {
        int index = list.getSelectedIndex();
        if (index != -1) {
            model.removeElementAt(index);
        }
    }

    public boolean isRunning() {
        return propertiesPanel.isRunning();
    }

    public PropertiesPanel getPropertiesPanel() {
        return propertiesPanel;
    }
}