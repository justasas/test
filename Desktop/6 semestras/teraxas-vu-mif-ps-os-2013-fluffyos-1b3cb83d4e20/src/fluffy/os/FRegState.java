package fluffy.os;

import fluffy.machine.FCPU;
import fluffy.machine.registers.FRegister2B;
import fluffy.machine.registers.FRegister4B;
import fluffy.machine.registers.FRegisterInterrupt;
import fluffy.machine.registers.FRegisterLogicByte;

/**
 * Class for saving CPU state
 * @author karolis
 *
 */
public class FRegState {

	public FRegister4B regR1, regR2;
	public FRegister2B regIC;
	public FRegisterLogicByte regC;
	public FRegister4B regSP;
	
	public FRegisterInterrupt regPI, regSI;
	
	/**
	 * Initializes registers with default values
	 */
	public FRegState() {
		this.regC = new FRegisterLogicByte();
		this.regIC = new FRegister2B();
		this.regR1 = new FRegister4B();
		this.regR2 = new FRegister4B();
		this.regSP = new FRegister4B();
		
		this.regPI = new FRegisterInterrupt();
		this.regSI = new FRegisterInterrupt();
	}
	
	/**
	 * Save cpu state
	 * @param cpu
	 */
	public void saveState(FCPU cpu){
		this.regR1.setValStr(cpu.getRegR1().getValStr());
		this.regR2.setValStr(cpu.getRegR2().getValStr());
		this.regIC.setValInt(cpu.getRegIC().getValInt());
		this.regSP.setValStr(cpu.getRegSP().getValStr());
		
		this.regC.setFlagOverflow(cpu.getRegC().isFlagOverflow());
		this.regC.setFlagSign(cpu.getRegC().isFlagSign());
		this.regC.setFlagZero(cpu.getRegC().isFlagZero());
		
		this.regPI.setValue(cpu.regPI.getValue());
		this.regSI.setValue(cpu.regSI.getValue());
	}

	/**
	 * Restore saved cpu state
	 * @param cpu
	 */
	public void restoreState(FCPU cpu){
		cpu.getRegR1().setValStr(this.regR1.getValStr());
		cpu.getRegR2().setValStr(this.regR2.getValStr());
		cpu.getRegIC().setValInt(this.regIC.getValInt());
		cpu.getRegSP().setValStr(this.regSP.getValStr());
		
		cpu.getRegC().setFlagOverflow(this.regC.isFlagOverflow());
		cpu.getRegC().setFlagSign(this.regC.isFlagSign());
		cpu.getRegC().setFlagZero(this.regC.isFlagZero());
		
		resetInt();
		cpu.regPI.setValue(this.regPI.getValue());
		cpu.regSI.setValue(this.regSI.getValue());
	}
	
	private void resetInt() {
		this.regPI.setValue(0);
		this.regSI.setValue(0);
	}
	
	@Override
	public String toString() {
		String str = 
				"R1 = " + this.regR1.getValStr() +
				", R2 = " + this.regR2.getValStr() +
				", IC = " + this.regIC.getValStr();
		return str;
	}
}
