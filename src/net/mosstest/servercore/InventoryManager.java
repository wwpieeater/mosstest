package net.mosstest.servercore;

import net.mosstest.scripting.Player;
import org.apache.commons.lang.NotImplementedException;

import java.io.IOException;

public class InventoryManager {
    public class PlayerKey extends AbstractByteArrayStorable<Void> {
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

        @Override
        protected void setManager(Void manager) {
            // no-op as we have a void
        }

        @Override
        public void loadBytes(byte[] buf) throws IOException {
            throw new NotImplementedException("A player key cannot be created from a byte[]");
        }
    }
}
