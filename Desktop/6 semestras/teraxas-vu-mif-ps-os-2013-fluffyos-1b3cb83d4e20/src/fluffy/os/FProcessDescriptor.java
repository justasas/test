package fluffy.os;

import java.util.LinkedList;

import fluffy.machine.FCPU;
import fluffy.os.FluffyOS.ProcName;
import fluffy.os.FluffyOS.ProcessState;
import fluffy.os.FluffyOS.ResName;

/**
 * FluffyOS process descriptor
 * @author karolis
 *
 */
public class FProcessDescriptor {

	public LinkedList<FProcess> processList;
	public LinkedList<FProcess> childrenList;
	
	public LinkedList<FResource> createdResList;
	public LinkedList<FResource> ownedResList;
	public LinkedList<ResName> waitingFor;
	
	public int intId;
	public ProcName extId;
	public String pName;
	public FProcess myProc;
	public FProcess parentProcess;
	public int priority;
	
	public FCPU cpu;
	public FluffyOS os;
	
	public ProcessState pState;
	public FRegState savedState;
	
	public FProcessDescriptor(int intId, ProcName extId, String pName,
			LinkedList<FProcess> processList,
			FProcess parentProcess,
			FProcess myProcess,
			FCPU cpu, FluffyOS os,
			ProcessState pState, int priority) {
		this.intId = intId;
		this.extId = extId;
		this.pName = pName;
		this.priority = priority;
		this.myProc = myProcess;
		
		this.processList = processList;
		this.parentProcess = parentProcess;
		this.cpu = cpu;
		this.os = os;
		
		this.savedState = new FRegState();
		this.pState = ProcessState.READY;
		
		childrenList = new LinkedList<>();
		createdResList = new LinkedList<>();
		ownedResList = new LinkedList<>();
		waitingFor = new LinkedList<>();
	}
}
