package com.joy.security;

import android.support.v4.view.accessibility.AccessibilityEventCompat;
//import com.google.android.gms.drive.DriveFile;

class DES_SPboxes {
    private static final int BOXLEN = 64;
    public static final int KDDESEXTRA = 6;
    static final int[] bits1 = new int[]{4, 1024, 65536, 16777216};
    static final int[] bits2 = new int[]{32768, Integer.MIN_VALUE, 32, 1048576};
    static final int[] bits3 = new int[]{134217728, 8, 131072, 512};
    static final int[] bits4 = new int[]{1, 8388608, 8192, 128};
    static final int[] bits5 = new int[]{1073741824, 256, 524288, 33554432};
    static final int[] bits6 = new int[]{16384, AccessibilityEventCompat.TYPE_WINDOWS_CHANGED, 16, 2}; //DriveFile.MODE_READ_ONLY 마지막인자. 2로바꿔버렸음
    static final int[] bits7 = new int[]{67108864, 2048, 2097152, 2};
    static final int[] bits8 = new int[]{4096, 262144, 64, 2}; //DriveFile.MODE_READ_ONLY 마지막인자. 2로바꿔버렸음
    static final byte[] ds1;
    static final byte[] ds2;
    static final byte[] ds3;
    static final byte[] ds4;
    static final byte[] ds5;
    static final byte[] ds6;
    static final byte[] ds7;
    static final byte[] ds8;
    static final byte[] s31;
    static final byte[] s32;
    static final byte[] s33;
    static final byte[] s34;
    static final byte[] s35;
    static final byte[] s36;
    static final byte[] s37;
    static final byte[] s38;
    int[] SP1;
    int[] SP2;
    int[] SP3;
    int[] SP4;
    int[] SP5;
    int[] SP6;
    int[] SP7;
    int[] SP8;

    static {
        r0 = new byte[64];
        ds1 = r0;
        r0 = new byte[64];
        ds2 = r0;
        r0 = new byte[64];
        ds3 = r0;
        r0 = new byte[64];
        ds4 = r0;
        r0 = new byte[64];
        ds5 = r0;
        r0 = new byte[64];
        ds6 = r0;
        r0 = new byte[64];
        ds7 = r0;
        r0 = new byte[64];
        ds8 = r0;
        r0 = new byte[64];
        s31 = r0;
        r0 = new byte[64];
        s32 = r0;
        r0 = new byte[64];
        s33 = r0;
        r0 = new byte[64];
        s34 = r0;
        r0 = new byte[64];
        s35 = r0;
        r0 = new byte[64];
        s36 = r0;
        r0 = new byte[64];
        s37 = r0;
        r0 = new byte[64];
        r0[0] = (byte) 13;
        r0[1] = (byte) 10;
        r0[3] = (byte) 7;
        r0[4] = (byte) 3;
        r0[5] = (byte) 9;
        r0[6] = (byte) 14;
        r0[7] = (byte) 4;
        r0[8] = (byte) 2;
        r0[9] = (byte) 15;
        r0[10] = (byte) 12;
        r0[11] = (byte) 1;
        r0[12] = (byte) 5;
        r0[13] = (byte) 6;
        r0[14] = (byte) 11;
        r0[15] = (byte) 8;
        r0[16] = (byte) 2;
        r0[17] = (byte) 7;
        r0[18] = (byte) 13;
        r0[19] = (byte) 1;
        r0[20] = (byte) 4;
        r0[21] = (byte) 14;
        r0[22] = (byte) 11;
        r0[23] = (byte) 8;
        r0[24] = (byte) 15;
        r0[25] = (byte) 12;
        r0[26] = (byte) 6;
        r0[27] = (byte) 10;
        r0[28] = (byte) 9;
        r0[29] = (byte) 5;
        r0[31] = (byte) 3;
        r0[32] = (byte) 4;
        r0[33] = (byte) 13;
        r0[34] = (byte) 14;
        r0[36] = (byte) 9;
        r0[37] = (byte) 3;
        r0[38] = (byte) 7;
        r0[39] = (byte) 10;
        r0[40] = (byte) 1;
        r0[41] = (byte) 8;
        r0[42] = (byte) 2;
        r0[43] = (byte) 11;
        r0[44] = (byte) 15;
        r0[45] = (byte) 5;
        r0[46] = (byte) 12;
        r0[47] = (byte) 6;
        r0[48] = (byte) 8;
        r0[49] = (byte) 11;
        r0[50] = (byte) 7;
        r0[51] = (byte) 14;
        r0[52] = (byte) 2;
        r0[53] = (byte) 4;
        r0[54] = (byte) 13;
        r0[55] = (byte) 1;
        r0[56] = (byte) 6;
        r0[57] = (byte) 5;
        r0[58] = (byte) 9;
        r0[60] = (byte) 12;
        r0[61] = (byte) 15;
        r0[62] = (byte) 3;
        r0[63] = (byte) 10;
        s38 = r0;
    }

    private static void spbox(byte[] box, int[] mask, int[] compiled) {
        for (int i = 0; i < 64; i++) {
            int entry = 0;
            int spindex = ((i / 2) + ((i % 2) * 16)) + ((i & 32) >>> 1);
            for (int j = 0; j < 4; j++) {
                if ((box[spindex] & (1 << j)) != 0) {
                    entry |= mask[j];
                }
            }
            compiled[i] = entry;
        }
    }

    private void initDesSPboxes() {
        spbox(ds1, bits1, this.SP1);
        spbox(ds2, bits2, this.SP2);
        spbox(ds3, bits3, this.SP3);
        spbox(ds4, bits4, this.SP4);
        spbox(ds5, bits5, this.SP5);
        spbox(ds6, bits6, this.SP6);
        spbox(ds7, bits7, this.SP7);
        spbox(ds8, bits8, this.SP8);
    }

    private void inits3DesSPboxes() {
        spbox(s31, bits1, this.SP1);
        spbox(s32, bits2, this.SP2);
        spbox(s33, bits3, this.SP3);
        spbox(s34, bits4, this.SP4);
        spbox(s35, bits5, this.SP5);
        spbox(s36, bits6, this.SP6);
        spbox(s37, bits7, this.SP7);
        spbox(s38, bits8, this.SP8);
    }

    private static void KDspbox(byte[] box, int[] mask, int[] compiled, byte[] key, int offset, int index) {
        int rowswap = key[offset + 0] & (1 << index);
        int colswap = key[offset + 1] & (1 << index);
        int i = (key[2] == (byte) 0 || (1 << index) == 0) ? 0 : 1;
        int i2 = (key[3] == (byte) 0 || (1 << index) == 0) ? 0 : 2;
        i2 |= i;
        i = (key[4] == (byte) 0 || (1 << index) == 0) ? 0 : 4;
        i2 |= i;
        if (key[5] == (byte) 0 || (1 << index) == 0) {
            i = 0;
        } else {
            i = 8;
        }
        byte xor = (byte) (i | i2);
        for (int i3 = 0; i3 < 64; i3++) {
            int entry = 0;
            int spindex = ((i3 / 2) + ((i3 % 2) * 16)) + ((i3 & 32) >>> 1);
            if (rowswap != 0) {
                spindex = (spindex + 32) % 64;
            }
            if (colswap != 0) {
                int k = spindex / 32;
                spindex = (k * 32) + (((spindex - (k * 32)) + 16) % 32);
            }
            byte value = (byte) ((box[spindex] ^ xor) & 15);
            for (int j = 0; j < 4; j++) {
                if (((1 << j) & value) != 0) {
                    entry |= mask[j];
                }
            }
            compiled[i3] = entry;
        }
    }

    void initKDdesSPboxes(byte[] key, int offset) {
        KDspbox(ds2, bits1, this.SP1, key, offset, 0);
        KDspbox(ds4, bits2, this.SP2, key, offset, 1);
        KDspbox(ds6, bits3, this.SP3, key, offset, 2);
        KDspbox(ds7, bits4, this.SP4, key, offset, 3);
        KDspbox(ds3, bits5, this.SP5, key, offset, 4);
        KDspbox(ds1, bits6, this.SP6, key, offset, 5);
        KDspbox(ds5, bits7, this.SP7, key, offset, 6);
        KDspbox(ds8, bits8, this.SP8, key, offset, 7);
    }

    public DES_SPboxes() {
        this.SP1 = new int[64];
        this.SP2 = new int[64];
        this.SP3 = new int[64];
        this.SP4 = new int[64];
        this.SP5 = new int[64];
        this.SP6 = new int[64];
        this.SP7 = new int[64];
        this.SP8 = new int[64];
        initDesSPboxes();
    }

    public DES_SPboxes(int dummy) {
        this.SP1 = new int[64];
        this.SP2 = new int[64];
        this.SP3 = new int[64];
        this.SP4 = new int[64];
        this.SP5 = new int[64];
        this.SP6 = new int[64];
        this.SP7 = new int[64];
        this.SP8 = new int[64];
        inits3DesSPboxes();
    }

    public DES_SPboxes(byte[] key, int offset) {
        this.SP1 = new int[64];
        this.SP2 = new int[64];
        this.SP3 = new int[64];
        this.SP4 = new int[64];
        this.SP5 = new int[64];
        this.SP6 = new int[64];
        this.SP7 = new int[64];
        this.SP8 = new int[64];
        initKDdesSPboxes(key, offset);
    }
}
