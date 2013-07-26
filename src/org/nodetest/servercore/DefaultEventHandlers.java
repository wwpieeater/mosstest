package org.nodetest.servercore;

import org.nodetest.scripting.MossScriptEnv;
import org.nodetest.scripting.MossScriptException;

public class DefaultEventHandlers {

	public static void processEvent(MossEvent myEvent) throws MossScriptException {
		switch (myEvent.type) {
		case EVT_CHATCOMMAND:
			MossScriptEnv.sendChatMessage((Player) myEvent.actor, null,
					"No such chat command");
			break;
		case EVT_CHATMESSAGE:
			MossScriptEnv.sendChatAll((Player) myEvent.actor,
					myEvent.initiatingMessage);
			break;
		case EVT_DIEPLAYER:
			MossScriptEnv.setHp(myEvent.actor, 64); // Max HP=64
			// FIXME dolinsky296 MossScriptEnv.moveEntity(myEvent.actor,
			// Mapgen.getSpawnPoint);
			break;
		case EVT_DIGNODE:
			try {
				MossScriptEnv.damageTool((Player) myEvent.actor,
						myEvent.nodeBefore);
				MossScriptEnv.givePlayer((Player) myEvent.actor,
						myEvent.nodeBefore.getDrop());
			} catch (MossScriptException e) {
				//FIXME MossSecurityManager.log(e);
			}
			break;
		case EVT_ENTITY_DEATH:
			myEvent.actor.destroy();
			break;
		case EVT_ENTITY_PUNCH:
			//No default action
			break;
		case EVT_FSPEC_INVACTION:
			myEvent.action.clearAsOriginal();
			break;
		case EVT_FSPEC_OPEN:
			break;
		case EVT_FSPEC_SUBMIT:
			break;
		case EVT_GENERATE:
			break;
		case EVT_JOINPLAYER:
			break;
		case EVT_NEWPLAYER:
			break;
		case EVT_NODEMOVE:
			break;
		case EVT_PLACENODE:
			break;
		case EVT_QUITPLAYER:
			break;
		case EVT_SHUTDOWN:
			break;
		case EVT_THREADSTOP:
			break;
		default:
			break;

		}

	}

}
