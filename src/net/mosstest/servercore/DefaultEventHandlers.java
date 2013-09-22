package net.mosstest.servercore;

import net.mosstest.scripting.MossScriptEnv;
import net.mosstest.scripting.MossScriptException;

public class DefaultEventHandlers {

	public static void processEvent(MossEvent myEvent, MossScriptEnv env) throws MossScriptException {
		switch (myEvent.type) {
		case EVT_CHATCOMMAND:
			env.sendChatMessage((Player) myEvent.actor, null,
					"No such chat command");
			break;
		case EVT_CHATMESSAGE:
			env.sendChatAll((Player) myEvent.actor,
					myEvent.initiatingMessage);
			break;
		case EVT_DIEPLAYER:
			env.setHp(myEvent.actor, 64); // Max HP=64
			myEvent.actor.respawn();
			// FIXME rarkenin env.moveEntity(myEvent.actor,
			// Mapgen.getSpawnPoint);
			break;
		case EVT_DIGNODE:
			try {
				env.damageTool(myEvent.actor,
						myEvent.nodeBefore);
				env.givePlayer(myEvent.actor,
						new ItemStack(myEvent.nodeBefore.dropItem, 1));
			} catch (MossScriptException e) {
				//FIXME MossSecurityManager.log(e);
			}
			break;
		case EVT_ENTITY_DEATH:
			myEvent.actor.respawn();
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
