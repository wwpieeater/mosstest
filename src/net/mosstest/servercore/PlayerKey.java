package net.mosstest.servercore;

import net.mosstest.scripting.Player;
import net.mosstest.servercore.serialization.IByteArrayWritable;

    public class PlayerKey implements IByteArrayWritable {
        private final Player player;


        public PlayerKey(Player player) {
            this.player = player;
        }

        @Override
        public byte[] toBytes() {
            return player.name.getBytes();
        }




    }

