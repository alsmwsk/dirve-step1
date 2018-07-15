package com.joy.db;

public class DbUtil implements DbInterface {
    protected int parseShort(byte[] rcv, int pos) {
        return (rcv[pos + 1] & 255) | ((rcv[pos] & 255) << 8);
    }

    protected int parseShort1(byte[] rcv, int pos) {
        return (rcv[pos] & 255) | ((rcv[pos + 1] & 255) << 8);
    }

    protected int parseInt(byte[] rcv, int pos) {
        return (((rcv[pos] & 255) | ((rcv[pos + 1] & 255) << 8)) | ((rcv[pos + 2] << 16) & 255)) | ((rcv[pos + 3] & 255) << 24);
    }

    protected long parseLong(byte[] rcv, int pos) {
        return (long) ((((rcv[pos] & 255) | ((rcv[pos + 1] & 255) << 8)) | ((rcv[pos + 2] << 16) & 255)) | ((rcv[pos + 3] & 255) << 24));
    }

    protected byte[] parseByte(int nVal) {
        return new byte[]{(byte) nVal, (byte) ((nVal >> 8) & 255), (byte) ((nVal >> 16) & 255), (byte) ((nVal >> 24) & 255)};
    }

    protected byte[] parseByte(long nVal) {
        return new byte[]{(byte) ((int) nVal), (byte) ((int) ((nVal >> 8) & 255)), (byte) ((int) ((nVal >> 16) & 255)), (byte) ((int) ((nVal >> 24) & 255))};
    }
}
