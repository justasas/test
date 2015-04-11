package fluffy.machine;

/**
 * The "F" word
 * @author karolis
 */
public class FWord implements Cloneable{

	/**
	 * Default word size
	 */
	public static final int DEFAULT_WORD_SIZE = 4;
	
	/**
	 * Word size
	 */
	private int size;
	
	/**
	 * Word data
	 */
	private String value;
	
	/**
	 * Default constructor
	 */
	public FWord() {
		this(DEFAULT_WORD_SIZE);
	}
	
	/**
	 * Constructor
	 * Initializes Word with size
	 * @param value
	 */
	public FWord(int size) {
		this.setSize(size);
		this.value = "";
	}

	public FWord cloneFWord() throws CloneNotSupportedException{
		FWord clone = (FWord) this.clone();
		return clone;
	}
	
	/**
	 * @return data string
	 */
	public String getVal() {
		return value;
	}
	
	/**
	 * get integer data
	 * @return integer data
	 */
	public int getValInt() {
		try{
		return Integer.parseInt(this.value, 16);//the string should be hex
		} catch (NumberFormatException e){
			throw new IllegalArgumentException("Wrong type");
		}
	}

	/**
	 * Sets string value
	 * If data is too long - cuts it.
	 * @param value new string value
	 */
	public void setVal(String value) {
		if (value.length() <= this.size){
			this.value = value;
		} else {
			this.value = value.substring(this.size);
		}
	}
	
	/**
	 * Sets int value.
	 * Converts to hex string before setting.
	 * @param value integer value
	 */
	public void setValInt(int value){
		setVal(Integer.toHexString(value));
	}

	/**
	 * Get word size
	 * @return size size of word
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Set word size
	 * @param size new size
	 */
	private void setSize(int size) {
		this.size = size;
		
	}

}
