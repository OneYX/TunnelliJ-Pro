package net.boruvka.idea.tunnellij.ui;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author pingd
 */
public class CommonDialog extends DialogWrapper {
    private JComponent centerPanel;

    public CommonDialog(String title, JComponent centerPanel) {
        super(true);
        this.centerPanel = centerPanel;
        init();
        setTitle(title);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return centerPanel;
    }
}
