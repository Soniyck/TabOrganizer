package com.soniyck.taborganizer;

import com.intellij.ui.tabs.TabInfo;

import java.util.ArrayList;
import java.util.List;

public class TabFileGroup {
    public String groupPath;
    public String groupName;
    public TabInfo divider;
    public List<TabFile> tabs;

    public TabFileGroup(String groupPath, String groupName, TabInfo divider) {
        this.groupPath = groupPath;
        this.groupName = groupName;
        this.divider = divider;
        this.tabs = new ArrayList<>();
    }

    public static TabFileGroup AllocateTabFileGroup(String groupPath, String groupName, List<TabInfo> tabInfo, TabInfo defaultDivider) {
        for (TabInfo tab : tabInfo) {
            if (tab.getText() == groupName)
                return new TabFileGroup(groupPath, groupName, tab);
        }

        return new TabFileGroup(groupPath, groupName, defaultDivider);
    }
}