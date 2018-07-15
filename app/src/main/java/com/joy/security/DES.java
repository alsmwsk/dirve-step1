package com.joy.security;

public class DES {
    static DES_SPboxes sp = null;
    int[] dkeysched = null;
    int[] ekeysched = null;

    public DES() {
        if (sp == null) {
            sp = new DES_SPboxes();
        }
    }

    public void init(byte[] key, int offset, boolean triple) {
        this.ekeysched = new int[32];
        this.dkeysched = new int[32];
        DESkeys.single(key, offset, 0, this.ekeysched, 0);
        DESkeys.single(key, offset, 1, this.dkeysched, 0);
    }

    public void ecb(boolean encrypt, byte[] in, int offin, byte[] out, int offout) {
        if (encrypt) {
            DESengine.single(in, offin, out, offout, this.ekeysched, sp);
            return;
        }
        DESengine.single(in, offin, out, offout, this.dkeysched, sp);
    }

    public void destroy() {
        if (this.ekeysched != null) {
            for (int e = 0; e < this.ekeysched.length; e++) {
                this.ekeysched[e] = 0;
            }
        }
        this.ekeysched = null;
        if (this.dkeysched != null) {
            for (int d = 0; d < this.dkeysched.length; d++) {
                this.dkeysched[d] = 0;
            }
        }
        this.dkeysched = null;
    }

    public int getKeysize() {
        return 8;
    }

    public int getBlocksize() {
        return 8;
    }
}
