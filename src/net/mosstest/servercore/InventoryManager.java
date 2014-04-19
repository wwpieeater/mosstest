package net.mosstest.servercore;

import net.mosstest.scripting.Player;
import net.mosstest.servercore.serialization.IByteArrayWriteable;
import org.apache.commons.lang.NotImplementedException;

import java.io.IOException;

public class InventoryManager {
    public class PlayerKey implements IByteArrayWriteable {
        private final Player player;

        private final String inventory;

        public PlayerKey(Player player, String inventory) {
            this.player = player;
            this.inventory = inventory;
        }

        @Override
        public byte[] toBytes() {
            return new byte[0];
        }




    }
}
