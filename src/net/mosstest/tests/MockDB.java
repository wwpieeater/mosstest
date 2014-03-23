package net.mosstest.tests;

import org.iq80.leveldb.*;

import java.io.IOException;

public class MockDB implements org.iq80.leveldb.DB {
    @Override
    public byte[] get(byte[] bytes) throws DBException {
        return new byte[7];
    }

    @Override
    public byte[] get(byte[] bytes, ReadOptions readOptions) throws DBException {
        return new byte[33];
    }

    @Override
    public DBIterator iterator() {
        return null;
    }

    @Override
    public DBIterator iterator(ReadOptions readOptions) {
        return null;
    }

    @Override
    public void put(byte[] bytes, byte[] bytes2) throws DBException {

    }

    @Override
    public void delete(byte[] bytes) throws DBException {

    }

    @Override
    public void write(WriteBatch writeBatch) throws DBException {

    }

    @Override
    public WriteBatch createWriteBatch() {
        return null;
    }

    @Override
    public Snapshot put(byte[] bytes, byte[] bytes2, WriteOptions writeOptions) throws DBException {
        return null;
    }

    @Override
    public Snapshot delete(byte[] bytes, WriteOptions writeOptions) throws DBException {
        return null;
    }

    @Override
    public Snapshot write(WriteBatch writeBatch, WriteOptions writeOptions) throws DBException {
        return null;
    }

    @Override
    public Snapshot getSnapshot() {
        return null;
    }

    @Override
    public long[] getApproximateSizes(Range... ranges) {
        return new long[0];
    }

    @Override
    public String getProperty(String s) {
        return null;
    }

    @Override
    public void suspendCompactions() throws InterruptedException {

    }

    @Override
    public void resumeCompactions() {

    }

    @Override
    public void close() throws IOException {

    }
}
