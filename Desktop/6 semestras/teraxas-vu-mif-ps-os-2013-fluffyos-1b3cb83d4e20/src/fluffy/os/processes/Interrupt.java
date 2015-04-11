package fluffy.os.processes;

import java.util.LinkedList;

import fluffy.machine.FCPU;
import fluffy.os.FProcess;
import fluffy.os.FProcessDescriptor;
import fluffy.os.FResource;
import fluffy.os.FResourceManager;
import fluffy.os.FluffyOS;
import fluffy.os.FluffyOS.IntType;
import fluffy.os.FluffyOS.ProcName;
import fluffy.os.FluffyOS.ProcessState;
import fluffy.os.FluffyOS.ResName;
import fluffy.os.InterruptMessage;

public class Interrupt extends FProcess {

	public Interrupt(int intId, ProcName extId, String pName,
			LinkedList<FProcess> processList, FProcess parentProcess, FCPU cpu,
			FluffyOS os, ProcessState pState, int priority) {
		super(intId, extId, pName, processList, parentProcess, cpu, os, pState,
				priority);
	}

	@Override
	public void step() {
		switch (nxtInstruction) {
		case 1:
			os.requestResource(this, ResName.PRANESIMAS_PERTR);
			nxtInstruction++;
			break;
		case 2:
			processInterruptForVM();
			nxtInstruction = 1;
			break;
		}
	}

	private void processInterruptForVM() {
		FResource descRes = FResourceManager.findResourceByExtId(
				pDesc.ownedResList, ResName.PRANESIMAS_PERTR);
		FProcessDescriptor desc =
				((FProcessDescriptor) descRes.getComponent());
		int parentId = desc.parentProcess.pDesc.intId;
		
		FluffyOS.printStuff("PROCESSING INT: " + desc.extId);
		InterruptMessage msg = null;
		
		IntType type = IntType.READY;
		
		switch (desc.savedState.regSI.getValue()){
		case 1:
			type = IntType.INPUT;
			break;
		case 2:
			type = IntType.OUTPUT;
			break;
		case 3:
			type = IntType.HALT;
			break;
		}
		
		switch (desc.savedState.regPI.getValue()){
		case 1:
			type = IntType.ILLEGAL_COMMAND;
			break;
		case 2:
			type = IntType.NEGATIVE_RESULT;
			break;
		case 3:
			type = IntType.DIV_BY_ZERO;
			break;
		case 4:
			type = IntType.OVERFLOW;
			break;	
		}
		
		msg = new InterruptMessage(type, parentId);
		os.destroyResource(descRes);
		os.createResource(this, ResName.PERTRAUKIMAS, msg);
	}
	

}
