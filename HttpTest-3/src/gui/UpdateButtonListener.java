package gui;

import java.io.IOException;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class UpdateButtonListener extends SelectionAdapter {
	
	GuiBuilder g = new GuiBuilder();
	public void widgetSelected(SelectionEvent e){
		g.resetData();
		g.setCustomerData();
		try {
			g.setSensorData();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
