package net.mosstest.tests;

import net.mosstest.scripting.MapChunk;
import net.mosstest.scripting.Position;
import net.mosstest.scripting.SimplexMapGenerator;
import net.mosstest.servercore.AbstractByteArrayStorable;
import net.mosstest.servercore.LevelDBBackedMap;
import net.mosstest.servercore.MapGeneratorException;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.util.Arrays;

public class LevelDBBackedMapTest {

    @Test
    public void testBasicStore() throws MapGeneratorException, IOException {
        LevelDBBackedMap<TestByteArrayStorable, TestByteArrayStorable, Void> map = new LevelDBBackedMap<>(new MockDB(), TestByteArrayStorable.class);
        TestByteArrayStorable testKey = new TestByteArrayStorable(new byte[]{1,2});
       TestByteArrayStorable testVal = new TestByteArrayStorable(new byte[]{3,4});
        map.put(testKey, testVal);
        TestByteArrayStorable vOut = map.get(testKey);
        Assert.assertTrue(vOut.equals(testVal));
    }
    @Test
    public void testDbLookup() throws IOException {
        LevelDBBackedMap<TestByteArrayStorable, TestByteArrayStorable, Void> map = new LevelDBBackedMap<>(new MockDB(), TestByteArrayStorable.class);
        TestByteArrayStorable vOut = map.get(new TestByteArrayStorable(new byte[]{1,2}));
        Assert.assertNotNull(vOut);
    }

    @Test(expected = ClassCastException.class)
    public void testUncheckedKey() throws MapGeneratorException {
        LevelDBBackedMap<Position, MapChunk, Void> map = new LevelDBBackedMap<>(new MockDB(), MapChunk.class);

        Object notAPosition = new Object();

        MapChunk cOut = map.get(notAPosition);
    }


    public static class TestByteArrayStorable extends AbstractByteArrayStorable<Void>{
        byte[] buf;

        public TestByteArrayStorable(byte[] buf) throws IOException {

            buf = buf;
        }

        @Override
        public byte[] toBytes() {
            return buf;
        }

        @Override
        public void setManager(Void manager) {
            // noop
        }

        @Override
        public void loadBytes(byte[] buf) throws IOException {
            this.buf = buf;
        }

        public TestByteArrayStorable() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestByteArrayStorable that = (TestByteArrayStorable) o;

            if (!Arrays.equals(buf, that.buf)) return false;

            return true;
        }

        @Override
        public String toString() {
            return "TestByteArrayStorable{" +
                    "buf=" + Arrays.toString(buf) +
                    '}';
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(buf);
        }
    }
}
