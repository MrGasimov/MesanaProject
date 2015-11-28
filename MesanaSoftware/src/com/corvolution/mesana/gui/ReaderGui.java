package com.corvolution.mesana.gui;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.json.simple.JSONObject;

import com.corvolution.cm2.Sensor;
import com.corvolution.cm2.connection.ConnectionManager;
import com.corvolution.cm2.connection.SensorEvent;
import com.corvolution.mesana.configurator.PropertyManager;
import com.corvolution.mesana.data.Measurement;
import com.corvolution.mesana.data.MeasurementCollection;
import com.corvolution.mesana.rest.RestApiConnector;

public class ReaderGui{
	int destSize, copySize,size;
	String readOutDest;
	private static String login=null;
	private static String password=null;
	private Display display;
	private static Shell shell;
	private Button button;
	private static ProgressBar bar;
	private GridData gridData;
	private static Text text;
	MeasurementCollection mCollect;
	private String measurementName;
	RestApiConnector restApi;
	
	public ReaderGui(){
		restApi = new RestApiConnector();
		mCollect = new MeasurementCollection();
		String url = PropertyManager.getInstance().getProperty("REST_PATH")+"/measurements?state=ANALYZING";
		mCollect.setList(url);		
				
		display = new Display();		
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setText("Mesana CM200 Data Reader");
		GridLayout layout = new GridLayout(1, false);
		shell.setLayout(layout);
		text = new Text(shell, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;		
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.heightHint=50;
		gridData.widthHint = 100;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		text.setLayoutData(gridData);
		
		bar = new ProgressBar(shell, SWT.SMOOTH);
		button = new Button(shell,SWT.PUSH);
		button.setText("Readout");
		gridData = new GridData();
		gridData.verticalAlignment = GridData.CENTER;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.widthHint = 100;
		gridData.heightHint = 30;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		button.setLayoutData(gridData);
		
		setTrayIcon();
		text.setText("Please connect Sensor!");
		destSize=(int)FileUtils.sizeOf(new File("Z:/measurementData/"));
	
		
		readOutDest = PropertyManager.getInstance().getProperty("READOUT_DEST");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				text.setText("Reading data from sensors...");
				if(!ConnectionManager.getInstance().getConnectedSensorsList().isEmpty()){
					for(Sensor device:ConnectionManager.getInstance().getConnectedSensorsList()){
						for(Measurement element :mCollect.getList()){						
							if(element.getLinkId().equals(device.getReadConfiguration().getParameter("LinkId"))){
								measurementName = element.getID();
								restApiUpdate(element.getLinkId(),measurementName);
							}
							
						}
						device.readMeasurement(readOutDest+measurementName);		
						copySize =(int)FileUtils.sizeOf(new File("Z:/measurementData/"));
						size = (int) ConnectionManager.getInstance().measurementDataSize("all");
						bar.setSelection((((copySize-destSize)/size)*100));										
						if(bar.getMaximum()==size)						
							break;									
					}
				}								
				text.setText("All sensors were read out!");
				text.setFocus();				
			}
		});
		
		shell.pack();
		//shell.open();			
				
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
				if(e.getState() && e.getNumOfConnectedSensors()==1){
					operatorDialog();
					str = "Sensor "+e.getSensorPath()+" has been connected";
					text.setText(str);
				}else if(e.getState()&&e.getNumOfConnectedSensors()>=2){
					
					for(int i=0; i < e.getNumOfConnectedSensors()-1;i++)
					text.setText(text.getText()+"\r\n"+"Sensor "+e.getSensorPath()+" has been connected");				
				}else if(e.getState()&&e.getNumOfConnectedSensors()==0){
					text.setText("All Sensors has been disconnected");
					bar.setSelection(0);
				}
				
			}
			
		});
	}
	
	public void restApiUpdate(String deviceNumber, String mId){
		JSONObject json = new JSONObject();
		json.put("state","SENSOR_READOUT");
		json.put("user", login);
		json.put("sensorId",deviceNumber);
		String jsonString = json.toJSONString();		
		String sURL =PropertyManager.getInstance().getProperty("REST_PATH")+"measurements/"+mId+"/";			
		try {
			restApi.putMethod(jsonString,sURL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void setTrayIcon() {
		// image for tray icon
		Image image = new Image(display, "C:/Users/gasimov/Documents/Repo/MesanaSoftware/res/images/bulb.gif");
		final Tray tray = display.getSystemTray();

		if (tray == null) {
			System.out.println("The system tray is not available");
		} else {
			final TrayItem item = new TrayItem(tray, SWT.NONE);
			item.setToolTipText("Mesana Sensor Reader \r\n Version 1.0.1 \r\n Author Suleyman Gasimov");
			

			final Menu menu = new Menu(shell, SWT.POP_UP);

			MenuItem mi1 = new MenuItem(menu, SWT.PUSH);
			MenuItem mi2 = new MenuItem(menu, SWT.PUSH);
			MenuItem mi4 = new MenuItem(menu, SWT.PUSH);
			
			mi1.setText("Show");
			mi1.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					shell.setVisible(true);
					System.out.println("selection " + event.widget);
				}
			});

			mi2.setText("Hide");
			mi2.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					shell.setVisible(false);
					System.out.println("selection " + event.widget);
				}
			});

						
			mi4.setText("Close");
			mi4.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					System.exit(0);
					System.out.println("selection " + event.widget);
				}
			});

			

			item.addListener(SWT.MenuDetect, new Listener() {
				public void handleEvent(Event event) {
					menu.setVisible(true);
				}
			});

			item.setImage(image);
		}
	}
	
	public static void operatorDialog(){
		boolean check = false;
		InputDialog opdialog = new InputDialog(shell, SWT.DIALOG_TRIM);
		while(!check)
		{	
			String credential = opdialog.createDialogArea();
			login = credential.substring(0, credential.indexOf(File.separator));
			password = credential.substring(credential.indexOf(File.separator));
			check = true;
		}
	}
	
	
	
	
	

}
