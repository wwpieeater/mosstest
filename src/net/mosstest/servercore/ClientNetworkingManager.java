package net.mosstest.servercore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.CharBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientNetworkingManager {
	private AtomicBoolean runReader = new AtomicBoolean(true);
	private Socket bulkDataSocket = new Socket();
	private Socket lowLatencyStreamSocket = new Socket();
	private DatagramSocket udpSocket;
	private BufferedReader bulkReader;
	private BufferedReader lowlatencyReader;
	private BufferedReader udpReader;
	private BufferedWriter bulkWriter;
	private BufferedWriter lowlatencyWriter;
	private BufferedWriter udpWriter;
	private DataOutputStream bulkDataOut;
	private DataOutputStream lowlatencyDataOut;
	private DataOutputStream udpDataOut;
	private DataInputStream bulkDataIn;
	private DataInputStream lowlatencyDataIn;
	private DataInputStream udpDataIn;
	private boolean udpOn = false;
	private InetAddress endpoint;
	private int port;
	public ArrayBlockingQueue<MossNetPacket> packets=new ArrayBlockingQueue<>(1024);
	private Thread bulkReadHandler = new Thread(new Runnable() {
		
		@Override
		public void run() {

			try {
				while (runReader.get()) {

					if (bulkDataIn.readInt() != CommonNetworking.magic) {
						// Handle reconnect
					}
					int length = bulkDataIn.readInt();
					StringBuilder sb = new StringBuilder(length);
					int read = 0;
					int commandId = bulkDataIn.readUnsignedByte();

					while (read < length) {
						sb.append(bulkDataIn.readByte());
						read++;
					}
					packets.add(new MossNetPacket(commandId, sb.toString()));
					if(packets.remainingCapacity()<32) sendQuench();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}, "ClientBulkRecv");
	private Thread fastReadHandler = new Thread(new Runnable() {
//TODO
		@Override
		public void run() {
			while (runReader.get()) {

			}

		}
	}, "ClientBulkRecv");
	private Thread dgramReadHandler = new Thread(new Runnable() {
//TODO--spanish for "all"
		@Override
		public void run() {
			while (runReader.get()) {

			}

		}
	}, "ClientDgramRecv");

	public ClientNetworkingManager(String endpoint, int port, boolean useUdp)
			throws IOException {
		this.endpoint = InetAddress.getByName(endpoint);
		lowLatencyStreamSocket.setPerformancePreferences(0, 1, 0);
		lowLatencyStreamSocket.setTrafficClass(0x10);
		lowLatencyStreamSocket.setTcpNoDelay(true);
		bulkDataSocket.connect(new InetSocketAddress(endpoint, 16511), 16511);
		bulkReader = new BufferedReader(new InputStreamReader(
				bulkDataSocket.getInputStream()));
		bulkWriter = new BufferedWriter(new PrintWriter(
				bulkDataSocket.getOutputStream()));
		bulkDataOut = new DataOutputStream(bulkDataSocket.getOutputStream());
		bulkDataIn = new DataInputStream(bulkDataSocket.getInputStream());
		lowLatencyStreamSocket.connect(new InetSocketAddress(endpoint, 16511),
				16511);
		lowlatencyReader = new BufferedReader(new InputStreamReader(
				lowLatencyStreamSocket.getInputStream()));
		lowlatencyWriter = new BufferedWriter(new PrintWriter(
				lowLatencyStreamSocket.getOutputStream()));
		lowlatencyDataOut = new DataOutputStream(
				lowLatencyStreamSocket.getOutputStream());
		lowlatencyDataIn = new DataInputStream(
				lowLatencyStreamSocket.getInputStream());
		udpOn = false;
		if (useUdp) {
			try {
				udpSocket = new DatagramSocket(port,
						InetAddress.getByName(endpoint));

			} catch (SocketException e) {
				udpOn = false;
			}
		}

	}

	protected void sendQuench() {
		// TODO Sends a request for the server to back off with data and skip non-essential data.
		
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
	private void sendPacket(int commandId, String payload, boolean needsFast,
			boolean needsAck) throws IOException {
		if (needsFast) {
			if ((payload.length() < 250) && udpOn)
				sendPacketUdp(commandId, payload, needsAck);
			else
				sendPacketLowLatency(commandId, payload);
		} else
			sendPacketDefault(commandId, payload);

	}

	private void sendPacketDefault(int commandId, String payload)
			throws IOException {
		try {
			bulkDataOut.writeInt(CommonNetworking.magic);
			bulkDataOut.writeInt(payload.length());
			bulkDataOut.write(commandId);
			bulkDataOut.writeBytes(payload);
			bulkDataOut.flush();
		} catch (IOException e) {
			defaultReinit();
			bulkDataOut.writeInt(CommonNetworking.magic);
			bulkDataOut.writeInt(payload.length());
			bulkDataOut.write(commandId);
			bulkDataOut.writeBytes(payload);
			bulkDataOut.flush();
		}

	}

	private void defaultReinit() throws IOException {
		bulkDataIn.close();
		bulkDataOut.close();
		bulkReader.close();
		bulkWriter.close();
		bulkDataSocket.close();
		bulkDataSocket = new Socket();
		bulkDataSocket.connect(new InetSocketAddress(endpoint, port), 10000);
		bulkReader = new BufferedReader(new InputStreamReader(
				bulkDataSocket.getInputStream()));
		bulkWriter = new BufferedWriter(new PrintWriter(
				bulkDataSocket.getOutputStream()));
		bulkDataOut = new DataOutputStream(bulkDataSocket.getOutputStream());
		bulkDataIn = new DataInputStream(bulkDataSocket.getInputStream());
		performReconnect(bulkDataOut, bulkDataIn);

	}

	private void performReconnect(DataOutputStream oStream,
			DataInputStream iStream) {
		synchronized (oStream) {
			// PERFORM RECONNECTION
		}

	}

	private void sendPacketLowLatency(int commandId, String payload)
			throws IOException {
		try {
			lowlatencyDataOut.writeInt(CommonNetworking.magic);
			lowlatencyDataOut.writeInt(payload.length());
			lowlatencyDataOut.write(commandId);
			lowlatencyDataOut.writeBytes(payload);
			lowlatencyDataOut.flush();
		} catch (IOException e) {
			defaultReinit();
			lowlatencyDataOut.writeInt(CommonNetworking.magic);
			lowlatencyDataOut.writeInt(payload.length());
			lowlatencyDataOut.write(commandId);
			lowlatencyDataOut.writeBytes(payload);
			lowlatencyDataOut.flush();
		}
	}

	private void sendPacketUdp(int commandId, String payload, boolean needsAck) {
		// TODO Auto-generated method stub

	}

	public void sendChunkRequest(Position pos) {
		// TODO Auto-generated method stub

	}

	private class StateMachine {
		final int DISCONNECTED = 0;
		final int LINK = 1;
		final int AUTH = 2;
		final int RESOURCE_XFER = 3;
		final int ESTABLISHED = 4;
		final int DENIED = 5;
		final int TIMEDOUT = 6;
		int curStatus = 0;
	}

	public void beginConnectHandshake() {
		// TODO Auto-generated method stub

	}

}
