/**
 * 
 */
package fluffy;

import fluffy.gui.MainWindow;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import fluffy.machine.FMachine;
import fluffy.os.FluffyOS;
import javax.swing.SwingUtilities;

/**
 * @author karolis
 * 
 */
public class Main {

	/**
	 * Is test
	 */
	static final boolean TEST = false;

	/**
	 * Sets system look and feel
	 */
	static void initLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
	}

	static void initLogger() {
		Logger fLog = Logger.getLogger("fLog");

		fLog.setLevel(Level.ALL);

		FileHandler fHand = null;
		ConsoleHandler cHand = new ConsoleHandler();
		cHand.setFormatter(new SimpleFormatter());
		fLog.addHandler(cHand);

		try {
			fHand = new FileHandler("logs/flog.log");
			fHand.setFormatter(new SimpleFormatter());
			fLog.addHandler(fHand);
		} catch (SecurityException | IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Fluffy Operating System");
		initLookAndFeel();
		//initLogger();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				FluffyOS os = new FluffyOS(new FMachine());
				MainWindow mw = new MainWindow(os);
				mw.setVisible(true);
			}
		});

	}

}
