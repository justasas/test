package fluffy.machine;

import fluffy.machine.registers.FRegister2B;
import fluffy.machine.registers.FRegister4B;
import fluffy.machine.registers.FRegisterInterrupt;
import fluffy.machine.registers.FRegisterLogic;
import fluffy.machine.registers.FRegisterLogicByte;

/**
 * A class containing CPU registers
 * @author karolis
 *
 */
public class FCPU {

	/**
	 * Data register
	 */
	public FRegister4B regR1, regR2;
	
	/**
	 * Code index register
	 */
	public FRegister2B regIC;
	
	/**
	 * 1 byte logic register
	 * (ZF, SF, OF)
	 */
	public FRegisterLogicByte regC;
	
	/**
	 * Page Table register
	 */
	public FRegister4B regPLR;
	
	/**
	 * Stack pointer register
	 */
	public FRegister4B regSP;
	
	/**
	 * CPU mode register:
	 * True - supervisor;
	 * False - user;
	 */
	public FRegisterLogic regMODE;
	
	/**
	 * Program interrupts:
	 * 0 - OK;
	 * 1 - Illegal command;
	 * 2 - Negative result;
	 * 3 - division by 0;
	 * 4 - overflow;
	 */
	public FRegisterInterrupt regPI; 
	
	/**
	 * System interrupts:
	 * 0 - OK;
	 * 1 - Input;
	 * 2 - Output;
	 * 3 - HALT;
	 */
	public FRegisterInterrupt regSI;
	
	/**
	 * IOI register:
	 * 1 - input;
	 * 2 - output;
	 */
	public FRegisterInterrupt regIOI;
	
	/**
	 * Channel registers
	 * K1 - input
	 * K2 - output
	 * IS CHANNEL USED? 
	 * True - used, False - free;
	 */
	public FRegisterLogic regK1, regK2, regK3;
	
	/**
	 * Timer register 
	 */
	public FRegisterInterrupt regTIME;
	
	/**
	 * Constructor
	 */
	public FCPU() {
		//Registers also used by VM
		this.regC = new FRegisterLogicByte();
		this.regIC = new FRegister2B();
		this.regR1 = new FRegister4B();
		this.regR2 = new FRegister4B();
		this.regSP = new FRegister4B();
		
		//Page Table Register
		this.regPLR = new FRegister4B();
		
		//System registers
		this.regK1 = new FRegisterLogic();
		this.regK2 = new FRegisterLogic();
		this.regK3 = new FRegisterLogic();
		this.regMODE = new FRegisterLogic();
		this.regPI = new FRegisterInterrupt();
		this.regSI = new FRegisterInterrupt();
		this.regIOI = new FRegisterInterrupt();
		this.regTIME = new FRegisterInterrupt();
		
		//No interrupts on start!
		this.regPI.setValue(0);
		this.regSI.setValue(0);
		this.regIOI.setValue(0);
	}

        
        @Override
        public String toString(){
            String str = 
                    "R1 = " + this.regR1.getValStr() +
                    ", R2 = " + this.regR2.getValStr() +
                    ", IC = " + this.regIC.getValStr();
            return str;
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

	public FRegister4B getRegPLR() {
		return regPLR;
	}

	public void setRegPLR(FRegister4B regPLR) {
		this.regPLR = regPLR;
	}

	public FRegister4B getRegSP() {
		return regSP;
	}

	public void setRegSP(FRegister4B regSP) {
		this.regSP = regSP;
	}

	public FRegisterLogic getRegMODE() {
		return regMODE;
	}

	public void setRegMODE(FRegisterLogic regMODE) {
		this.regMODE = regMODE;
	}

	public FRegisterInterrupt getRegPI() {
		return regPI;
	}

	public void setRegPI(FRegisterInterrupt regPI) {
		this.regPI = regPI;
	}

	public FRegisterInterrupt getRegSI() {
		return regSI;
	}

	public void setRegSI(FRegisterInterrupt regSI) {
		this.regSI = regSI;
	}

	public FRegisterInterrupt getRegIOI() {
		return regIOI;
	}

	public void setRegIOI(FRegisterInterrupt regIOI) {
		this.regIOI = regIOI;
	}

	public FRegisterLogic getRegK1() {
		return regK1;
	}

	public void setRegK1(FRegisterLogic regK1) {
		this.regK1 = regK1;
	}

	public FRegisterLogic getRegK2() {
		return regK2;
	}

	public void setRegK2(FRegisterLogic regK2) {
		this.regK2 = regK2;
	}

	public FRegisterLogic getRegK3() {
		return regK3;
	}

	public void setRegK3(FRegisterLogic regK3) {
		this.regK3 = regK3;
	}

	public FRegisterInterrupt getRegTIME() {
		return regTIME;
	}

	public void setRegTIME(FRegisterInterrupt regTIME) {
		this.regTIME = regTIME;
	}

}
