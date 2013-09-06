package net.mosstest.servercore;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ClientNetworkingManager {
	// There's a potential DoS attack here but it can only be mounted by the
	// server, so you might as well just not use that server. No security
	// threat, won't fix.
	protected AtomicBoolean runThreads = new AtomicBoolean(true);
	protected Socket bulkDataSocket = new Socket();
	protected Socket lowLatencyStreamSocket = new Socket();
	protected DatagramSocket udpSocket;
	protected DataOutputStream bulkDataOut;
	protected DataOutputStream lowlatencyDataOut;
	protected DataInputStream bulkDataIn;
	protected DataInputStream lowlatencyDataIn;
	protected boolean udpOn = false;
	protected AtomicBoolean fastLinkAckd = new AtomicBoolean(false);
	protected final InetAddress endpoint;
	protected int port;
	protected AtomicLong lastBulkOut = new AtomicLong();
	protected AtomicLong lastBulkIn = new AtomicLong();
	protected AtomicLong lastFastOut = new AtomicLong();
	protected AtomicLong lastFastIn = new AtomicLong();
	protected AtomicLong lastUdpOut = new AtomicLong();
	protected AtomicLong lastUdpIn = new AtomicLong();
	/*
	 * Should be no need for another lowlatency queue unless we find poor
	 * performance
	 */
	public ArrayBlockingQueue<MossNetPacket> packets = new ArrayBlockingQueue<>(
			1024);
	protected Thread bulkReadHandler = new Thread(new Runnable() {

		@Override
		public void run() {

			try {
				recvLoop: while (ClientNetworkingManager.this.runThreads.get()) {

					if (ClientNetworkingManager.this.bulkDataIn.readInt() != CommonNetworking.magic) {
						// Handle reconnect
					}

					int length = ClientNetworkingManager.this.bulkDataIn
							.readShort();
					byte[] buf = new byte[length];

					int commandId = ClientNetworkingManager.this.bulkDataIn
							.readUnsignedByte();
					if (commandId == 255) {
						ClientNetworkingManager.this.partyQuenched.set(true);
						continue recvLoop;
					}

					ClientNetworkingManager.this.bulkStreamIn.read(buf);
					if (commandId == 254) {
						ClientNetworkingManager.this.fastLinkAckd.set(true); 
						sendPacketLowLatency(254, buf);
						sendPacketUdp(254, buf, true);
						continue recvLoop;
					}
					ClientNetworkingManager.this.packets.add(new MossNetPacket(
							commandId, buf));
					ClientNetworkingManager.this.lastBulkIn.set(System
							.currentTimeMillis());
					if (ClientNetworkingManager.this.packets
							.remainingCapacity() < 32)
						sendQuench();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}, "ClientBulkRecv"); //$NON-NLS-1$
	protected Thread fastReadHandler = new Thread(new Runnable() {
		// TODO
		@Override
		public void run() {

			try {
				recvLoop: while (ClientNetworkingManager.this.runThreads.get()) {

					if (ClientNetworkingManager.this.lowlatencyDataIn.readInt() != CommonNetworking.magic) {
						// Handle reconnect
					}
					int length = ClientNetworkingManager.this.lowlatencyDataIn
							.readShort();
					byte[] buf = new byte[length];

					int commandId = ClientNetworkingManager.this.lowlatencyDataIn
							.readUnsignedByte();
					if (commandId == 255) {
						ClientNetworkingManager.this.partyQuenched.set(true);
						continue recvLoop;
					}
					ClientNetworkingManager.this.fastStreamIn.read(buf);
					ClientNetworkingManager.this.packets.add(new MossNetPacket(
							commandId, buf));
					ClientNetworkingManager.this.lastFastIn.set(System
							.currentTimeMillis());
					ClientNetworkingManager.this.packets.add(new MossNetPacket(
							commandId, buf));
					if (ClientNetworkingManager.this.packets
							.remainingCapacity() < 32)
						sendQuench();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}, "ClientBulkRecv"); //$NON-NLS-1$
	protected AtomicBoolean partyQuenched = new AtomicBoolean(false);
	AtomicLong quenchedSince = new AtomicLong(0);
	protected Thread dgramReadHandler = new Thread(new Runnable() {
		// TODO--spanish for "all"
		@Override
		public void run() {

			recvLoop: while (ClientNetworkingManager.this.runThreads.get()) {
				byte[] buf = new byte[270];
				DatagramPacket pckt = new DatagramPacket(buf, 270);
				try {
					ClientNetworkingManager.this.udpSocket.receive(pckt);
					ByteArrayInputStream bufStr = new ByteArrayInputStream(
							pckt.getData());
					if (!pckt.getAddress().equals(
							ClientNetworkingManager.this.endpoint)) {
						System.out.println("received mismatched packet source"); //$NON-NLS-1$
						continue recvLoop;
					}
					DataInputStream dos = new DataInputStream(bufStr);
					int magic = dos.readInt();

					if (magic == CommonNetworking.magic)
						sendAck(dos.readUnsignedShort());
					if (!(magic == CommonNetworking.magic || magic == CommonNetworking.magicNoAck)) {
						System.out.println("bad magic"); //$NON-NLS-1$
						continue recvLoop;
					}
					int length = dos.readUnsignedByte();
					int commandId = dos.readUnsignedByte();
					if (commandId == 255) {
						ClientNetworkingManager.this.partyQuenched.set(true);
						continue recvLoop;
					}
					byte[] pBuf = new byte[length];
					bufStr.read(pBuf);
					ClientNetworkingManager.this.lastUdpIn.set(System
							.currentTimeMillis());
					ClientNetworkingManager.this.packets.add(new MossNetPacket(
							commandId, pBuf));

				} catch (IOException e) {
					ClientNetworkingManager.this.udpOn = false;
				}
			}

		}
	}, "ClientDgramRecv"); //$NON-NLS-1$

	protected InputStream bulkStreamIn;
	protected OutputStream bulkStreamOut;
	protected InputStream fastStreamIn;
	protected OutputStream fastStreamOut;

	public ClientNetworkingManager(String endpoint, int port, boolean useUdp)
			throws IOException {
		this.endpoint = InetAddress.getByName(endpoint);
		this.lowLatencyStreamSocket.setPerformancePreferences(0, 1, 0);
		this.lowLatencyStreamSocket.setTrafficClass(0x10);
		this.lowLatencyStreamSocket.setTcpNoDelay(true);
		this.bulkDataSocket.connect(new InetSocketAddress(endpoint, 16511),
				16511);
		this.bulkStreamIn = this.bulkDataSocket.getInputStream();
		this.bulkStreamOut = this.bulkDataSocket.getOutputStream();
		this.bulkDataOut = new DataOutputStream(this.bulkStreamOut);
		this.bulkDataIn = new DataInputStream(this.bulkStreamIn);
		this.lowLatencyStreamSocket.connect(new InetSocketAddress(endpoint,
				16511), 16511);
		this.fastStreamIn = this.lowLatencyStreamSocket.getInputStream();
		this.fastStreamOut = this.lowLatencyStreamSocket.getOutputStream();
		this.lowlatencyDataOut = new DataOutputStream(this.fastStreamOut);
		this.lowlatencyDataIn = new DataInputStream(this.fastStreamIn);
		this.udpOn = false;
		if (useUdp) {
			try {
				this.udpSocket = new DatagramSocket(port,
						InetAddress.getByName(endpoint));
				this.udpSocket.setSoTimeout(0);
				sendTosUdpConn();
			} catch (SocketException e) {
				this.udpOn = false;
			}
		}
		this.lastBulkIn.set(System.currentTimeMillis());
		this.lastBulkOut.set(System.currentTimeMillis());
		this.lastFastIn.set(System.currentTimeMillis());
		this.lastFastOut.set(System.currentTimeMillis());
		this.lastUdpIn.set(System.currentTimeMillis());
		this.lastUdpOut.set(System.currentTimeMillis());
		this.bulkReadHandler.start();
		this.fastReadHandler.start();
		this.dgramReadHandler.start();
		this.sendQueueThread.start();
		this.netTimeoutThread.start();
	}

	protected void sendAck(int seqnum) {
		// TODO Auto-generated method stub

	}

	protected void sendTosUdpConn() {
		// TODO Auto-generated method stub

	}

	protected void sendQuench() {
		// TODO Sends a request for the server to back off with data and skip
		// non-essential data.

	}

	/**
	 * Send a packet, dispatching to the correct
	 * 
	 * @param commandId
	 *            Byte representing command ID.
	 * @param payload
	 *            The data to send.
	 * @param latencyPrio
	 * @throws IOException
	 */
	protected void sendPacket(MossNetPacket p) throws IOException {
		if(this.partyQuenched.get()&&!p.isImportant) return;
		
		if (p.needsFast) {
			if ((p.payload.length < 250) && this.udpOn)
				sendPacketUdp(p.commandId, p.payload, p.needsAck);
			else {
				sendPacketLowLatency(p.commandId, p.payload);
			}
		} else
			sendPacketDefault(p.commandId, p.payload);

	}

	protected void sendPacketDefault(int commandId, byte[] payload)
			throws IOException {
		this.lastBulkOut.set(System.currentTimeMillis());
		synchronized (this.bulkDataOut) {
			try {
				this.bulkDataOut.writeInt(CommonNetworking.magic);
				this.bulkDataOut.writeShort(payload.length);
				this.bulkDataOut.write(commandId);
				this.bulkDataOut.write(payload);
				this.bulkDataOut.flush();
			} catch (IOException e) {
				defaultReinit();
				this.bulkDataOut.writeInt(CommonNetworking.magic);
				this.bulkDataOut.writeShort(payload.length);
				this.bulkDataOut.write(commandId);
				this.bulkDataOut.write(payload);
				this.bulkDataOut.flush();
			}
		}
	}

	protected void defaultReinit() throws IOException {
		this.bulkDataIn.close();
		this.bulkDataOut.close();
		this.bulkDataSocket.close();
		this.bulkDataSocket = new Socket();
		this.bulkDataSocket.connect(new InetSocketAddress(this.endpoint,
				this.port), 10000);

		this.bulkDataOut = new DataOutputStream(
				this.bulkDataSocket.getOutputStream());
		this.bulkDataIn = new DataInputStream(
				this.bulkDataSocket.getInputStream());
		performBulkReconnect();
		this.lastBulkIn.set(System.currentTimeMillis());
		this.lastBulkOut.set(System.currentTimeMillis());

	}

	protected void performBulkReconnect() {
		synchronized (this.bulkDataOut) {
			// PERFORM RECONNECTION
		}

	}

	protected void sendPacketLowLatency(int commandId, byte[] payload)
			throws IOException {
		if (!this.fastLinkAckd.get()) {
			sendPacketDefault(commandId, payload);
		} else {
			this.lastFastOut.set(System.currentTimeMillis());
			try {
				this.lowlatencyDataOut.writeInt(CommonNetworking.magic);
				this.lowlatencyDataOut.writeShort(payload.length);
				this.lowlatencyDataOut.write(commandId);
				this.lowlatencyDataOut.flush();
				this.fastStreamOut.write(payload);
				this.fastStreamOut.flush();

			} catch (IOException e) {
				defaultReinit();
				this.lowlatencyDataOut.writeInt(CommonNetworking.magic);
				this.lowlatencyDataOut.writeShort(payload.length);
				this.lowlatencyDataOut.write(commandId);
				this.lowlatencyDataOut.flush();
				this.fastStreamOut.write(payload);
				this.fastStreamOut.flush();
			}
		}
	}

	protected void sendPacketUdp(int commandId, byte[] payload, boolean needsAck)
			throws IOException {
		this.lastUdpOut.set(System.currentTimeMillis());
		DatagramPacket toSend = new DatagramPacket(payload, payload.length);
		toSend.setSocketAddress(new InetSocketAddress(this.endpoint, this.port));
		synchronized (this.udpSocket) {
			this.udpSocket.send(toSend);
		}

	}

	public void enqueuePacket(MossNetPacket p) throws InterruptedException {
		this.sendQueue.put(p);
	}

	protected class StateMachine {
		static final int DISCONNECTED = 0;
		static final int LINK = 1;
		static final int AUTH = 2;
		static final int RESOURCE_XFER = 3;
		static final int ESTABLISHED = 4;
		static final int DENIED = 5;
		static final int TIMEDOUT = 6;
		int curStatus = 0;
	}

	public void beginConnectHandshake() {
		// TODO Auto-generated method stub

	}

	final ArrayBlockingQueue<MossNetPacket> sendQueue = new ArrayBlockingQueue<>(
			1024);

	private Thread sendQueueThread = new Thread(new Runnable() {

		@Override
		public void run() {
			while (ClientNetworkingManager.this.runThreads.get()) {
				try {
					MossNetPacket p = ClientNetworkingManager.this.sendQueue.take();
					ClientNetworkingManager.this.sendPacket(p);
				} catch (InterruptedException | IOException e) {
					// superfluous exception
				}
			}
		}
	}, "netClientSendQueue");

	private Thread netTimeoutThread = new Thread(new Runnable() {

		@Override
		public void run() {
			while (ClientNetworkingManager.this.runThreads.get()) {
				long cTime = System.currentTimeMillis();
				if (cTime - ClientNetworkingManager.this.lastBulkIn.get() > 10000
						|| cTime
								- ClientNetworkingManager.this.lastFastIn.get() > 10000
						|| cTime - ClientNetworkingManager.this.lastUdpIn.get() > 10000) {
					ClientManager
							.showErrorScreen(
									"Network Timeout",
									"The connection to the server has been lost or has become too slow to continue. \r\nThis is likely caused by an unstable connection, poor WiFi, loose cables, or a firewall issue.",
									true);

				}
				try {
					if (cTime - ClientNetworkingManager.this.lastBulkOut.get() > 4000)
						sendPacketDefault(00, new byte[] {});
				} catch (IOException e) {
					// pass
				}
				try {
					if (cTime - ClientNetworkingManager.this.lastFastOut.get() > 4000)
						sendPacketLowLatency(00, new byte[] {});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if (cTime - ClientNetworkingManager.this.lastUdpOut.get() > 4000)
						sendPacketUdp(00, new byte[] {}, false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (cTime - ClientNetworkingManager.this.quenchedSince.get() > 4000)
					ClientNetworkingManager.this.partyQuenched.set(false);

				try {
					// oh, joy
					Thread.sleep(4000
							- cTime
							+ Math.min(
									ClientNetworkingManager.this.lastBulkIn
											.get(),
									Math.min(
											ClientNetworkingManager.this.lastBulkOut
													.get(),
											Math.min(
													ClientNetworkingManager.this.lastFastIn
															.get(),
													Math.min(
															ClientNetworkingManager.this.lastFastOut
																	.get(),
															Math.min(
																	ClientNetworkingManager.this.lastUdpIn
																			.get(),
																	Math.min(
																			ClientNetworkingManager.this.lastUdpOut
																					.get(),
																			ClientNetworkingManager.this.quenchedSince
																					.get())))))));
				} catch (InterruptedException e) {
					// pass
				}

			}

		}
	}, "netTimeout");

}
