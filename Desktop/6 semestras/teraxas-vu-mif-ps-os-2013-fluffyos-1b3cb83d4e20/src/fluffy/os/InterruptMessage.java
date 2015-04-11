package fluffy.os;

import fluffy.os.FluffyOS.IntType;

/**
 * Class for contacting JobGovernor
 * @author karolis
 *
 */
public class InterruptMessage {

	public IntType action;
	public int procIntId;
	public InterruptMessage(IntType action, int procIntId) {
		this.action = action;
		this.procIntId = procIntId;
	}

}
