package org.nodetest.servercore;

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

public class ClientNetworkingManager {
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

	public void init(String endpoint, int port, boolean useUdp)
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
		// TODO we need to get all of our back-and-forth stuff done. And use nio
		// for that matter.
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

	}

	private void sendPacketLowLatency(int commandId, String payload) throws IOException {
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

}
