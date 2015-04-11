package fluffy.machine;

import fluffy.devices.InputPanel;
import fluffy.devices.OutputPanel;
import fluffy.gui.IOwindow;



public class FMachine {

	public FCPU cpu;
	public FMemory memory;
        
    //System io devices
    public InputPanel sysInputDevice;
    public OutputPanel sysOutputDevice;
    private IOwindow sysIoWindow;
        
    //User io devices
    public InputPanel usrInputDevice;
    public OutputPanel usrOutputDevice;
    private IOwindow usrIoWindow;
        
	
	/**
	 * Constructor
	 */
	public FMachine() {
		this.cpu = new FCPU();
		this.memory = new FMemory();
                
				//Used by PrintLine, GetLine
                this.usrIoWindow = new IOwindow();
                this.usrInputDevice = usrIoWindow.getInputDevice();
                this.usrOutputDevice = usrIoWindow.getOutputDevice();
                usrIoWindow.setName("VM IO devices");
                usrIoWindow.setTitle("VM IO devices");
                
                this.sysIoWindow = new IOwindow();
                this.sysInputDevice = sysIoWindow.getInputDevice();
                this.sysOutputDevice = sysIoWindow.getOutputDevice();
                sysIoWindow.setName("System IO devices");
                sysIoWindow.setTitle("System IO devices");
                
                
                this.sysIoWindow.setVisible(true);
                this.usrIoWindow.setVisible(true);
	}
	
	public FCPU getCpu() {
		return cpu;
	}
	
	public void setCpu(FCPU cpu) {
		this.cpu = cpu;
	}

	public FMemory getMemory() {
		return memory;
	}

	public void setMemory(FMemory memory) {
		this.memory = memory;
	}

}
