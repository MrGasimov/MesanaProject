package com.corvolution.mesana.gui;

import java.io.IOException;
import java.util.HashMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.json.simple.JSONObject;
import com.corvolution.cm2.ConnectionManager;
import com.corvolution.cm2.Sensor;
import com.corvolution.cm2.SensorEvent;
import com.corvolution.mesana.configurator.ConfigSensor;
import com.corvolution.mesana.configurator.PropertyManager;
import com.corvolution.mesana.data.AddressData;
import com.corvolution.mesana.data.Measurement;
import com.corvolution.mesana.data.MeasurementCollection;
import com.corvolution.mesana.data.SensorCollection;
import com.corvolution.mesana.data.SensorData;
import com.corvolution.mesana.data.TaskCollection;

public class ConfigGui {
	
	private String login, password;
	private Display display;
	private static Shell shell;
	private Label listLabel,customerLabel,sensorLabel,commentLabel, measLabel,measTaskLabel, sensorTaskLabel;
	private static StyledText customerText,commentText,measurTaskText, sensorTaskText, sensorText,measureText;
	private static Text statusBar;
	private static Combo priorityCombo, electrodeCombo;
	private static List customerList;			
	private Button updateButton, configButton;	
	private static MeasurementCollection mCollect;	
	private SensorCollection sCollect;	
	private TaskCollection tCollect;
	private static Measurement mObject;	
	private static AddressData aData;	
	private SensorData sData;	
	private PropertyManager pManager;

	// Constructor
	public ConfigGui(String log, String pass) {
		pManager = new PropertyManager();
		setOperatorData(log, pass);
		setGui();
		if (ConnectionManager.state) {
			// initialize customer data and SensorData to GUI if sensor connected		
			statusBar.setText("Sensor connected Successfully");
			setCustomerData();
			try {
				setSensorData();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}

		setListeners();
		shell.pack();
		setShell();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}

	
	//Method for updating gui depending on connection state
	public static void update(SensorEvent e)
	{
		Display.getDefault().asyncExec(new Runnable(){					
			@Override
			public void run() {
				if(e.getState() && !shell.isVisible() && e.getNumOfConnectedSensors()==1){
					try {
						setData();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					statusBar.setText("Sensor "+e.getSensorPath()+" has been connected Succussfully");
					setShell();
				}else if(e.getState() && shell.isVisible() && e.getNumOfConnectedSensors()==1){
					try {
						setData();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					statusBar.setText("Sensor "+e.getSensorPath()+" has been connected Succussfully");
				}else if(!e.getState()&& shell.isVisible() && e.getNumOfConnectedSensors()==1){
					statusBar.setText("Only Sensor "+e.getSensorPath()+" has been connected");
				}else if(e.getNumOfConnectedSensors()>=2){
					statusBar.setText("Multiple Sensors connected.Please remove all except single one");
				}else{
					resetData();
					statusBar.setText("Sensor "+e.getSensorPath()+" has been disconnected");
				}
				
			}
			
		});
	}
	public void setGui() {
		display = new Display();
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setText("Sensor Configurator");
		// create a new GridLayout with 3 columns of same size
		GridLayout layout = new GridLayout(3, false);
		shell.setLayout(layout);

		setTrayIcon();
		
		Group groupLeft = new Group(shell, SWT.SHADOW_OUT);
		groupLeft.setLayout(new GridLayout());
		groupLeft.setText("Customer Group");
		
		listLabel = new Label(groupLeft, SWT.NONE);
		listLabel.setText("List of Customers");
		listLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		
		customerList = new List(groupLeft, SWT.BORDER | SWT.V_SCROLL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.BEGINNING;
		gridData.heightHint = 207;
		gridData.widthHint = 160;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		customerList.setLayoutData(gridData);
		//customerList.select();
		// customerList.showSelection();
		
		// Combo selection
		priorityCombo = new Combo(groupLeft, SWT.READ_ONLY);
		priorityCombo.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		priorityCombo.setItems(new String[] {"Filter by Priority", "HIGH", "MEDIUM", "LOW" });
		priorityCombo.select(0);
		
		// updateButton
		updateButton = new Button(groupLeft, SWT.PUSH);
		updateButton.setText("Update List");
		gridData = new GridData();
		gridData.verticalAlignment = GridData.CENTER;
		gridData.horizontalAlignment = GridData.CENTER;
		gridData.widthHint = 100;
		gridData.heightHint = 30;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		updateButton.setLayoutData(gridData);
		
		
		//center group
		Group groupCenter = new Group(shell, SWT.SHADOW_OUT);
		GridLayout groupLayout = new GridLayout(2,false);
		groupCenter.setLayout(groupLayout);
		groupCenter.setText("Measurement group");
		
		customerLabel = new Label(groupCenter, SWT.NONE);
		customerLabel.setText("Customer Info");
		customerLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		
		// Measurement Label
		measLabel = new Label(groupCenter, SWT.NONE);
		measLabel.setText("Measurement Info");
		measLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1));
		
		//customerText
		customerText = new StyledText(groupCenter, SWT.BORDER | SWT.V_SCROLL | SWT.READ_ONLY | SWT.MULTI);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.BEGINNING;
		gridData.heightHint = 135;
		gridData.widthHint = 90;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		customerText.setLayoutData(gridData);
		
		// Measurement Text
		measureText = new StyledText(groupCenter, SWT.BORDER | SWT.V_SCROLL | SWT.READ_ONLY | SWT.MULTI);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.BEGINNING;
		gridData.heightHint = 135;
		gridData.widthHint = 150;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		measureText.setLayoutData(gridData);
		
		// Comment Label
		commentLabel = new Label(groupCenter, SWT.NONE);
		commentLabel.setText("Comment Here!");
		commentLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
	
		// Measurement Task Label
		measTaskLabel = new Label(groupCenter, SWT.NONE);
		measTaskLabel.setText("Measurement Tasks!");
		measTaskLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		
		// Comment Text
		commentText = new StyledText(groupCenter, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		gridData.verticalAlignment = GridData.BEGINNING;
		gridData.heightHint = 80;
		gridData.widthHint = 100;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		commentText.setLayoutData(gridData);

		// Measurement Task Text
		measurTaskText = new StyledText(groupCenter, SWT.BORDER | SWT.V_SCROLL | SWT.READ_ONLY | SWT.MULTI);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		gridData.verticalAlignment = GridData.BEGINNING;
		gridData.heightHint = 80;
		gridData.widthHint = 150;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		measurTaskText.setLayoutData(gridData);
		
		// Electrode selection
		electrodeCombo = new Combo(groupCenter, SWT.READ_ONLY);
		electrodeCombo.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 2, 1));
		electrodeCombo.setItems(new String[] {"Electrode", "Simple", "Test", "New" });
		electrodeCombo.select(0);		
		
		//right group 
		Group groupRight = new Group(shell, SWT.SHADOW_OUT);
		groupRight.setLayout(new GridLayout());
		groupRight.setText("Sensor group");

		sensorLabel = new Label(groupRight, SWT.NONE);
		sensorLabel.setText("Sensor Info");
		sensorLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));

		sensorText = new StyledText(groupRight, SWT.BORDER | SWT.V_SCROLL | SWT.READ_ONLY | SWT.MULTI);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.BEGINNING;
		gridData.heightHint = 130;
		gridData.widthHint = 150;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		sensorText.setLayoutData(gridData);
		
		// sensor Task label
		sensorTaskLabel = new Label(groupRight, SWT.NONE);
		sensorTaskLabel.setText("Sensor Tasks");
		sensorTaskLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		
		sensorTaskText = new StyledText(groupRight, SWT.BORDER | SWT.V_SCROLL | SWT.READ_ONLY | SWT.MULTI);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		gridData.verticalAlignment = GridData.BEGINNING;
		gridData.heightHint = 80;
		gridData.widthHint = 150;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		sensorTaskText.setLayoutData(gridData);
		
		// configButton
		configButton = new Button(groupRight, SWT.PUSH);
		configButton.setText("Start Configure");
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		gridData.verticalAlignment = GridData.CENTER;
		gridData.widthHint = 100;
		gridData.heightHint = 30;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		configButton.setLayoutData(gridData);

		statusBar = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.SINGLE);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 4;
		gridData.verticalSpan = 1;
		statusBar.setLayoutData(gridData);

	}

	public void setListeners() {

		// register listener for customerList.
		customerList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int index = customerList.getSelectionIndex();				
				if(index >=0 && index <= mCollect.getList().size()){
					measureText.setText(mCollect.getList().get(index).getMeasurementData());
					aData = new AddressData(mCollect.getMesID(index));
					customerText.setText(aData.getCustomerData());
					try {
						setTaskData(mCollect.getMesID(index));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}

		});

		// register listener for combo selection
		priorityCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				if (priorityCombo.getText().equals("HIGH")) {
					customerList.removeAll();
					mCollect.highPriorityfilter();
					for(Measurement mObject:mCollect.getList()){
       				aData = new AddressData(mObject.getID());
       				if(mObject.getPriority().equals("HIGH")){				
       					customerList.add("+ "+aData.guiAddressData());
       				}else if(mObject.getPriority().equals("MEDIUM")){
       					customerList.add("- "+aData.guiAddressData());
       				}else {
       					customerList.add("o "+aData.guiAddressData());
       				}
       				
       				
       			 } 
					statusBar.setText("List filtered according High priority");
					
				} else if (priorityCombo.getText().equals("MEDIUM")) {
					customerList.removeAll();
					mCollect.mediumPriorityfilter();
					for(Measurement mObject:mCollect.getList()){
       				aData = new AddressData(mObject.getID());
       				if(mObject.getPriority().equals("HIGH")){				
       					customerList.add("+ "+aData.guiAddressData());
       				}else if(mObject.getPriority().equals("MEDIUM")){
       					customerList.add("- "+aData.guiAddressData());
       				}else {
       					customerList.add("o "+aData.guiAddressData());
       				} 
       				
       			 } 
					statusBar.setText("List filtered according Medium priority");
					
				} else if(priorityCombo.getText().equals("LOW")) {
					customerList.removeAll();
					mCollect.lowPriorityfilter();
					for(Measurement mObject:mCollect.getList()){
       				aData = new AddressData(mObject.getID());
       				if(mObject.getPriority().equals("HIGH")){				
       					customerList.add("+ "+aData.guiAddressData());
       				}else if(mObject.getPriority().equals("MEDIUM")){
       					customerList.add("- "+aData.guiAddressData());
       				}else {
       					customerList.add("o "+aData.guiAddressData());
       				}
       				
				} 
					statusBar.setText("List filtered according Low priority");
			} 
			}
		});

		// register listener for the electrode combo
		electrodeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				if(electrodeCombo.getText().equals("Simple")){
					restApiUpdate("Simple","electrode");
				}else if(electrodeCombo.getText().equals("Test")){
					restApiUpdate("Test","electrode");
				}else if(electrodeCombo.getText().equals("New")){
					restApiUpdate("New","electrode");	
				}else{
					
				}
			}

		});

		// delete all data on gui, update list and set text fields to first selected item
		updateButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				resetData();
				setCustomerData();
				try {
					setSensorData();					
					statusBar.setText("List updated successfully");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		// register listener for the selection event
		configButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					//push comment to RestApi
					restApiUpdate(null,"comment");												
					//Write configuration file to sensor
					//ConnectionManager.currentSensor(0).writeConfigFile(ConnectionManager.currentSensor(0).getSensorPath()+":/config2.txt");					
					//change state of measurement in RestApi
					restApiUpdate("CONFIGURING","state");
					Thread.sleep(3000);
					restApiUpdate("SENSOR_OUTBOX", "state");										
					statusBar.setText("Sensor is configurated.Please connect another sensor.");					
					//statusBar.setText("Configuration is failed");				
					Thread.sleep(4000);
					statusBar.setText("Reseting GUI..");	
					resetData();															
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 

			}
		});

	}
	
	
	//para1 is state for measurement update , para2 which method run
	public void restApiUpdate(String para1, String para2){		
	
		String mId = measureText.getText().substring(4,17);	
			
		switch(para2){
			case "comment":
				JSONObject jsonComment = new JSONObject();
				jsonComment.put("comment",commentText.getText());
				jsonComment.put("user", login);
				jsonComment.put("sensorId",ConnectionManager.currentSensor(0).getSerialNumber());
				String commentJSON = jsonComment.toJSONString();
				String cURL =pManager.getProperty("REST_PATH")+"measurements/"+mId+"/comments";			
				try {
					mObject.postMethod(commentJSON,cURL);
				}catch (Exception e){			
					e.printStackTrace();
				}
			case "electrode":
				JSONObject jsonElectrode = new JSONObject();
				jsonElectrode.put("comment",para1);
				jsonElectrode.put("user", login);
				String electrodeJSON = jsonElectrode.toJSONString();
				String eURL =pManager.getProperty("REST_PATH")+"measurements/"+mId+"/electrodes";	
				try {
					mObject.postMethod(electrodeJSON,eURL);
				}catch (Exception e){			
					e.printStackTrace();
				}
			case "state":
				JSONObject jsonState = new JSONObject();
				jsonState.put("state", para1);
				jsonState.put("user", login);
				jsonState.put("sensorId",ConnectionManager.currentSensor(0).getSerialNumber());
				String stateJSON = jsonState.toJSONString();
				String sURL =pManager.getProperty("REST_PATH")+"measurements/"+mId+"/";	
				try {
					mObject.putMethod(stateJSON,sURL);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
		}		
	}
	
	public void setTrayIcon() {
		// image for tray icon
		Image image = new Image(display, "C:/Users/gasimov/Documents/Repo/HttpTest-3/res/images/bulb.gif");
		final Tray tray = display.getSystemTray();

		if (tray == null) {
			System.out.println("The system tray is not available");
		} else {
			final TrayItem item = new TrayItem(tray, SWT.NONE);
			item.setToolTipText("Mesana TrayItem");
			item.addListener(SWT.Show, new Listener() {
				public void handleEvent(Event event) {

					System.out.println("show");
				}
			});
			item.addListener(SWT.Hide, new Listener() {
				public void handleEvent(Event event) {

					System.out.println("hide");
				}
			});
			item.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {

				}
			});
			item.addListener(SWT.DefaultSelection, new Listener() {
				public void handleEvent(Event event) {

				}
			});

			final Menu menu = new Menu(shell, SWT.POP_UP);

			MenuItem mi1 = new MenuItem(menu, SWT.PUSH);
			MenuItem mi2 = new MenuItem(menu, SWT.PUSH);
			MenuItem mi3 = new MenuItem(menu, SWT.PUSH);

			mi1.setText("Show");
			mi1.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					System.out.println("selection " + event.widget);
				}
			});

			mi2.setText("Hide");
			mi2.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					System.out.println("selection " + event.widget);
				}
			});

			mi3.setText("About");
			mi3.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {

					System.out.println("selection " + event.widget);
				}
			});

			// menu.setDefaultItem(mi1);

			item.addListener(SWT.MenuDetect, new Listener() {
				public void handleEvent(Event event) {
					menu.setVisible(true);
				}
			});

			item.setImage(image);
		}
	}

	public static void setCustomerData() {
		mCollect = new MeasurementCollection();
		mCollect.setList();

		for (int i=0 ; i<mCollect.getList().size(); i++ ) {
			mObject = mCollect.getList().get(i);			
			String mID = mObject.getID();
			aData = new AddressData(mID);
			if(mObject.getPriority().equals("HIGH")){				
					customerList.add("+ "+aData.guiAddressData());
				}else if(mObject.getPriority().equals("MEDIUM")){
					customerList.add("- "+aData.guiAddressData());
				}else {
					customerList.add("o "+aData.guiAddressData());
				}
			
		}
		customerText.setText(aData.getCustomerData());
		measureText.setText(mObject.getMeasurementData());

	}
	
	
	public static void setSensorData() throws IOException {
		if (ConnectionManager.state) {
			SensorCollection sCollect = new SensorCollection();
			sCollect.setList();			
			for (SensorData sData : sCollect.getList()) {				
				if (sData.getID().equalsIgnoreCase(ConnectionManager.currentSensor(0).getSerialNumber()));
							sensorText.setText(sData.getSensorData()+
							"\r\n"+(ConnectionManager.currentSensor(0).getDeviceName()+
							"\r\n"+ConnectionManager.currentSensor(0).getManufacturerName()+
							"\r\n"+ConnectionManager.currentSensor(0).getFlashDate()+
							"\r\n"+ConnectionManager.currentSensor(0).getSensorVoltage()));
					
			}

		}

	}
	
	public void setTaskData(String mID) throws IOException{
		tCollect = new TaskCollection(mID ,ConnectionManager.currentSensor(0).getSerialNumber());		
		if(tCollect.getMeasTask().isEmpty()){			
			measurTaskText.setText(" ");
		}else{
			for(int i=0; i<tCollect.getMeasTask().size(); i++){
				measurTaskText.setText(tCollect.getMeasTask().get(i).getTaskDetails());	
			}
						
		}
		//if sensortask is set skip
		if(!sensorTaskText.equals("")){
			for(int i=0; i<tCollect.getSensorTask().size(); i++){
				sensorTaskText.setText(tCollect.getSensorTask().get(i).getTaskDetails());
				}
		}
		
	}
	
	public static void setShell() {
		if (ConnectionManager.state)
			shell.open();
	}

	public static void resetData() {
		priorityCombo.select(0);
		electrodeCombo.select(0);
		customerList.removeAll();
		customerText.setText("");
		commentText.setText("");
		sensorText.setText("");
		measureText.setText("");
		measurTaskText.setText("");
		sensorTaskText.setText("");

	}

	public static void setData() throws IOException {
		setCustomerData();
		setSensorData();
		
	}
	
	public void setOperatorData(String login, String password) {
		this.login = login;
		this.password = password;
	}

}
