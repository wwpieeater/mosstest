package net.mosstest.servercore;

import java.util.ArrayList;

public enum ToClientNetCommand {
	TOCLIENT_NOP(0x00),
	TOCLIENT_AUTH_REQUESTED,
	TOCLIENT_AUTH_ACCEPTED,
	TOCLIENT_AUTH_DENIED,
	TOCLIENT_BULK_TRANFER,
	TOCLIENT_MAPBLOCK,
	TOCLIENT_INV,
	TOCLIENT_BULK_TRANFER_PARAMS,
	TOCLIENT_PLAYER_MAPPING,
	TOCLIENT_ENTITY,
	TOCLIENT_TELEPORT_ENTITY,
	TOCLIENT_MOVE_ENTITY_DELTA, 
	TOCLIENT_ANIMATE_MESH,
	TOCLIENT_NODE_DEF,
	TOCLIENT_PLACE_NODE,
	TOCLIENT_REMOVE_NODE,
	TOCLIENT_SLIDE_NODE,
	TOCLIENT_FORMSPEC,
	TOCLIENT_FORMSPEC_INSTANCE,
	TOCLIENT_PlAYER_EVENT,
	TOCLIENT_ENTITY_FOLLOW_NODEPATH;
	int command;
	static ArrayList<ToClientNetCommand> commands=new ArrayList<>();
	ToClientNetCommand(int cmd){
		this.command=cmd;
		add(this);
	}
	ToClientNetCommand(){
		this.command=0xFF;
	}
	private static void add(ToClientNetCommand tcnc){
		ToClientNetCommand.commands.add(tcnc.command, tcnc);
	}
}
