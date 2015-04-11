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
 * WaitForJob process waits for job input
 * @author karolis
 *
 */
public class WaitForJob extends FProcess {

	public WaitForJob(int intId, ProcName extId, String pName,
			LinkedList<FProcess> processList, FProcess parentProcess, FCPU cpu,
			FluffyOS os, ProcessState pState, int priority) {
		super(intId, extId, pName, processList, parentProcess, cpu, os, pState,
				priority);
	}

	@Override
	public void step() {
		switch (nxtInstruction){
		case 1:
			os.requestResource(this, ResName.IVEDIMO_SRAUTAS);
			nxtInstruction++;
			break;
		case 2:
			takeFile();
			if (pDesc.ownedResList.size() == 0)
				nxtInstruction = 1;
			break;
		}
	}

	private void takeFile() {
		FResource fileRes;
		fileRes = FResourceManager.findResourceByExtId(
				pDesc.ownedResList, ResName.IVEDIMO_SRAUTAS);
		
		if (fileRes == null){
			//null check
			nxtInstruction = 1;
			return;
		}
		
		//Shit's fluffy!
		File jobFile = (File) fileRes.getComponent();
		
		os.createResource(this, ResName.UZDUOTIS_ISOR,
				jobFile);
		os.destroyResource(fileRes);
	}
}
