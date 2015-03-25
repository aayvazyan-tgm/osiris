package at.pria.osiris.osiris.view.elements.naviBar;

/**
 * Created by helmuthbrunner on 25/03/15.
 */

/**
 * A Class which contains the text and the pic-ressource for the section-item.
 *
 * source: http://www.tutecentral.com/android-custom-navigation-drawer/
 */
public class DrawerItem {

    private String itemName;
    private int imgResID;

    public DrawerItem(String itemName, int imgResID) {
        super();
        this.itemName = itemName;
        this.imgResID = imgResID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getImgResID() {
        return imgResID;
    }

    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }

}