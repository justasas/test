package fluffy.os;

import fluffy.os.FluffyOS.ResName;

public class FResourceDescriptor {

	private int intId;
	private ResName extId;
	private boolean reusable;
	private FluffyOS os;
	
	private Object component;
	
	private FProcess user;
	
	private FProcess creatorProcess;

	public FResourceDescriptor(int intId, ResName extId, FluffyOS os, 
			FProcess creatorProcess, boolean reusable,
			Object component) {
		this.intId = intId;
		this.extId = extId;
		this.reusable = reusable;
		this.os = os;
		this.creatorProcess = creatorProcess;
		this.setComponent(component);
	}

	public FluffyOS getOS() {
		return os;
	}

	public void setOS(FluffyOS os) {
		this.os = os;
	}
	
	public FProcess getCreatorProcess() {
		return creatorProcess;
	}

	public void setCreatorProcess(FProcess creatorProcess) {
		this.creatorProcess = creatorProcess;
	}
	public int getIntId() {
		return intId;
	}

	public void setIntId(int intId) {
		this.intId = intId;
	}

	public ResName getExtId() {
		return extId;
	}

	public void setExtId(ResName extId) {
		this.extId = extId;
	}

	public FluffyOS getOs() {
		return os;
	}

	public void setOs(FluffyOS os) {
		this.os = os;
	}

	public boolean isReusable() {
		return reusable;
	}

	public void setReusable(boolean reusable) {
		this.reusable = reusable;
	}

	public Object getComponent() {
		return component;
	}

	public void setComponent(Object component) {
		this.component = component;
	}

	public FProcess getUser() {
		return user;
	}

	public void setUser(FProcess user) {
		this.user = user;
	}
}
