package net.mosstest.servercore;

import net.mosstest.scripting.ItemStack;
import net.mosstest.scripting.MossEvent;
import net.mosstest.scripting.MossScriptEnv;
import net.mosstest.scripting.MossScriptException;
import net.mosstest.scripting.Player;

public class DefaultEventHandlers {

public static void processEvent(final MossEvent evt, MossScriptEnv env) throws MossScriptException {
		switch (evt.type) {
		case EVT_CHATCOMMAND:
			env.sendChatMessage((Player) evt.actor, null,
					"No such chat command");
			break;
		case EVT_CHATMESSAGE:
			env.sendChatAll((Player) evt.actor,
					evt.initiatingMessage);
			break;
		case EVT_DIEPLAYER:
			env.setHp(evt.actor, 64); // Max HP=64
			evt.actor.respawn();
			// FIXME rarkenin env.moveEntity(myEvent.actor,
			// Mapgen.getSpawnPoint);
			break;
		case EVT_DIGNODE:
			try {
				env.damageTool(evt.actor,
						evt.nodeBefore);
				env.givePlayer(evt.actor,
						new ItemStack(evt.nodeBefore.dropItem, 1));
				env.removeNode(evt.pos);
			} catch (MossScriptException e) {
				//FIXME MossSecurityManager.log(e);
			}
			break;
		case EVT_ENTITY_DEATH:
			env.getFuturesProcessor().runOnce(8000, new Runnable() {
				
				@Override
				public void run() {
					evt.actor.respawn();
					
				}
			});
			break;
		case EVT_ENTITY_PUNCH:
			//No default action
			break;
		case EVT_FSPEC_INVACTION:
			evt.action.acceptAsStated();
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
			env.setNode(evt.pos, evt.nodeAfter);
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
