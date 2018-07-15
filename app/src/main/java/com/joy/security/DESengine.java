package com.joy.security;

public class DESengine {
    public static final int BLOCKSIZE = 8;

    private DESengine() {
    }

    static void single(byte[] inblock, int offin, byte[] outblock, int offout, int[] keys, DES_SPboxes key) {
        int leftt = ((((inblock[offin + 0] & 255) << 24) | ((inblock[offin + 1] & 255) << 16)) | ((inblock[offin + 2] & 255) << 8)) | (inblock[offin + 3] & 255);
        int right = ((((inblock[offin + 4] & 255) << 24) | ((inblock[offin + 5] & 255) << 16)) | ((inblock[offin + 6] & 255) << 8)) | (inblock[offin + 7] & 255);
        int work = ((leftt >>> 4) ^ right) & 252645135;
        right ^= work;
        leftt ^= work << 4;
        work = ((leftt >>> 16) ^ right) & 65535;
        right ^= work;
        leftt ^= work << 16;
        work = ((right >>> 2) ^ leftt) & 858993459;
        leftt ^= work;
        right ^= work << 2;
        work = ((right >>> 8) ^ leftt) & 16711935;
        leftt ^= work;
        right ^= work << 8;
        right = ((right << 1) | ((right >>> 31) & 1)) & -1;
        work = (leftt ^ right) & -1431655766;
        leftt ^= work;
        right ^= work;
        leftt = ((leftt << 1) | ((leftt >>> 31) & 1)) & -1;
        int s = 0;
        for (int round = 0; round < 8; round++) {
            int s2 = s + 1;
            work = ((right << 28) | (right >>> 4)) ^ keys[s];
            int fval = ((key.SP7[work & 63] | key.SP5[(work >>> 8) & 63]) | key.SP3[(work >>> 16) & 63]) | key.SP1[(work >>> 24) & 63];
            s = s2 + 1;
            work = right ^ keys[s2];
            leftt ^= (((fval | key.SP8[work & 63]) | key.SP6[(work >>> 8) & 63]) | key.SP4[(work >>> 16) & 63]) | key.SP2[(work >>> 24) & 63];
            s2 = s + 1;
            work = ((leftt << 28) | (leftt >>> 4)) ^ keys[s];
            fval = ((key.SP7[work & 63] | key.SP5[(work >>> 8) & 63]) | key.SP3[(work >>> 16) & 63]) | key.SP1[(work >>> 24) & 63];
            s = s2 + 1;
            work = leftt ^ keys[s2];
            right ^= (((fval | key.SP8[work & 63]) | key.SP6[(work >>> 8) & 63]) | key.SP4[(work >>> 16) & 63]) | key.SP2[(work >>> 24) & 63];
        }
        right = (right << 31) | (right >>> 1);
        work = (leftt ^ right) & -1431655766;
        leftt ^= work;
        right ^= work;
        leftt = (leftt << 31) | (leftt >>> 1);
        work = ((leftt >>> 8) ^ right) & 16711935;
        right ^= work;
        leftt ^= work << 8;
        work = ((leftt >>> 2) ^ right) & 858993459;
        right ^= work;
        leftt ^= work << 2;
        work = ((right >>> 16) ^ leftt) & 65535;
        leftt ^= work;
        right ^= work << 16;
        work = ((right >>> 4) ^ leftt) & 252645135;
        leftt ^= work;
        right ^= work << 4;
        outblock[offout + 0] = (byte) ((right >>> 24) & 255);
        outblock[offout + 1] = (byte) ((right >>> 16) & 255);
        outblock[offout + 2] = (byte) ((right >>> 8) & 255);
        outblock[offout + 3] = (byte) (right & 255);
        outblock[offout + 4] = (byte) ((leftt >>> 24) & 255);
        outblock[offout + 5] = (byte) ((leftt >>> 16) & 255);
        outblock[offout + 6] = (byte) ((leftt >>> 8) & 255);
        outblock[offout + 7] = (byte) (leftt & 255);
    }

    static void triple(byte[] inblock, int offin, byte[] outblock, int offout, int[] keys, DES_SPboxes key) {
        int leftt = ((((inblock[offin + 0] & 255) << 24) | ((inblock[offin + 1] & 255) << 16)) | ((inblock[offin + 2] & 255) << 8)) | (inblock[offin + 3] & 255);
        int right = ((((inblock[offin + 4] & 255) << 24) | ((inblock[offin + 5] & 255) << 16)) | ((inblock[offin + 6] & 255) << 8)) | (inblock[offin + 7] & 255);
        int work = ((leftt >>> 4) ^ right) & 252645135;
        right ^= work;
        leftt ^= work << 4;
        work = ((leftt >>> 16) ^ right) & 65535;
        right ^= work;
        leftt ^= work << 16;
        work = ((right >>> 2) ^ leftt) & 858993459;
        leftt ^= work;
        right ^= work << 2;
        work = ((right >>> 8) ^ leftt) & 16711935;
        leftt ^= work;
        right ^= work << 8;
        right = ((right << 1) | ((right >>> 31) & 1)) & -1;
        work = (leftt ^ right) & -1431655766;
        leftt ^= work;
        int iterate = 0;
        work = right ^ work;
        right = ((leftt << 1) | ((leftt >>> 31) & 1)) & -1;
        leftt = work;
        int s = 0;
        while (iterate < 3) {
            work = right;
            right = leftt;
            leftt = work;
            iterate++;
            int s2 = s;
            for (int round = 0; round < 8; round++) {
                s = s2 + 1;
                work = ((right << 28) | (right >>> 4)) ^ keys[s2];
                int fval = ((key.SP7[work & 63] | key.SP5[(work >>> 8) & 63]) | key.SP3[(work >>> 16) & 63]) | key.SP1[(work >>> 24) & 63];
                s2 = s + 1;
                work = right ^ keys[s];
                leftt ^= (((fval | key.SP8[work & 63]) | key.SP6[(work >>> 8) & 63]) | key.SP4[(work >>> 16) & 63]) | key.SP2[(work >>> 24) & 63];
                s = s2 + 1;
                work = ((leftt << 28) | (leftt >>> 4)) ^ keys[s2];
                fval = ((key.SP7[work & 63] | key.SP5[(work >>> 8) & 63]) | key.SP3[(work >>> 16) & 63]) | key.SP1[(work >>> 24) & 63];
                s2 = s + 1;
                work = leftt ^ keys[s];
                right ^= (((fval | key.SP8[work & 63]) | key.SP6[(work >>> 8) & 63]) | key.SP4[(work >>> 16) & 63]) | key.SP2[(work >>> 24) & 63];
            }
            s = s2;
        }
        right = (right << 31) | (right >>> 1);
        work = (leftt ^ right) & -1431655766;
        leftt ^= work;
        right ^= work;
        leftt = (leftt << 31) | (leftt >>> 1);
        work = ((leftt >>> 8) ^ right) & 16711935;
        right ^= work;
        leftt ^= work << 8;
        work = ((leftt >>> 2) ^ right) & 858993459;
        right ^= work;
        leftt ^= work << 2;
        work = ((right >>> 16) ^ leftt) & 65535;
        leftt ^= work;
        right ^= work << 16;
        work = ((right >>> 4) ^ leftt) & 252645135;
        leftt ^= work;
        right ^= work << 4;
        outblock[offout + 0] = (byte) ((right >>> 24) & 255);
        outblock[offout + 1] = (byte) ((right >>> 16) & 255);
        outblock[offout + 2] = (byte) ((right >>> 8) & 255);
        outblock[offout + 3] = (byte) (right & 255);
        outblock[offout + 4] = (byte) ((leftt >>> 24) & 255);
        outblock[offout + 5] = (byte) ((leftt >>> 16) & 255);
        outblock[offout + 6] = (byte) ((leftt >>> 8) & 255);
        outblock[offout + 7] = (byte) (leftt & 255);
    }
}
