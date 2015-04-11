package fluffy.machine.registers;

import fluffy.machine.FWord;

/**
 * 4 Byte Data register
 * @author karolis
 */
public class FRegister4B {
	
	/**
	 * Default init register value
	 */
	final static int DEFAULT_INIT_VAL = 0;
	
	/**
	 * Register size (bytes)
	 */
	final static int DEFAULT_SIZE = 4;
	
	/**
	 * Value is a word. 
	 */
	private FWord value;
	
	/**
	 * Default constructor
	 */
	public FRegister4B() {
		this(DEFAULT_INIT_VAL);
	}
	
	/**
	 * Initializes register with initial value
	 * @param initVal initial value
	 */
	public FRegister4B(int initVal){
		this.initFWord(DEFAULT_SIZE);
		this.setValInt(initVal);
	}
	
	/**
	 * Initializes register with initial value
	 * @param initVal initial value
	 */
	public FRegister4B(String initVal){
		this.initFWord(DEFAULT_SIZE);
		this.setValStr(initVal);
	}
	
	protected void initFWord(int size){
		value = new FWord(size);
	}
	
	/**
	 * Sets new register value
	 * @param value new value
	 */
	public void setValInt(int value){
		this.value.setValInt(value);
	}

	/**
	 * Sets new register value
	 * @param valuenew value
	 */
	public void setValStr(String value){
		this.value.setVal(value);
	}
	
	/**
	 * Get register value (String)
	 * @return register value String
	 */
	public String getValStr(){
		return this.value.getVal();
	}
	
	/**
	 * Get register value (int)
	 * @return register value int
	 */
	public int getValInt(){
		return this.value.getValInt();
	}
	
	public FWord getValWord() {
		return value;
	}

}
