package com.amazon.kindle.bean;

/**
 * @time 2017/3/6 18:11
 * @des $弹窗菜单的对象
 */
public class ShareMenuItemInfo {

    private String itemName;
    private int itemLogo;
    private int action;

    public ShareMenuItemInfo(){
        super();
    }

    public ShareMenuItemInfo(String itemName, int itemLogo, int action) {
        this.itemName = itemName;
        this.itemLogo = itemLogo;
        this.action = action;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemLogo() {
        return itemLogo;
    }

    public void setItemLogo(int itemLogo) {
        this.itemLogo = itemLogo;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "ShareMenuItemInfo{" +
                "itemName='" + itemName + '\'' +
                ", itemLogo=" + itemLogo +
                ", action=" + action +
                '}';
    }
}