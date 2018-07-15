package com.joy.security;

import com.google.android.gms.drive.DriveFile;

public class DESkeys {
    public static final int DE1 = 1;
    public static final int EN0 = 0;
    public static final int KEYSIZE = 8;
    public static final int LONGS = 32;
    private static final int[][][] precomp;

    static {
        r0 = new int[56][][];
        r0[0] = new int[][]{new int[]{1, 3, 4, 7, 9, 10, 12, 17, 18, 20, 23, 25, 26, 30, -1, -1}, new int[]{1048576, 67108864, 262144, 65536, DriveFile.MODE_READ_ONLY, 131072, 67108864, 33554432, 1048576, DriveFile.MODE_READ_ONLY, 134217728, 131072, 134217728, 65536, -1}};
        r0[1] = new int[][]{new int[]{1, 2, 4, 7, 8, 11, 12, 15, 16, 19, 22, 25, 26, 28, 31, -1}, new int[]{134217728, DriveFile.MODE_WRITE_ONLY, 524288, 16777216, 65536, 67108864, 262144, 65536, 33554432, 524288, 2097152, 33554432, 1048576, DriveFile.MODE_READ_ONLY, 2097152}};
        r1 = new int[2][];
        int[] iArr = new int[]{5, 9, 10, 12, 15, 19, 20, 23, 24, 27, 30, -1, -1, -1, -1, iArr};
        r1[1] = new int[]{2097152, 262144, 2097152, DriveFile.MODE_WRITE_ONLY, 524288, 16777216, 1048576, 16777216, DriveFile.MODE_WRITE_ONLY, 33554432, 524288, 67108864, -1, -1, -1};
        r0[2] = r1;
        r2 = new int[2][];
        int[] iArr2 = new int[]{4, 7, 9, 11, 12, 15, 16, 18, 20, 25, 26, 29, 30, -1, -1, iArr2};
        r2[1] = new int[]{256, 4096, 8, 256, 32, 32, 16, 8192, 8, 1024, 512, 2048, 2, 4, -1};
        r0[3] = r2;
        r0[4] = new int[][]{new int[]{1, 3, 5, 7, 8, 11, 13, 16, 21, 22, 25, 26, 28, 30, -1, -1}, new int[]{2048, 8, 256, 32, 32, 16, 4096, 1024, 512, 2048, 2, 256, 1, 4096, -1}};
        r0[5] = new int[][]{new int[]{3, 5, 9, 10, 13, 15, 17, 18, 20, 23, 24, 26, 29, 30, -1, -1}, new int[]{16, 4096, 4, 2, 1, 8192, 2, 256, 1, 2048, 16, 512, 1024, 32, -1}};
        r0[6] = new int[][]{new int[]{2, 5, 7, 8, 12, 15, 16, 18, 21, 24, 26, 28, 31, -1, -1, -1}, new int[]{2, 1, 8192, 4, 4096, 8, 16, 512, 1024, 8192, 8, 1024, 4, -1, -1}};
        r0[7] = new int[][]{new int[]{1, 2, 5, 6, 9, 12, 15, 17, 21, 22, 24, 27, 28, 31, -1, -1}, new int[]{67108864, 16777216, DriveFile.MODE_WRITE_ONLY, 33554432, 524288, 2097152, 33554432, 262144, 2097152, DriveFile.MODE_WRITE_ONLY, 524288, 16777216, 65536, 1048576, -1}};
        r2 = new int[2][];
        iArr2 = new int[]{3, 4, 9, 10, 13, 14, 17, 18, 20, 25, 29, 31, -1, -1, -1, iArr2};
        r2[1] = new int[]{DriveFile.MODE_WRITE_ONLY, 131072, 134217728, 1048576, 16777216, DriveFile.MODE_WRITE_ONLY, 33554432, DriveFile.MODE_READ_ONLY, 131072, 67108864, 262144, 2097152, 134217728, -1, -1};
        r0[8] = r2;
        r0[9] = new int[][]{new int[]{3, 4, 6, 9, 11, 12, 16, 19, 20, 23, 25, 26, 28, 30, -1, -1}, new int[]{33554432, 1048576, DriveFile.MODE_READ_ONLY, 134217728, 131072, 134217728, 65536, 67108864, 262144, 65536, DriveFile.MODE_READ_ONLY, 131072, 67108864, 2097152, -1}};
        r0[10] = new int[][]{new int[]{2, 5, 6, 8, 11, 14, 17, 21, 22, 25, 27, 28, 30, -1, -1, -1}, new int[]{1, 2048, 16, 512, 1024, 8192, 4096, 4, 2, 1, 8192, 4, 256, -1, -1}};
        r0[11] = new int[][]{new int[]{1, 2, 4, 7, 10, 12, 14, 17, 18, 21, 23, 24, 28, 31, -1, -1}, new int[]{8, 16, 512, 1024, 8192, 8, 1024, 4, 2, 1, 8192, 4, 4096, 2048, -1}};
        r0[12] = new int[][]{new int[]{1, 2, 4, 6, 11, 12, 15, 16, 20, 23, 25, 27, 28, -1, -1, -1}, new int[]{16, 8192, 8, 1024, 512, 2048, 2, 4, 4096, 8, 256, 32, 32, -1, -1}};
        r2 = new int[2][];
        iArr2 = new int[]{3, 4, 7, 8, 10, 13, 14, 17, 19, 20, 23, 25, 29, -1, -1, iArr2};
        r2[1] = new int[]{2, 512, 2048, 2, 256, 1, 2048, 16, 256, 32, 32, 16, 4096, 4, -1};
        r0[13] = r2;
        r2 = new int[2][];
        iArr2 = new int[]{2, 5, 7, 8, 10, 15, 16, 18, 21, 23, 24, 29, 31, -1, -1, iArr2};
        r2[1] = new int[]{16777216, 262144, 65536, DriveFile.MODE_READ_ONLY, 131072, 67108864, 262144, 1048576, DriveFile.MODE_READ_ONLY, 134217728, 131072, 134217728, 1048576, 67108864, -1};
        r0[14] = r2;
        r0[15] = new int[][]{new int[]{1, 2, 5, 6, 9, 10, 13, 15, 17, 20, 23, 24, 26, 29, 30, -1}, new int[]{131072, 524288, 16777216, 65536, 67108864, 262144, 65536, DriveFile.MODE_READ_ONLY, 524288, 2097152, 33554432, 1048576, DriveFile.MODE_READ_ONLY, 134217728, DriveFile.MODE_WRITE_ONLY}};
        r0[16] = new int[][]{new int[]{1, 3, 7, 8, 10, 13, 14, 17, 18, 21, 22, 25, 28, -1, -1, -1}, new int[]{33554432, 262144, 2097152, DriveFile.MODE_WRITE_ONLY, 524288, 16777216, 65536, 1048576, 16777216, DriveFile.MODE_WRITE_ONLY, 33554432, 524288, 2097152, -1, -1}};
        r2 = new int[2][];
        iArr2 = new int[]{2, 5, 7, 9, 10, 13, 15, 16, 18, 23, 24, 27, 28, -1, -1, iArr2};
        r2[1] = new int[]{1, 4096, 8, 256, 32, 32, 16, 4096, 8, 1024, 512, 2048, 2, 256, -1};
        r0[17] = r2;
        r2 = new int[2][];
        iArr2 = new int[]{3, 5, 6, 9, 11, 15, 19, 20, 23, 24, 26, 29, 31, -1, -1, iArr2};
        r2[1] = new int[]{16, 256, 32, 32, 16, 4096, 4, 512, 2048, 2, 256, 1, 2048, 8, -1};
        r0[18] = r2;
        r2 = new int[2][];
        iArr2 = new int[]{3, 7, 8, 11, 13, 14, 16, 18, 21, 22, 24, 27, 31, -1, -1, iArr2};
        r2[1] = new int[]{8192, 4096, 4, 2, 1, 8192, 4, 256, 1, 2048, 16, 512, 1024, 16, -1};
        r0[19] = r2;
        r0[20] = new int[][]{new int[]{1, 3, 5, 6, 10, 13, 15, 16, 19, 22, 24, 26, 30, -1, -1, -1}, new int[]{512, 1, 8192, 4, 4096, 8, 256, 512, 1024, 8192, 8, 1024, 2, -1, -1}};
        r2 = new int[2][];
        iArr2 = new int[]{3, 4, 7, 10, 13, 14, 19, 20, 22, 25, 26, 29, 30, -1, -1, iArr2};
        r2[1] = new int[]{262144, DriveFile.MODE_WRITE_ONLY, 33554432, 524288, 2097152, 33554432, 1048576, 2097152, DriveFile.MODE_WRITE_ONLY, 524288, 16777216, 65536, 67108864, 16777216, -1};
        r0[21] = r2;
        r2 = new int[2][];
        iArr2 = new int[]{2, 7, 8, 11, 12, 15, 16, 18, 23, 27, 28, 31, -1, -1, -1, iArr2};
        r2[1] = new int[]{524288, 134217728, 1048576, 16777216, DriveFile.MODE_WRITE_ONLY, 33554432, 524288, 131072, 67108864, 262144, 2097152, DriveFile.MODE_WRITE_ONLY, 131072, -1, -1};
        r0[22] = r2;
        r0[23] = new int[][]{new int[]{1, 2, 4, 7, 9, 10, 15, 17, 18, 21, 23, 24, 26, 31, -1, -1}, new int[]{262144, 1048576, DriveFile.MODE_READ_ONLY, 134217728, 131072, 134217728, 1048576, 67108864, 262144, 65536, DriveFile.MODE_READ_ONLY, 131072, 67108864, 33554432, -1}};
        r2 = new int[2][];
        iArr2 = new int[]{3, 4, 6, 9, 12, 14, 19, 20, 23, 25, 26, 30, -1, -1, -1, iArr2};
        r2[1] = new int[]{4096, 2048, 16, 512, 1024, 8192, 8, 4, 2, 1, 8192, 4, 1, -1, -1};
        r0[24] = r2;
        r0[25] = new int[][]{new int[]{1, 2, 5, 8, 10, 12, 16, 19, 21, 22, 26, 29, 30, -1, -1, -1}, new int[]{256, 512, 1024, 8192, 8, 1024, 2, 1, 8192, 4, 4096, 8, 16, -1, -1}};
        r0[26] = new int[][]{new int[]{1, 2, 4, 9, 10, 13, 14, 18, 21, 23, 25, 26, 29, 30, -1, -1}, new int[]{4096, 8, 1024, 512, 2048, 2, 256, 4096, 8, 256, 32, 32, 16, 8192, -1}};
        r0[27] = new int[][]{new int[]{1, 2, 5, 6, 8, 11, 12, 14, 17, 18, 21, 23, 27, 28, 31, -1}, new int[]{1, 2048, 2, 256, 1, 2048, 16, 512, 32, 32, 16, 4096, 4, 2, 512}};
        r0[28] = new int[][]{new int[]{1, 3, 5, 6, 8, 13, 16, 19, 21, 22, 27, 28, 30, -1, -1, -1}, new int[]{DriveFile.MODE_WRITE_ONLY, 65536, DriveFile.MODE_READ_ONLY, 131072, 67108864, 262144, DriveFile.MODE_READ_ONLY, 134217728, 131072, 134217728, 1048576, 16777216, 262144, -1, -1}};
        r2 = new int[2][];
        iArr2 = new int[]{3, 4, 7, 8, 11, 13, 14, 18, 21, 22, 24, 27, 29, 30, -1, iArr2};
        r2[1] = new int[]{134217728, 16777216, 65536, 67108864, 262144, 65536, DriveFile.MODE_READ_ONLY, 131072, 2097152, 33554432, 1048576, DriveFile.MODE_READ_ONLY, 134217728, 131072, 524288};
        r0[29] = r2;
        r2 = new int[2][];
        iArr2 = new int[]{5, 6, 8, 11, 12, 15, 16, 19, 20, 23, 26, 29, 31, -1, -1, iArr2};
        r2[1] = new int[]{1048576, 2097152, DriveFile.MODE_WRITE_ONLY, 524288, 16777216, 65536, 67108864, 16777216, DriveFile.MODE_WRITE_ONLY, 33554432, 524288, 2097152, 33554432, 262144, -1};
        r0[30] = r2;
        r0[31] = new int[][]{new int[]{1, 2, 4, 9, 13, 14, 17, 18, 23, 24, 27, 28, 31, -1, -1, -1}, new int[]{524288, 131072, 67108864, 262144, 2097152, DriveFile.MODE_WRITE_ONLY, 131072, 134217728, 1048576, 16777216, DriveFile.MODE_WRITE_ONLY, 33554432, DriveFile.MODE_READ_ONLY, -1, -1}};
        r2 = new int[2][];
        iArr2 = new int[]{3, 4, 7, 9, 13, 14, 17, 18, 21, 22, 24, 27, 28, 31, -1, iArr2};
        r2[1] = new int[]{512, 32, 32, 16, 4096, 4, 2, 512, 2048, 2, 256, 1, 2048, 16, 256};
        r0[32] = r2;
        r2 = new int[2][];
        iArr2 = new int[]{5, 6, 9, 11, 12, 16, 19, 20, 22, 25, 28, 31, -1, -1, -1, iArr2};
        r2[1] = new int[]{8, 4, 2, 1, 8192, 4, 1, 2048, 16, 512, 1024, 8192, 4096, -1, -1};
        r0[33] = r2;
        r2 = new int[2][];
        iArr2 = new int[]{3, 4, 8, 11, 13, 15, 17, 20, 22, 24, 29, 31, -1, -1, -1, iArr2};
        r2[1] = new int[]{2048, 8192, 4, 4096, 8, 256, 32, 1024, 8192, 8, 1024, 512, 1, -1, -1};
        r0[34] = r2;
        r0[35] = new int[][]{new int[]{1, 2, 5, 8, 11, 12, 14, 17, 18, 20, 23, 24, 27, 28, 31, -1}, new int[]{65536, 33554432, 524288, 2097152, 33554432, 1048576, DriveFile.MODE_READ_ONLY, 2097152, DriveFile.MODE_WRITE_ONLY, 524288, 16777216, 65536, 67108864, 262144, DriveFile.MODE_WRITE_ONLY}};
        r0[36] = new int[][]{new int[]{1, 5, 6, 9, 10, 13, 16, 21, 25, 26, 28, 30, -1, -1, -1, -1}, new int[]{16777216, 1048576, 16777216, DriveFile.MODE_WRITE_ONLY, 33554432, 524288, 67108864, 262144, 2097152, DriveFile.MODE_WRITE_ONLY, 524288, 134217728, -1, -1, -1}};
        r0[37] = new int[][]{new int[]{2, 5, 7, 8, 13, 14, 16, 19, 21, 22, 24, 29, 30, -1, -1, -1}, new int[]{DriveFile.MODE_READ_ONLY, 134217728, 131072, 134217728, 1048576, 16777216, 262144, 65536, DriveFile.MODE_READ_ONLY, 131072, 67108864, 262144, 1048576, -1, -1}};
        r2 = new int[2][];
        iArr2 = new int[]{4, 7, 8, 10, 13, 15, 16, 19, 20, 23, 24, 27, 29, 31, -1, iArr2};
        r2[1] = new int[]{131072, 2097152, 33554432, 1048576, DriveFile.MODE_READ_ONLY, 134217728, 131072, 524288, 16777216, 65536, 67108864, 262144, 65536, DriveFile.MODE_READ_ONLY, 524288};
        r0[38] = r2;
        r0[39] = new int[][]{new int[]{1, 3, 6, 8, 10, 15, 17, 19, 20, 24, 27, 29, 30, -1, -1, -1}, new int[]{32, 1024, 8192, 8, 1024, 512, 1, 8192, 4, 4096, 8, 256, 512, -1, -1}};
        r0[40] = new int[][]{new int[]{2, 7, 8, 11, 12, 14, 16, 19, 21, 23, 24, 27, 29, 30, -1, -1}, new int[]{1024, 512, 2048, 2, 256, 1, 4096, 8, 256, 32, 32, 16, 4096, 8, -1}};
        r0[41] = new int[][]{new int[]{1, 3, 4, 6, 9, 10, 12, 15, 16, 19, 21, 25, 26, 29, 30, -1}, new int[]{8192, 2, 256, 1, 2048, 16, 512, 1024, 32, 16, 4096, 4, 2, 1, 2048}};
        r2 = new int[2][];
        iArr2 = new int[]{3, 4, 6, 11, 15, 17, 19, 20, 25, 26, 29, 31, -1, -1, -1, iArr2};
        r2[1] = new int[]{33554432, DriveFile.MODE_READ_ONLY, 131072, 67108864, 262144, 2097152, 134217728, 131072, 134217728, 1048576, 16777216, DriveFile.MODE_WRITE_ONLY, 65536, -1, -1};
        r0[42] = r2;
        r0[43] = new int[][]{new int[]{2, 5, 6, 9, 11, 12, 14, 16, 19, 20, 22, 25, 27, 28, 31, -1}, new int[]{65536, 67108864, 262144, 65536, DriveFile.MODE_READ_ONLY, 131072, 67108864, 2097152, 33554432, 1048576, DriveFile.MODE_READ_ONLY, 134217728, 131072, 134217728, 16777216}};
        r2 = new int[2][];
        iArr2 = new int[]{3, 4, 6, 9, 10, 13, 14, 17, 18, 21, 24, 27, 28, -1, -1, iArr2};
        r2[1] = new int[]{DriveFile.MODE_READ_ONLY, 2097152, DriveFile.MODE_WRITE_ONLY, 524288, 16777216, 65536, 67108864, 262144, DriveFile.MODE_WRITE_ONLY, 33554432, 524288, 2097152, 33554432, 1048576, -1};
        r0[44] = r2;
        r0[45] = new int[][]{new int[]{2, 7, 11, 12, 14, 16, 21, 22, 25, 26, 29, 30, -1, -1, -1, -1}, new int[]{67108864, 262144, 2097152, DriveFile.MODE_WRITE_ONLY, 524288, 134217728, 1048576, 16777216, DriveFile.MODE_WRITE_ONLY, 33554432, 524288, 131072, -1, -1, -1}};
        r0[46] = new int[][]{new int[]{1, 2, 5, 7, 11, 12, 15, 16, 19, 20, 22, 25, 26, 28, 31, -1}, new int[]{1024, 32, 16, 4096, 4, 2, 1, 2048, 2, 256, 1, 2048, 16, 512, 32}};
        r2 = new int[2][];
        iArr2 = new int[]{3, 4, 7, 9, 10, 14, 17, 18, 20, 23, 26, 28, -1, -1, -1, iArr2};
        r2[1] = new int[]{1024, 4, 2, 1, 8192, 4, 4096, 2048, 16, 512, 1024, 8192, 8, -1, -1};
        r0[47] = r2;
        r0[48] = new int[][]{new int[]{1, 2, 6, 9, 11, 13, 14, 18, 20, 22, 27, 28, 31, -1, -1, -1}, new int[]{2, 4, 4096, 8, 256, 32, 32, 8192, 8, 1024, 512, 2048, 8192, -1, -1}};
        r0[49] = new int[][]{new int[]{1, 3, 6, 9, 10, 12, 15, 16, 18, 21, 22, 25, 26, 29, 30, -1}, new int[]{DriveFile.MODE_READ_ONLY, 524288, 2097152, 33554432, 1048576, DriveFile.MODE_READ_ONLY, 134217728, DriveFile.MODE_WRITE_ONLY, 524288, 16777216, 65536, 67108864, 262144, 65536, 33554432}};
        r2 = new int[2][];
        iArr2 = new int[]{3, 4, 7, 8, 11, 14, 19, 23, 24, 26, 29, -1, -1, -1, -1, iArr2};
        r2[1] = new int[]{65536, 1048576, 16777216, DriveFile.MODE_WRITE_ONLY, 33554432, 524288, 2097152, 262144, 2097152, DriveFile.MODE_WRITE_ONLY, 524288, 16777216, -1, -1, -1};
        r0[50] = r2;
        r0[51] = new int[][]{new int[]{1, 3, 5, 6, 11, 12, 15, 17, 19, 20, 22, 27, 30, -1, -1, -1}, new int[]{2097152, 134217728, 131072, 134217728, 1048576, 16777216, DriveFile.MODE_WRITE_ONLY, 65536, DriveFile.MODE_READ_ONLY, 131072, 67108864, 262144, DriveFile.MODE_READ_ONLY, -1, -1}};
        r2 = new int[2][];
        iArr2 = new int[]{2, 5, 6, 8, 11, 13, 14, 17, 18, 21, 22, 25, 27, 28, -1, iArr2};
        r2[1] = new int[]{67108864, 2097152, 33554432, 1048576, DriveFile.MODE_READ_ONLY, 134217728, 131072, 134217728, 16777216, 65536, 67108864, 262144, 65536, DriveFile.MODE_READ_ONLY, 131072};
        r0[52] = r2;
        r2 = new int[2][];
        iArr2 = new int[]{4, 6, 8, 13, 14, 17, 18, 22, 25, 27, 29, 31, -1, -1, -1, iArr2};
        r2[1] = new int[]{32, 8192, 8, 1024, 512, 2048, 8192, 4, 4096, 8, 256, 32, 1024, -1, -1};
        r0[53] = r2;
        r0[54] = new int[][]{new int[]{1, 5, 6, 9, 10, 12, 15, 17, 19, 21, 22, 25, 27, 30, -1, -1}, new int[]{4, 512, 2048, 2, 256, 1, 2048, 8, 256, 32, 32, 16, 4096, 1024, -1}};
        r2 = new int[2][];
        iArr2 = new int[]{2, 4, 7, 8, 10, 13, 17, 19, 23, 24, 27, 29, 31, -1, -1, iArr2};
        r2[1] = new int[]{4, 256, 1, 2048, 16, 512, 1024, 16, 4096, 4, 2, 1, 8192, 2, -1};
        r0[55] = r2;
        precomp = r0;
    }

    static void single(byte[] hexkey, int off1, int mode, int[] keybuf, int offset) {
        int bbidx = 0;
        int ep = offset + 32;
        int dp = offset;
        while (dp < ep) {
            int dp2 = dp + 1;
            keybuf[dp] = 0;
            dp = dp2;
        }
        while (bbidx < precomp.length) {
            int[] bip;
            int[] kip;
            int j;
            int i;
            int i2;
            int idx;
            int off12 = off1 + 1;
            int test = hexkey[off1] & 255;
            if ((test & 128) != 0) {
                bip = precomp[bbidx][1];
                kip = precomp[bbidx][0];
                j = 0;
                i = 0;
                while (true) {
                    i2 = i + 1;
                    idx = kip[i];
                    if (idx < 0) {
                        break;
                    }
                    int i3 = idx + offset;
                    int j2 = j + 1;
                    keybuf[i3] = keybuf[i3] | bip[j];
                    j = j2;
                    i = i2;
                }
            }
            bbidx++;
            if ((test & 64) != 0) {
                bip = precomp[bbidx][1];
                kip = precomp[bbidx][0];
                j = 0;
                i = 0;
                while (true) {
                    i2 = i + 1;
                    idx = kip[i];
                    if (idx < 0) {
                        break;
                    }
                    i3 = idx + offset;
                    j2 = j + 1;
                    keybuf[i3] = keybuf[i3] | bip[j];
                    j = j2;
                    i = i2;
                }
            }
            bbidx++;
            if ((test & 32) != 0) {
                bip = precomp[bbidx][1];
                kip = precomp[bbidx][0];
                j = 0;
                i = 0;
                while (true) {
                    i2 = i + 1;
                    idx = kip[i];
                    if (idx < 0) {
                        break;
                    }
                    i3 = idx + offset;
                    j2 = j + 1;
                    keybuf[i3] = keybuf[i3] | bip[j];
                    j = j2;
                    i = i2;
                }
            }
            bbidx++;
            if ((test & 16) != 0) {
                bip = precomp[bbidx][1];
                kip = precomp[bbidx][0];
                j = 0;
                i = 0;
                while (true) {
                    i2 = i + 1;
                    idx = kip[i];
                    if (idx < 0) {
                        break;
                    }
                    i3 = idx + offset;
                    j2 = j + 1;
                    keybuf[i3] = keybuf[i3] | bip[j];
                    j = j2;
                    i = i2;
                }
            }
            bbidx++;
            if ((test & 8) != 0) {
                bip = precomp[bbidx][1];
                kip = precomp[bbidx][0];
                j = 0;
                i = 0;
                while (true) {
                    i2 = i + 1;
                    idx = kip[i];
                    if (idx < 0) {
                        break;
                    }
                    i3 = idx + offset;
                    j2 = j + 1;
                    keybuf[i3] = keybuf[i3] | bip[j];
                    j = j2;
                    i = i2;
                }
            }
            bbidx++;
            if ((test & 4) != 0) {
                bip = precomp[bbidx][1];
                kip = precomp[bbidx][0];
                j = 0;
                i = 0;
                while (true) {
                    i2 = i + 1;
                    idx = kip[i];
                    if (idx < 0) {
                        break;
                    }
                    i3 = idx + offset;
                    j2 = j + 1;
                    keybuf[i3] = keybuf[i3] | bip[j];
                    j = j2;
                    i = i2;
                }
            }
            bbidx++;
            if ((test & 2) != 0) {
                bip = precomp[bbidx][1];
                kip = precomp[bbidx][0];
                j = 0;
                i = 0;
                while (true) {
                    i2 = i + 1;
                    idx = kip[i];
                    if (idx < 0) {
                        break;
                    }
                    i3 = idx + offset;
                    j2 = j + 1;
                    keybuf[i3] = keybuf[i3] | bip[j];
                    j = j2;
                    i = i2;
                }
            }
            bbidx++;
            off1 = off12;
        }
        if (mode == 1) {
            dp2 = offset + 30;
            ep = offset + 16;
            int ccp = offset;
            while (ccp < ep) {
                test = keybuf[dp2];
                keybuf[dp2] = keybuf[ccp];
                int ccp2 = ccp + 1;
                keybuf[ccp] = test;
                test = keybuf[dp2 + 1];
                keybuf[dp2 + 1] = keybuf[ccp2];
                ccp = ccp2 + 1;
                keybuf[ccp2] = test;
                dp2 -= 2;
            }
            return;
        }
    }

    static void dbl(byte[] hexkey, int off1, int mode, int[] keyout, int offset) {
        single(hexkey, off1 + 8, mode == 0 ? 1 : 0, keyout, offset + 32);
        single(hexkey, off1, mode, keyout, offset);
        for (int i = 0; i < 32; i++) {
            keyout[(offset + 64) + i] = keyout[offset + i];
        }
    }

    static void triple(byte[] hexkey, int off1, int mode, int[] keyout, int offset) {
        int revmod;
        int first = 0;
        int third = 16;
        if (mode == 0) {
            revmod = 1;
        } else {
            revmod = 0;
            first = 16;
            third = 0;
        }
        single(hexkey, off1 + first, mode, keyout, offset);
        single(hexkey, off1 + 8, revmod, keyout, offset + 32);
        single(hexkey, off1 + third, mode, keyout, offset + 64);
    }

    private DESkeys() {
    }
}
