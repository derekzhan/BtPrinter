package com.yefeng.night.btprinter;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.yefeng.night.btprinter.print.GPrinterCommand;
import com.yefeng.night.btprinter.print.PrintPic;
import com.yefeng.night.btprinter.print.PrintQueue;
import com.yefeng.night.btprinter.print.PrintQueue2;
import com.yefeng.night.btprinter.print.PrintUtil;
import com.yefeng.night.btprinter.print.escp.params.TemplateParamBean;

import java.io.BufferedInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by yefeng on 6/2/15.
 * github:yefengfreedom
 * <p/>
 * print ticket service
 */
public class BtService extends IntentService {

    public BtService() {
        super("BtService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BtService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equals(PrintUtil.ACTION_PRINT_TEST)) {
            printTest2();
        } else if (intent.getAction().equals(PrintUtil.ACTION_PRINT)) {
            print(intent.getByteArrayExtra(PrintUtil.PRINT_EXTRA));
        } else if (intent.getAction().equals(PrintUtil.ACTION_PRINT_TICKET)) {
        } else if (intent.getAction().equals(PrintUtil.ACTION_PRINT_BITMAP)) {
            printBitmapTest();
        } else if (intent.getAction().equals(PrintUtil.ACTION_PRINT_PAINTING)) {
            printPainting();
        }
    }

    private void printTest2() {

        TemplateParamBean templateParamBean = new TemplateParamBean();

            String template= "{" +
                    "    \"header\": [" +
                    "        {" +
                    "            \"text\": \"{$shopname}\"," +
                    "            \"size\": 2," +
                    "            \"bold\": true," +
                    "            \"format\": 1," +
                    "            \"line\": 2," +
                    "            \"underline\": true," +
                    "            \"type\": 0" +
                    "        }," +
                    "        {" +
                    "            \"text\": \"--------------------------------\"," +
                    "            \"size\": 0," +
                    "            \"bold\": false," +
                    "            \"format\": 1," +
                    "            \"line\": 2," +
                    "            \"underline\": false," +
                    "            \"type\": 0" +
                    "        }," +
                    "    ]," +
                    "    \"goods\": [" +
                    "        {" +
                    "            \"name\": \"菜名\"," +
                    "            \"width\": 24," +
                    "            \"format\": 0," +
                    "            \"variable\": \"name\"" +
                    "        }," +
                    "        {" +
                    "            \"name\": \"数量\"," +
                    "            \"width\": 8," +
                    "            \"format\": 1," +
                    "            \"variable\": \"num\"" +
                    "        }," +
                    "        {" +
                    "            \"name\": \"单位\"," +
                    "            \"width\": 8," +
                    "            \"format\": 1," +
                    "            \"variable\": \"unit\"" +
                    "        }," +
                    "        {" +
                    "            \"name\": \"金额\"," +
                    "            \"width\": 8," +
                    "            \"format\": 2," +
                    "            \"variable\": \"pay\"" +
                    "        }" +
                    "    ]," +
                    "    \"bill\": [" +
                    "        {" +
                    "            \"text\": \"{$cash}\"," +
                    "            \"size\": 2," +
                    "            \"bold\": true," +
                    "            \"format\": 1," +
                    "            \"line\": 2," +
                    "            \"underline\": false," +
                    "            \"type\": 0" +
                    "        }," +

                    "    ]," +
                    "    \"footer\": [" +
                    "        {" +
                    "            \"text\": \"详情请访问官网\"," +
                    "            \"size\": 2," +
                    "            \"bold\": true," +
                    "            \"format\": 1," +
                    "            \"line\": 2," +
                    "            \"underline\": true," +
                    "            \"type\": 0" +
                    "        }," +
                    "        {" +
                    "            \"text\": \"http://www.diancaiwawa.com\"," +
                    "            \"format\": 1," +
                    "            \"line\": 2," +
                    "            \"type\": 0" +
                    "        }" +
                    "    ]" +
                    "}";
            String param = "{" +
                    "  \"keys\": {" +
                    "    \"shopname\": \"黄太吉\"," +
                    "    \"time\": \"15:35\"," +
                    "    \"num\": 14," +
                    "    \"cash\": \"小计  ￥324.5\"," +
                    "    \"adv\": \"关注微信，有大大地活动哦\"" +
                    "  }," +
                    "  \"goods\": [" +
                    "    {" +
                    "      \"name\": \"鱼香肉丝\"," +
                    "      \"num\": 1," +
                    "      \"unit\": \"份\"," +
                    "      \"pay\": 12.8" +
                    "    }," +
                    "    {" +
                    "      \"name\": \"葱油粑粑\"," +
                    "      \"num\": 1," +
                    "      \"unit\": \"份\"," +
                    "      \"pay\": 4.8" +
                    "    }," +
                    "    {" +
                    "      \"name\": \"辣椒炒肉,很好吃的额哦\"," +
                    "      \"num\": 1," +
                    "      \"unit\": \"份\"," +
                    "      \"pay\": 14.8" +
                    "    }" +
                    "  ]" +
                    "}";
        templateParamBean.setTemplate(template);
        templateParamBean.setParam(param);

        PrintQueue2.getQueue(getApplicationContext()).add(templateParamBean);

    }

    private void printTest() {
        try {
            ArrayList<byte[]> bytes = new ArrayList<byte[]>();
            String message = "[总单] 吧台\n"
                    + "厅名：A区   台名：A29\n"
                    + "账单号：002231231   人数：2\n"
                    + "-------------------------------------------\n"
                    + "菜名\t\t\t数量\t单位\t价格\n"
                    + "韭菜煎饼好吃的很\t1\t份\t20\n"
                    + "凉拌黄瓜ssss\t1\t份\t30\n"
                    + "无果养生粥\t1\t份\t40\n"
                    + "-------------------------------------------\n"
                    + "小计\t ￥90.00\n"
                    + "2017-02-01 20：05\n"
                    + "\n"
                    ;
            bytes.add(GPrinterCommand.reset);
            bytes.add(GPrinterCommand.bold);
            //bytes.add(GPrinterCommand.text_big_size);
            bytes.add(message.getBytes("gbk"));
            bytes.add(GPrinterCommand.print);

            bytes.add(GPrinterCommand.reset);
            bytes.add("No.11231231".getBytes("gbk"));
            bytes.add(GPrinterCommand.print);
            bytes.add(GPrinterCommand.print);
            bytes.add(GPrinterCommand.print);

            PrintQueue.getQueue(getApplicationContext()).add(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void print(byte[] byteArrayExtra) {
        if (null == byteArrayExtra || byteArrayExtra.length <= 0) {
            return;
        }
        PrintQueue.getQueue(getApplicationContext()).add(byteArrayExtra);
    }

    private void printBitmapTest() {
        BufferedInputStream bis;
        try {
            bis = new BufferedInputStream(getAssets().open(
                    "name.bmp"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeStream(bis);
        PrintPic printPic = PrintPic.getInstance();
        printPic.init(bitmap);
        if (null != bitmap) {
            if (bitmap.isRecycled()) {
                bitmap = null;
            } else {
                bitmap.recycle();
                bitmap = null;
            }
        }
        byte[] bytes = printPic.printDraw();
        ArrayList<byte[]> printBytes = new ArrayList<byte[]>();
        printBytes.add(GPrinterCommand.reset);
        printBytes.add(GPrinterCommand.print);
        printBytes.add(bytes);
        Log.e("BtService", "image bytes size is :" + bytes.length);
        printBytes.add(GPrinterCommand.print);
        PrintQueue.getQueue(getApplicationContext()).add(bytes);
    }

    private void printPainting() {
        byte[] bytes = PrintPic.getInstance().printDraw();
        ArrayList<byte[]> printBytes = new ArrayList<byte[]>();
        printBytes.add(GPrinterCommand.reset);
        printBytes.add(GPrinterCommand.print);
        printBytes.add(bytes);
        Log.e("BtService", "image bytes size is :" + bytes.length);
        printBytes.add(GPrinterCommand.print);
        PrintQueue.getQueue(getApplicationContext()).add(bytes);
    }
}