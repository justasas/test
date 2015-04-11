package fluffy.os.processes;

import java.util.LinkedList;

import javax.swing.SwingUtilities;

import fluffy.devices.InputPanel;
import fluffy.machine.FCPU;
import fluffy.os.FProcess;
import fluffy.os.FProcessDescriptor;
import fluffy.os.FResource;
import fluffy.os.FResourceManager;
import fluffy.os.FluffyOS;
import fluffy.os.FluffyOS.ProcName;
import fluffy.os.FluffyOS.ProcessState;
import fluffy.os.FluffyOS.ResName;
import fluffy.os.VMemory;

public class GetLine extends FProcess {

	private FResource deviceRes;
	private FResource descRes;
	
	public GetLine(int intId, ProcName extId, String pName,
			LinkedList<FProcess> processList, FProcess parentProcess, FCPU cpu,
			FluffyOS os, ProcessState pState, int priority) {
		super(intId, extId, pName, processList, parentProcess, cpu, os, pState,
				priority);
	}

	@Override
	public void step() {
		switch (nxtInstruction) {
		case 1:
			os.requestResource(this, ResName.PRANESIMAS_GETLINE);
			nxtInstruction++;
			break;
		case 2:
			os.requestResource(this, ResName.IVEDIMO_IRENGINYS);
			nxtInstruction++;
			break;
		case 3:
			initiateInput();
			os.requestResource(this, ResName.IVEDIMO_SRAUTAS2);
			nxtInstruction++;
			break;
		case 4:
			os.printStuffDevice("GL>> Got Input");
			endInput();
			nxtInstruction++;
			break;
		case 5:
			os.destroyResource(descRes);
			nxtInstruction++;
			break;
		case 6:
			os.releaseResource(deviceRes);
			nxtInstruction = 1;
			break;
		default:
			break;
		}
	}

	private void initiateInput() {
		deviceRes = FResourceManager.findResourceByExtId(
				pDesc.ownedResList, ResName.IVEDIMO_IRENGINYS);
		descRes = FResourceManager.findResourceByExtId(pDesc.ownedResList, 
				ResName.PRANESIMAS_GETLINE);
		
		final InputPanel device = (InputPanel) deviceRes.getComponent();
		final FProcessDescriptor desc = (FProcessDescriptor)
				descRes.getComponent();
		
		os.printStuffDevice("Waiting for INPUT [" + desc.intId +
				":" + desc.pName + "]");
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				device.initInput(desc.myProc);
			}
		});
	}
	
	private void endInput() {
		FResource lineRes = FResourceManager.findResourceByExtId(
				pDesc.ownedResList, ResName.IVEDIMO_SRAUTAS2); 
		
		String bufferInput = (String) lineRes.getComponent();
		FProcessDescriptor desc = (FProcessDescriptor)
				descRes.getComponent();
		
		int type = desc.savedState.regR1.getValInt();
		int vStartAddress = desc.savedState.regR2.getValInt();
		VMemory vMem = ((VirtualMachine) desc.myProc).memory;
		
		switch (type){
		case 2: //String
			//SPLIT data
			String subLine = bufferInput + "#";
			String chars4;
			int writeAddress = vStartAddress;
			while (subLine.length() > 4) {
				chars4 = getFirst4Bytes(subLine);
				subLine = getValue(subLine, 4);
				setMemVal(chars4, writeAddress, vMem);
				writeAddress++;
			}
			setMemVal(subLine, writeAddress, vMem);
			break;
		case 1: //Integer
			setMemValInt(bufferInput, vStartAddress, vMem);
			break;
		default:
			throw new IllegalArgumentException("TYPE: " + type);
		}
		
		os.destroyResource(lineRes);
		os.createResource(this, ResName.IVESTA_EILUTE, desc);
	}

	/**
	 * Get first 4 chars of string
	 * @param line
	 * @return string of first 4 chars
	 */
	private String getFirst4Bytes(String line){
		String newString = new String("");
		for (int i = 0; i != 4; i++){
			try {
				newString = newString + line.charAt(i);
			} catch (IndexOutOfBoundsException e){
				break;
			}
		}
		return newString;
	}
	
	/**
	 * get value from line
	 * Example: ".NAM hahaha"
	 * 	getValue(".NAM hahaha", 5) -> "hahaha"
	 * @param line
	 * @param valuePoint point where value begins
	 * @return value of line
	 */
	private String getValue(String line, int valuePoint) {
		String value = line.substring(valuePoint);
		return value;
	}
	
	/**
	 * Sets memory value
	 * @param line line to add
	 * @param address where to add
	 */
	private void setMemVal(String line, int address, VMemory vMem){
		vMem.getWordAtAddress(address).setVal(line);
		System.out.println("INPUT: " + address + " : " + line);
	}
	
	/**
	 * Sets memory value
	 * @param line line to add
	 * @param address where to add
	 */
	private void setMemValInt(String line, int address, VMemory vMem){
		try{
			int numb = (Integer.parseInt(line));
			String hex = Integer.toHexString(numb);
			this.setMemVal(hex, address, vMem);
		} catch (NumberFormatException e){
			System.out.println("NFE :(");
		}
	}
}
