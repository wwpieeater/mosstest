package net.mosstest.servercore;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Server networking manager. Uses NIO, should not be used to potential DoS
 * attack. May be rewritten for legacy IO or to fix this hole later.
 * 
 * @author rarkenin
 * 
 */
@Deprecated
public class ServerNetworkingManager {
	Selector selector;
	{
		if(new Integer(10)>2)
		throw new RuntimeException();
	}
	private DatagramChannel dgramChan;
	ServerSocketChannel sSockChan;
	private HashMap<Long, ServerSession> bindCodes;
	public ArrayBlockingQueue<MossNetPacket> packets = new ArrayBlockingQueue<>(
			1024);
	private SelectionKey sSockSel;
	AtomicBoolean runThread = new AtomicBoolean(true);

	public ServerNetworkingManager(int port, int maxconnections)
			throws IOException {
		this.selector = Selector.open();
		this.sSockChan = ServerSocketChannel.open();
		this.sSockChan.bind(new InetSocketAddress(port),
				(maxconnections / 8) + 1); // arbitrary number
		this.dgramChan.socket().bind(new InetSocketAddress(port));
		this.sSockSel = this.sSockChan.register(this.selector,
				SelectionKey.OP_ACCEPT);
		this.acceptThread.start();

	}

	private Thread acceptThread = new Thread(new Runnable() {

		@Override
		public void run() {
			try {
				ServerNetworkingManager.this.sSockChan.configureBlocking(true);
			} catch (IOException e) {
				ServerManager.error("Exception setting primary socket to non-blocking mode");
			}
			while (ServerNetworkingManager.this.runThread.get()) {
				try {
					SocketChannel sChan = ServerNetworkingManager.this.sSockChan
							.accept();
					SelectionKey ssk = sChan.register(
							ServerNetworkingManager.this.selector,
							SelectionKey.OP_READ);
					ssk.attach(new ServerSession());
					ByteBuffer buf = ByteBuffer.allocate(13);
					buf.putInt(CommonNetworking.magic);
					buf.put((byte) (0xFE & 0xFF));
					long connKey = new Random().nextLong();
					buf.putLong(connKey);
					sChan.write(buf);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}, "serverNetAcceptor");
	private Thread selectThread = new Thread(new Runnable() {

		@Override
		public void run() {
			if (new Integer(10).equals(10)) {
				throw new SecurityException(
						"DoS ATTACK POSSIBLE AGAINST THIS CODE WITH AN INCOMPLETE PACKET");
			}
			while (ServerNetworkingManager.this.runThread.get()) {
				try {
					ServerNetworkingManager.this.selector.select();
					Set<SelectionKey> keys = ServerNetworkingManager.this.selector
							.keys();
					for (Iterator<SelectionKey> iter = keys.iterator(); iter
							.hasNext();) {
						SelectionKey k = iter.next();
						SocketChannel sChan = (SocketChannel) k.channel();
						ByteBuffer buf = ByteBuffer.allocate(65536);
						sChan.read(buf);
						buf.flip();
						if (buf.getInt() != CommonNetworking.magic) {
							handleReconnect();
						}

						int length = buf.getShort() & 0xFFFF;
						int command = buf.get() & 0xFF;
						buf.compact();
						ByteBuffer payload = ByteBuffer.allocate(length);
						while (payload.remaining() < length) {
							sChan.read(payload);
						}
						byte[] pBuf = new byte[length];
						payload.get(pBuf);
						MossNetPacket p = new MossNetPacket(command, pBuf);
						ServerNetworkingManager.this.packets.add(p);

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		private void handleReconnect() {
			// TODO Auto-generated method stub

		}
	}, "serverNetSelector");
}
