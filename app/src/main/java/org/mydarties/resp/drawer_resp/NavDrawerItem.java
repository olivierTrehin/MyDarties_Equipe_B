package org.mydarties.resp.drawer_resp;

/**
 * Created by DartiesB on 02/12/2016.
 */

public class NavDrawerItem {

    String navTittle;
    int navIcon;

    public NavDrawerItem(String title, int icon){
        this.navTittle = title;
        this.navIcon = icon;
    }

    public String getNavTittle() {
        return navTittle;
    }

    public void setNavTittle(String navTittle) {
        this.navTittle = navTittle;
    }

    public int getNavIcon() {
        return navIcon;
    }

    public void setNavIcon(int navIcon) {
        this.navIcon = navIcon;
    }
}
