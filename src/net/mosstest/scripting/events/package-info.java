/**
 * @author rarkenin
 * 
 * This package contains various immutable Java objects to contain the parameters of events which scripts shall handle.
 * <p>
 * <b>Sequence numbers, aka <code>seqnum</code>s</b> are serial IDs given to events with certain ordering guarantees, which include guarantees that: 
 * <ul>
 * <li>Seqnums are unique until wraparound occurs at 18446744073709551616.</li>
 * <li>A seqnum with a higher value was received at the server after one with a lower value, even if the seqnums differ between sessions.</li>
 * <li>Within a given session between the server and a compliant client, a lower-value seqnum is assigned to an event arising from an earlier client action than a higher-value seqnum.
 * 
 * </ul>
 */
package net.mosstest.scripting.events;