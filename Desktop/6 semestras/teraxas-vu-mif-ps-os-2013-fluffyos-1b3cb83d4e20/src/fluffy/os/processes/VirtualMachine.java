package fluffy.os.processes;

import java.util.LinkedList;

import fluffy.machine.FCPU;
import fluffy.machine.FProcessingUtility;
import fluffy.machine.registers.FRegister2B;
import fluffy.machine.registers.FRegister4B;
import fluffy.machine.registers.FRegisterLogicByte;
import fluffy.os.FProcess;
import fluffy.os.FProcessDescriptor;
import fluffy.os.FResource;
import fluffy.os.FResourceManager;
import fluffy.os.FluffyOS;
import fluffy.os.VMemory;
import fluffy.os.FluffyOS.ProcName;
import fluffy.os.FluffyOS.ProcessState;
import fluffy.os.FluffyOS.ResName;

public class VirtualMachine extends FProcess {
	
	public static int vmCount = 0;
	
	
	//References to registers for ease of use
	public FRegister4B regR1, regR2;
	public FRegister2B regIC;
	public FRegisterLogicByte regC;
	public FRegister4B regSP;
	
	public VMemory memory;
		
		
	public VirtualMachine(int intId, ProcName extId, String pName,
			LinkedList<FProcess> processList, FProcess parentProcess, FCPU cpu,
			FluffyOS os, ProcessState pState, int priority) {
		super(intId, extId, pName, processList, parentProcess, cpu, os, pState,
				priority);
		
		//For ease of use
		this.regC = cpu.getRegC();
		this.regIC = cpu.getRegIC();
		this.regR1 = cpu.getRegR1();
		this.regR2 = cpu.getRegR2();
		this.regSP = cpu.getRegSP();
				
		this.pDesc.savedState.saveState(cpu);
				
		vmCount++;
		saveCPU();
	}
	
	
	@Override
	public void step() {
		switch (nxtInstruction){
		case 1:
			//Request JOB in memory
			if (os.requestResource(this, ResName.UZDUOTIS_VYKDYMUI))
				nxtInstruction++;
			else
				setTimer(0);
			break;
		case 2:
			//Take JOB
			FResource memRes = FResourceManager.findResourceByExtId(
					pDesc.ownedResList, ResName.UZDUOTIS_VYKDYMUI);
			this.memory = (VMemory) memRes.getComponent();
			pDesc.pName = memory.pName;
			pDesc.cpu.regIC.setValInt(0);
			os.destroyResource(memRes);
			nxtInstruction++;
			break;
		case 3:
			//STEP!
			vmStep();
			break;
		case 4:
			//Wait for resume
			os.requestResource(this, ResName.RESUME_VM);
			nxtInstruction = 5;
			break;
		case 5:
			nxtInstruction = 3;
			os.printStuffDevice("VM:" + pDesc.pName + " - RESUMING AFTER INT");
			FResource resumeRes = FResourceManager.findResourceByExtId(
					pDesc.ownedResList, ResName.RESUME_VM);
			if (resumeRes != null)
				os.destroyResource(resumeRes);
			
			break;
		}
	}

	/**
	 * VM job step
	 */
	private void vmStep() {
		int tmpIC = pDesc.cpu.regIC.getValInt();
		
		String cmd = getCommandAtIC();
		if (cmd.equals("HALT")){
			halted = true;
			os.printStuffDevice("HALTED: " + toString());
		}
		
		try {
			FProcessingUtility.processCommand(this, cmd);
		} catch (IllegalArgumentException e){
			FluffyOS.printStuff(e.toString());
			pDesc.cpu.regPI.setValue(1);
		}
		
		if (pDesc.cpu.regIC.getValInt() == tmpIC){
			tmpIC++;
			pDesc.cpu.regIC.setValInt(tmpIC);
		}
		
		saveCPU();
		
		if (chkInterrupts() != 0){
			os.createResource(this, ResName.PRANESIMAS_PERTR, pDesc);
			os.stopProcess(this);
			os.requestResource(this, ResName.RESUME_VM);
			nxtInstruction = 5;
			
			os.printStuffDevice("INTERRUPT:" + toString());
			os.printStuffDevice(pDesc.savedState.toString());
		} else {
			nxtInstruction = 3;
		}
	}
	
	/**
	 * Get next job instruction string
	 * @return job instruction
	 */
	private String getCommandAtIC(){
		String tmp = memory.getWordAtAddress(
				pDesc.cpu.regIC.getValInt()).getVal();
		return tmp;
	}
	
	public void destroy(){
		if (this.memory != null){
			this.memory.returnMem();
			FluffyOS.printStuff("VM #" + pDesc.intId +
					" memory released...");
		} else {
			FluffyOS.printStuff("VM #" + pDesc.intId +
					" memory failed to release...");
			FluffyOS.printStuff(toString());
		}
		this.memory = null;
	}
	
	@Override
	public boolean fitRes(FResource res) {
		if (res.resDesc.getExtId() == ResName.RESUME_VM){
			if (((FProcessDescriptor) res.getComponent()).intId ==
					pDesc.intId){
				
				os.printStuffDevice("VM>> Took RESUME_VM (" +
						((FProcessDescriptor) res.getComponent()).intId + "=" +
						pDesc.intId + ")");
				for (ResName r : pDesc.waitingFor) {
					os.printStuffDevice(String.valueOf(r));
				}
				
				return true;
			} else{
				return false;
			}
		} else {
			return super.fitRes(res);
		}
	}
	
	public FCPU getCpu(){
		return this.pDesc.cpu;
	}
	
	public FRegister4B getRegR1() {
		return regR1;
	}

	public void setRegR1(FRegister4B regR1) {
		this.regR1 = regR1;
	}

	public FRegister4B getRegR2() {
		return regR2;
	}

	public void setRegR2(FRegister4B regR2) {
		this.regR2 = regR2;
	}

	public FRegister2B getRegIC() {
		return regIC;
	}

	public void setRegIC(FRegister2B regIC) {
		this.regIC = regIC;
	}

	public FRegisterLogicByte getRegC() {
		return regC;
	}

	public void setRegC(FRegisterLogicByte regC) {
		this.regC = regC;
	}

	public FRegister4B getRegSP() {
		return regSP;
	}

	public void setRegSP(FRegister4B regSP) {
		this.regSP = regSP;
	}

	public VMemory getMemory() {
		return memory;
	}

	public void setMemory(VMemory memory) {
		this.memory = memory;
	}
}
