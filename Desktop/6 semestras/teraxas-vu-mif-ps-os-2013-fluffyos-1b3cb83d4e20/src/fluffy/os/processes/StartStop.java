package fluffy.os.processes;

import java.util.LinkedList;

import fluffy.machine.FCPU;
import fluffy.os.FProcess;
import fluffy.os.FResource;
import fluffy.os.FluffyOS;
import fluffy.os.FluffyOS.ProcName;
import fluffy.os.FluffyOS.ProcessState;
import fluffy.os.FluffyOS.ResName;

/**
 * FluffyOS root process
 * @author karolis
 *
 */
public class StartStop extends FProcess {

	public StartStop(int intId, ProcName extId, String pName,
			LinkedList<FProcess> processList, FProcess parentProcess, FCPU cpu,
			FluffyOS os, ProcessState pState, int priority) {
		super(intId, extId, pName, processList, parentProcess, cpu, os, pState,
				priority);
		
	}

	@Override
	public void step() {
		switch (nxtInstruction){
		case 1:
			createSystemResources();
			FluffyOS.printStuff("Resources created!");
			nxtInstruction++;
			break;
		case 2:
			createSystemProcesses();
			nxtInstruction++;
			break;
		case 3:
			os.requestResource(this, ResName.MOS_PABAIGA);
			nxtInstruction++;
			break;
		case 4:
			destroySystemProcesses();
			nxtInstruction++;
			break;
		case 5:
			destroySystemResources();
			os.stopOS = true;
			break;
		}
		
	}
	
	private void createSystemResources(){
		//Memory
		os.createResource(this, ResName.VARTOTOJO_ATMINTIS,
				os.getRealMachine().getMemory());
		os.createResource(this, ResName.IVEDIMO_IRENGINYS,
				os.getRealMachine().usrInputDevice);
		os.createResource(this, ResName.ISVEDIMO_IRENGINYS
				, os.getRealMachine().usrOutputDevice);
	}
	
	private void createSystemProcesses() {
		os.createProcess(this, ProcName.WAIT_FOR_JOB);
		os.createProcess(this, ProcName.LOADER);
		os.createProcess(this, ProcName.MAIN_PROC);
		os.createProcess(this, ProcName.INTERRUPT);
		os.createProcess(this, ProcName.GET_LINE);
		os.createProcess(this, ProcName.PRINT_LINE);
	}
	
	private void destroySystemResources() {
		LinkedList<FResource> tmpList = new LinkedList<>();
		for (FResource res : this.pDesc.createdResList) {
			tmpList.add(res);
		}
		
		for (FResource res : tmpList) {
			os.destroyResource(res);
		}
	}
	
	private void destroySystemProcesses() {
		LinkedList<FProcess> tmpList = new LinkedList<>();
		for (FProcess proc : this.pDesc.childrenList) {
			tmpList.add(proc);
		}
		
		for (FProcess proc : tmpList) {
			os.destroyProcess(proc);
		}
	}
}
