package com.soniyck.taborganizer;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.TextIcon;
import com.intellij.ui.tabs.TabInfo;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.intellij.openapi.diagnostic.Logger;

import javax.swing.*;

public class TabOrganizerManager implements FileEditorManagerListener {
    private static final Logger log = Logger.getInstance("TabOrganizerManager");

    private static final Comparator<VirtualFile> comparer = (a,b)->{
        String aPath = a.getPath();
        String bPath = b.getPath();
        if (!aPath.equals(bPath)) {
            return aPath.compareTo(bPath);
        }

        String aName = a.getName();
        String bName = b.getName();
        return aName.compareTo(bName);
    };

    @Override
    public void fileOpened(@NotNull FileEditorManager manager, @NotNull VirtualFile file) {
        var windows = ((FileEditorManagerEx)manager).getWindows();
        for (var window : windows) {
            reorderTabsInWindow(window, file);
        }
    }

    private void reorderTabsInWindow(EditorWindow window, VirtualFile file) {
        // Automatically set placement to left
        window.setTabsPlacement(SwingConstants.LEFT);

        // Logging for debugging => delete
        logTabsArray(window.getTabbedPane().getTabs().getTabs(), "Unordered tabs:");

        // Order the opened files
        VirtualFile[] orderedFiles = window.getFiles();
        Arrays.sort(orderedFiles, comparer);

        logFilesArray(orderedFiles, "Ordered files:");

        // Logging for debugging => delete
        logTabsArray(window.getTabbedPane().getTabs().getTabs(), "Unordered tabs:");

        // Get the tab editor component
        var tabEditor = window.getTabbedPane();
        var tabs = tabEditor.getTabs();

        // Build a list of groups holding files with associated tabs
        // TODO: fix concurrent names
        List<TabFileGroup> groups = new ArrayList<>();
        for (VirtualFile orderedFile : orderedFiles) {
            // TODO: switch based on granularity
            String groupPath = orderedFile.getParent().getPath();
            String groupName = orderedFile.getParent().getName();

            TabFileGroup group = null;
            for (TabFileGroup g : groups) {
                if (groupPath.equals(g.groupPath) && groupName.equals(g.groupName)) {
                    group = g;
                    break;
                }
            }

            if (group == null) {
                var defaultDivider = createDividerTab(window, tabs.getTabAt(0).getComponent(), groupName);
                group = TabFileGroup.AllocateTabFileGroup(groupPath, groupName, tabs.getTabs(), defaultDivider);
                groups.add(group);
            }

            var tabFile = TabFile.AssociateFileWithTab(orderedFile, tabs.getTabs());
            group.tabs.add(tabFile);
        }

        // Update the tabs according to built list
        for (TabInfo tab : tabs.getTabs()) {
            tabs.removeTab(tab);
        }

        for (TabFileGroup group : groups) {

            for (TabFile tabFile : group.tabs) {
                tabs.addTab(tabFile.tabInfo);

                // Select the opened file
                if (tabFile.virtualFile.equals(file)) {
                    tabEditor.setSelectedIndex(tabs.getIndexOf(tabFile.tabInfo));
                }
            }

            // for some reason must initialize this after the tabs... wellp what u gonna do
            // TODO: render divider before tabs, I think setting at specific index doesnt work much
            tabs.addTab(group.divider);
        }

        // Logging for debugging => delete
        logGroups(groups, "Groups:");

        // Logging for debugging => delete
        logTabsArray(window.getTabbedPane().getTabs().getTabs(), "Ordered tabs:");
    }

    private static TabInfo createDividerTab(EditorWindow window, JComponent component, String title) {
        TabInfo dividerTab = new TabInfo(component);
        dividerTab.setText(""); // No text for the tab itself
        dividerTab.setEnabled(false); // Disable the TabInfo to make it non-clickable
        dividerTab.clearText(true);
        dividerTab.setText(title);
        dividerTab.setIcon(new TextIcon("-", Color.black, Color.white, 0));

        return dividerTab;
    }

    private void logFilesArray(VirtualFile[] files, String message) {
        StringBuilder f = new StringBuilder();

        for (VirtualFile file : files) {
            f.append(file.getName()).append("  ").append(file.getPath()).append("\n");
        }

        log.error(message + "\n" + f);
    }

    private void logTabsArray(List<TabInfo> tabs, String message) {
        StringBuilder f = new StringBuilder();

        for (TabInfo tab : tabs) {
            var tabFileNameParts = tab.getText().split("\\/");
            var tabFileName = tabFileNameParts[tabFileNameParts.length - 1];
            f.append(tabFileName).append("\n");
        }

        log.error(message + "\n" + f);
    }

    private void logGroups(List<TabFileGroup> groups, String message) {
        StringBuilder f = new StringBuilder();

        for (TabFileGroup group : groups) {
            f.append(group.groupPath).append(" - ").append(group.groupName).append("\n");

            for (TabFile tabFile : group.tabs) {
                f.append(tabFile.virtualFile.getName()).append(tabFile.tabInfo.getText()).append("\n");
            }
        }

        log.error(message + "\n" + f);
    }
}





