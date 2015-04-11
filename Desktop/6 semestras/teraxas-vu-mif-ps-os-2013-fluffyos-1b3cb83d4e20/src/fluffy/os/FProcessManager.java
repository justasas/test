/**
 * 
 */
package fluffy.os;

import fluffy.os.FluffyOS.ProcName;
import fluffy.os.FluffyOS.ProcessState;

/**
 * Process manager class
 * @author karolis
 *
 */
public class FProcessManager {

	private FluffyOS os;
	
	public FProcessManager(FluffyOS os) {
		this.os = os;
		
	}

	public void execute(){
		FluffyOS.printStuff("================PROCMAN=================");
		
		if (os.runProcess != null){
			FluffyOS.printStuff("---" + os.runProcess.toString());
			checkCurrent();
		} else {
			FluffyOS.printStuff("--- NOTHING");
		}
		//Block current process if needed
		
		
		FProcess candidate = getTopPriorityReadyProc();
		
		if (candidate != null && os.runProcess == null){
			//Jei procesorius laisvas ir yra kandidatas jį užimti - duodam
			prepareProcess(candidate);
		} else if (candidate != null && os.runProcess != null){
			//Jei užimtas - tikrinam ar yra svarbesnių procesų
			if (candidate.pDesc.priority > os.runProcess.pDesc.priority &&
					os.runProcess.pDesc.pState == ProcessState.READY){
				stopProcess();
				prepareProcess(candidate);
			}
		} else {
			//Jei nėra nei kandidato, nei vykdomo proceso
			FluffyOS.printStuff("Resources:");
			for (FResource item : os.resources) {
				FluffyOS.printStuff(item.toString());
			}
		}
		
		if (os.runProcess != null)
			FluffyOS.printStuff("+++" + os.runProcess.toString());
		else {
			FluffyOS.printStuff("NO READY PROCESS");
			
		}
		FluffyOS.printStuff("----------------------------------------");
	}
	
	/**
	 * Load registers and prepare process for RUN state
	 */
	private void prepareProcess(FProcess newProcess) {
		os.runProcess = newProcess;
		newProcess.pDesc.pState = ProcessState.RUN;
		os.readyProcesses.remove(newProcess);
		newProcess.loadCPU();
		newProcess.resetTimer();
	}
	
	/**
	 * Execute process blocks, 
	 * set READY processes to RUN state.
	 */
	private void checkCurrent(){
		FProcess proc = os.runProcess;
		
		if (proc.pDesc.pState == ProcessState.BLOCKED){
			stopProcess();
		} else if (proc.pDesc.pState == ProcessState.READY) {
				proc.pDesc.pState = ProcessState.RUN;
		}
	}
	
	public void stopProcess(){
		FProcess proc = os.runProcess;
		os.runProcess = null;
		
		if (proc.pDesc.extId == ProcName.VIRTUAL_MACHINE){
			proc.decPriority();
		}
		
		//Save CPU state
		proc.saveCPU();
		
		if (proc.pDesc.pState != ProcessState.BLOCKED){
			os.readyProcesses.add(proc);
			
		} else {
			os.blockedProcesses.add(proc);
			os.readyProcesses.remove(proc);
		}
			
	}
	
	private FProcess getTopPriorityReadyProc() {
		FProcess tmp = null;
		
		if (!os.readyProcesses.isEmpty()){
			tmp = os.readyProcesses.element();
			for (FProcess proc : os.readyProcesses) {
				if (proc.pDesc.priority > tmp.pDesc.priority)
					tmp = proc;
			}
		}
		
		return tmp;
	}
	
}
