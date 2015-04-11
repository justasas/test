package fluffy.os;

import fluffy.machine.FMemBlock;
import fluffy.machine.FMemory;
import fluffy.machine.FWord;
import fluffy.machine.registers.FRegister4B;


/**
 * Virtual memory
 * @author karolis
 */
public class VMemory {

	/**
	 * Default virtual memory size
	 */
	public final int DEFAULT_MEMSIZE = 10;
	
	/**
	 * Memory size
	 */
	private int memSize;
	
	/**
	 * Page table register
	 * (ref. to real machine)
	 */
	private FWord PLR;
	
	/**
	 * Real machine memory (reference)
	 */
	private FMemory realMemory;

	public String pName;
	
	/**
	 * constructor
	 * @param real real memory
	 * @param PLR page table register
	 * @throws CloneNotSupportedException 
	 */
	public VMemory(FMemory real, FRegister4B PLR) throws CloneNotSupportedException {
		//this.PLR = PLR.getValWord();
		this.realMemory = real;
		this.memSize = DEFAULT_MEMSIZE;
		this.createTable();
		this.PLR = this.PLR.cloneFWord();
	}

	/**
	 * Creates new page table
	 */
	private void createTable(){
		//We find an unused block for PT:
		this.PLR.setValInt(realMemory.getFreeBlockIdx());
		FMemBlock tableBlock = 
				realMemory.getBlockAtIdx(this.PLR.getValInt());
		tableBlock.setUsed(true);
		//We get an array of addresses of empty blocks
		int[] tableArray = realMemory.getFreeBlocksArray(memSize);
		
		//Now put this table to THE TABLE!!!
		int tableIndex = 0;
		for (int realAddress : tableArray) {
			//NOTE:it might be wise to multiply by 10...
			tableBlock.getWordAtIdx(tableIndex).
				setValInt(realAddress);
			realMemory.getBlockAtIdx(realAddress).setUsed(true);
			
			tableIndex++;
		}
	}
	
	/**
	 * Get word at virtual address
	 * @param address
	 * @return
	 */
	public FWord getWordAtAddress(int address){
		//if (address > (this.memSize * 10 - 1)) return null;
		
		int block = address / 10;
		int word = address % 10;
		
		try{
			return getBlock(block).getWordAtIdx(word);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(e);
			System.out.println("   " + (block * 10 + word));
			return null;
		}
	}
	
	/**
	 * Get block at index (virtualAddress / 10)
	 * @param idx
	 * @return
	 */
	private FMemBlock getBlock(int idx){
		if (idx > this.memSize) return null;
		
		FMemBlock pageTable = 
				realMemory.getBlockAtIdx(this.PLR.getValInt());
		int realBlockAddress = pageTable.getWordAtIdx(idx).getValInt();
		
		return realMemory.getBlockAtIdx(realBlockAddress);
	}
	
	/**
	 * Prints whole VM memory to system out
	 */
	public void printMemory() {
		String str;
		int addr;
		for (int i = 0; i < this.DEFAULT_MEMSIZE; i++){
			str = "BLOCK: " + i;
			System.out.println(str);
			for (int j = 0; j < 10; j++){
				addr = i * 10 + j;
				str = addr + ":" + j + ": " + this.getWordAtAddress(addr).getVal();
				System.out.println(str);
			}
		}
	}

	/**
	 * resets "used" boolean
	 */
	public void returnMem() {
		for (int i = 0; i < this.DEFAULT_MEMSIZE; i++){
			this.getBlock(i).setUsed(false);
		}
		realMemory.getBlockAtIdx(this.PLR.getValInt()).setUsed(false);
	}

	public int getPLR() {
		return this.PLR.getValInt();
	}
	
}
