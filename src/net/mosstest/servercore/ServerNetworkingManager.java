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
 * injury. The proximity of large and fast streams and datagram ice
 * floes is also a danger.
 * 
 * @author rarkenin
 * 
 */
public class ServerNetworkingManager {
	public class AcceptRunnable implements Runnable {
		@Override
		public void run() {
			acLoop: while (ServerNetworkingManager.this.runConnections.get()) {
				try {
					Socket sock = ServerNetworkingManager.this.sSock.accept();
					if (ServerNetworkingManager.this.currentConnections.get() >= ServerNetworkingManager.this.maxConnections) {
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
						new Thread(ServerNetworkingManager.this.svrNetGroup, new SocketRecvRunnable(),
								"connection thread" + Math.random()).start();
					}

				} catch (IOException | InterruptedException e) {
					ServerManager
							.error("IOError or interrupt accepting connection");
				}
			}
		}

	}

	public ServerNetworkingManager(int port, MossWorld world)
			throws IOException {
		this.w = world;
		this.bindingIdentifiers = new HashMap<>();
		this.acceptThread.start();
		this.maxConnections = EngineSettings.getInt("net.maxConnections", 255) * 2 + 1;
		this.runConnections = new AtomicBoolean(true);
		this.currentConnections = new AtomicInteger(0);
		this.currentServicingThreads = new AtomicInteger(0);
		this.sSock = new ServerSocket(port);
		this.connectionQueue = new ArrayBlockingQueue<>(4);
		this.acceptThread = new Thread(new AcceptRunnable(), "svrNetAccept");
		this.acceptThread.start();
	}

	private MossWorld w;
	private HashMap<Long, ServerSession> bindingIdentifiers;
	private final int maxConnections;
	private ThreadGroup svrNetGroup = new ThreadGroup("SvrNetGroup");
	protected AtomicBoolean runConnections;
	protected AtomicInteger currentConnections;
	protected AtomicInteger currentServicingThreads;
	protected ServerSocket sSock;
	protected ArrayBlockingQueue<Socket> connectionQueue;
	private Thread acceptThread;

	protected static void writeTcpPacket(OutputStream os, int commandId,
			byte[] payload) throws IOException {

	}

	private class SocketRecvRunnable implements Runnable {

		@SuppressWarnings("resource")
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
						long bindId = new Random().nextLong();
						ServerSession sess = new ServerSession();
						ArrayBlockingQueue<MossNetPacket> sendQueue = new ArrayBlockingQueue<>(
								128);
						sess.bulk = sendQueue;
						while (ServerNetworkingManager.this.runConnections
								.get() && !sock.isClosed()) {
							if (dataIn.readInt() != CommonNetworking.magic) {
								// Handle reconnect
							}
							int length = dataIn.readShort();
							byte[] buf = new byte[length];
							int commandId = dataIn.readUnsignedByte();
							if (commandId == 255) {
								sess.quenchedSince = System.currentTimeMillis();
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
