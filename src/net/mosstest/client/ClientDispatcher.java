package net.mosstest.client;

import net.mosstest.netcommand.MalformedPacketException;
import net.mosstest.netcommand.ToClientAuthDenied;
import net.mosstest.netcommand.ToClientAuthRequested;
import net.mosstest.servercore.MossNetPacket;
import net.mosstest.servercore.MosstestFatalDeathException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * Created by hexafraction on 4/27/14.
 */
public class ClientDispatcher {
    public static final byte[] EMPTY_PAYLOAD = {};
    private MossClient client;
    private boolean hasAuthed, hasBootstrapped;

    private static final Logger logger = Logger.getLogger(ClientDispatcher.class);

    public void dispatch(MossNetPacket inbound) {

        this.dispatch0(inbound);

    }


    private void dispatch0(MossNetPacket inbound) {
        switch (inbound.commandId) {
            case 0x01: //TOCLIENT_AUTH_REQUESTED
                handleAuthRequested(inbound);
                break;
            case 0x03: //TOCLIENT_AUTH_DENIED
                handleAuthDenied(inbound);
                break;
            case 0x00: //SYS_NOP
            case 0xFE: //SYS_BIND_CODE
            case 0xFF: //SYS_QUENCH
                // fall-through for packets already handled at a lower level
                break;
            default:
                logger.warn(Messages.getString("PACKET_NOT_DISPATCHABLE"));
        }
    }

    private void handleAuthDenied(MossNetPacket inbound) {
        try {
            ToClientAuthDenied parsed = new ToClientAuthDenied(inbound.payload);
            switch (parsed.getReason()) {
                case REASON_UNKNWN:
                    logger.fatal(Messages.getString("AUTH_FAILED_UNKNWN"));
                    break;
                case REASON_BAD_PASS:
                    logger.fatal(Messages.getString("AUTH_FAILED_BAD_PASS"));
                    break;
                case REASON_BANNED:
                    logger.fatal(Messages.getString("AUTH_FAILED_BAN"));
                    break;
                case REASON_PLAYER_LIMIT:
                    logger.fatal(Messages.getString("AUTH_FAILED_PLAYER_LIM"));
                    break;
                case REASON_LOGON_HOUR:
                    logger.fatal(Messages.getString("AUTH_FAILED_LOGON_HOUR"));
                    break;
                case REASON_NO_NEW_PLAYERS:
                    logger.fatal(Messages.getString("AUTH_FAILED_NO_REGISTER"));
                    break;
                case REASON_VERSION_MISMATCH:
                    logger.fatal(Messages.getString("AUTH_FAILED_VERSION"));
                    break;
                case REASON_AUTH_TIMED_OUT:
                    logger.fatal(Messages.getString("AUTH_FAILED_TIMEOUT"));
                    break;
                case REASON_SERVER_MAINT:
                    logger.fatal(Messages.getString("AUTH_FAILED_MAINTENANCE"));
                    break;
                case REASON_FAILED_CONNECTION:
                    logger.fatal(Messages.getString("AUTH_FAILED_CONN"));
                    break;
            }
            throw new MosstestFatalDeathException(Messages.getString("AUTH_FAIL_EXCEPTION"));
        } catch (IOException e) {
            logger.fatal(Messages.getString("IOEXCEPTION_DESERIALIZE_AUTH_FAIL_PCKT"));
            throw new MosstestFatalDeathException(e);
        }
    }

    private void handleAuthRequested(MossNetPacket inbound) {
        if (hasAuthed) {
            logger.error(Messages.getString("ALREADY_AUTHED"));
            return;
        }
        try {
            ToClientAuthRequested parsed = new ToClientAuthRequested(inbound.payload);
            authenticate(parsed);
        } catch (IOException e) {
            logger.fatal(Messages.getString("IOEXCEPTION_DESERIALIZE_AUTH_PCKT"));
            throw new MosstestFatalDeathException(e);
        } catch (MalformedPacketException e) {
            logger.warn(Messages.getString("MALFORMED_TC_AUTH_REQUESTED"));
        }
    }

    private void authenticate(ToClientAuthRequested parsed) throws IOException {

        byte[] pass = client.getPassword();
        switch (parsed.getAuthType()) {
            case AUTH_NIL:
                logger.warn(Messages.getString("SVR_NO_AUTH"));
                client.net.sendPacket(new MossNetPacket(0x02, EMPTY_PAYLOAD));
            case AUTH_PLAIN:
                logger.warn(Messages.getString("SVR_AUTH_PLAIN"));
                // FIXME some sort of confirmation dialog
                client.net.sendPacket(new MossNetPacket(0x02, pass));
            case AUTH_HASH_SHA512:
                client.net.sendPacket(new MossNetPacket(0x02, DigestUtils.sha512(
                                ArrayUtils.addAll(
                                        ArrayUtils.addAll(
                                                client.getUsername().getBytes(), pass), parsed.getAuthParam()
                                )
                        )
                        )
                );
            case AUTH_CHALLENGE_RESPONSE:
                logger.fatal(Messages.getString("SERVER_AUTH_CHALLENGE_RESP"));
                throw new MosstestFatalDeathException(Messages.getString("AUTH_CHAP_FAILED"));
        }


    }
}
