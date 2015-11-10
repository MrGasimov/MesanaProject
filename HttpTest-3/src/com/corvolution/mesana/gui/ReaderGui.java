package com.corvolution.mesana.gui;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.corvolution.cm2.ConnectionListener;
import com.corvolution.cm2.ConnectionManager;
import com.corvolution.cm2.Sensor;
import com.corvolution.mesana.configurator.ConfigSensor;

public class ReaderGui {
	int destSize, copySize,size;
	Display display;
	Shell shell;
	Button button;
	ProgressBar bar;
	GridData gridData;
	Text text;
	File source1, dest1,source2, dest2,compare;
	Thread listen; 
	
	public ReaderGui(){
		display = new Display();
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setText("Sensor Reader");
		GridLayout layout = new GridLayout(1, false);
		shell.setLayout(layout);
				
		text = new Text(shell, SWT.READ_ONLY | SWT.SINGLE | SWT.BORDER);
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
		
		
		
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				text.setText("Reading data from sensors...");
				destSize=(int)FileUtils.sizeOf(new File("Z:/measurementData"));
				for(Sensor device:ConnectionManager.getConnectedSensorsList()){																
					device.readMeasurementFromSensor(dest, measurementName);		
					copySize =(int)FileUtils.sizeOf(new File("Z:/measurementData"));
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
			
		listen = new Thread(new Runnable(){			
			public void run() {
				while(true){
					Display.getDefault().asyncExec(new Runnable() {
					    public void run() {	
					    	if(!button.isFocusControl()){
					    		if(ConnectionManager.getState() || ConnectionManager.getCounter()!= 0){				    				
						    		text.setText(ConnectionManager.getCounter()+" sensors connected");							    		
						    	}else{
						    		text.setText("Please connect sensors");
						    		bar.setSelection(0);						    
						    	}
					    	}
					    		
						    
					    	
						}
					});	
				}
				
			}
			
		});
		listen.start();
								
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	
	
	

}
