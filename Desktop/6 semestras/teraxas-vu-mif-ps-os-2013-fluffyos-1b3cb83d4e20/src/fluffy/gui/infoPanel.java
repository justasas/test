/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fluffy.gui;

import fluffy.os.FProcess;
import fluffy.os.FResource;
import fluffy.os.FluffyOS;

/**
 * 
 * @author karolis
 */
public class infoPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = -5003848485384357068L;

	/**
	 * Creates new form infoPanel
	 */
	public infoPanel() {
		initComponents();
	}

	public void update(FluffyOS os) {
		procArea.setText(os.realMachine.cpu.toString());

		String resListString = "";
		for (FResource res : os.resources) {
			resListString += res.toString() + "\n";
		}
		resArea.setText(resListString);

		String procListString = "";
		for (FProcess proc : os.processes) {
			procListString += proc.toString() + "\n";
		}
		procArea.setText(procListString);

		memArea.setText(os.realMachine.memory.toString());
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		procArea = new javax.swing.JTextArea();
		jScrollPane2 = new javax.swing.JScrollPane();
		resArea = new javax.swing.JTextArea();
		jScrollPane3 = new javax.swing.JScrollPane();
		regArea = new javax.swing.JTextArea();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jScrollPane4 = new javax.swing.JScrollPane();
		memArea = new javax.swing.JTextArea();
		jLabel4 = new javax.swing.JLabel();

		procArea.setColumns(20);
		procArea.setRows(5);
		jScrollPane1.setViewportView(procArea);

		resArea.setColumns(20);
		resArea.setRows(5);
		jScrollPane2.setViewportView(resArea);

		regArea.setColumns(20);
		regArea.setRows(5);
		jScrollPane3.setViewportView(regArea);

		jLabel1.setText("Registers:");

		jLabel2.setText("Resources:");

		jLabel3.setText("Processes:");

		memArea.setColumns(20);
		memArea.setRows(5);
		jScrollPane4.setViewportView(memArea);

		jLabel4.setText("Memory:");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.TRAILING,
												false)
												.addComponent(
														jScrollPane3,
														javax.swing.GroupLayout.Alignment.LEADING,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														455, Short.MAX_VALUE)
												.addComponent(
														jScrollPane2,
														javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														jLabel1,
														javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														jLabel2,
														javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														jLabel3,
														javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														jScrollPane1,
														javax.swing.GroupLayout.Alignment.LEADING))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														jScrollPane4,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														0, Short.MAX_VALUE)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		jLabel4)
																.addGap(0,
																		67,
																		Short.MAX_VALUE)))));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jLabel3)
												.addComponent(jLabel4))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING,
												false)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		jScrollPane1,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		177,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		jLabel2)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		jScrollPane2,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		141,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		jLabel1)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		jScrollPane3,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		73,
																		javax.swing.GroupLayout.PREFERRED_SIZE))
												.addComponent(jScrollPane4))));
	}// </editor-fold>//GEN-END:initComponents
		// Variables declaration - do not modify//GEN-BEGIN:variables

	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JTextArea memArea;
	private javax.swing.JTextArea procArea;
	private javax.swing.JTextArea regArea;
	private javax.swing.JTextArea resArea;
	// End of variables declaration//GEN-END:variables
}