package com.pince.ut.dialog;

import com.pince.ut.LogUtil;

import java.util.LinkedList;

/**
 * @author zhenghuan
 * @date 2018/4/4
 * 多弹窗异步请求，转为同步展示的控制工具类
 */

public class MultiDialogSynchronization {

    private static MultiDialogSynchronization instance;
    private volatile LinkedList<DialogItem> dialogItems = new LinkedList<>();
    public static final int INDEX_INVALID = -1;

    private int currentShowingGlobalIndex = INDEX_INVALID;

    private MultiDialogSynchronization() {

    }

    public static MultiDialogSynchronization getInstance() {
        if (null == instance) {
            synchronized (MultiDialogSynchronization.class) {
                if (null == instance) {
                    instance = new MultiDialogSynchronization();
                }
            }
        }
        return instance;
    }

    /**
     * 根据所有Root节点的dialog对应的index进行初始化
     *
     * @param size
     */
    public void init(int size) {
        dialogItems.clear();
        for (int i = 0; i < size; ++i) {
            DialogItem item = new DialogItem(i);
            dialogItems.add(item);
        }
    }

    /**
     * 每个root节点的子dialog收到请求结果，准备显示时
     */
    public void onShow(int globalIndex, IDialogShower dialogShower) {
        if (dialogItems.getFirst().getIndex() == globalIndex) {
            //如果当前需要展示的弹窗位于list首位
            show(globalIndex, dialogShower);
        } else {
            int currentIndex = findCurrentIndexWithGlobalIndex(globalIndex);
            if (currentIndex == -1) {
                return;
            }
            dialogItems.get(currentIndex).setDialogShower(dialogShower);
            LogUtil.d("dialog pending show globalIndex = " + globalIndex);
        }
    }

    private void show(int globalIndex, IDialogShower dialogShower) {
        LogUtil.d("dialog really show globalIndex = " + globalIndex);
        currentShowingGlobalIndex = globalIndex;
        if (null != dialogShower) {
            dialogShower.show();
        }
    }

    /**
     * globalIndex对应的流程执行完毕
     * 1：每个子dialog的root节点收到请求，放弃显示或该弹窗逻辑完成时
     * :2：一条子路线走完
     *
     * @param globalIndex 需要被销毁的globalIndex
     */
    public void onFinished(int globalIndex) {
        DialogItem currentItem = getCurrentDialogItem(globalIndex);

        if (null != currentItem && dialogItems.contains(currentItem)) {
            dialogItems.remove(currentItem);
            LogUtil.d("dialog remove globalIndex = " + globalIndex);
            if (currentShowingGlobalIndex != INDEX_INVALID && currentShowingGlobalIndex < globalIndex) {
                //销毁后面的dialog时，列表中前面的dialog正在显示
                return;
            } else if (currentShowingGlobalIndex == INDEX_INVALID || currentShowingGlobalIndex == globalIndex) {
                showFirstDialog();
            } else {
                LogUtil.e("wrong status");
            }
        }
    }

    private DialogItem getCurrentDialogItem(int globalIndex) {
        int currentIndex = findCurrentIndexWithGlobalIndex(globalIndex);
        if (currentIndex == -1) {
            return null;
        }
        return dialogItems.get(currentIndex);
    }

    private void showFirstDialog() {
        if (dialogItems.size() > 0) {
            DialogItem firstItem = dialogItems.getFirst();
            if (firstItem.getDialogShower() != null) {
                show(firstItem.getIndex(), firstItem.getDialogShower());
            }
        }
    }

    private int findCurrentIndexWithGlobalIndex(int globalIndex) {
//        LogUtil.d("globalIndex = " + globalIndex + " initSize = " + initSize + " dialogItems.size() = " + dialogItems.size());
        int currentIndex = -1;
        for (int i = 0; i < dialogItems.size(); ++i) {
            if (dialogItems.get(i).getIndex() == globalIndex) {
                currentIndex = i;
                break;
            }
        }
        LogUtil.d("currentIndex = " + currentIndex);
        return currentIndex;
    }


}
