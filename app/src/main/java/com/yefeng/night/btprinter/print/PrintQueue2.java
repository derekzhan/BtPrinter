package com.yefeng.night.btprinter.print;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.yefeng.night.btprinter.base.AppInfo;
import com.yefeng.night.btprinter.bt.BtService;
import com.yefeng.night.btprinter.print.escp.params.TemplateParamBean;

import java.util.ArrayList;

/**
 * Created by yefeng on 5/28/15.
 * github:yefengfreedom
 * <p/>
 * this is print queue.
 * you can simple add print bytes to queue. and this class will send those bytes to bluetooth device
 */
public class PrintQueue2 {

    /**
     * instance
     */
    private static PrintQueue2 mInstance;
    /**
     * context
     */
    private static Context mContext;
    /**
     * print queue
     */
    private ArrayList<TemplateParamBean> mQueue;
    /**
     * bluetooth adapter
     */
    private BluetoothAdapter mAdapter;
    /**
     * bluetooth service
     */
    private BtService mBtService;


    private PrintQueue2() {
    }

    public static PrintQueue2 getQueue(Context context) {
        if (null == mInstance) {
            mInstance = new PrintQueue2();
        }
        if (null == mContext) {
            mContext = context;
        }
        return mInstance;
    }

    /**
     * add print bytes to queue. and call print
     *
     * @param bean bytes
     */
    public synchronized void add(TemplateParamBean bean) {
        if (null == mQueue) {
            mQueue = new ArrayList<TemplateParamBean>();
        }
        if (null != bean) {
            mQueue.add(bean);
        }
        print();
    }




    /**
     * print queue
     */
    public synchronized void print() {
        try {
            if (null == mQueue || mQueue.size() <= 0) {
                return;
            }
            if (null == mAdapter) {
                mAdapter = BluetoothAdapter.getDefaultAdapter();
            }
            if (null == mBtService) {
                mBtService = new BtService(mContext);
            }
            if (mBtService.getState() != BtService.STATE_CONNECTED) {
                if (!TextUtils.isEmpty(AppInfo.btAddress)) {
                    BluetoothDevice device = mAdapter.getRemoteDevice(AppInfo.btAddress);
                    mBtService.connect(device);
                    Log.e("PrintQueue2","State is close, reconnecting.");
                    return;
                }
            }
            while (mQueue.size() > 0) {
                mBtService.write(mQueue.get(0));
                mQueue.remove(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * disconnect remote device
     */
    public void disconnect() {
        try {
            if (null != mBtService) {
                mBtService.stop();
                mBtService = null;
            }
            if (null != mAdapter) {
                mAdapter = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * when bluetooth status is changed, if the printer is in use,
     * connect it,else do nothing
     */
    public void tryConnect() {
        try {
            if (TextUtils.isEmpty(AppInfo.btAddress)) {
                return;
            }
            if (null == mAdapter) {
                mAdapter = BluetoothAdapter.getDefaultAdapter();
            }
            if (null == mAdapter) {
                return;
            }
            if (null == mBtService) {
                mBtService = new BtService(mContext);
            }
            if (mBtService.getState() != BtService.STATE_CONNECTED) {
                if (!TextUtils.isEmpty(AppInfo.btAddress)) {
                    BluetoothDevice device = mAdapter.getRemoteDevice(AppInfo.btAddress);
                    mBtService.connect(device);
                    return;
                }
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * 将打印命令发送给打印机
     *
     * @param bytes bytes
     */
    public void write(byte[] bytes) {
        try {
            if (null == bytes || bytes.length <= 0) {
                return;
            }
            if (null == mAdapter) {
                mAdapter = BluetoothAdapter.getDefaultAdapter();
            }
            if (null == mBtService) {
                mBtService = new BtService(mContext);
            }
            if (mBtService.getState() != BtService.STATE_CONNECTED) {
                if (!TextUtils.isEmpty(AppInfo.btAddress)) {
                    BluetoothDevice device = mAdapter.getRemoteDevice(AppInfo.btAddress);
                    mBtService.connect(device);
                    return;
                }
            }
            mBtService.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
