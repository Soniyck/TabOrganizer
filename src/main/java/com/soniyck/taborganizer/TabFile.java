package com.soniyck.taborganizer;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.tabs.TabInfo;

import java.util.List;

public class TabFile {
    private static final Logger log = Logger.getInstance("TabOrganizerManager");

    public VirtualFile virtualFile;
    public TabInfo tabInfo;

    public TabFile(VirtualFile virtualFile, TabInfo tabInfo) {
        this.virtualFile = virtualFile;
        this.tabInfo = tabInfo;
    }

    public static TabFile AssociateFileWithTab(VirtualFile virtualFile, List<TabInfo> tabInfo) {
        for (TabInfo tab : tabInfo) {
            if (virtualFile.getName().endsWith(tab.getText())) {
                String group = virtualFile.getParent().getName();
                return new TabFile(virtualFile, tab);
            }
        }

        log.error("No associated tab with file " + virtualFile.getPath());
        return null;
    }
}