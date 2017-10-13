package org.caojun.morseman.jni;

public class ImageUtilEngine {

    static {
        System.loadLibrary("native-lib");
    }

    public native int[] decodeYUV420SP(byte[] buf, int width, int heigth);
}
