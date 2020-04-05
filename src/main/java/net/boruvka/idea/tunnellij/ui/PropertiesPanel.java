package net.boruvka.idea.tunnellij.ui;

import net.boruvka.idea.tunnellij.TunnelConfig;
import net.boruvka.idea.tunnellij.net.TunnelListener;
import net.boruvka.idea.tunnellij.util.PortNumberVerifier;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author pingd
 */
public class PropertiesPanel extends JPanel implements TunnelListener {

    private JTextField srcPort;

    private JTextField destHost;

    private JTextField destPort;

    private PortNumberVerifier portNumberVerifier = new PortNumberVerifier();

    private boolean isRunning = false;

    public boolean isRunning() {
        return isRunning;
    }

    public PropertiesPanel() {
        super();
        initComponents();
    }

    protected void initComponents() {
        setLayout(new BorderLayout());

        JPanel subPanelAddress = new JPanel();
        subPanelAddress.setBorder(new TitledBorder("properties"));

        srcPort = new JTextField(TunnelConfig.getSourcePort());
        srcPort.setInputVerifier(portNumberVerifier);
        srcPort.setHorizontalAlignment(JTextField.RIGHT);
        srcPort.setColumns(5);

        destPort = new JTextField(TunnelConfig
                .getDestinationPort());
        destPort.setInputVerifier(portNumberVerifier);
        destPort.setHorizontalAlignment(JTextField.RIGHT);
        destPort.setColumns(5);

        destHost = new JTextField(TunnelConfig
                .getDestinationString());
        destHost.setHorizontalAlignment(JTextField.RIGHT);
        destHost.setColumns(24);

        subPanelAddress.add(new JLabel("tunnel from localhost:"));
        subPanelAddress.add(srcPort);
        subPanelAddress.add(new JLabel("to "));
        subPanelAddress.add(destHost);
        subPanelAddress.add(new JLabel(":"));
        subPanelAddress.add(destPort);

        add(subPanelAddress, BorderLayout.SOUTH);
    }

    public void setControlPanelEditable(boolean b) {

        TunnelConfig.properties.put(TunnelConfig.DST_HOST,
                destHost.getText());
        TunnelConfig.properties.put(TunnelConfig.DST_PORT,
                destPort.getText());
        TunnelConfig.properties.put(TunnelConfig.SRC_PORT,
                srcPort.getText());

        srcPort.setEditable(b);
        destHost.setEditable(b);
        destPort.setEditable(b);

        srcPort.setEnabled(b);
        destHost.setEnabled(b);
        destPort.setEnabled(b);
    }

    public int getSrcPort() {
        return Integer.parseInt(srcPort.getText());
    }

    public int getDestPort() {
        return Integer.parseInt(destPort.getText());
    }

    public String getDestHost() {
        return destHost.getText();
    }

    @Override
    public void tunnelStarted() {
        isRunning = true;
        setControlPanelEditable(false);
    }

    @Override
    public void tunnelStopped() {
        isRunning = false;
        setControlPanelEditable(true);
    }

}

