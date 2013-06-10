package org.nodetest.servercore;

public class MossEvent {
	enum EvtType {
		EVT_DIEPLAYER, EVT_DIGNODE, EVT_PREGENERATE, EVT_POSTGENERATE, EVT_JOINPLAYER, EVT_QUITPLAYER, EVT_NEWPLAYER, EVT_PLACENODE,
		EVT_FSPEC_OPEN, EVT_FSPEC_SUBMIT, EVT_FSPEC_INVACTION, EVT_THREADSTOP, EVT_ENTITY_PUNCH, EVT_ENTITY_DEATH, EVT_CHATMESSAGE,  EVT_SHUTDOWN,
		EVT_CHATCOMMAND, EVT_NODEMOVE
	}
	MossEvent.EvtType type;
	Entity actor; //Player extends entity
	long posx;
	long posy;
	long posz;
	MapNode nodeBefore;
	MapNode nodeAfter;
	Entity recvEntity;
	MossFormspec fspec;
	MossInventoryAction action;
}
