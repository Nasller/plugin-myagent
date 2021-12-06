package com.jiweichengzhu.fineagent;

public class ArraysFilter {
    private static final byte[] aa = new byte[]{117, -57, 62, 23, -19, 84, -68, -70, 16, 91, -38, 11, 99, -82, -26, -93, 114, 38, 59, 103, 68, 14, -2, -83, -47, -93, 17, 46, 107, -73, 109, 67, 25, 39, -105, 57, 115, -63, 0, 88, -31, -103, -17, -105, -92, -13, 79, 73, -119, 98, -35, -42, 78, 72, 114, 6, 90, 93, -57, 18, 32, -95, -102, 1};
    private static final byte[] bb = new byte[]{1, 120, -91, 124, -7, -27, 3, 104, -20, -100, 23, 8, -72, -8, -2, 72, -20, -8, -56, 27, -25, 84, -85, 91, 69, -58, -80, -27, -93, -52, 112, 107, -56, -93, -20, -75, 6, -74, -30, 14, 88, 85, 120, -68, -23, 56, -25, 28, -89, -88, 40, 42, -115, -29, -13, 102, -53, -84, 64, 95, -26, -41, 6, -17};

    public static Object testEquals(byte[] b1, byte[] b2) {
        if (null == b1) {
            return null;
        }

        if (ArrayUtils.equals(b1, aa) || ArrayUtils.equals(b1, bb)) {
            return true;
        }

        return null;
    }
}
