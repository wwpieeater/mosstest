package org.nodetest.servercore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;


public class ClientNetworkingManager {
	private static Socket bulkDataSocket=new Socket();
	private static Socket lowLatencyStreamSocket=new Socket();
	private static Socket udpSocket=new Socket();
	private static BufferedReader bulkReader;
	private static BufferedReader lowlatencyReader;
	private static BufferedReader udpReader;
	private static BufferedWriter bulkWriter;
	private static BufferedWriter lowlatencyWriter;
	private static BufferedWriter udpWriter;
	private static DataOutputStream bulkDataOut;
	private static DataOutputStream lowlatencyDataOut;
	private static DataOutputStream udpDataOut;
	private static DataInputStream bulkDataIn;
	private static DataInputStream lowlatencyDataIn;
	private static DataInputStream udpDataIn;
	public static void init(String endpoint, int port, boolean useUdp) throws IOException{
		lowLatencyStreamSocket.setPerformancePreferences(0, 1, 0);
		lowLatencyStreamSocket.setTrafficClass(0x10);
		lowLatencyStreamSocket.setTcpNoDelay(true);
		bulkDataSocket.connect(new InetSocketAddress(endpoint, port), 10000);
		bulkReader=new BufferedReader(new InputStreamReader(bulkDataSocket.getInputStream()));
		bulkWriter=new BufferedWriter(new PrintWriter(bulkDataSocket.getOutputStream()));
		bulkDataOut=new DataOutputStream(bulkDataSocket.getOutputStream());
		bulkDataIn=new DataInputStream(bulkDataSocket.getInputStream());
		//TODO we need to get all of our back-and-forth stuff done. And use nio for that matter.
	}
}
