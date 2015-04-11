package fluffy.machine.registers;

public class FRegisterLogicByte {

	/**
	 * Zero flag
	 */
	boolean flagZero;

	/**
	 * Sign flag
	 */
	boolean flagSign;
	
	/**
	 * Overflow flag
	 */
	boolean flagOverflow;
	
	/**
	 * Default constructor
	 */
	public FRegisterLogicByte() {
		resetFlags();
	}

	public void resetFlags() {
		this.flagOverflow = false;
		this.flagSign = false;
		this.flagZero = false;
	}
	
	/**
	 * @return the flagZero
	 */
	public boolean isFlagZero() {
		return flagZero;
	}

	/**
	 * @return the flagSign
	 */
	public boolean isFlagSign() {
		return flagSign;
	}

	/**
	 * @return the flagOverflow
	 */
	public boolean isFlagOverflow() {
		return flagOverflow;
	}

	/**
	 * @param flagZero the flagZero to set
	 */
	public void setFlagZero(boolean flagZero) {
		this.flagZero = flagZero;
	}

	/**
	 * @param flagSign the flagSign to set
	 */
	public void setFlagSign(boolean flagSign) {
		this.flagSign = flagSign;
	}

	/**
	 * @param flagOverflow the flagOverflow to set
	 */
	public void setFlagOverflow(boolean flagOverflow) {
		this.flagOverflow = flagOverflow;
	}
	
	
}
