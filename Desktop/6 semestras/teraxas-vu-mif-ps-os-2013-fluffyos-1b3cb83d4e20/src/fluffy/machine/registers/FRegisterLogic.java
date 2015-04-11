package fluffy.machine.registers;

/**
 * Logical (true/false) register
 * @author karolis
 */
public class FRegisterLogic {
	
	/**
	 * Register value
	 */
	private boolean value;
	
	/**
	 * Default constructor
	 */
	public FRegisterLogic() {
		this(true);
	}
	
	/**
	 * Constructor
	 * Initializes register with initial value 
	 * @param initVal initial value
	 */
	public FRegisterLogic(boolean initVal) {
		this.value = initVal;
	}

	/**
	 * Returns current register value
	 * @return register value
	 */
	public boolean isValue() {
		return value;
	}

	/**
	 * Set new register value
	 * @param value new value
	 */
	public void setValue(boolean value) {
		this.value = value;
	}

}
