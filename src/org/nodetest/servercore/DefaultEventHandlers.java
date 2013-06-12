package org.nodetest.servercore;

public class DefaultEventHandlers {

	public static void processEvent(MossEvent myEvent) {
		//TODO
		switch(myEvent.type){
		case EVT_CHATCOMMAND:
			MossScriptEnv.sendChatMessage(myEvent.actor, "No such chat command");
		}
		
	}

}
