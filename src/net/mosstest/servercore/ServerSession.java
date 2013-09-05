package net.mosstest.servercore;

import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

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
    public SocketChannel bulk;
    public SocketChannel fast;
    public DatagramChannel dgram;
    public ServerSession.State state;
    public enum State{
    	CONN_NEW, CONN_AUTH_SENT, CONN_AUTH_RECV, CONN_GAME_HANDSHAKE, CONN_PLAYING, CONN_BOT, CONN_TIMEOUT;
    }
}
