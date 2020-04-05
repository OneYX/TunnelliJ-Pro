package net.boruvka.idea.tunnellij;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import net.boruvka.idea.tunnellij.action.*;
import net.boruvka.idea.tunnellij.ui.Icons;
import net.boruvka.idea.tunnellij.ui.TunnelPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * @author boruvka
 */
public class TunnelPlugin implements ProjectComponent {

    private static TunnelPanel tunnelPanel;

    private static final String COMPONENT_NAME = "net.boruvka.idea.tunnellij.TunnelWindow";

    private static final String TOOL_WINDOW_ID = TunnelBundle.getBundle().getString("TunnelliJ.version");

    private Project project;

    public TunnelPlugin(Project project) {
        this.project = project;
    }

    @Override
    public void projectOpened() {
        initToolWindow();
    }

    @Override
    public void projectClosed() {
        unregisterToolWindow();
    }

    @NotNull
    @Override
    public String getComponentName() {
        return COMPONENT_NAME;
    }

    @Override
    public synchronized void initComponent() {
        TunnelConfig.loadProperties();
    }

    @Override
    public synchronized void disposeComponent() {
        TunnelConfig.saveProperties();
    }

    /**
     * 注册窗口
     */
    private void initToolWindow() {

        ToolWindowManager toolWindowManager = ToolWindowManager
                .getInstance(project);
        tunnelPanel = createTunnelPanel();
        ToolWindow tunnelWindow = toolWindowManager.registerToolWindow(TOOL_WINDOW_ID,
                false, ToolWindowAnchor.BOTTOM);
        tunnelWindow.setIcon(Icons.ICON_WATCH);
        Content content = ContentFactory.SERVICE.getInstance().createContent(tunnelPanel, null, false);
        tunnelWindow.getContentManager().addContent(content);

        DefaultActionGroup actionGroup = initToolbarActionGroup();
        ActionToolbar toolBar = ActionManager.getInstance()
                .createActionToolbar("tunnellij.Toolbar", actionGroup, false);

        tunnelPanel.add(toolBar.getComponent(), BorderLayout.WEST);
    }

    /**
     * 反注册窗口
     */
    private void unregisterToolWindow() {
        ToolWindowManager toolWindowManager = ToolWindowManager
                .getInstance(project);
        toolWindowManager.unregisterToolWindow(TOOL_WINDOW_ID);
    }

    /**
     * 创建面板
     * @return TunnelPanel
     */
    private TunnelPanel createTunnelPanel() {
        TunnelPanel panel = new TunnelPanel();
        panel.setBorder(null);
        panel.setBackground(UIManager.getColor("Tree.textBackground"));
        return panel;
    }

    /**
     * 创建动作组
     * @return DefaultActionGroup
     */
    private DefaultActionGroup initToolbarActionGroup() {
        DefaultActionGroup actionGroup = new DefaultActionGroup();

        AnAction startAction = new StartAction();
        AnAction stopAction = new StopAction();
        AnAction clearAction = new ClearAction();
        AnAction clearSelectedAction = new ClearSelectedAction();
        ToggleAction wrapAction = new WrapAction();
        AnAction editAction = new EditAction();
        AnAction aboutAction = new AboutAction();

        actionGroup.add(startAction);
        actionGroup.add(stopAction);
        actionGroup.add(clearSelectedAction);
        actionGroup.add(clearAction);
        actionGroup.add(wrapAction);
        actionGroup.add(editAction);
        actionGroup.add(aboutAction);

        return actionGroup;
    }

    public static TunnelPanel getTunnelPanel(Project project) {
        return tunnelPanel;
    }

}
