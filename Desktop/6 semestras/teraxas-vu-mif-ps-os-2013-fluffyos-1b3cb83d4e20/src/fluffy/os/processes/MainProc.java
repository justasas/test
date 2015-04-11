package fluffy.os.processes;

import java.io.File;
import java.util.LinkedList;

import fluffy.machine.FCPU;
import fluffy.os.FProcess;
import fluffy.os.FResource;
import fluffy.os.FResourceManager;
import fluffy.os.FluffyOS;
import fluffy.os.FluffyOS.ProcName;
import fluffy.os.FluffyOS.ProcessState;
import fluffy.os.FluffyOS.ResName;

/**
 * Class for creating and destroying JobGovernor processes
 * @author karolis
 *
 */
public class MainProc extends FProcess{


	public MainProc(int intId, ProcName extId, String pName,
			LinkedList<FProcess> processList, FProcess parentProcess, FCPU cpu,
			FluffyOS os, ProcessState pState, int priority) {
		super(intId, extId, pName, processList, parentProcess, cpu, os, pState,
				priority);
	}

	@Override
	public void step() {
		switch(nxtInstruction){
		case 1:
			os.requestResource(this, ResName.UZDUOTIS_ISOR);
			nxtInstruction = 2;
			break;
		case 2:
			takeFile();
			nxtInstruction = 3;
			break;
		case 3:
			chkGovernors();
			os.createProcess(this, ProcName.JOB_GOVERNOR);
			nxtInstruction = 1;
			break;
		}
	}

	private void chkGovernors() {
		JobGovernor govproc;
		for (FProcess proc : pDesc.childrenList) {
			govproc = (JobGovernor) proc;
			if (govproc.isJobHalted){
				os.destroyProcess(proc);
			}
		}
	}
	
	private void takeFile() {
		FResource fileRes;
		fileRes = FResourceManager.findResourceByExtId(pDesc.ownedResList,
				ResName.UZDUOTIS_ISOR);
		
		if (fileRes == null){
			FluffyOS.printStuff("FAILED: file is null :(");
			System.out.println("waiting: " + pDesc.waitingFor.size());
			nxtInstruction = 1;
			return;
		}
		
		File jobFile = (File) fileRes.getComponent();
		
		os.createResource(this, ResName.UZDUOTIS_MP,
				jobFile);
		os.destroyResource(fileRes);
	}
}
