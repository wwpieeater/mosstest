package net.mosstest.servercore;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Server networking manager. Now uses standard IO. This is a nasty thread pool.
 * Misuse may result in chlorine poisoning, asphyxiation, drowning, death, or
 * injury may occur. The proximity of large and fast streams and datagram ice
 * floes is also a danger.
 * 
 * @author rarkenin
 * 
 */
public class ServerNetworkingManager {
	private HashMap<Long, ServerSession> bindingIdentifiers = new HashMap<>();
	private static final int maxConnections = EngineSettings.getInt(
			"maxPlayers", 128) * 2 + 1;
	private static ThreadGroup svrNetGroup = new ThreadGroup("SvrNetGroup");
	protected AtomicBoolean runConnections = new AtomicBoolean(true);
	protected AtomicInteger currentConnections = new AtomicInteger(0);
	protected AtomicInteger currentServicingThreads = new AtomicInteger(0);
	protected ServerSocket sSock;
	protected ArrayBlockingQueue<Socket> connectionQueue = new ArrayBlockingQueue<>(
			4);
	private Thread acceptThread = new Thread(svrNetGroup, new Runnable() {

		@Override
		public void run() {
			acLoop: while (ServerNetworkingManager.this.runConnections.get()) {
				try {
					Socket sock = ServerNetworkingManager.this.sSock.accept();
					if (ServerNetworkingManager.this.currentConnections.get() >= maxConnections) {
						writeTcpPacket(sock.getOutputStream(), 0x03,
								new byte[] { 0x03 });
						sock.close();
						continue acLoop;
					}
					ServerNetworkingManager.this.currentConnections
							.incrementAndGet();
					ServerNetworkingManager.this.connectionQueue.put(sock);
					if (ServerNetworkingManager.this.currentConnections.get() > ServerNetworkingManager.this.currentServicingThreads
							.get()) {
						new Thread(svrNetGroup, new SocketRecvRunnable(),
								"connection thread" + Math.random()).start();
					}

				} catch (IOException | InterruptedException e) {
					ServerManager
							.error("IOError or interrupt accepting connection");
				}
			}
		}
	}, "svrNetAccept");

	protected static void writeTcpPacket(OutputStream os, int commandId,
			byte[] payload) throws IOException {

	}

	private class SocketRecvRunnable implements Runnable {

		@Override
		public void run() {
			ServerNetworkingManager.this.currentServicingThreads
					.incrementAndGet();
			try {
				while (ServerNetworkingManager.this.runConnections.get()) {
					try {
						Socket sock = ServerNetworkingManager.this.connectionQueue
								.take();
						InputStream in = sock.getInputStream();
						OutputStream out = sock.getOutputStream();
						DataInputStream dataIn = new DataInputStream(in);
						DataOutputStream dataOut = new DataOutputStream(out);
						long bindId=new Random().nextLong();
						ServerSession sess=new ServerSession();
						ArrayBlockingQueue<MossNetPacket> sendQueue=new ArrayBlockingQueue<>(128);
						sess.bulk=sendQueue;
						while (ServerNetworkingManager.this.runConnections
								.get() && !sock.isClosed()) {
							if (dataIn.readInt() != CommonNetworking.magic) {
								// Handle reconnect
							}
							int length = dataIn.readShort();
							byte[] buf = new byte[length];
							int commandId = dataIn.readUnsignedByte();
							if(commandId == 255) {
								sess.quenchedSince=System.currentTimeMillis();
							}
							

						}

					} catch (InterruptedException e) {
						// pass
					}
				}
			} catch (Exception e) {// pass
			} finally {
				ServerNetworkingManager.this.currentServicingThreads
						.decrementAndGet();
			}
		}
	}

}
