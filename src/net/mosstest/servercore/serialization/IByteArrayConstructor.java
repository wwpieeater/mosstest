package net.mosstest.servercore.serialization;

public interface IByteArrayConstructor<T> {
    public T construct(byte[] buf);
}
