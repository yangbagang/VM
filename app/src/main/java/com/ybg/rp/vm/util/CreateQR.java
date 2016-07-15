package com.ybg.rp.vm.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * 二维码工具
 * <p/>
 * 包            名:      com.cnpay.ppvending.util
 * 类            名:      CreateQR
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/6/15
 */
public class CreateQR {
    // 图片宽度的一般
    private static final int IMAGE_HALFWIDTH = 20;

    /**
     * 生成无图片二维码
     *
     * @param str
     * @return
     * @throws WriterException
     */
    public static Bitmap cretaeBitmap(String str) throws WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败 300*300
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, 200, 200, hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;//前景色
                } else { // 无信息设置像素点为白色
                    pixels[y * width + x] = 0xffffffff;//背景色;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);//ARGB_8888
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 生成有图片的二维码
     *
     * @param str
     * @param icon
     * @return
     * @throws WriterException
     */
    public static Bitmap cretaeBitmap(String str, Bitmap icon) throws WriterException {
        // 缩放一个40*40的图片
        icon = zoomBitmap(icon, IMAGE_HALFWIDTH);
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, 300, 300, hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                        && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {
                    pixels[y * width + x] = icon.getPixel(x - halfW
                            + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                } else {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;//前景色
                    } else { // 无信息设置像素点为白色
                        pixels[y * width + x] = 0xffffffff;//背景色
                    }
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);//ARGB_8888
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return bitmap;
    }

/*    *//**
     * 保存二维码
     *
     * @param b
     * @return
     *//*
    public static boolean writeBitmap(Bitmap b) {
        ByteArrayOutputStream by = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, by);
        byte[] stream = by.toByteArray();
        return Untilly.writeToSdcard(stream, Config.PIC_PATH + "lidynast/pic/"
                , System.currentTimeMillis() + ".png");
    }*/


    /**
     * 缩放图片
     *
     * @param icon
     * @param h
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap icon, int h) {
        // 缩放图片
        Matrix m = new Matrix();
        float sx = (float) 2 * h / icon.getWidth();
        float sy = (float) 2 * h / icon.getHeight();
        m.setScale(sx, sy);
        // 重新构造一个2h*2h的图片
        return Bitmap.createBitmap(icon, 0, 0, icon.getWidth(),
                icon.getHeight(), m, false);
    }
}
