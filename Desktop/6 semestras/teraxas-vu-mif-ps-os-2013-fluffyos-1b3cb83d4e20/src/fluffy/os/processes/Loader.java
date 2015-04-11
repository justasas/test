package fluffy.os.processes;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import fluffy.machine.FCPU;
import fluffy.machine.FProcessingUtility;
import fluffy.os.FProcess;
import fluffy.os.FResource;
import fluffy.os.FResourceManager;
import fluffy.os.FluffyOS;
import fluffy.os.FluffyOS.ProcName;
import fluffy.os.FluffyOS.ProcessState;
import fluffy.os.FluffyOS.ResName;
import fluffy.os.VMemory;

/**
 * Process for loading jobs to memory
 * @author karolis
 *
 */
public class Loader extends FProcess {

	public Loader(int intId, ProcName extId, String pName,
			LinkedList<FProcess> processList, FProcess parentProcess, FCPU cpu,
			FluffyOS os, ProcessState pState, int priority) {
		super(intId, extId, pName, processList, parentProcess, cpu, os, pState,
				priority);
	}

	@Override
	public void step() {
		switch (nxtInstruction){
		case 1:
			if (FResourceManager.findResourceByExtId(
					pDesc.ownedResList, ResName.PRANESIMAS_LOADER) != null){
				nxtInstruction++;
				break;
			}
			os.requestResource(this, ResName.PRANESIMAS_LOADER);
			nxtInstruction++;
			break;
		case 2:
			if (FResourceManager.findResourceByExtId(
					pDesc.ownedResList, ResName.VIRTUAL_ATMINTIS) != null){
				nxtInstruction++;
				break;
			}
			os.requestResource(this, ResName.VARTOTOJO_ATMINTIS);
			nxtInstruction++;
			break;
		case 3:
			readJob();
			nxtInstruction = 1;
		}
	}
	
	private void readJob() {
		FResource memoryRes = FResourceManager.findResourceByExtId(
				pDesc.ownedResList, ResName.VIRTUAL_ATMINTIS);
		
		if (memoryRes == null){
			nxtInstruction = 1;
			return;
		}
		
		VMemory vMemory = (VMemory) memoryRes.getComponent();
		this.pDesc.cpu.regPLR.setValInt(vMemory.getPLR());
		
		FResource fileRes = FResourceManager.findResourceByExtId(
				pDesc.ownedResList, ResName.PRANESIMAS_LOADER);
		File jobFile = (File) fileRes.getComponent();
		
		try {
			FProcessingUtility.loadMemory(vMemory, jobFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		os.createResource(this, 
				ResName.UZDUOTIS_VM, vMemory);
		
		os.destroyResource(fileRes);
		os.destroyResource(memoryRes);
		
	}

}
