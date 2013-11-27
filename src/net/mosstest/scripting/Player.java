package net.mosstest.scripting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

import net.mosstest.servercore.PlayerCommunicator;
import net.mosstest.servercore.RenderProcessor;

public class Player {
	private HashMap<String, MossInventory> inventories = new HashMap<>();
	public final String name;
	public volatile double xoffset;
	public volatile double yoffset;
	public volatile double zoffset;
	public volatile int xchk;
	public volatile int ychk;
	public volatile int zchk;
	public volatile double xVelocity;
	public volatile double yVelocity;
	public volatile double zVelocity;
	public volatile double oldx;
	public volatile double oldy;
	public volatile double oldz;
	public volatile int oldxchk;
	public volatile int oldychk;
	public volatile int oldzchk;
	public volatile long lastAnticheatMillis;
	private PlayerCommunicator comm;
	private HashSet<String> privs;
	/**
	 * Object to be synchronized on for
	 */
	public final Object antiCheatDataLock = new Object();

	public Player(String name) {
		this.name = name;
		this.inventories.put("default", new MossInventory(96, 8, 6));
	} //$NON-NLS-1$

	public MossInventory createInventory(String name, int rows, int cols,
			int maxStack) {
		MossInventory inv = new MossInventory(maxStack, rows, cols);
		this.inventories.put(name, inv);
		return inv;
	}

	public void respawn() {
		synchronized (this.antiCheatDataLock) {

		}

	}

	public void grantPrivilege(String... privs) {
		for (String priv : privs)
			if (priv.matches("^[a-zA-Z0-9]*$") && priv.length() > 0)
				this.privs.add(priv);

	}

	public void revokePrivilege(String... privs) {
		for (String priv : privs)
			if (priv.matches("^[a-zA-Z0-9]*$") && priv.length() > 0)
				this.privs.remove(priv);

	}

	public synchronized void setChunkPosition(int x, int y, int z) {
		this.xchk = x;
		this.ychk = y;
		this.zchk = z;
	}

	public synchronized void setPositionOffsets(double x, double y, double z) {
		this.xoffset = x;
		this.yoffset = y;
		this.zoffset = z;
	}

	public String privsToString() {
		return StringUtils.join(this.privs, ':');
	}

	public void setPrivsFromString(String serPrivs) {
		this.privs.clear();
		for (String priv : serPrivs.split(":"))
			if (priv.matches("^[a-zA-Z0-9]*$") && priv.length() > 0)
				this.privs.add(priv);

	}

	public void forceSetPosition(double offsetx, double offsety,
			double offsetz, int cx, int cy, int cz) throws InterruptedException {
		this.comm.forceSetPosition(this, cx, cy, cz, offsetx, offsety, offsetz);
	}

	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		dos.writeInt(this.xchk);
		dos.writeInt(this.ychk);
		dos.writeInt(this.zchk);
		dos.writeDouble(this.xoffset);
		dos.writeDouble(this.yoffset);
		dos.writeDouble(this.zoffset);
		dos.writeUTF(this.name);
		dos.writeUTF(this.privsToString());
		dos.flush();
		bos.flush();
		return bos.toByteArray();
	}

	public Player(PlayerCommunicator comm_, byte[] buf) throws IOException,
			InterruptedException {
		this.comm = comm_;
		ByteArrayInputStream bis = new ByteArrayInputStream(buf);
		DataInputStream dis = new DataInputStream(bis);
		this.xchk = dis.readInt();
		this.ychk = dis.readInt();
		this.zchk = dis.readInt();
		this.xoffset = dis.readDouble();
		this.yoffset = dis.readDouble();
		this.zoffset = dis.readDouble();
		this.name = dis.readUTF();
		this.setPrivsFromString(dis.readUTF());
		this.comm.forceSetPosition(this, this.xchk, this.ychk, this.zchk,
				this.xoffset, this.yoffset, this.zoffset);
	}

}
