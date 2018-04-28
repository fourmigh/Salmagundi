package org.caojun.cameracolor.jni;

public class ImageUtilEngine {

    static {
        System.loadLibrary("native-lib");
    }

    public native int[] decodeYUV420SP(byte[] buf, int width, int height);
}
