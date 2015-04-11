package fluffy.os;

import fluffy.os.FluffyOS.ResName;

/**
 * FluffyOS resource class
 * @author karolis
 *
 */
public class FResource {

	public FResourceDescriptor resDesc;
	public static int numberOfInstances;
	private Object component;
	
	public FResource(int intId, ResName extId, FluffyOS os, 
			FProcess creatorProcess, boolean reusable,
			Object component) {
		
		this.component = component;
		this.resDesc = new FResourceDescriptor(
				intId, extId, os, creatorProcess,
				reusable, component);
	}

	public String toString(){
		String tmp = "RES:" + resDesc.getIntId() + 
				":" + resDesc.getExtId();
		if (resDesc.getUser() != null){
			tmp += " [USED:" + resDesc.getUser().pDesc.extId +
					"]";
		}
		if (component == null){
			tmp += " [null]";
		}
		return tmp;
	}
	
	public FResourceDescriptor getResDesc() {
		return resDesc;
	}

	public void setResDesc(FResourceDescriptor resDesc) {
		this.resDesc = resDesc;
	}

	public Object getComponent() {
		return component;
	}

	public void setComponent(Object component) {
		this.component = component;
	}
	
}
