package com.corvolution.mesana.gui;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import org.apache.commons.io.FileUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.corvolution.cm2.UsbListener;
import com.corvolution.cm2.ConnectionManager;
import com.corvolution.cm2.Sensor;
import com.corvolution.cm2.SensorEvent;
import com.corvolution.cm2.SensorListener;
import com.corvolution.mesana.configurator.ConfigSensor;
import com.corvolution.mesana.configurator.GuiUpdater;
import com.corvolution.mesana.configurator.PropertyManager;
import com.corvolution.mesana.data.Measurement;
import com.corvolution.mesana.data.MeasurementCollection;
import com.corvolution.mesana.rest.RestApiConnector;

public class ReaderGui{
	int destSize, copySize,size;
	String readOutDest;
	private String login,password;
	private Display display;
	private Shell shell;
	private Button button;
	private ProgressBar bar;
	private GridData gridData;
	private static Text text;
	PropertyManager pManager;
	ConnectionManager manager;
	MeasurementCollection mCollect;
	private String measurementName;
	
	public ReaderGui(String login,String password){
		this.login = login;
		this.password = password;
		mCollect = new MeasurementCollection();
		mCollect.setList();
		pManager = new PropertyManager();
		
		display = new Display();		
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setText("Sensor Reader");
		GridLayout layout = new GridLayout(1, false);
		shell.setLayout(layout);
			
		text = new Text(shell, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;		
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		text.setLayoutData(gridData);
		
		bar = new ProgressBar(shell, SWT.SMOOTH);
		button = new Button(shell,SWT.PUSH);
		button.setText("Readout");
		gridData = new GridData();
		gridData.verticalAlignment = GridData.CENTER;
		gridData.horizontalAlignment = GridData.CENTER;
		gridData.widthHint = 100;
		gridData.heightHint = 30;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		button.setLayoutData(gridData);
		text.setText("Please connect Sensor!");
		destSize=(int)FileUtils.sizeOf(new File("Z:/measurementData/"));
		;
		
		readOutDest = pManager.getProperty("READOUT_DEST");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				text.setText("Reading data from sensors...");				
				for(Sensor device:ConnectionManager.getConnectedSensorsList()){
					for(Measurement element :mCollect.getList()){
						System.out.println(device.getConfiguration());
						element.getLinkId().equals(device.getConfiguration().getLinkId());
						measurementName = element.getID();
					};
					device.readMeasurementFromSensor(readOutDest+measurementName);		
					copySize =(int)FileUtils.sizeOf(new File("Z:/measurementData/"));
					size = (int) ConnectionManager.measurementDataSize("all");
					bar.setSelection((((copySize-destSize)/size)*100));										
					if(bar.getMaximum()==size)						
						break;									
				}
				text.setText("All sensors were read out!");
				text.setFocus();
				
			}
		});
		
		shell.pack();
		shell.open();			
				
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	
	public static void update(SensorEvent e)
	{
		Display.getDefault().asyncExec(new Runnable(){					
			@Override
			public void run() {
				String str="";
				if(e.getState()){					
					str = "Sensor "+e.getSensorPath()+" has been connected";
					text.setText(str);
				}else if(e.getState()&&e.getNumOfConnectedSensors()>=2){
					str = str.concat("\r\n"+"Sensor "+e.getSensorPath()+" has been connected");
					text.setText(str);
				}else{
					text.setText("Sensor "+e.getSensorPath()+" has been disconnected");
				}
				
			}
			
		});
	}

	
	
	
	
	
	
	

}
