/**
 * 
 */
package fluffy.machine;

import java.lang.reflect.Array;

import fluffy.os.FluffyOS;


/**
 * @author karolis
 *
 */
public class FMemory {

	/**
	 * Size in blocks
	 */
	private static final int DEFAULT_MEMORY_SIZE = 50;
	
	/**
	 * Memory blocks
	 */
	private FMemBlock[] blocks;
	
	/**
	 * Default constructor
	 */
	public FMemory() {
		this(DEFAULT_MEMORY_SIZE);
	}
	
	/**
	 * Constructor
	 * Inits memory with setted amount of blocks
	 * @param size amount of blocks
	 */
	public FMemory(int size) {
		blocks = new FMemBlock[size];
		/*for (@SuppressWarnings("unused") FMemBlock bl : blocks) {
			bl = new FMemBlock();
		}*/
		for (int i = 0; i < size; i++){
			blocks[i] = new FMemBlock();
		}
	}

	/**
	 * Returns memory block at index
	 * @param index block index
	 * @return memory block
	 */
	public FMemBlock getBlockAtIdx(int index){
		return blocks[index];
	}
	
	/**
	 * Returns word at address
	 * @param address address of word
	 * @return word at address
	 */
	public FWord getWordAtAddress(int address){
		int block = address / 10;
		int word = address % 10;
		try{
			return this.blocks[block].getWordAtIdx(word);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(e);
			System.out.println("   " + address);
			return null;
		}

		
	}
	
	/**
	 * Returns an unused memory block index
	 * @return unused memory block index, -1 if full.
	 */
	public int getFreeBlockIdx(){
		int i;
		for (i = 0; i < Array.getLength(blocks); i++) {
			if( blocks[i].isUsed() == false){
				return i;
			}
		}
		return -1; //-1 full
	}
	
	/**
	 * Get some free fluffy blocks - they are soooooo cute ^-^
	 * @param blocksNeeded amount of fluffies needed
	 * @return array of fluffies
	 */
	public int[] getFreeBlocksArray(int blocksNeeded){
		int tmp;
		int[] results = new int[blocksNeeded];
		for(int i = 0; i < blocksNeeded; i++){
			tmp = this.getFreeBlockIdx();
			if (tmp == -1){
				FluffyOS.printStuff("FMem: ERROR - not enough memory");
				return null; //not enough memory
			} else {
				this.blocks[tmp].setUsed(true);
				results[i] = tmp;
			}
		}
		return results;
	}
	
	/**
	 * Prints whole memory to syso
	 */
	public void printMemory() {
            System.out.println(toString());
	}
        
        @Override
        public String toString(){
            String str = "";
            int addr;
            int j;
            for (int i = 0; i < DEFAULT_MEMORY_SIZE; i++){
                str += "BLOCK: " + i + "\n";
                for (j = 0; j < 10; j++){
                    addr = i * 10 + j;
                    str += addr + ": " + blocks[i].getWordAtIdx(j).getVal() + "\n";
                }
            }
            return str;
        }

	public int freeBlocksCount(){
		int freeCount = 0;
		for (FMemBlock block : blocks) {
			if (!block.isUsed()){
				freeCount++;
			}
		}
		return freeCount;
	}
	
	public boolean isEnoughMem() {
		int freeCount = freeBlocksCount();
		FluffyOS.printStuff("FreeBlocks: " + freeCount);
		if (freeCount > 11)
			return true;
		else
			return false;
	}
	
	public int getSize(){
		return Array.getLength(blocks);
	}

    public FMemBlock[] getBlocks() {
        return blocks;
    }

    public void reset() {
        for (FMemBlock block : blocks) {
            block.setUsed(false);
        }
    }
        
        
}
