package net.boruvka.idea.tunnellij.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import net.boruvka.idea.tunnellij.TunnelPlugin;
import net.boruvka.idea.tunnellij.ui.CommonDialog;
import net.boruvka.idea.tunnellij.ui.Icons;
import net.boruvka.idea.tunnellij.ui.TunnelPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author boruvka
 */
public class EditAction extends AnAction {

    public EditAction() {
        super("Edit Properties", "Edit properties", Icons.ICON_EDIT);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = (Project) event.getDataContext().getData("project");
        TunnelPanel tunnelPanel = TunnelPlugin.getTunnelPanel(project);
        new CommonDialog("Edit Properties", tunnelPanel.getPropertiesPanel()).show();
    }
}
