package fluffy.os.processes;

import java.io.File;
import java.util.LinkedList;

import fluffy.machine.FCPU;
import fluffy.os.FProcess;
import fluffy.os.FProcessDescriptor;
import fluffy.os.FResource;
import fluffy.os.FResourceManager;
import fluffy.os.FluffyOS;
import fluffy.os.FluffyOS.ProcName;
import fluffy.os.FluffyOS.ProcessState;
import fluffy.os.FluffyOS.ResName;
import fluffy.os.InterruptMessage;
import fluffy.os.VMemory;

/**
 * Job Governor process for creating VM processes and 
 * processing actions not available for VirtualMachine
 * @author karolis
 *
 */
public class JobGovernor extends FProcess {

	private FProcess myVM;
	public boolean isJobHalted;
	
	public JobGovernor(int intId, ProcName extId, String pName,
			LinkedList<FProcess> processList, FProcess parentProcess, FCPU cpu,
			FluffyOS os, ProcessState pState, int priority) {
		super(intId, extId, pName, processList, parentProcess, cpu, os, pState,
				priority);
		isJobHalted = false;
	}

	@Override
	public void step() {
		switch (nxtInstruction){
		case 1:
			os.requestResource(this, ResName.UZDUOTIS_MP);
			nxtInstruction++;
			break;
		case 2:
			takeFile();
			nxtInstruction++;
			break;
		case 3:
			os.requestResource(this, ResName.UZDUOTIS_VM);
			nxtInstruction++;
			break;
		case 4:
			createVM();
			nxtInstruction++;
			break;
		case 5:
			os.requestResource(this, ResName.PERTRAUKIMAS);
			nxtInstruction++;
			break;
		case 6:
			processInterrupt();
			//nxtInstruction is set in method!
			break;
		case 7:
			os.createResource(this, ResName.RESUME_VM, myVM.pDesc);
			nxtInstruction = 5;
			break;
		case 8:
			FResource isvEilRes = FResourceManager.findResourceByExtId(
					pDesc.ownedResList, ResName.ISVESTA_EILUTE);
			os.destroyResource(isvEilRes);
			nxtInstruction = 7;
			break;
		case 9:
			FResource ivEilRes = FResourceManager.findResourceByExtId(
					pDesc.ownedResList, ResName.IVESTA_EILUTE);
			os.destroyResource(ivEilRes);
			nxtInstruction = 7;
			break;
		}
	}
	
	private void processInterrupt() {
		FluffyOS.printStuff("Got interrupt: " + this.toString());

		FResource resInt = FResourceManager.findResourceByExtId(
				pDesc.ownedResList, ResName.PERTRAUKIMAS);
		InterruptMessage inter = (InterruptMessage) resInt.getComponent();
		os.destroyResource(resInt);
		
		FluffyOS.printStuff("Interrupt!!! [" + inter.action + "]");
		
		switch (inter.action){
		case DIV_BY_ZERO:
		case HALT:
		case ILLEGAL_COMMAND:
		case OVERFLOW:
		case NEGATIVE_RESULT:
			FluffyOS.printStuff(pDesc.intId + ":" +
					pDesc.extId + ": Destroy[" + myVM.pDesc.intId
					+ ":" + myVM.pDesc.extId);
			((VirtualMachine) myVM).destroy();
			os.destroyProcess(myVM);
			isJobHalted = true;
			nxtInstruction = 5;
			break;
		case INPUT:
			os.createResource(this, ResName.PRANESIMAS_GETLINE, myVM.pDesc);
			os.requestResource(this, ResName.IVESTA_EILUTE);
			nxtInstruction = 9;
			break;
		case OUTPUT:
			os.createResource(this, ResName.EILUTE_ATMINTYJE, myVM.pDesc);
			os.requestResource(this, ResName.ISVESTA_EILUTE);
			nxtInstruction = 8;
			break;
		case READY:
			
			nxtInstruction = 5;
			break;
		default:
			break;
		
		}
	}

	private void createVM() {
		FResource vMemRes = FResourceManager.findResourceByExtId(
				pDesc.ownedResList, ResName.UZDUOTIS_VM);
		VMemory vMem = (VMemory) vMemRes.getComponent();
		
		os.destroyResource(vMemRes);
		
		os.createResource(this, ResName.UZDUOTIS_VYKDYMUI, vMem);
		myVM = os.createProcess(this, ProcName.VIRTUAL_MACHINE, 2);
		
	}
	
	private void takeFile() {
		FResource fileRes;
		fileRes = FResourceManager.findResourceByExtId(
				pDesc.ownedResList, ResName.UZDUOTIS_MP);
		
		
		//Shit's fluffy!
		File jobFile = (File) fileRes.getComponent();
		
		os.createResource(this, ResName.PRANESIMAS_LOADER,
				jobFile);
		os.destroyResource(fileRes);
	}
	
	/**
	 * Check if resource (PERTRAUKIMAS) is for this JobGovernor
	 */
	@Override
	public boolean fitRes(FResource res){
		if (res.resDesc.getExtId() == ResName.PERTRAUKIMAS){
			if (((InterruptMessage) res.getComponent()).procIntId ==
					this.pDesc.intId){
				FluffyOS.printStuff("Taking INT: " +
						((InterruptMessage) res.getComponent()).procIntId +
						" = " + this.pDesc.intId);
				return true;
			} else {
//				FluffyOS.printStuff("Refusing INT: " +
//						((InterruptMessage) res.getComponent()).procIntId +
//						" = " + this.pDesc.intId);
				return false;
			}
		} else if (res.resDesc.getExtId() == ResName.ISVESTA_EILUTE){
			if (((FProcessDescriptor) res.getComponent()).intId ==
					this.myVM.pDesc.intId){
				
				os.printStuffDevice("JG>> Took ISVESTA_EILUTE (" +
						((FProcessDescriptor) res.getComponent()).intId + "=" +
						this.myVM.pDesc.intId + ")");
				for (ResName r : pDesc.waitingFor) {
					os.printStuffDevice(String.valueOf(r));
				}
				
				return true;
			} else{
				return false;
			}
		} else if (res.resDesc.getExtId() == ResName.IVESTA_EILUTE) {
			if (((FProcessDescriptor) res.getComponent()).intId ==
					this.myVM.pDesc.intId){
				os.printStuffDevice("JG>> Took IVESTA_EILUTE (" +
						((FProcessDescriptor) res.getComponent()).intId + "=" +
						this.myVM.pDesc.intId + ")");
				for (ResName r : pDesc.waitingFor) {
					os.printStuffDevice(String.valueOf(r));
				}
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		String tmp = super.toString();
		if (this.isJobHalted){
			tmp += " [VM HALTED]";
		}
		return tmp;
	}
}
