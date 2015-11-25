package com.corvolution.mesana.gui;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.json.simple.JSONObject;
import com.corvolution.cm2.ConnectionManager;
import com.corvolution.cm2.SensorEvent;
import com.corvolution.mesana.configurator.PropertyManager;
import com.corvolution.mesana.data.AddressData;
import com.corvolution.mesana.data.Measurement;
import com.corvolution.mesana.data.MeasurementCollection;
import com.corvolution.mesana.data.SensorCollection;
import com.corvolution.mesana.data.SensorData;
import com.corvolution.mesana.data.TaskCollection;

public class ConfigGui {
	private int messageCode;
	private String login, password;
	private Display display;
	private static Shell shell;
	private Label listLabel,customerLabel,sensorLabel,commentLabel, measLabel,measTaskLabel, sensorTaskLabel;
	private static StyledText customerText,commentText,measurTaskText, sensorTaskText, sensorText,measureText;
	private static Text statusBar;
	private static Combo priorityCombo, electrodeCombo;
	private static List customerList;			
	private static Button updateButton, configButton;	
	private static MeasurementCollection mCollect;
	private static SensorCollection sCollect;
	private TaskCollection tCollect;
	private static Measurement mObject;	
	private static AddressData aData;	
	private PropertyManager pManager;
	private ConnectionManager cManager;
	boolean configState = false;
	boolean shellCheck ;
	
	// Constructor
	public ConfigGui(String log, String pass) {
		pManager = new PropertyManager();
		cManager = ConnectionManager.getInstance();
		setOperatorData(log, pass);
		setGui();
		if (ConnectionManager.state) {
			// initialize customer data and SensorData to GUI if sensor connected		
			try {
				setData();				
			} catch (IOException e) {				
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
					configButton.setEnabled(true);
					try {
						setData();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					statusBar.setText("Sensor "+e.getSensorPath()+" has been connected Succussfully");
					setShell();
				}else if(e.getState() && shell.isVisible() && e.getNumOfConnectedSensors()==1){
					configButton.setEnabled(true);
					try {
						setData();
						
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					statusBar.setText("Sensor "+e.getSensorPath()+" has been connected Succussfully");
					configButton.setEnabled(true);
				}else if(!e.getState()&& shell.isVisible() && e.getNumOfConnectedSensors()==1){
					configButton.setEnabled(true);
					resetData();
					try {
						setData();
						
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					statusBar.setText("Only Sensor "+ConnectionManager.currentSensor(0).getSensorPath()+" has been connected");
				}else if(e.getNumOfConnectedSensors()>=2){
					configButton.setEnabled(false);
					statusBar.setText("Multiple Sensors connected.Please remove all except one sensor");
				}else{
					resetData();
					configButton.setEnabled(false);
					statusBar.setText("Sensor "+e.getSensorPath()+" has been disconnected. Please connect sensor for configuration");
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
		gridData.heightHint = 220;
		gridData.widthHint = 150;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		customerList.setLayoutData(gridData);
		customerList.select(0);
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
		gridData.heightHint = 145;
		gridData.widthHint = 110;
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
		gridData.heightHint = 145;
		gridData.widthHint = 160;
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
		commentText = new StyledText(groupCenter, SWT.BORDER  | SWT.MULTI);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.BEGINNING;
		gridData.heightHint = 80;
		gridData.widthHint = 110;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		commentText.setLayoutData(gridData);

		// Measurement Task Text
		measurTaskText = new StyledText(groupCenter, SWT.BORDER  | SWT.READ_ONLY | SWT.MULTI);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.BEGINNING;
		gridData.heightHint = 80;
		gridData.widthHint = 160;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		measurTaskText.setLayoutData(gridData);
		
		// Electrode selection
		electrodeCombo = new Combo(groupCenter, SWT.READ_ONLY);
		electrodeCombo.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 2, 1));
		electrodeCombo.setItems(new String[] {"Electrode", "Simple", "Test", "New"});
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
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = 140;
		gridData.widthHint = 180;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		sensorText.setLayoutData(gridData);
		
		// sensor Task label
		sensorTaskLabel = new Label(groupRight, SWT.NONE);
		sensorTaskLabel.setText("Sensor Tasks");
		sensorTaskLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		
		sensorTaskText = new StyledText(groupRight, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		gridData.verticalAlignment = GridData.BEGINNING;
		gridData.heightHint = 80;
		gridData.widthHint = 180;
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
	public int batteryWarning(){
		// create a dialog with ok and cancel buttons and a question icon
		MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK| SWT.CANCEL);
		dialog.setText("Battery status");
		dialog.setMessage("Sensor battery is below minimum. Do you want proceed configuration?");
		// open dialog and await user selection
		int returnCode = dialog.open(); 
		System.out.println(returnCode);
		return returnCode;
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
					if(ConnectionManager.currentSensor(0).getSensorVoltage()<3.8){
						messageCode = batteryWarning();
						if(messageCode==32)
							configurate();
					} else{
						configurate();
						printAddress();
					}																									
			}			
		});
		
		shell.addShellListener(new ShellAdapter(){
			public void shellDeactivated(ShellEvent shellEvent) {
				shellCheck = false;
				System.out.println("shell deactivated");				
				Timer timer = new Timer();
				timer.schedule(new TimerTask(){
					@Override
					public void run() {							
						Display.getDefault().asyncExec(new Runnable(){
							@Override
							public void run() {
								while(!shellCheck){
									statusBar.setText("Operator access requesting.....");
									InputDialog opdialog = new InputDialog (shell, SWT.DIALOG_TRIM);
									opdialog.createDialogArea();																			
									statusBar.setText("You have logged in as "+login);
								}																	
						}						
					});
	    						
				}
		    	  
		      }, 10000);
				
		      
			}			
			public void shellActivated(ShellEvent arg1){
				shellCheck = true;
				System.out.println("Shell activated");
			}
		});
	
	}
	
	
	public class InputDialog extends Dialog{
		private Label userLabel, passwordLabel, msgLabel;
		private Text userField,passwordField;
	    private String userString,passwordString;
		private boolean status;
	    public InputDialog(Shell arg0, int arg1) {

			super(arg0, arg1);
			// TODO Auto-generated constructor stub
		}
	    
	    protected boolean createDialogArea() {
	    	Shell shell = new Shell(getParent(), getStyle());
	    	shell.setText(getText());
			shell.setLayout(new GridLayout(1, false));
			shell.setText("Operator credentials");
			
			Group group = new Group(shell, SWT.SHADOW_OUT);
			group.setLayout(new GridLayout());
			group.setText("Operator Access");
			group.setLayout(new GridLayout(2,false));
			
			msgLabel = new Label(group, SWT.NONE);
			msgLabel.setText("Please enter operator credentials!");
			msgLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
			
			userLabel = new Label(group, SWT.RIGHT);
			userLabel.setText("Login: ");			
			userField =new Text(group, SWT.SINGLE | SWT.BORDER ); 
			
			passwordLabel = new Label(group, SWT.RIGHT);
			passwordLabel.setText("Password: ");			
			passwordField = new Text(group, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);	
			
			GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
			userField.setLayoutData(data);
			passwordField.setLayoutData(data);
			
			Button okButton = new Button(group, SWT.PUSH);
			okButton.setText("OK");
			data = new GridData(SWT.CENTER,SWT.CENTER,false,false,1,1);
			data.heightHint = 30;
			data.widthHint = 60;
			okButton.setLayoutData(data);
			
			Button cancelButton = new Button(group, SWT.PUSH);
			cancelButton.setText("Cancel");
			data = new GridData(SWT.CENTER,SWT.CENTER,false,false,1,1);
			data.heightHint = 30;
			data.widthHint = 60;
			cancelButton.setLayoutData(data);
			
			okButton.addSelectionListener(new SelectionAdapter() {
			      public void widgetSelected(SelectionEvent event) {
			        userString = userField.getText();
			        passwordString = passwordField.getText();
			        if(userString.equals(login)&& passwordString.equals(password)){
			        	shell.close();
			        }else{
			        	userField.setText("");
			        	passwordField.setText("");
			        	msgLabel.setText("Login or password is wrong");
			        }			        
			      }
			    });
			
			cancelButton.addSelectionListener(new SelectionAdapter() {
			      public void widgetSelected(SelectionEvent event) {
			        System.exit(0);
			    	  shell.close();
			      }
			    });
			
			shell.setDefaultButton(okButton);
			
			shell.pack();
			shell.open();
			
			Display display = getParent().getDisplay();
		    while (!shell.isDisposed()) {
		      if (!display.readAndDispatch()) {
		        display.sleep();
		      }
		    }
			return status;
	    }		
	}
	
	
	//para1 is state for measurement update , para2 which method run
	public void restApiUpdate(String para1, String para2){		
	
		String mId = measureText.getText().substring(4,17);	
		if(para2.equals("comment")){
			JSONObject jsonComment = new JSONObject();
			jsonComment.put("comment",commentText.getText());
			jsonComment.put("user", login);
			jsonComment.put("sensorId",ConnectionManager.getInstance().currentSensor(0).getSerialNumber());
			String commentJSON = jsonComment.toJSONString();
			String cURL =pManager.getProperty("REST_PATH")+"measurements/"+mId+"/comments";	
			
			try {
				mObject.postMethod(commentJSON,cURL);
			}catch (Exception e){			
				e.printStackTrace();
			}
		}else if(para2.equals("electrode")){
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
		}else if(para2.equals("firmware")){
			JSONObject jsonFirmware = new JSONObject();
			jsonFirmware.put("firmware","1.32.4");
			jsonFirmware.put("user", login);	
			String firmwareJson = jsonFirmware.toJSONString();
			String fURL =pManager.getProperty("REST_PATH")+"sensors/"+para1;
			
			
			try {
				mObject.putMethod(firmwareJson,fURL);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		
		}else if(para2.equals("state1")) {			
			JSONObject jsonState = new JSONObject();
			jsonState.put("state",para1);
			jsonState.put("user",login);			
			String stateJSON = jsonState.toJSONString();
			String sURL =pManager.getProperty("REST_PATH")+"measurements/"+mId+"/";	
			
			try {
				mObject.putMethod(stateJSON,sURL);
			} catch (Exception e) {
				
				e.printStackTrace();
			}					
		}else {
			JSONObject jsonState = new JSONObject();
			jsonState.put("state",para1);
			jsonState.put("user",login);
			jsonState.put("sensorId",cManager.currentSensor(0).getSerialNumber());
			String stateJSON = jsonState.toJSONString();
			String sURL =pManager.getProperty("REST_PATH")+"measurements/"+mId+"/";	
			
			try {
				mObject.putMethod(stateJSON,sURL);
			} catch (Exception e) {
				
				e.printStackTrace();
			}					
		}
	}
	public void configurate(){
		//change state of measurement in RestApi
		restApiUpdate("CONFIGURING","state1");
		checkFirmwareVersion();
		//Write configuration file to sensor
		//ConnectionManager.currentSensor(0).writeConfigFile();
							
		//push comment to RestApi
		restApiUpdate(ConnectionManager.currentSensor(0).getSerialNumber(),"comment");	
		
		//change state of measurement in RestApi
		restApiUpdate("SENSOR_OUTBOX", "state2");
		//print address
		statusBar.setText("Sensor is configurated.Please connect another sensor.");										
		
		
		//writing date to sensor for synchronization					
		//ConnectionManager.currentSensor(0).synchronize();
							
		//resetData();
		//remove sensor 
		//ConnectionManager.currentSensor(0).disconnect("remove");
	}
	
	private void printAddress() {
		ConnectionManager.currentSensor(0).writeEncryptedParameters();		
	}
	
	public void setTrayIcon() {
		// image for tray icon
		Image image = new Image(display, "C:/Users/gasimov/Documents/Repo/MesanaSoftware/res/images/bulb.gif");
		final Tray tray = display.getSystemTray();

		if (tray == null) {
			System.out.println("The system tray is not available");
		} else {
			final TrayItem item = new TrayItem(tray, SWT.NONE);
			item.setToolTipText("Mesana Configurator \r\n Version 1.0.1 \r\n Author Suleyman Gasimov");
			

			final Menu menu = new Menu(shell, SWT.POP_UP);

			MenuItem mi1 = new MenuItem(menu, SWT.PUSH);
			MenuItem mi2 = new MenuItem(menu, SWT.PUSH);
			MenuItem mi3 = new MenuItem(menu, SWT.PUSH);
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

			mi3.setText("About");
			mi3.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					
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

	public static void setCustomerData() {
		mCollect = new MeasurementCollection();
		String sURL = "http://chili/mk/backend.mesana.com/api/v4/measurements?state=WAIT_FOR_CONFIG";
		mCollect.setList(sURL);

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
	
	
	public static  void setSensorData() throws IOException {
			sCollect = new SensorCollection();
			sCollect.setList();			
			for (SensorData sData : sCollect.getList()) {				
				if (sData.getID().equals(ConnectionManager.currentSensor(0).getSerialNumber())){					
					sensorText.setText(sData.getSensorData()+
					"\r\n"+"Device: "+(ConnectionManager.currentSensor(0).getDeviceName()+
					"\r\n"+"Manufacture: "+ConnectionManager.currentSensor(0).getManufacturerName()+
					"\r\n"+"FlashDate: "+ConnectionManager.currentSensor(0).getFlashDate()+
					"\r\n"+"Battery: "+ConnectionManager.currentSensor(0).getSensorVoltage()));
					break;
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
		//if sensor task is set skip
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

	public static  void setData() throws IOException {
		setCustomerData();
		setSensorData();
		
	}
	
	public void checkFirmwareVersion(){
		for(SensorData sensorData: sCollect.getList()){
			if(sensorData.getID().equals(ConnectionManager.currentSensor(0).getSerialNumber())){
				if(!ConnectionManager.currentSensor(0).getFirwareVersion().equals(sensorData.getFirmware())){
					restApiUpdate(ConnectionManager.currentSensor(0).getSerialNumber(),"firmware");
				}
			break;
			}
			
			
		}
		
	}
	
	public void setOperatorData(String login, String password) {
		this.login = login;
		this.password = password;
	}

}
