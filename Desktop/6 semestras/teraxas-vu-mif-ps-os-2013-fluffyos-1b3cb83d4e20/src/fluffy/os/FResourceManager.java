package fluffy.os;

import java.util.Collections;
import java.util.LinkedList;

import fluffy.machine.FMemory;
import fluffy.machine.registers.FRegister4B;
import fluffy.os.FluffyOS.ProcessState;
import fluffy.os.FluffyOS.ResName;

/**
 * Fluffy OS Resource Manager. Manages resource usage.
 * @author karolis
 *
 */
public class FResourceManager {

	private FluffyOS os;
	
	public FResourceManager(FluffyOS os) {
		this.os = os;
	}

	/**
	 * Checks if any of the waited resources can be provided
	 */
	public void execute(){
		
		//Create list of processes waiting for some resource
		LinkedList<FProcess> waitingForRes = new LinkedList<>();
		for (FProcess process : os.processes) {
			if (process.pDesc.waitingFor.size() > 0){
				waitingForRes.add(process);
			}
		}
		
		sortProcessListByPriority(waitingForRes);
		
		FluffyOS.printStuff("=============RESMAN================");
		FluffyOS.printStuff("Resman: memory status = (" +
				os.getRealMachine().getMemory().freeBlocksCount() +
				"/"+ os.getRealMachine().getMemory().getSize() + ")");
		FluffyOS.printStuff("ResMan: waiting count = " + 
				waitingForRes.size());
		
		LinkedList<FResource> tmpList = null;
		LinkedList<ResName> tmpWaitedList = null;
		
		//Check for available resources
		for (FProcess process : waitingForRes) {
//			FluffyOS.printStuff(process.toString());
			
			//Copy waited res list
			tmpWaitedList = new LinkedList<>();
			for (ResName resn : process.pDesc.waitingFor) {
				tmpWaitedList.add(resn);
			}
			
			for (ResName waitedRes : tmpWaitedList) {
				
				//Memory is only given when enough free blocks are available
				if (waitedRes == ResName.VARTOTOJO_ATMINTIS){
					
					FMemory memory = os.getRealMachine().getMemory();
					
					if (memory.isEnoughMem()){
						FRegister4B plr = new FRegister4B();
						plr.setValInt(memory.getFreeBlockIdx());
						VMemory vMem = null;
						try {
							 vMem = new VMemory(memory, plr );
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
						
						FResource vMemRes = os.createSimpleResource(
								os.starStopProc, 
								ResName.VIRTUAL_ATMINTIS, vMem);
						
						giveResource(process, vMemRes);
						process.pDesc.waitingFor.remove(
								ResName.VARTOTOJO_ATMINTIS);
					} else {
						FluffyOS.printStuff("NOT ENOUGH MEMORY!!!");
					}
					
				} else {
					tmpList = FResourceManager.findResourceListByExtId(
							os.freeResources, waitedRes);
					
					for (FResource res : tmpList) {
						if (process.fitRes(res)){
							os.giveResource(process, res);
							FluffyOS.printStuff(
									process.pDesc.extId + "<-" + 
										res.resDesc.getExtId());
							break;
						}
					}
				}
			}
			
			//No resource available or not all resources available
			if (process.pDesc.waitingFor.size() == 0){
				process.pDesc.pState = ProcessState.READY;
				if (os.blockedProcesses.contains(process)){
					os.blockedProcesses.remove(process);
					os.readyProcesses.add(process);
				}
			} else {
				if (process.pDesc.pState == ProcessState.READY){
					process.pDesc.pState = ProcessState.BLOCKED;
					os.readyProcesses.remove(process);
					os.blockedProcesses.add(process);
				} else if (process.pDesc.pState == ProcessState.RUN){
					process.pDesc.pState = ProcessState.BLOCKED;
					os.blockedProcesses.add(process);
				}
			}
		}
		os.procMan.execute();
	}
	
	/**
	 * Gives resource to process
	 * @param process
	 * @param res
	 */
	public void giveResource(FProcess process, FResource res) {
		process.pDesc.ownedResList.add(res);
		process.pDesc.waitingFor.remove(res.resDesc.getExtId());
		
		os.freeResources.remove(res);
		os.usedResources.add(res);
		
		res.resDesc.setUser(process);
	}
	
	/**
	 * Sorts list of processes by priority in descending order
	 * @param list
	 */
	public static void sortProcessListByPriority(
			LinkedList<FProcess> list){
		Collections.sort(list);
	}
	
	/**
	 * Finds resource by name in a list
	 * @param list List of resources
	 * @param extId Resource name (extId)
	 * @return The resource. Null if not found.
	 */
	public static FResource findResourceByExtId(
			LinkedList<FResource> list, ResName extId){
		for (FResource res : list) {
			if (res.resDesc.getExtId() == extId)
				return res;
		}
		return null;
	}
	
	/**
	 * Finds all resources with the provided extId in a list
	 * @param list List of resources
	 * @param extId Resource name (extId)
	 * @return List of resources
	 */
	public static LinkedList<FResource> findResourceListByExtId(
			LinkedList<FResource> list, ResName extId) {
		LinkedList<FResource> tmpList = new LinkedList<>();
		for (FResource res : list) {
			if (res.resDesc.getExtId() == extId)
				tmpList.add(res);
		}
		return tmpList;
	}
	
}
