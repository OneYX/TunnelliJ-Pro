package net.boruvka.idea.tunnellij.action;

import net.boruvka.idea.tunnellij.TunnelPlugin;
import net.boruvka.idea.tunnellij.ui.Icons;
import net.boruvka.idea.tunnellij.ui.TunnelPanel;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * @author boruvka
 * @since
 */
public class WrapAction extends ToggleAction {

    public WrapAction() {
        super("Wrap Lines", "Wrap lines", Icons.ICON_WRAP);
    }

    private boolean selected = false;

    @Override
    public boolean isSelected(@NotNull AnActionEvent event) {
        return selected;
    }

    @Override
    public void setSelected(AnActionEvent event, boolean b) {
        selected = b;

        Project project = (Project) event.getDataContext().getData("project");
        TunnelPanel tunnelPanel = TunnelPlugin.getTunnelPanel(project);

        if (selected) {
            tunnelPanel.wrap();
        } else {
            tunnelPanel.unwrap();
        }

    }
}
