package com.pince.ut.dialog;

/**
 * Created by zhenghuan on 2018/4/4.
 */

public class DialogItem {

    /**
     * dialog的index，同时可以作为key
     */
    private int index;

    private IDialogShower dialogShower;


    public DialogItem(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public IDialogShower getDialogShower() {
        return dialogShower;
    }

    public void setDialogShower(IDialogShower dialogShower) {
        this.dialogShower = dialogShower;
    }
}
