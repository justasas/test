package fluffy.machine.registers;

public class FRegisterInterrupt {

	/**
	 * Register value
	 */
	byte value;
	
	/**
	 * Constructor
	 */
	public FRegisterInterrupt() {
		value = 0;
	}

	/**
	 * @return the value
	 */
	public byte getValue() {
		return value;
	}

	/**
	 * @param i the value to set
	 */
	public void setValue(int i) {
		this.value = (byte) i;
	}
	
	/**
	 * @param i the value to set
	 */
	public void setValue(byte i) {
		this.value = i;
	}

}
