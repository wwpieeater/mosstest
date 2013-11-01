//just keep the same package and import lines
package net.mosstest.servercore.netcommand;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

//make sure this name matches the filename
public class ToClientAuthRequested extends ToClientCommand {
	// From the fields of the packet
	AuthType authType;
	byte[] authParam;

	// create thingies like this for choices
	enum AuthType {
		AUTH_NIL, AUTH_PLAIN, AUTH_HASH_SHA512, AUTH_CHALLENGE_RESPONSE
	}

	// Change the name of the below method to match the file name
	public ToClientAuthRequested(byte[] buf) throws IOException,
			MalformedPacketException {

		// constructor from byte[] is parsing
		// Keep lines below for all of these tasks
		ByteArrayInputStream bs = new ByteArrayInputStream(buf);
		DataInputStream ds = new DataInputStream(bs);

		// Now start changing around below:
		// AuthType is an enum so we need a switch statement
		// AuthType is marked as [1] so we will have to read an "unsigned byte"
		int aType = ds.readUnsignedByte();

		// for those with multiple bullet choices. Match aType with previous line
		switch (aType) {
		case 0x00:
			authType = AuthType.AUTH_NIL; // get names from line 9, and line
											// 14...
			break; // don't forget the break
		case 0x01:
			authType = AuthType.AUTH_PLAIN; // same here
			break; // don't forget the break
		case 0x02:
			authType = AuthType.AUTH_HASH_SHA512;
			break;
		case 0x03:
			authType = AuthType.AUTH_CHALLENGE_RESPONSE;
			break;

		// keep the default thingy the same
		default:
			throw new MalformedPacketException();
		}

		// now let's do a variable thing.
		// For a thing marked [VARIABLE] just do the following:
		bs.read(authParam); //where authParam is the target
	}
	
	//And now the second thingy we need to add:
	public ToClientAuthRequested(AuthType authType, byte[] authParam) {
		this.authType=authType;
		this.authParam=authParam;
	}
	
	//and the last thing. This is simply a reverse of the first thingy with parentheses we added.
	public byte[] toByteArray() throws IOException{
		//prepare by using this line always
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		DataOutputStream dos=new DataOutputStream(bos);
		
		//now let's start sticking things in into the correct order
		//Our multi-bullet field means a switch thingy
		//order doesn;t matter as long as the numerical codes match up with the worded codes
		switch(this.authType){
		case AUTH_CHALLENGE_RESPONSE:
			dos.writeByte(0x03);
			break;
		case AUTH_HASH_SHA512:
			dos.writeByte(0x02);
			break;
		case AUTH_NIL:
			dos.writeByte(0x01);
			break;
		case AUTH_PLAIN:
			dos.writeByte(0x00);
			break;		
		}
		
		//let's write the [variable] part now:
		bos.write(this.authParam);
		
		//Use this line at the end to finish up
		return bos.toByteArray();
	}
}
