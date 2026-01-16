package org.raflab.studsluzbadesktopclient.services;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabNavigationAction {
    private final TabPane tabPane;
    private final Tab tab;

    public TabNavigationAction(TabPane tabPane, Tab tab) {
        this.tabPane = tabPane;
        this.tab = tab;
    }

    public void restore() {
        if (tabPane != null && tab != null) {
            tabPane.getSelectionModel().select(tab);
        }
    }

    public Tab getTab() { return tab; }
}