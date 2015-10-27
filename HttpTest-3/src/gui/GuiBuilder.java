package gui;

import java.io.IOException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
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
import com.corvolution.mesana.configurator.AddressData;
import com.corvolution.mesana.configurator.ConfigSensor;
import com.corvolution.mesana.configurator.Measurement;
import com.corvolution.mesana.configurator.MeasurementCollection;
import com.corvolution.mesana.configurator.SensorCollection;
import com.corvolution.mesana.configurator.SensorData;

public class GuiBuilder {
	
	private String login, password;
	Display display;
	Shell shell;
	Label listLabel,customerLabel,sensorLabel,commentLabel, measLabel,measTaskLabel, sensorTaskLabel;	
	List customerList;	
	StyledText customerText, commentText, measurTaskText,sensorTaskText ,sensorText, measureText;	
	Text statusBar;	
	Button updateButton, configButton;	
	Combo priorityCombo, electrodeCombo;	
	MeasurementCollection mCollect;	
	SensorCollection sCollect;	
	Measurement mObject;	
	AddressData aData;	
	SensorData sData;

	// fist Constructor
	public GuiBuilder() {

	}

	// second Cosnsructor
	public GuiBuilder(String log, String pass) throws IOException {
		setOperatorData(log, pass);
		setGui();
		if (ConfigSensor.checkSensorConnected()) {
			// initialize customer data and SensorData to Gui if sensor
			// connected
			statusBar.setText("Sensor connected Successfully");
			setCustomerData();
			setSensorData();
		}

		setListeners();

		shell.pack();
		setShell();

		// background Thread for updating Gui depending on Sensor connection
		Thread updateThread = new Thread() {
			public void run() {
				while (true) {
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							// listens if sensor disconnected or connected
							if (!ConfigSensor.checkSensorConnected()) {
								resetData();
								statusBar.setText("Please connect Sensor!");
								if (sensorText.getText().equals("") && ConfigSensor.checkSensorConnected()) {
									try {
										updateGui();
										statusBar.setText("Sensor connected Succussfully");

									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}

							} else if (ConfigSensor.checkSensorConnected() && !shell.isVisible())
								setShell();

						}

					});

				}
			}
		};

		updateThread.setDaemon(true);
		updateThread.start();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}

	public void setGui() {
		display = new Display();
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setText("Configurator");
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
		electrodeCombo.setItems(new String[] {"Filter by Electrode", "Simple", "Test", "New" });
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
				if(index >=0 && index <= mCollect.measList.size()){
					measureText.setText(mCollect.measList.get(index).getMeasurementData());
					mObject = mCollect.measList.get(index);
					aData = new AddressData(mObject.getID());
					customerText.setText(aData.getCustomerData());
				}
				
			}

		});

		// register listener for combo selection
		priorityCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				if (priorityCombo.getText().equals("HIGH")) {
					customerList.removeAll();
					mCollect.highPriorityfilter();
					for(Measurement mObject:mCollect.measList){
       				aData = new AddressData(mObject.getID());
       				customerList.add(aData.guiAddressData());  
       				
       			 } 
					statusBar.setText("List filtered according High priority");
					
				} else if (priorityCombo.getText().equals("MEDIUM")) {
					customerList.removeAll();
					mCollect.mediumPriorityfilter();
					for(Measurement mObject:mCollect.measList){
       				aData = new AddressData(mObject.getID());
       				customerList.add(aData.guiAddressData());
       				
       			 } 
					statusBar.setText("List filtered according Medium priority");
					
				} else if(priorityCombo.getText().equals("LOW")) {
					customerList.removeAll();
					mCollect.lowPriorityfilter();
					for(Measurement mObject:mCollect.measList){
       				aData = new AddressData(mObject.getID());
       				customerList.add(aData.guiAddressData());
       				
				} 
					statusBar.setText("List filtered according Low priority");
			} 
			}
		});

		// register listener for the electrode combo
		electrodeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

			}

		});

		// delete all data on gui, update list and set text fields to first
		// selected item
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
					// save text in database
					String text = commentText.getText();
					ConfigSensor.streamCopy();
					if (ConfigSensor.checkConfigState()) {
						statusBar.setText("Sensor is configurated!");
					} else {
						statusBar.setText("Configuration is failed");
					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

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

	public void setCustomerData() {
		mCollect = new MeasurementCollection();
		mCollect.getMethod(null);

		for (int i=0 ; i<mCollect.measList.size(); i++ ) {
			mObject = mCollect.measList.get(i);
			
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

	public void setSensorData() throws IOException {
		if (ConfigSensor.checkSensorConnected()) {
			SensorCollection sCollect = new SensorCollection();
			sCollect.getMethod(null);
			String sID = ConfigSensor.readConfigFile("E:/SensorInfo.txt").substring(5, 10).trim();

			for (SensorData sData : sCollect.getList()) {
				if (sData.getID().equalsIgnoreCase(sID))
					sensorText.setText(sData.getSensorData());
			}

		}

	}

	public void setShell() {
		if (ConfigSensor.checkSensorConnected())
			shell.open();
	}

	public void resetData() {
		customerList.removeAll();
		customerText.setText("");
		sensorText.setText("");
		measureText.setText("");
		measurTaskText.setText("");
		sensorTaskText.setText("");

	}

	public void updateGui() throws IOException {
		// resetData();
		setCustomerData();
		setSensorData();
	}
	
	public void setOperatorData(String login, String password) {
		this.login = login;
		this.password = password;
	}

}
