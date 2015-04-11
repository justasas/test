package fluffy.os;

import java.util.LinkedList;

import fluffy.machine.FCPU;
import fluffy.os.FluffyOS.ProcName;
import fluffy.os.FluffyOS.ProcessState;

/**
 * Base class for processes
 * @author karolis
 *
 */
public abstract class FProcess implements Comparable<FProcess> {

	public static int TIMER_INTERVAL = 2;
	public FProcessDescriptor pDesc;
	//public static int numberOfInstances;
	
	/**
	 * Instruction number for next step.
	 */
	protected int nxtInstruction;
	
	protected FluffyOS os;
	public int interval;
	protected boolean halted;
	
	public FProcess(int intId, ProcName extId, String pName, 
			LinkedList<FProcess> processList, 
			FProcess parentProcess, 
			FCPU cpu, FluffyOS os, 
			ProcessState pState, int priority) {
		nxtInstruction = 1;
		this.os = os;
		pDesc = new FProcessDescriptor(intId, extId, pName, 
				processList, parentProcess, this, cpu, os, 
				pState, priority);
		if (parentProcess != null)
			parentProcess.pDesc.childrenList.add(this);
		interval = TIMER_INTERVAL;
		setTimer(interval);
		halted = false;
		
		//numberOfInstances++;
	}

	/**
	 * Process step with interrupt checking and CPU state loading/saving
	 */
	public void fullStep(){
		resetTimer();
		
		if (!halted)
			step();
		
		decTimer();
		if (chkInterrupts() == 0){
			if (getTimer() == 0)
				timerInterrupt();
		}
	}
	
	/**
	 * Process step, depends on "nxtInstruction". 
	 * Does not check for interrupts.
	 * No CPU state saving/loading.
	 */
	public abstract void step();
	
	/**
	 * Prepares cpu for usage - restore state for this VM
	 */
	protected void loadCPU() {
		pDesc.savedState.restoreState(pDesc.cpu);
	}
	
	/**
	 * Saves CPU state to descriptor 
	 */
	protected void saveCPU() {
		pDesc.savedState.saveState(pDesc.cpu);
	}

	@Override
	public int compareTo(FProcess o){
		if (this.pDesc.priority < o.pDesc.priority)
			return 1;
		else if (this.pDesc.priority > o.pDesc.priority)
			return -1;
		return 0;
	}
	
	@Override
	public String toString() {
		String str = pDesc.intId + ":" + 
				pDesc.extId + "(" + pDesc.pName + 
				"): [" + pDesc.pState + ";priority=" + 
				pDesc.priority + 
				";nxtInstruction="+ nxtInstruction + "]";
		if (pDesc.pState == ProcessState.BLOCKED){
			str += " Waiting: " + pDesc.waitingFor.element();
		}
		return str;
	}

	public void timerInterrupt() {
		FluffyOS.printStuff(pDesc.intId + ":" + pDesc.extId + 
				": TIMER interrupt (nxt: " + nxtInstruction + ")");
		saveCPU();
		resetTimer();
		os.stopProcess(this);
		if (pDesc.extId == ProcName.VIRTUAL_MACHINE)
			decPriority();
	}
	
	public void setTimer(int newVal) {
		this.pDesc.cpu.regTIME.setValue(newVal);
	}
	
	public int getTimer() {
		return this.pDesc.cpu.regTIME.getValue();
	}
	
	public void resetTimer() {
		this.setTimer(interval);
	}
	
	public void decPriority(){
		if (pDesc.priority > 1)
			pDesc.priority--;
	}
	
	protected void decTimer() {
		int tmp = pDesc.cpu.regTIME.getValue();
		
		if (tmp > 0){
			tmp--;
			setTimer(tmp);
		}
		
		FluffyOS.printStuff("Time left: " + getTimer());
	}
	
	/**
	 * Check for interrupts (SI, PI)
	 * @return
	 */
	protected int chkInterrupts() {
		int tmp = pDesc.cpu.regPI.getValue() + pDesc.cpu.regSI.getValue();
		return tmp;
	}
	
	public boolean fitRes(FResource res) {
		return true;
	}
}
