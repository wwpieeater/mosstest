package net.mosstest.servercore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.CharBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientNetworkingManager {
	protected AtomicBoolean runReader = new AtomicBoolean(true);
	protected Socket bulkDataSocket = new Socket();
	protected Socket lowLatencyStreamSocket = new Socket();
	protected DatagramSocket udpSocket;
	protected DataOutputStream bulkDataOut;
	protected DataOutputStream lowlatencyDataOut;
	protected DataInputStream bulkDataIn;
	protected DataInputStream lowlatencyDataIn;
	protected boolean udpOn = false;
	protected InetAddress endpoint;
	protected int port;
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
				while (ClientNetworkingManager.this.runReader.get()) {

					if (ClientNetworkingManager.this.bulkDataIn.readInt() != CommonNetworking.magic) {
						// Handle reconnect
					}
					int length = ClientNetworkingManager.this.bulkDataIn
							.readInt();
					StringBuilder sb = new StringBuilder(length);
					int read = 0;
					int commandId = ClientNetworkingManager.this.bulkDataIn
							.readUnsignedByte();

					while (read < length) {
						sb.append(ClientNetworkingManager.this.bulkDataIn
								.readByte());
						read++;
					}
					ClientNetworkingManager.this.packets.add(new MossNetPacket(
							commandId, sb.toString()));
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
				while (ClientNetworkingManager.this.runReader.get()) {

					if (ClientNetworkingManager.this.lowlatencyDataIn.readInt() != CommonNetworking.magic) {
						// Handle reconnect
					}
					int length = ClientNetworkingManager.this.lowlatencyDataIn
							.readInt();
					StringBuilder sb = new StringBuilder(length);
					int read = 0;
					int commandId = ClientNetworkingManager.this.lowlatencyDataIn
							.readUnsignedByte();

					while (read < length) {
						sb.append(ClientNetworkingManager.this.lowlatencyDataIn
								.readByte());
						read++;
					}
					ClientNetworkingManager.this.packets.add(new MossNetPacket(
							commandId, sb.toString()));
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
	protected Thread dgramReadHandler = new Thread(new Runnable() {
		// TODO--spanish for "all"
		@Override
		public void run() {

			recvLoop: while (ClientNetworkingManager.this.runReader.get()) {
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
					StringBuilder sb = new StringBuilder();
					int read = 0;
					while (read < length) {
						sb.append(dos.readByte());
						read++;
					}
					ClientNetworkingManager.this.packets.add(new MossNetPacket(
							commandId, sb.toString()));

				} catch (IOException e) {
					ClientNetworkingManager.this.udpOn = false;
				}
			}

		}
	}, "ClientDgramRecv"); //$NON-NLS-1$

	public ClientNetworkingManager(String endpoint, int port, boolean useUdp)
			throws IOException {
		this.endpoint = InetAddress.getByName(endpoint);
		this.lowLatencyStreamSocket.setPerformancePreferences(0, 1, 0);
		this.lowLatencyStreamSocket.setTrafficClass(0x10);
		this.lowLatencyStreamSocket.setTcpNoDelay(true);
		this.bulkDataSocket.connect(new InetSocketAddress(endpoint, 16511),
				16511);

		this.bulkDataOut = new DataOutputStream(
				this.bulkDataSocket.getOutputStream());
		this.bulkDataIn = new DataInputStream(
				this.bulkDataSocket.getInputStream());
		this.lowLatencyStreamSocket.connect(new InetSocketAddress(endpoint,
				16511), 16511);

		this.lowlatencyDataOut = new DataOutputStream(
				this.lowLatencyStreamSocket.getOutputStream());
		this.lowlatencyDataIn = new DataInputStream(
				this.lowLatencyStreamSocket.getInputStream());
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

	// TODO we need to get all of our back-and-forth stuff done. And use nio
	// for that matter.

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
	@SuppressWarnings("unused")
	protected void sendPacket(int commandId, String payload, boolean needsFast,
			boolean needsAck) throws IOException {
		if (needsFast) {
			if ((payload.length() < 250) && this.udpOn)
				sendPacketUdp(commandId, payload, needsAck);
			else
				sendPacketLowLatency(commandId, payload);
		} else
			sendPacketDefault(commandId, payload);

	}

	protected void sendPacketDefault(int commandId, String payload)
			throws IOException {
		synchronized (this.bulkDataOut) {
			try {
				this.bulkDataOut.writeInt(CommonNetworking.magic);
				this.bulkDataOut.writeInt(payload.length());
				this.bulkDataOut.write(commandId);
				this.bulkDataOut.writeBytes(payload);
				this.bulkDataOut.flush();
			} catch (IOException e) {
				defaultReinit();
				this.bulkDataOut.writeInt(CommonNetworking.magic);
				this.bulkDataOut.writeInt(payload.length());
				this.bulkDataOut.write(commandId);
				this.bulkDataOut.writeBytes(payload);
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

	}

	protected void performBulkReconnect() {
		synchronized (this.bulkDataOut) {
			// PERFORM RECONNECTION
		}

	}

	protected void sendPacketLowLatency(int commandId, String payload)
			throws IOException {
		try {
			this.lowlatencyDataOut.writeInt(CommonNetworking.magic);
			this.lowlatencyDataOut.writeInt(payload.length());
			this.lowlatencyDataOut.write(commandId);
			this.lowlatencyDataOut.writeBytes(payload);
			this.lowlatencyDataOut.flush();
		} catch (IOException e) {
			defaultReinit();
			this.lowlatencyDataOut.writeInt(CommonNetworking.magic);
			this.lowlatencyDataOut.writeInt(payload.length());
			this.lowlatencyDataOut.write(commandId);
			this.lowlatencyDataOut.writeBytes(payload);
			this.lowlatencyDataOut.flush();
		}
	}

	protected void sendPacketUdp(int commandId, String payload, boolean needsAck) {
		// TODO Auto-generated method stub

	}

	public void sendChunkRequest(Position pos) {
		// TODO Auto-generated method stub

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

}
