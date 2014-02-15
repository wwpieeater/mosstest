package net.mosstest.servercore;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Enum ToClientNetCommand.
 */
public enum ToClientNetCommand {
	
	/** The toclient nop. */
	TOCLIENT_NOP(0x00),
	
	/** The toclient auth requested. */
	TOCLIENT_AUTH_REQUESTED,
	
	/** The toclient auth accepted. */
	TOCLIENT_AUTH_ACCEPTED,
	
	/** The toclient auth denied. */
	TOCLIENT_AUTH_DENIED,
	
	/** The toclient bulk tranfer. */
	TOCLIENT_BULK_TRANFER,
	
	/** The toclient mapblock. */
	TOCLIENT_MAPBLOCK,
	
	/** The toclient inv. */
	TOCLIENT_INV,
	
	/** The toclient bulk tranfer params. */
	TOCLIENT_BULK_TRANFER_PARAMS,
	
	/** The toclient player mapping. */
	TOCLIENT_PLAYER_MAPPING,
	
	/** The toclient entity. */
	TOCLIENT_ENTITY,
	
	/** The toclient teleport entity. */
	TOCLIENT_TELEPORT_ENTITY,
	
	/** The toclient move entity delta. */
	TOCLIENT_MOVE_ENTITY_DELTA, 
	
	/** The toclient animate mesh. */
	TOCLIENT_ANIMATE_MESH,
	
	/** The toclient node def. */
	TOCLIENT_NODE_DEF,
	
	/** The toclient place node. */
	TOCLIENT_PLACE_NODE,
	
	/** The toclient remove node. */
	TOCLIENT_REMOVE_NODE,
	
	/** The toclient slide node. */
	TOCLIENT_SLIDE_NODE,
	
	/** The toclient formspec. */
	TOCLIENT_FORMSPEC,
	
	/** The toclient formspec instance. */
	TOCLIENT_FORMSPEC_INSTANCE,
	
	/** The TOCLIEN t_ pl aye r_ event. */
	TOCLIENT_PlAYER_EVENT,
	
	/** The toclient entity follow nodepath. */
	TOCLIENT_ENTITY_FOLLOW_NODEPATH;
	
	/** The command. */
	int command;
	
	/** The commands. */
	static ArrayList<ToClientNetCommand> commands=new ArrayList<>();
	
	/**
	 * Instantiates a new to client net command.
	 *
	 * @param cmd the cmd
	 */
	ToClientNetCommand(int cmd){
		this.command=cmd;
		add(this);
	}
	
	/**
	 * Instantiates a new to client net command.
	 */
	ToClientNetCommand(){
		this.command=0xFF;
	}
	
	/**
	 * Adds the.
	 *
	 * @param tcnc the tcnc
	 */
	private static void add(ToClientNetCommand tcnc){
		ToClientNetCommand.commands.add(tcnc.command, tcnc);
	}
}
