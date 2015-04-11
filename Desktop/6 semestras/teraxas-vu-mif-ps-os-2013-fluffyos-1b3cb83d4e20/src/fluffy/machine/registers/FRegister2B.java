package fluffy.machine.registers;

/**
 * 2 Byte register
 * @author karolis
 *
 */
public class FRegister2B extends FRegister4B{

	/**
	 * Register size (bytes)
	 */
	final static int DEFAULT_SIZE = 2;
	
	/**
	 * Default constructor
	 */
	public FRegister2B() {
		super();
	}
	
	/**
	 * Initializes register with initial value
	 * @param initVal initial value
	 */
	public FRegister2B(int initVal){
		super(initVal);
	}
	
	/**
	 * Initializes register with initial value
	 * @param initVal initial value
	 */
	public FRegister2B(String initVal){
		super(initVal);
	}

}
