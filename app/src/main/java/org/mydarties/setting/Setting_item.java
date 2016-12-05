package org.mydarties.setting;

/**
 * Created by windows8 on 25/11/2016.
 */

public class Setting_item {

    private String icon;
    private String label;

    public Setting_item(String icon, String label){
        this.icon = icon;
        this.label = label;
    }

    public String getIcon() {
        return icon;
    }

    public String getLabel() {
        return label;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
