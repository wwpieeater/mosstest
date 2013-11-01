package net.mosstest.servercore;

import java.net.DatagramSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import net.mosstest.scripting.Player;

/**
 * The ServerSession class unifies a session on a server. Each incoming connection has a different session.
 * When a connection is determined to be associated with an existing session,  
 * 
 * @author rarkenin
 *
 */
public class ServerSession {
    public Player player;
    public String authChallenge;
    public ArrayBlockingQueue<MossNetPacket> packets;
    public volatile Socket bulkSocket;
    public volatile Socket fastSocket;
    public volatile DatagramSocket dgramSocket;
    public AtomicBoolean isValid=new AtomicBoolean(true);
    public ServerSession.State state;
    public volatile long quenchedSince=0;
    public static enum State{
    	CONN_NEW, CONN_AUTH_SENT, CONN_AUTH_RECV, CONN_GAME_HANDSHAKE, CONN_PLAYING, CONN_BOT, CONN_TIMEOUT;
    }
}
