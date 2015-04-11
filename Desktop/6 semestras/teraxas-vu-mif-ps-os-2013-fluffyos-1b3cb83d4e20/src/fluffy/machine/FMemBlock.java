package fluffy.machine;



/**
 * Memory block
 * @author karolis
 */
public class FMemBlock {

	/**
	 * Hardcoded block size
	 */
	static final int DEFAULT_BLOCK_SIZE = 10;

	/**
	 * Block as array of Words
	 */
	private FWord[] block;
	
	/**
	 * Is block in use?
	 */
	private boolean used;
	
	/**
	 * Constructor
	 * Initializes Block of setted size
	 * @param size size of block
	 */
	public FMemBlock(int size) {
		this.block = new FWord[size];
		/*for (@SuppressWarnings("unused") FWord wrd : block) {
			wrd = new FWord();
		}*/
		for (int i = 0; i < size; i++){
			block[i] = new FWord();
		}
	}
	
	/**
	 * Default constructor.
	 * Initializes Block of default size
	 */
	public FMemBlock() {
		this(DEFAULT_BLOCK_SIZE);
	}
	
	/**
	 * Get word by index
	 * @param index Word index in the block
	 * @return Word at index
	 */
	public FWord getWordAtIdx(int index) {
		return block[index];
	}

	/**
	 * @return the used
	 */
	public boolean isUsed() {
		return used;
	}

	/**
	 * @param used the used to set
	 */
	public void setUsed(boolean used) {
		this.used = used;
	}

    public FWord[] getBlock() {
        return block;
    }
}
