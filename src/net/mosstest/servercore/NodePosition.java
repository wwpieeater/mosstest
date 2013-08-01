package org.nodetest.servercore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NodePosition extends Position {
byte xl, yl, zl;
	public NodePosition(int x, int y, int z, int realm, byte xl, byte yl, byte zl) {
		super(x, y, z, realm);
		this.xl=xl;
		this.yl=yl;
		this.zl=zl;
	}
	public NodePosition(byte[] bytes) throws IOException {
		super();
		ByteArrayInputStream bis=new ByteArrayInputStream(bytes);
		DataInputStream dis=new DataInputStream(bis);
		x=dis.readInt();
		y=dis.readInt();
		z=dis.readInt();
		realm=dis.readInt();
		xl=dis.readByte();
		yl=dis.readByte();
		zl=dis.readByte();
		isValid=true;
	}
	public byte[] toBytes() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			dos.writeInt(realm);
			dos.writeByte(xl);
			dos.writeByte(yl);
			dos.writeByte(zl);
			dos.flush();
			bos.flush();
			return bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			isValid = false;
		}
		return null;
	}

}
