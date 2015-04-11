/**
 * 
 */
package fluffy.machine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import fluffy.machine.registers.FRegister4B;
import fluffy.machine.registers.FRegisterLogicByte;
import fluffy.os.FluffyOS;
import fluffy.os.VMemory;
import fluffy.os.processes.VirtualMachine;

/**
 * Utility class for: 
 * 	 processing commands, 
 *   loading programs to memory, 
 *   making things go fluffy
 * @author karolis
 */
public class FProcessingUtility {
	
	/**
	 * Recognizes and processes command for virtual machine
	 * @param machina virtual machine
	 * @param command 4-byte command
	 * @throws IllegalArgumentException
	 */
	public static void processCommand(VirtualMachine machina, String command){
		char[] chars = command.toCharArray();
		boolean error = false;
		
		FluffyOS.printStuff(
				machina.pDesc.intId + ":" + 
				machina.pDesc.extId + "(" + 
				machina.pDesc.pName + "):" + command +
				", IC = " + machina.pDesc.cpu.regIC.getValInt());
		
		if (command.equals("HALT")){
			cmHALT(machina);
			return;
		}
		
		switch(chars[0]){
		//---L***
		case 'L': //LOAD
			switch(chars[1]){
			case '1':
				cmLzxy(command, machina);
				break;
			case '2':
				cmLzxy(command, machina);
				break;
			default:
				error = true;
				break;
			}
			break;
			
		//S***
		case 'S': //SAVE
			switch(chars[1]){
			case '1':
				cmSzxy(command, machina);
				break;
			case '2':
				cmSzxy(command, machina);
				break;
			case 'E'://Register SETTERS
				switch(chars[2]){
				case 'I':
					if (command == "SEIC"){ //SEIC
						cmSEIC(machina);
					} else {
						error = true;
					}
					break;
				case 'S':
					if (command == "SESP"){ //SESP
						cmSESP(machina);
					} else {
						error = true;
					}
					break;
				case 'C': //SECx
					cmSECx(command, machina);
				}
				break;
			default:
				error = true;
			}
			break;
		
		//C***
		case 'C':
			switch (chars[1]){
			case '1': //Cmp1 - C1xy
				cmCzxy(command, machina);
				break;
			case '2': //Cmp2 - C2xy
				cmCzxy(command, machina);
				break;
			case 'P': //CPYx
				if (command.charAt(2) == 'Y'){
					cmCPYx(command, machina);
				} else {
					error = true;
				}
				break;
			}
			break;
		
		//A***
		case 'A':
			switch (chars[1]) {
			case '1': //Add
				cmABMzxy(command, machina);
				break;
			case '2': //Add
				cmABMzxy(command, machina);
				break;

			default:
				error = true;
			}
			break;
			
		//B***
		case 'B':
			switch(chars[1]){
			case '1':
				cmABMzxy(command, machina);
				break;
			case '2':
				cmABMzxy(command, machina);
				break;
			default:
				error = true;
			}
			break;
			
		//M***
		case 'M':
			switch(chars[1]){
			case '1': //Multiply 1
				cmABMzxy(command, machina);
				break;
			case '2': //Multiply 2
				cmABMzxy(command, machina);
				break;
			default:
				error = true;
			}
			break;
			
		//D***
		case 'D':
			switch(chars[1]){
			case 'I': //DIxy
				cmDIxy(command, machina);
				break;
			case 'E': //DEC
				if (command == "DEC1"){
					cmDecInc(command, machina);
				} else if (command == "DEC2"){
					cmDecInc(command, machina);
				}
				break;
			default:
				error = true;
			}
			break;
			
		//I***
		case 'I':
			switch(chars[1]){
			case 'N': //INC
				if (command.equals("INC1")){
					cmDecInc(command, machina);
				} else if (command.equals("INC2")){
					cmDecInc(command, machina);
				} else { //INxy - Input
					cmINxy(command, machina);
				}
				break;
			default:
				error = true;
			}
			break;
			
		//J***
		case 'J':
			switch(chars[1]){
			//Fluffy JUMPS!!! 
			case 'P': //JP
				cmJUMPxy(command, machina);
				break;
			case 'E': //JE
				cmJUMPxy(command, machina);
				break;
			case 'L': //JL
				cmJUMPxy(command, machina);
				break;
			case 'G': //JG
				cmJUMPxy(command, machina);
				break;
			}
			break;
			
		//O*** (OOOOoooo... Fluffy)
		case 'O':
			if (chars[1] == 'P'){ //OPxy - Output
				cmOPxy(command, machina);
			} else {
				error = true;
			}
			break;
			
		//G***
		case 'G':
			switch (chars[1]){
			case 'E': //Register GETTERS
				switch (chars[2]){
				case 'I':
					if (command == "GEIC"){ //GEIC
						cmGEIC(machina);
					} else {
						error = true;
					}
					break;
				case 'C'://GECx
					cmGECx(command, machina);
					break;
				case 'S':
					if (command == "GESP"){ //GESP
						cmGESP(machina);
					} else {
						error = true;
					}
					break;
				default:
					error = true;
					break;
				}
			default:
				error = true;
				break;
			}
			break;
			
			default:
				error = true;
				break;
			
		}
		
		if (error) throw new IllegalArgumentException(command);
	}
	
	/**
	 * Loads a program from file to virtual memory
	 * @param file Source file
	 * @throws IOException 
	 */
	public static String loadMemory(VMemory vMem, File source) throws IOException{
		String pName = null;//Program name
		
		//In Java working with files is not fluffy at all :(
		@SuppressWarnings("resource")
		FileInputStream fis = new FileInputStream(source);
		InputStreamReader isr = new InputStreamReader(fis);
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(isr);
		
		int dsSize = 0;//Data segment size
		
		//Now let's read fluffy stuff :)
		int writeAddress = 0;
		String line = null;
		String subLine = null;
		String chars4 = null;
		char firstChar;
		
		while ((line = reader.readLine()) != null){
			firstChar = line.charAt(0);
			if (line.length() > 4){ //Data or values
				if (firstChar == '.'){ //Headers
					if (getFirst4Bytes(line).equals(".NAM")){
						//Name
						pName = getValue(line, 5);
					} else if (getFirst4Bytes(line).equals(".DAT")) {
						//Data segment size
						try{
							dsSize = Integer.parseInt(FProcessingUtility.getValue(line, 5));
						} catch (NumberFormatException e){
							e.printStackTrace();
							throw(e);
						}
					}
				} else if (firstChar == '/'){
					//Nothing - it's a comment
				}else if (firstChar == '*' && line.length() == 5){
					//DECIMAL NUMBER
					setMemValInt(FProcessingUtility.getValue(line, 1), writeAddress, vMem);
					writeAddress++;
				} else { //Data
					//SPLIT data
					subLine = line;
					do {
						chars4 = getFirst4Bytes(subLine);
						subLine = getValue(subLine, 4);
						setMemVal(chars4, writeAddress, vMem);
						writeAddress++;
					} while (subLine.length() > 4);
					setMemVal(subLine, writeAddress, vMem);
					writeAddress++;
				}	
			} else { //Commands, data, addressing (<4)
				if (firstChar == '$'){ //Addressing or header
					if (line.equals("$FLJ")){
						//Header
					} else if (line.equals("$END")){
						System.out.println("Be nice - be FLUFFY!!!");
						//end of file
						break;
					} else if (line.equals("$FLC")) {//GOTO code
						writeAddress = 0;
					} else if (line.equals("$DAT")) {//GOTO data segment
						writeAddress = 
							10 * (vMem.DEFAULT_MEMSIZE - dsSize);
					} else{
						writeAddress = Integer.parseInt(getValue(line, 1));
					}
				} else if (firstChar == '/'){
					//Nothing - it's a comment
				} else if (firstChar == '*'){
					//DECIMAL NUMBER
					setMemValInt(getValue(line, 1), writeAddress, vMem);
					writeAddress++;
				} else { //Code or data (hex number?)
					setMemVal(line, writeAddress, vMem);
					writeAddress++;
				}
			}
		}
		vMem.pName = pName;
		FluffyOS.printStuff("File successfully loaded to VirtualMemory");
		return pName;
	}
	
	
// ===============================================================
// ---- U T I L I T I T S ----
	
	/**
	 * get value from line
	 * Example: ".NAM hahaha"
	 * 	getValue(".NAM hahaha", 5) -> "hahaha"
	 * @param line
	 * @param valuePoint point where value begins
	 * @return value of line
	 */
	private static String getValue(String line, int valuePoint) {
		String value = line.substring(valuePoint);
		return value;
	}
	
	/**
	 * Get first 4 chars of string
	 * @param line
	 * @return string of first 4 chars
	 */
	private static String getFirst4Bytes(String line){
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
	 * Sets memory value
	 * @param line line to add
	 * @param address where to add
	 */
	private static void setMemVal(String line, int address, VMemory vMem){
		vMem.getWordAtAddress(address).setVal(line);
	}
	
	/**
	 * Sets memory value
	 * @param line line to add
	 * @param address where to add
	 */
	private static void setMemValInt(String line, int address, VMemory vMem){
		try{
			int numb = (Integer.parseInt(line));
			String hex = Integer.toHexString(numb);
			setMemVal(hex, address, vMem);
		} catch (NumberFormatException e){
			System.out.println("NFE :(");
		}
	}

// ===============================================================
// ---- C O M M A N D S ----
	
	/**
	 * Lzxy
	 * Load to R[z] from [x*10+y] OR  [R[y]]
	 * @param cmd
	 * @throws IllegalArgumentException
	 */
	private static void cmLzxy(String cmd, VirtualMachine machina) throws IllegalArgumentException{
		FRegister4B destination = null;
		int source;
		
		if (cmd.charAt(1) == '1'){
			destination = machina.getRegR1();
		} else if (cmd.charAt(1) == '2'){
			destination = machina.getRegR2();
		} else {
			throw new IllegalArgumentException(cmd);
		}
		
		if (cmd.charAt(2) == 'R'){
			switch (cmd.charAt(3)){
			case '1':
				source = machina.getRegR1().getValInt();
				break;
			case '2':
				source = machina.getRegR2().getValInt();
				break;
			default:
				throw new IllegalArgumentException(cmd);
			}
		} else {
			source = Integer.parseInt(getValue(cmd, 2));
		}
		
		destination.setValStr(machina.getMemory().
				getWordAtAddress(source).getVal());
	}
	
	/**
	 * Szxy
	 * Save from R[z] to [x*10+y] OR  [R[y]]
	 * @param cmd
	 * @throws IllegalArgumentException
	 */
	private static void cmSzxy(String cmd, VirtualMachine machina) throws IllegalArgumentException{
		FRegister4B src = null;
		int dest;
		
		if (cmd.charAt(1) == '1'){
			src = machina.getRegR1();
		} else if (cmd.charAt(1) == '2'){
			src = machina.getRegR2();
		} else {
			throw new IllegalArgumentException(cmd);
		}
		
		if (cmd.charAt(2) == 'R'){
			switch (cmd.charAt(3)){
			case '1':
				dest = machina.getRegR1().getValInt();
				break;
			case '2':
				dest = machina.getRegR2().getValInt();
				break;
			default:
				throw new IllegalArgumentException(cmd);
			}
		} else {
			dest = Integer.parseInt(getValue(cmd, 2));
		}
		
		machina.getMemory().getWordAtAddress(dest).setVal(src.getValStr());
	}
	
	/**
	 * CPYx
	 * Copy value to R[x] from R[y];
	 * @param command
	 * @throws IllegalArgumentException
	 */
	private static void cmCPYx(String command, VirtualMachine machina) throws IllegalArgumentException{
		if (command.charAt(3) == 1){ //CPY1
			machina.getRegR1().setValStr(
					machina.getRegR2().getValStr());
		} else if (command.charAt(3) == 2) { //CPY2
			machina.getRegR2().setValStr(
					machina.getRegR1().getValStr());
		} else {
			throw new IllegalArgumentException(command);
		}
	}
	
	/**
	 * Azxy/Bzxy/Mzxy - Addition AND Subtraction AND Multiplication
	 * R[z] + [x*10 + y]
	 * @param command
	 * @throws IllegalArgumentException
	 */
	private static void cmABMzxy(String command, VirtualMachine machina) throws IllegalArgumentException{
		FRegister4B destination = null;
		int numb;
		
		if (command.charAt(1) == '1'){
			destination = machina.getRegR1();
		} else if (command.charAt(1) == '2'){
			destination = machina.getRegR2();
		} else {
			throw new IllegalArgumentException(command);
		}
		
		if (command.charAt(2) == 'R'){
			switch (command.charAt(3)){
			case '1':
				numb = machina.getRegR1().getValInt();
				break;
			case '2':
				numb = machina.getRegR2().getValInt();
				break;
			default:
				throw new IllegalArgumentException(command);
			}
		} else {
			numb = machina.getMemory().
					getWordAtAddress(
							Integer.parseInt(getValue(command, 2))).
							getValInt();
		}
		
		int res = 0;
		if (command.charAt(0) == 'A'){ //Addition
			res = destination.getValInt() + numb;
		} else if (command.charAt(0) == 'B'){ //Subtraction
			res = destination.getValInt() - numb;
		} else if (command.charAt(0) == 'M'){
			res = destination.getValInt() * numb;
		} else {
			throw new IllegalArgumentException(command);
		}
		
		destination.setValInt(res);
		
		//If result is longer than FWord - Overflow
		if(Integer.toString(res, 16).length() > FWord.DEFAULT_WORD_SIZE){
			machina.getRegC().setFlagOverflow(true);
		} else {
			machina.getRegC().setFlagOverflow(false);
		}
	}
	
	/**
	 * DIxy - Division
	 * R1 = R1 div [x*10 + y]; R2 = R1 mod [x*10 + y];
	 * @param command
	 * @throws NumberFormatException
	 */
	private static void cmDIxy(String command, VirtualMachine machina) throws NumberFormatException{
		int address;
		try{
			address = Integer.parseInt(getValue(command, 2));
		} catch (NumberFormatException e){
			e.printStackTrace();
			throw(e);
		}
		
		int valR1 = machina.getRegR1().getValInt();
		int valXY = machina.getMemory().
				getWordAtAddress(address).getValInt();
		
		int divVal = valR1 / valXY;
		int modVal = valR1 % valXY;
		
		machina.getRegR1().setValInt(divVal);
		machina.getRegR2().setValInt(modVal);
	}
	
	/**
	 * DEC[x], DEC[x], INC[x], INC[x]
	 * Increments/Decrements R[x] register
	 * @param command
	 * @throws IllegalArgumentException
	 */
	private static void cmDecInc(String command, VirtualMachine machina) throws IllegalArgumentException{
		FRegister4B reg = null;
		
		switch (command.charAt(3)){
		case '1':
			reg = machina.getRegR1();
			break;
		case '2':
			reg = machina.getRegR2();
			break;
		default:
			throw new IllegalArgumentException(command);
		}
		
		switch (command.charAt(0)){
		case 'D':
			reg.setValInt(reg.getValInt() - 1); 
			break;
		case 'I':
			reg.setValInt(reg.getValInt() + 1); 
			break;
		default:
			throw new IllegalArgumentException(command);
		}
	}
	
	/**
	 * Czxy - Compare
	 * Compares [R[z]] with value at [x*10 + y]
	 * @param command
	 * @throws IllegalArgumentException
	 */
	private static void cmCzxy(String command, VirtualMachine machina) throws IllegalArgumentException{
		FRegister4B reg = null;
		int address = Integer.parseInt(getValue(command, 2));
		int valXY = machina.getMemory().
				getWordAtAddress(address).getValInt();
		
		switch (command.charAt(1)){
		case '1':
			reg = machina.getRegR1();
			break;
		case '2':
			reg = machina.getRegR2();
			break;
		default:
			throw new IllegalArgumentException(command);
		}
		
		int regVal = reg.getValInt();
		FRegisterLogicByte lReg = machina.getRegC();
		if (regVal > valXY){
			lReg.setFlagZero(false);
			lReg.setFlagSign(false);
			lReg.setFlagOverflow(false);
		} else if (regVal < valXY){
			lReg.setFlagZero(false);
			lReg.setFlagSign(true);
			lReg.setFlagOverflow(false);
		} else if (regVal == valXY){
			lReg.setFlagZero(true);
			lReg.setFlagSign(false);
			lReg.setFlagOverflow(false);
		}
	}
	
	/**
	 * JUMPS!
	 * JPxy (jump), 
	 * JExy (jump if equal), JLxy (jump if lower), JGxy (jump if greater) 
	 * @param command
	 * @throws IllegalArgumentException
	 */
	private static void cmJUMPxy(String command, VirtualMachine machina) throws IllegalArgumentException{
		int address = Integer.parseInt(getValue(command, 2));
		
		switch(command.charAt(1)){
		//Fluffy JUMPS!!! 
		case 'P': //JP
			machina.getRegIC().setValInt(address);
			break;
		case 'E': //JE
			if (machina.getRegC().isFlagZero())
				machina.getRegIC().setValInt(address);
			break;
		case 'L': //JL
			if (!machina.getRegC().isFlagZero() && 
					machina.getRegC().isFlagOverflow() == 
						machina.getRegC().isFlagSign())
				machina.getRegIC().setValInt(address);
			break;
		case 'G': //JG
			if (!machina.getRegC().isFlagZero() && 
					machina.getRegC().isFlagOverflow() != 
						machina.getRegC().isFlagSign())
				machina.getRegIC().setValInt(address);
			break;
		default:
			throw new IllegalArgumentException(command);
		}
	}
	
	/**
	 * INxy - Input.
	 * Calls interrupt for input to [xy].
	 * Sets R2 value to address of Input.
	 * @param command
	 */
	private static void cmINxy(String command, VirtualMachine machina) {
		machina.getRegR2().setValInt(
				Integer.parseInt(getValue(command, 2)));
		machina.getCpu().getRegSI().setValue(1);
	}
	
	/**
	 * OPxy = Output.
	 * Calls interrupt for output from [xy].
	 * Sets R2 value to the address of Output.
	 * @param command
	 */
	private static void cmOPxy(String command, VirtualMachine machina) {
		machina.getRegR2().setValInt(
				Integer.parseInt(getValue(command, 2)));
		machina.getCpu().getRegSI().setValue(2);
	}
	
	/**
	 * HALT
	 * Calls interrupt to stop VM
	 */
	private static void cmHALT(VirtualMachine machina) {
		machina.getCpu().getRegSI().setValue(3);
	}
	
	/**
	 * Get IC
	 * Puts IC value to R2
	 */
	private static void cmGEIC(VirtualMachine machina) {
		int val = machina.getRegIC().getValInt();
		machina.getRegR2().setValInt(val);
	}
	
	/**
	 * Get C[x]
	 * Puts C[x] value to R2
	 * x = 1 - Zero Flag
	 * x = 2 - Sign Flag
	 * x = 3 - Overflow Flag
	 * @param command
	 * @throws IllegalArgumentException
	 */
	private static void cmGECx(String command, VirtualMachine machina) throws IllegalArgumentException{
		int val = 0;
		char lastChar = command.charAt(3);
		
		switch (lastChar){
		case '1':
			if (machina.getRegC().isFlagZero()){
				val = 1;
			} else{
				val = 0;
			}
			break;
		case '2':
			if (machina.getRegC().isFlagSign()){
				val = 1;
			} else{
				val = 0;
			}
			break;
		case '3':
			if (machina.getRegC().isFlagOverflow()){
				val = 1;
			} else{
				val = 0;
			}
			break;
		default:
			throw new IllegalArgumentException(command);
		}
		
		machina.getRegR2().setValInt(val);
	}
	
	/**
	 * Get SP
	 * puts SP value to R2
	 */
	private static void cmGESP(VirtualMachine machina) {
		int val = machina.getRegSP().getValInt();
		machina.getRegR2().setValInt(val);
	}
	
	/**
	 * Set IC
	 * Sets IC value to R2 value
	 */
	private static void cmSEIC(VirtualMachine machina) {
		int val = machina.getRegR2().getValInt();
		machina.getRegIC().setValInt(val);
	}
	
	/**
	 * Set SP
	 * Sets SP value to R2 value
	 */
	private static void cmSESP(VirtualMachine machina) {
		int val = machina.getRegR2().getValInt();
		machina.getRegIC().setValInt(val);
	}
	
	/**
	 * Set C[x]
	 * Sets C[x] value to R2 value
	 * if R2 value is NOT "0" or "1" -> FALSE
	 * x = 1 - Zero Flag
	 * x = 2 - Sign Flag
	 * x = 3 - Overflow Flag
	 * @throws IllegalArgumentException
	 */
	private static void cmSECx(String command, VirtualMachine machina) throws IllegalArgumentException {
		boolean val = false;
		if (machina.getRegR2().getValInt() == 1){
			val = true;
		}
		
		char lastChar = command.charAt(3);
		
		switch (lastChar){
		case '1':
			machina.getRegC().setFlagZero(val);
			break;
		case '2':
			machina.getRegC().setFlagSign(val);
			break;
		case '3':
			machina.getRegC().setFlagOverflow(val);
			break;
		default:
			throw new IllegalArgumentException(command);
		}	
	}
	
	
}
