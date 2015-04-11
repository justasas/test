package fluffy.os.processes;

import java.util.LinkedList;

import javax.swing.SwingUtilities;

import fluffy.devices.OutputPanel;
import fluffy.machine.FCPU;
import fluffy.os.FProcess;
import fluffy.os.FProcessDescriptor;
import fluffy.os.FResource;
import fluffy.os.FResourceManager;
import fluffy.os.FluffyOS;
import fluffy.os.VMemory;
import fluffy.os.FluffyOS.ProcName;
import fluffy.os.FluffyOS.ProcessState;
import fluffy.os.FluffyOS.ResName;

public class PrintLine extends FProcess {

	private FResource deviceRes;
	private FResource descRes;
	
	public PrintLine(int intId, ProcName extId, String pName,
			LinkedList<FProcess> processList, FProcess parentProcess, FCPU cpu,
			FluffyOS os, ProcessState pState, int priority) {
		super(intId, extId, pName, processList, parentProcess, cpu, os, pState,
				priority);
	}
	
	@Override
	public void step() {
		switch (nxtInstruction){
		case 1:
			os.requestResource(this, ResName.EILUTE_ATMINTYJE);
			nxtInstruction++;
			break;
		case 2:
			os.requestResource(this, ResName.ISVEDIMO_IRENGINYS);
			nxtInstruction++;
			break;
		case 3:
			try {
				executeOutput();
			} catch (IllegalArgumentException e){
				os.printStuffDevice("ERROR:PrintLine:IllegalArgument");
			}
			nxtInstruction++;
			break;
		case 4:
			os.destroyResource(descRes);
			nxtInstruction++;
			break;
		case 5:
			os.releaseResource(deviceRes);
			nxtInstruction++;
			break;
		case 6:
			os.createResource(this, ResName.ISVESTA_EILUTE, descRes.getComponent());
			nxtInstruction = 1;
			break;
		}
	}

	private void executeOutput() {
		deviceRes = FResourceManager.findResourceByExtId(
				pDesc.ownedResList, ResName.ISVEDIMO_IRENGINYS);
		descRes = FResourceManager.findResourceByExtId(pDesc.ownedResList, 
				ResName.EILUTE_ATMINTYJE);
		
		final OutputPanel device = (OutputPanel) deviceRes.getComponent();
		FProcessDescriptor desc = (FProcessDescriptor)
				descRes.getComponent();
		
		
		
		final String line = readFromMemory(((VirtualMachine) desc.myProc).memory,
				desc.savedState.regR2.getValInt(), 
				desc.savedState.regR1.getValInt()) ;
		
		
		final String sender = desc.intId + ":" + desc.pName;
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				device.appendLine(sender, line);
			}
		});
		
	}
	
	/**
	 * Starts Output
	 * @param vMem
	 * @param vStartAddress
	 * @param type 1 - number, 2 - string
	 */
	public String readFromMemory(VMemory vMem, int vStartAddress,
			int type){
		int address = vStartAddress;
		String word;
		String bufferOutput = "";
		os.printStuffDevice("Printing from: " + vStartAddress + "," +
				" type: " + type);
		vMem.printMemory();
		switch (type){
		case 1://OUTPUT Number
			try {
				bufferOutput = String.valueOf(
						vMem.getWordAtAddress(address).getValInt());
			} catch (IllegalArgumentException e) {
				bufferOutput = "ERROR: " + e.toString();
			}
			break;
			
		case 2://OUTPUT String
			do {
				word = getWord(vMem, address);
				bufferOutput += word;
				address++;
				System.out.println(bufferOutput);
			} while (!isLast(word));
			bufferOutput = bufferOutput.substring(
					0, bufferOutput.length()-1);
			break;
			
		case 3://OUTPUT Hex number
			bufferOutput = getWord(vMem, address);
			break;
		default:
			throw new IllegalArgumentException(
					"Wrong output type: " + type);
		}
		
		return bufferOutput;
	}
		/**
		 * Get String value of word at address
		 * @param vMem Virtual memory
		 * @param vAddress Address at virtual memory
		 * @return String value of given word
		 */
		private String getWord(VMemory vMem, int vAddress) {
			String str = vMem.getWordAtAddress(vAddress).getVal();
			return str;
		}
		
		/**
		 * Checks if the given words last symbol is "#"
		 * @param word
		 * @return true if the last symbol is "#"
		 */
		private boolean isLast(String word) {
			System.out.println("CheckLast: " + word);
			for(int i = 0; i < word.length(); i++){
				if (word.charAt(i) == '#')
					return true;
			}
			return false;
				
		}
}
