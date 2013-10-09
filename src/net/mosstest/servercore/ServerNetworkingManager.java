package net.mosstest.servercore;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
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
 * injury. The proximity of large and fast streams and datagram ice floes is
 * also a danger.
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
						new Thread(ServerNetworkingManager.this.svrNetGroup,
								new SocketRecvRunnable(), "connection thread"
										+ Math.random()).start();
					}

				} catch (IOException | InterruptedException e) {
					//pass
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
	protected ArrayBlockingQueue<ServerSession> sendThreadFormQueue;
	private Thread acceptThread;

	protected static void writeTcpPacket(OutputStream os, int commandId,
			byte[] payload) throws IOException {

	}

	protected class SocketSendRunnable implements Runnable {
		ServerSession sess;
		@Override
		public void run() {
			this.sess = ServerNetworkingManager.this.sendThreadFormQueue.poll();
			if (this.sess == null) {
				return;
			}
			pLoop: while (this.sess.isValid.get()) {
				try {
					MossNetPacket p = this.sess.packets.take();
					if (this.sess.quenchedSince < System.currentTimeMillis() - 4000
							&& !p.isImportant)
						continue pLoop;

					if (p.needsFast) {
						if ((p.payload.length < 250) && this.sess.dgramSocket!=null)
							sendPacketUdp(p.commandId, p.payload, p.needsAck);
						else {
							sendPacketLowLatency(p.commandId, p.payload);
						}
					} else
						sendPacketDefault(p.commandId, p.payload);

				} catch (InterruptedException | IOException e) {
					// PASS
				}

			}

		}

		private void sendPacketDefault(int commandId, byte[] payload) throws IOException {
			sendImpl(commandId, payload, this.sess.bulkSocket);
			
		}

		@SuppressWarnings("resource")
		private void sendPacketLowLatency(int commandId, byte[] payload) throws IOException {
			Socket targetSocket=(this.sess.fastSocket==null)?this.sess.bulkSocket:this.sess.fastSocket;
			sendImpl(commandId, payload, targetSocket);			
		}

		private void sendImpl(int commandId, byte[] payload, Socket targetSocket) throws IOException {
			
			OutputStream os=targetSocket.getOutputStream();
			DataOutputStream dos=new DataOutputStream(os);
			dos.writeInt(CommonNetworking.magic);
			dos.writeInt(payload.length);
			dos.writeByte(commandId);
			dos.write(payload);
			dos.flush();
			os.flush();
			
		}

		private void sendPacketUdp(int commandId, byte[] payload,
				boolean needsAck) throws IOException {
			if(this.sess.dgramSocket==null) throw new NullPointerException("Null datagram socket!"); //$NON-NLS-1$
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			DataOutputStream dos=new DataOutputStream(bos);
			dos.writeInt(CommonNetworking.magicNoAck);
			dos.writeByte(commandId);
			dos.writeByte(payload.length);
			dos.flush();
			bos.write(payload);
			DatagramPacket pc=new DatagramPacket(bos.toByteArray(), bos.toByteArray().length);
			sess.dgramSocket.send(pc);
			
		}

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
						DataInputStream dataIn = new DataInputStream(in);
						long bindId = new Random().nextLong();
						ServerSession sess = new ServerSession();
						ServerNetworkingManager.this.sendThreadFormQueue
								.put(sess);
						sess.bulkSocket = sock;
						ByteArrayOutputStream ba = new ByteArrayOutputStream();
						DataOutputStream dos = new DataOutputStream(ba);
						dos.writeLong(bindId);
						dos.flush();
						ba.flush();
						sess.packets.put(new MossNetPacket(0xFE, ba
								.toByteArray(), false, true, true));
						SocketSendRunnable ssr = new SocketSendRunnable();
						Thread sendThread = new Thread(ssr);
						sendThread.start();
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
							if (commandId == 254) {
								// FIXME 0.1
								sess.isValid.set(false);
								sess=bindingIdentifiers.get(Long.valueOf(dataIn.readLong()));
								sess.fastSocket=sock;
								sock.setTcpNoDelay(true);
								
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
