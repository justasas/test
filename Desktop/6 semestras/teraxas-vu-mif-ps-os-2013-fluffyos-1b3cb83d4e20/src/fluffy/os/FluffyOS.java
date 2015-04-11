package fluffy.os;

import java.io.File;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import fluffy.machine.FMachine;
import fluffy.os.processes.GetLine;
import fluffy.os.processes.Interrupt;
import fluffy.os.processes.JobGovernor;
import fluffy.os.processes.Loader;
import fluffy.os.processes.MainProc;
import fluffy.os.processes.PrintLine;
import fluffy.os.processes.StartStop;
import fluffy.os.processes.VirtualMachine;
import fluffy.os.processes.WaitForJob;

/**
 * Main OS class.
 * 
 * @author karolis
 * 
 */
public class FluffyOS implements Runnable {

	public static final String FP_PREFIX = "fluffies/";
	public static final String FP_END = ".fluffy";
	public static boolean VERBOSE = true;
	public static boolean LOG = false;
	public static int SPEED = 100;

	public static String TEST_FILEPATH = "fluffies/t_input.fluffy";
	public static String TEST_FILEPATH2 = "fluffies/t_halt.fluffy";
	public static String TEST_FILEPATH3 = "fluffies/t_output.fluffy";

	public enum ProcessState {
		RUN, READY, STOPPED, BLOCKED
	}

	public enum ProcName {
		START_STOP, WAIT_FOR_JOB, MAIN_PROC, LOADER, JOB_GOVERNOR, VIRTUAL_MACHINE, INTERRUPT, PRINT_LINE, GET_LINE
	}

	public enum ResName {
		MOS_PABAIGA, VARTOTOJO_ATMINTIS, IVEDIMO_IRENGINYS, ISVEDIMO_IRENGINYS, VIRTUAL_ATMINTIS,

		ISVESTA_EILUTE, EILUTE_ATMINTYJE, // ProcessDescriptor
		PRANESIMAS_GETLINE, IVESTA_EILUTE,

		PRANESIMAS_PERTR, PERTRAUKIMAS, // processor descr.
		RESUME_VM, // int intId

		IVEDIMO_SRAUTAS, IVEDIMO_SRAUTAS2, UZDUOTIS_ISOR, PRANESIMAS_LOADER, // is
																				// JG
		UZDUOTIS_MP, // is MainProc
		UZDUOTIS_VM, // is Loader (atmintyje)
		UZDUOTIS_VYKDYMUI, // is JG->VM
	}

	public enum IntType {
		READY,

		// Lower priority interrupts
		HALT, INPUT, OUTPUT,

		// Critical interrupts
		ILLEGAL_COMMAND, NEGATIVE_RESULT, DIV_BY_ZERO, OVERFLOW
	}

	public LinkedList<FProcess> processes;
	public LinkedList<FProcess> readyProcesses;
	public LinkedList<FProcess> blockedProcesses;
	public LinkedList<FProcess> stoppedProcesses;

	/**
	 * The currently running process
	 */
	public FProcess runProcess;

	public LinkedList<FResource> resources;
	public LinkedList<FResource> usedResources;
	public LinkedList<FResource> freeResources;

	public FMachine realMachine;

	public FProcessManager procMan;
	public FResourceManager resMan;
	public boolean stopOS;

	public FProcess starStopProc;
	private int newProcId;

	public FluffyOS(FMachine realMachine) {
		this.setRealMachine(realMachine);
		initLists();

		this.resMan = new FResourceManager(this);
		this.procMan = new FProcessManager(this);

		this.stopOS = false;

		this.starStopProc = this.createProcess(null, ProcName.START_STOP, 99);
		this.runProcess = this.starStopProc;

		this.realMachine.usrInputDevice.setOS(this);
		this.realMachine.sysInputDevice.setOS(this);
		initInputDevices();
		this.realMachine.usrInputDevice.getInpField().setEditable(false);
	}

	@Override
	public void run() {
		while (!stopOS) {
			osStep();

			try {
				Thread.sleep(FluffyOS.SPEED);
			} catch (InterruptedException ex) {

			}
		}
	}

	public void reset() {
		stopOS = true;

		processes.clear();
		stoppedProcesses.clear();
		readyProcesses.clear();
		blockedProcesses.clear();

		resources.clear();
		usedResources.clear();
		freeResources.clear();

		realMachine.memory.reset();

		this.starStopProc = this.createProcess(runProcess, ProcName.START_STOP);
		this.runProcess = this.starStopProc;
	}

	private void osStep() {
		printStuff("!!!!!!!!!!!!!===OS_STEP===!!!!!!!!!!!!!!");
		for (FProcess prc : processes) {
			FluffyOS.printStuff(prc.toString());
		}
		if (runProcess != null) {
			FluffyOS.printStuff("RUNNNING: " + runProcess.toString());

			runProcess.fullStep();
		} else {
			resMan.execute();
		}

		printStuff("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}

	/**
	 * Initializes resources and processes lists
	 */
	private void initLists() {
		processes = new LinkedList<>();
		readyProcesses = new LinkedList<>();
		blockedProcesses = new LinkedList<>();
		stoppedProcesses = new LinkedList<>();

		resources = new LinkedList<>();
		usedResources = new LinkedList<>();
		freeResources = new LinkedList<>();

	}

	public FProcess createProcess(FProcess creator, ProcName extId) {
		return createProcess(creator, extId, 1);
	}

	public FProcess createProcess(FProcess creator, ProcName extId, int priority) {
		FProcess proc = null;
		int intId = generateProcessInternalID();

		switch (extId) {
		case GET_LINE:
			proc = new GetLine(intId, extId, "GetLine", processes, creator,
					realMachine.getCpu(), this, ProcessState.READY, 91);
			break;
		case INTERRUPT:
			proc = new Interrupt(intId, extId, "Int", processes, creator,
					realMachine.getCpu(), this, ProcessState.READY, 90);
			break;
		case JOB_GOVERNOR:
			proc = new JobGovernor(intId, extId, "JobGov", processes, creator,
					realMachine.getCpu(), this, ProcessState.READY, 89);
			break;
		case LOADER:
			proc = new Loader(intId, extId, "Loader", processes, creator,
					realMachine.getCpu(), this, ProcessState.READY, 88);
			break;
		case MAIN_PROC:
			proc = new MainProc(intId, extId, "MainProc", processes, creator,
					realMachine.getCpu(), this, ProcessState.READY, 92);
			break;
		case PRINT_LINE:
			proc = new PrintLine(intId, extId, "PrintLine", processes, creator,
					realMachine.getCpu(), this, ProcessState.READY, 91);
			break;
		case START_STOP:
			proc = new StartStop(intId, extId, "StartStop", processes, creator,
					realMachine.getCpu(), this, ProcessState.READY, 99);
			break;
		case VIRTUAL_MACHINE:
			proc = new VirtualMachine(intId, extId, "VM", processes, creator,
					realMachine.getCpu(), this, ProcessState.READY, 80);
			break;
		case WAIT_FOR_JOB:
			proc = new WaitForJob(intId, extId, "WFJ", processes, creator,
					realMachine.getCpu(), this, ProcessState.READY, 93);

			break;
		default:
			break;
		}

		if (proc != null) {
			printStuffDevice("CREATED PROC: " + extId);

			this.processes.add(proc);
			this.readyProcesses.add(proc);
			procMan.execute();
			return proc;
		} else {
			printStuffDevice("CREATION FAILED: " + extId);
		}
		procMan.execute();
		return null;
	}

	/**
	 * Destroys selected process
	 * 
	 * @param proc
	 */
	public void destroyProcess(FProcess proc) {
		// 1 - destroy created resources
		LinkedList<FResource> destrRes = new LinkedList<>();
		for (FResource crRes : proc.pDesc.createdResList) {
			destrRes.add(crRes);
		}
		for (FResource desRes : destrRes) {
			destroyResource(desRes);
		}

		// 2 - destroy created processes (recursion)
		LinkedList<FProcess> destrProc = new LinkedList<>();
		for (FProcess crProc : proc.pDesc.childrenList) {
			destrProc.add(crProc);
		}
		for (FProcess desProc : destrProc) {
			destroyProcess(desProc);
		}

		// 3 - remove from all lists
		this.readyProcesses.remove(proc);
		this.blockedProcesses.remove(proc);
		this.stoppedProcesses.remove(proc);
		this.processes.remove(proc);

		FProcess parent = proc.pDesc.parentProcess;
		parent.pDesc.createdResList.remove(proc);

		printStuffDevice("DESTROYED: " + proc.toString());
	}

	/**
	 * Marks process for a stop
	 * 
	 * @param proc
	 */
	public void stopProcess(FProcess proc) {
		if (runProcess.pDesc.intId == proc.pDesc.intId) {
			runProcess = null;
		}

		switch (proc.pDesc.pState) {
		case BLOCKED:
			blockedProcesses.remove(proc);
			stoppedProcesses.add(proc);
			proc.pDesc.pState = ProcessState.STOPPED;
			break;
		case READY:
			readyProcesses.remove(proc);
			stoppedProcesses.add(proc);
			proc.pDesc.pState = ProcessState.STOPPED;
			break;
		case RUN:
			runProcess = null;
			readyProcesses.add(proc);
			proc.pDesc.pState = ProcessState.READY;
			break;
		default:
			break;
		}
		procMan.execute();
	}

	/**
	 * Activates stopped process
	 */
	public void activateProcess(FProcess proc) {
		if (this.stoppedProcesses.contains(proc)) {
			this.stoppedProcesses.remove(proc);
			this.readyProcesses.add(proc);
			proc.pDesc.pState = ProcessState.READY;
		} else {
			FluffyOS.printStuff("ERROR: process is not stopped");
		}
	}

	/**
	 * Create new resource
	 * 
	 * @param creator
	 *            Creator process
	 * @param extId
	 *            Type of process
	 * @param component
	 *            Object containing the resource
	 * @return
	 */
	public FResource createResource(FProcess creator, ResName extId,
			Object component) {
		int intId = generateResourceInternalID();
		FResource res = null;

		switch (extId) {
		// Vienkartiniai
		case VARTOTOJO_ATMINTIS:
		case UZDUOTIS_VM:
		case UZDUOTIS_ISOR:
		case IVEDIMO_SRAUTAS:
		case IVEDIMO_SRAUTAS2:
		case UZDUOTIS_MP:
		case IVESTA_EILUTE:
		case ISVESTA_EILUTE:
		case EILUTE_ATMINTYJE:
		case PRANESIMAS_LOADER:
		case PRANESIMAS_PERTR:
		case PERTRAUKIMAS:
		case PRANESIMAS_GETLINE:
		case MOS_PABAIGA:
		case UZDUOTIS_VYKDYMUI:
		case RESUME_VM:

			res = new FResource(intId, extId, this, creator, false, component);
			break;

		// Daugkartiniai
		case IVEDIMO_IRENGINYS:
		case ISVEDIMO_IRENGINYS:

			res = new FResource(intId, extId, this, creator, false, component);
			break;

		default:
			throw (new IllegalArgumentException("Illegal resource"));
		}

		printStuffDevice("CREATED RES: " + extId);

		// Add to lists
		creator.pDesc.createdResList.add(res);
		this.resources.add(res);
		this.freeResources.add(res);
		this.resMan.execute();
		return res;
	}

	/**
	 * Releases the resource and puts it to the list of free resources
	 * 
	 * @param res
	 */
	public void releaseResource(FResource res) {
		FProcess currentUser;
		if (res.getResDesc().getUser() != null) {
			currentUser = res.getResDesc().getUser();
			currentUser.pDesc.ownedResList.remove(res);
			res.getResDesc().setUser(null);
		}

		this.usedResources.remove(res);
		this.freeResources.add(res);

		resMan.execute();
	}

	/**
	 * Releases the resource and removes from lists
	 * 
	 * @param res
	 */
	public void destroyResource(FResource res) {
		FProcess currentUser;
		if (res.getResDesc().getUser() != null) {
			currentUser = res.getResDesc().getUser();
			currentUser.pDesc.ownedResList.remove(res);
			res.getResDesc().setUser(null);
		}

		res.resDesc.getCreatorProcess().pDesc.createdResList.remove(res);
		this.freeResources.remove(res);
		this.usedResources.remove(res);
		this.resources.remove(res);

		// resMan.execute();
	}

	public void giveResource(FProcess proc, FResource res) {
		this.resMan.giveResource(proc, res);
	}

	public boolean requestResource(FProcess proc, ResName res) {
		proc.pDesc.waitingFor.add(res);
		this.resMan.execute();

		if (proc.pDesc.waitingFor.size() == 0)
			return true;
		else
			return false;
	}

	private int generateProcessInternalID() {
		newProcId++;
		return newProcId;
	}

	private int generateResourceInternalID() {
		int newId = FResource.numberOfInstances;
		FResource.numberOfInstances++;
		return newId;
	}

	/**
	 * Print stuff to console/log
	 * 
	 * @param stuff
	 *            line to print
	 */
	public static void printStuff(String stuff) {
		printStuff(stuff, Level.FINE,
				Thread.currentThread().getStackTrace()[0].getClassName());
	}

	/**
	 * Print stuff to console/log with level
	 * 
	 * @param stuff
	 *            line to print
	 */
	public static void printStuff(String stuff, Level lvl, Object obj) {
		if (VERBOSE) {
			System.out.println(stuff);
		}

		if (LOG) {
			Logger.getLogger("fLog").log(lvl, stuff, obj);
		}

	}

	public void printStuffDevice(String stuff) {
		final String tmp = stuff;
		if (VERBOSE) {
			System.out.println(stuff);
		}

		if (LOG) {
			Logger.getLogger("fLog").log(Level.FINE, stuff);
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				realMachine.sysOutputDevice.appendLine("OS", tmp);
			}
		});
	}

	/**
	 * Adds some jobs to test OS - creates "IVEDIMO_SRAUTAS"
	 */
	public void addTestJobs() {
		this.createResource(starStopProc, ResName.IVEDIMO_SRAUTAS, new File(
				TEST_FILEPATH2));
		this.createResource(starStopProc, ResName.IVEDIMO_SRAUTAS, new File(
				TEST_FILEPATH3));
		this.createResource(starStopProc, ResName.IVEDIMO_SRAUTAS, new File(
				TEST_FILEPATH));
		this.createResource(starStopProc, ResName.IVEDIMO_SRAUTAS, new File(
				TEST_FILEPATH2));
		this.createResource(starStopProc, ResName.IVEDIMO_SRAUTAS, new File(
				TEST_FILEPATH2));
		this.createResource(starStopProc, ResName.IVEDIMO_SRAUTAS, new File(
				TEST_FILEPATH2));

	}

	private int getTimer() {
		return realMachine.getCpu().getRegTIME().getValue();
	}

	public void decrementTimer() {
		int timer = getTimer();
		if (timer > 0) {
			timer--;
			realMachine.getCpu().getRegTIME().setValue(timer);
		}
	}

	/**
	 * creates resource without running resMan
	 * 
	 * @param creator
	 *            creator of resource
	 * @param extId
	 *            external ID
	 * @param component
	 *            object of resource
	 * @return
	 */
	public FResource createSimpleResource(FProcess creator, ResName extId,
			Object component) {
		FResource res = new FResource(generateResourceInternalID(), extId,
				this, creator, false, component);
		resources.add(res);
		return res;
	}

	private void initInputDevices() {

		// USER
		realMachine.usrInputDevice.getInpField().addActionListener(
				realMachine.usrInputDevice.usrAL);
		realMachine.usrInputDevice.getButton().addActionListener(
				realMachine.usrInputDevice.usrAL);

		// SYSTEM
		realMachine.sysInputDevice.getInpField().addActionListener(
				realMachine.sysInputDevice.sysAL);
		realMachine.sysInputDevice.getButton().addActionListener(
				realMachine.sysInputDevice.sysAL);
		realMachine.sysInputDevice.getInpField().setEditable(true);
		realMachine.sysInputDevice.getInpField().setEnabled(true);
	}

	public FMachine getRealMachine() {
		return realMachine;
	}

	public void setRealMachine(FMachine realMachine) {
		this.realMachine = realMachine;
	}

}
