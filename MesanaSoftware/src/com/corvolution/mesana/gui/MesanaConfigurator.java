package com.corvolution.mesana.gui;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Cursor;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.json.simple.JSONObject;
import com.corvolution.cm2.configuration.ConfigurationInterface_v1_0;
import com.corvolution.cm2.configuration.ConfigurationSet;
import com.corvolution.cm2.configuration.ConfigurationSets;
import com.corvolution.cm2.configuration.SensorConfiguration;
import com.corvolution.cm2.configuration.StartMode;
import com.corvolution.cm2.configuration.StartModes;
import com.corvolution.cm2.connection.ConnectionEvent;
import com.corvolution.cm2.connection.ConnectionManager;
import com.corvolution.cm2.connection.DisconnectionEvent;
import com.corvolution.mesana.configurator.Constants;
import com.corvolution.mesana.configurator.Printer;
import com.corvolution.mesana.configurator.PropertyManager;
import com.corvolution.mesana.data.AddressData;
import com.corvolution.mesana.data.Measurement;
import com.corvolution.mesana.data.MeasurementCollection;
import com.corvolution.mesana.data.SensorCollection;
import com.corvolution.mesana.data.SensorData;
import com.corvolution.mesana.data.TaskCollection;

public class MesanaConfigurator
{
	private int messageCode;
	private static String login;
	private static String password;
	private static Display display;
	private static Shell shell;
	private Label listLabel, customerLabel, sensorLabel, commentLabel, measLabel, measTaskLabel, sensorTaskLabel;
	private static StyledText customerText, commentText, measurTaskText, sensorTaskText, sensorText, measureText;
	private static Text statusBar;
	private static Combo priorityCombo, electrodeCombo;
	private static List customerList;
	private static Button updateButton, configButton;
	private static MeasurementCollection mCollect;
	private static SensorCollection sCollect;
	private TaskCollection tCollect;
	private static Measurement mObject;
	private static AddressData aData;
	public boolean configState = false;
	public boolean shellCheck;

	// Constructor
	public MesanaConfigurator()
	{ 
		setGui();
		if (ConnectionManager.getInstance().connectionState)
		{
			// initialize customer data and SensorData to GUI if sensor connected
			try
			{
				setData();
				statusBar.setText("Sensor " + ConnectionManager.getInstance().currentSensor(0).getSensorPath()
						+ " has been connected successfully!");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			for(SensorData element :sCollect.getList())
			{	
				System.out.println("for ok");
				if(element.getID().equals(ConnectionManager.getInstance().currentSensor(0).getSerialNumber()))
						{
					System.out.println("first if ok");
							if(!element.getState().equals(Constants.SENSOR_STATE_STOCK))
							{
								System.out.println("second if ok");
								configButton.setEnabled(false);
							}
									
						}
			}
		}
		setGuiListeners();
		shell.pack();

		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}

	// Method for updating gui depending on connection state
	public static void connection(ConnectionEvent e)
	{
		Display.getDefault().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				if (e.getState() && !shell.isVisible() && e.getNumOfConnectedSensors() == 1)
				{
					boolean check = false;
					InputDialog opdialog = new InputDialog(shell, SWT.DIALOG_TRIM);
					while (!check)
					{
						String credential = opdialog.createDialogArea();
						login = credential.substring(0, credential.indexOf(File.separator));
						password = credential.substring(credential.indexOf(File.separator));
						check = true;
					}
					
					configButton.setEnabled(true);
					try
					{
						setData();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}

					statusBar.setText("Sensor " + e.getSensorPath() + " has been connected Succussfully");
				}
				else if (e.getState() && shell.isVisible() && e.getNumOfConnectedSensors() == 1)
				{
					configButton.setEnabled(true);
					try
					{
						setData();

					}
					catch (IOException e)
					{

						e.printStackTrace();
					}
					statusBar.setText("Sensor " + e.getSensorPath() + " has been connected Succussfully");
					configButton.setEnabled(true);
				}
				else if (e.getNumOfConnectedSensors() >= 2)
				{
					configButton.setEnabled(false);
					statusBar.setText("Multiple Sensors connected.Please remove all except one sensor");
				}
				

			}

		});
	}

	public static void disconnection(DisconnectionEvent e)
	{
		Display.getDefault().asyncExec(new Runnable(){

			@Override
			public void run()
			{
				if(!e.getState() && e.getNumOfConnectedSensors()==0)
				{
					resetGuiData();
					configButton.setEnabled(false);
					statusBar.setText("Sensor " + e.getSensorPath() + " has been disconnected. Please connect sensor for configuration");
				} else if (!e.getState() && shell.isVisible() && e.getNumOfConnectedSensors() == 1)
				{
					configButton.setEnabled(true);
					resetGuiData();
					try
					{
						setData();

					}
					catch (IOException e)
					{

						e.printStackTrace();
					}
					statusBar.setText("Only Sensor " + ConnectionManager.getInstance().currentSensor(0).getSensorPath()
							+ " has been connected");
				} else if (e.getState())
				{
					statusBar.setText("Please connect any sensor for configuration!");
					resetGuiData();
				}
			}
			
		});
	}

	public void setGui()
	{
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
		priorityCombo.setItems(new String[] {"Filter by Priority", "HIGH", "MEDIUM", "LOW"});
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

		// center group
		Group groupCenter = new Group(shell, SWT.SHADOW_OUT);
		GridLayout groupLayout = new GridLayout(2, false);
		groupCenter.setLayout(groupLayout);
		groupCenter.setText("Measurement group");

		customerLabel = new Label(groupCenter, SWT.NONE);
		customerLabel.setText("Customer Info");
		customerLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));

		// Measurement Label
		measLabel = new Label(groupCenter, SWT.NONE);
		measLabel.setText("Measurement Info");
		measLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1));

		// customerText
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
		commentText = new StyledText(groupCenter, SWT.BORDER | SWT.MULTI);
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
		measurTaskText = new StyledText(groupCenter, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI);
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

		// right group
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

	public int batteryWarning()
	{
		MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
		dialog.setText("Battery status");
		dialog.setMessage("Sensor battery is below minimum. Do you want proceed configuration?");
		int returnCode = dialog.open();
		System.out.println(returnCode);
		return returnCode;
	}

	public void setGuiListeners()
	{
		customerList.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				int index = customerList.getSelectionIndex();
				if (index >= 0 && index <= mCollect.getList().size())
				{
					measureText.setText(mCollect.getList().get(index).getMeasurementData());
					aData = new AddressData(mCollect.getMesID(index));
					customerText.setText(aData.getCustomerData());
					try
					{
						setTaskData(mCollect.getMesID(index));
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}

		});

		priorityCombo.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{

				if (priorityCombo.getText().equals("HIGH"))
				{
					customerList.removeAll();
					mCollect.highPriorityfilter();
					for (Measurement mObject : mCollect.getList())
					{
						aData = new AddressData(mObject.getID());
						if (mObject.getPriority().equals("HIGH"))
						{
							customerList.add("+ " + aData.guiAddressData());
						}
						else if (mObject.getPriority().equals("MEDIUM"))
						{
							customerList.add("- " + aData.guiAddressData());
						}
						else
						{
							customerList.add("o " + aData.guiAddressData());
						}

					}
					statusBar.setText("List filtered according High priority");

				}
				else if (priorityCombo.getText().equals("MEDIUM"))
				{
					customerList.removeAll();
					mCollect.mediumPriorityfilter();
					for (Measurement mObject : mCollect.getList())
					{
						aData = new AddressData(mObject.getID());
						if (mObject.getPriority().equals("HIGH"))
						{
							customerList.add("+ " + aData.guiAddressData());
						}
						else if (mObject.getPriority().equals("MEDIUM"))
						{
							customerList.add("- " + aData.guiAddressData());
						}
						else
						{
							customerList.add("o " + aData.guiAddressData());
						}

					}
					statusBar.setText("List filtered according Medium priority");

				}
				else if (priorityCombo.getText().equals("LOW"))
				{
					customerList.removeAll();
					mCollect.lowPriorityfilter();
					for (Measurement mObject : mCollect.getList())
					{
						aData = new AddressData(mObject.getID());
						if (mObject.getPriority().equals("HIGH"))
						{
							customerList.add("+ " + aData.guiAddressData());
						}
						else if (mObject.getPriority().equals("MEDIUM"))
						{
							customerList.add("- " + aData.guiAddressData());
						}
						else
						{
							customerList.add("o " + aData.guiAddressData());
						}

					}
					statusBar.setText("List filtered according Low priority");
				}
			}
		});

		// register listener for the electrode combo
		electrodeCombo.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				if (electrodeCombo.getText().equals("Simple"))
				{
					restApiUpdate("Simple", "electrode");
				}
				else if (electrodeCombo.getText().equals("Test"))
				{
					restApiUpdate("Test", "electrode");
				}
				else if (electrodeCombo.getText().equals("New"))
				{
					restApiUpdate("New", "electrode");
				}
				else
				{

				}
			}

		});

		// delete all data on gui, update list and set text fields to first selected item
		updateButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				resetGuiData();
				setCustomerData();
				try
				{
					setSensorData();
					statusBar.setText("List updated successfully");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
		});

		// register listener for the selection event
		configButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (ConnectionManager.getInstance().currentSensor(0).getBatteryVoltage() < 3.8)
				{
					messageCode = batteryWarning();
					if (messageCode == 32)
					{
						configurateSensor();
						configButton.setEnabled(false);
					}

				}
				else
				{
					configurateSensor();
					configButton.setEnabled(false);

				}
			}
		});

		shell.addShellListener(new ShellAdapter()
		{
			public void shellDeactivated(ShellEvent shellEvent)
			{
				shellCheck = false;
				Timer timer = new Timer();
				timer.schedule(new TimerTask()
				{
					@Override
					public void run()
					{
						Display.getDefault().asyncExec(new Runnable()
						{
							@Override
							public void run()
							{
								while (!shellCheck)
								{
									statusBar.setText("Operator access requesting.....");
									InputDialog opdialog = new InputDialog(shell, SWT.DIALOG_TRIM);
									opdialog.createDialogArea();
									statusBar.setText("You have logged in as " + login);
								}
							}
						});

					}

				}, 600000);

			}

			public void shellActivated(ShellEvent arg1)
			{
				shellCheck = true;

			}
		});

	}

	public void printLabel(String address, String linkId)
	{
		Printer.getInstance().addParameter("Address", address);
		Printer.getInstance().addParameter("LinkId", linkId);
		Printer.getInstance().print();

	}

	// para1 is state for measurement update , para2 which method run
	public void restApiUpdate(String para1, String para2)
	{
		String state = null;
		for (SensorData element : sCollect.getList())
		{
			if (ConnectionManager.getInstance().currentSensor(0).getSerialNumber() == element.getID())
			{
				state = element.getState();
			}
		}

		String mId = measureText.getText().substring(4, 17);
		if (para2.equals("comment"))
		{
			JSONObject jsonComment = new JSONObject();
			jsonComment.put("comment", commentText.getText());
			jsonComment.put("user", login);
			jsonComment.put("sensorId", ConnectionManager.getInstance().currentSensor(0).getSerialNumber());
			String commentJSON = jsonComment.toJSONString();
			String cURL = PropertyManager.getInstance().getProperty("REST_PATH") + "measurements/" + mId + "/comments";

			try
			{
				mObject.postMethod(commentJSON, cURL);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (para2.equals("electrode"))
		{
			JSONObject jsonElectrode = new JSONObject();
			jsonElectrode.put("comment", para1);
			jsonElectrode.put("user", login);
			String electrodeJSON = jsonElectrode.toJSONString();
			String eURL = PropertyManager.getInstance().getProperty("REST_PATH") + "measurements/" + mId
					+ "/electrodes";

			try
			{
				mObject.postMethod(electrodeJSON, eURL);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (para2.equals("firmware"))
		{
			JSONObject jsonFirmware = new JSONObject();
			jsonFirmware.put("firmware", "1.32.4");
			jsonFirmware.put("user", login);
			String firmwareJson = jsonFirmware.toJSONString();
			String fURL = PropertyManager.getInstance().getProperty("REST_PATH") + "sensors/" + para1;

			try
			{
				mObject.putMethod(firmwareJson, fURL);
			}
			catch (Exception e)
			{

				e.printStackTrace();
			}

		}
		else if (para2.equals("state1"))
		{
			JSONObject jsonState = new JSONObject();
			jsonState.put("state", para1);
			jsonState.put("user", login);
			String stateJSON = jsonState.toJSONString();
			String sURL = PropertyManager.getInstance().getProperty("REST_PATH") + "measurements/" + mId + "/";

			try
			{
				mObject.putMethod(stateJSON, sURL);
			}
			catch (Exception e)
			{

				e.printStackTrace();
			}
		}
		else
		{
			JSONObject jsonState = new JSONObject();
			jsonState.put("state", para1);
			jsonState.put("user", login);
			jsonState.put("sensorId", ConnectionManager.getInstance().currentSensor(0).getSerialNumber());
			String stateJSON = jsonState.toJSONString();
			String sURL = PropertyManager.getInstance().getProperty("REST_PATH") + "measurements/" + mId + "/";

			try
			{
				mObject.putMethod(stateJSON, sURL);
			}
			catch (Exception e)
			{

				e.printStackTrace();
			}

		}

	}

	private void configurateSensor()
	{
		// change state of measurement in RestApi
		restApiUpdate("CONFIGURING", "state1");
		checkFirmwareVersion();
		// Write configuration file to sensor
		setAndWriteFiles();

		// push comment to RestApi
		restApiUpdate(ConnectionManager.getInstance().currentSensor(0).getSerialNumber(), "comment");

		// change state of measurement in RestApi
		restApiUpdate("SENSOR_OUTBOX", "state2");

		// print address
		int index2 = customerList.getSelectionIndex();
		for (Measurement element : mCollect.getList())
		{
			if (mCollect.getList().get(index2).getLinkId() == element.getLinkId())
			{
				aData = new AddressData(element.getID());
				printLabel(aData.getCustomerData(), element.getLinkId());
			}

		}

		// set configuration success message
		statusBar.setText("Sensor is configurated.Please connect another sensor.");

		// writing date to sensor for synchronization
		 ConnectionManager.getInstance().currentSensor(0).synchronize();

		// resetData();
		 ConnectionManager.getInstance().currentSensor(0).disconnect();

	}

	private void setAndWriteFiles()
	{
		SensorConfiguration config = new SensorConfiguration();
		String versionMajor = ConfigurationInterface_v1_0.VERSION.substring(0,
				ConfigurationInterface_v1_0.VERSION.indexOf('.'));
		String versionMinor = ConfigurationInterface_v1_0.VERSION
				.substring(ConfigurationInterface_v1_0.VERSION.indexOf('.') + 1);
		config.setConfigurationInterfaceVersion(versionMajor, versionMinor);

		StartModes startModes = new StartModes();
		// set startMode for sensorConfiguration
		for (StartMode element : startModes.getStartModeList())
		{
			if (element.getName().equals("DEFINED_TIME"))
			{
				config.setStartMode(element);
			}
		}

		ConfigurationSets configSets = new ConfigurationSets();
		// set configurationSet for sensorConfiguration
		for (ConfigurationSet element : configSets.getConfigSetList())
		{
			if (element.getName().equals("mesana"))
			{

				config.setConfigurationSet(element);
			}
		}

		if (config.getStartMode().getName().equals("DEFINED_TIME"))
		{

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 10);
			calendar.set(Calendar.HOUR_OF_DAY, 5);
			calendar.set(Calendar.MINUTE, 11);
			Date date = calendar.getTime();
			config.setRecordingStartTime(date);
		}
		else
		{
			config.setRecordingStartTime(null);
		}
		
		config.setRecordingDuration(12000);
		
		ConnectionManager.getInstance().currentSensor(0).setSensorConfiguration(config);
		ConnectionManager.getInstance().currentSensor(0).writeConfigFile();

		// write Encrypted data to sensor
		ConnectionManager.getInstance().currentSensor(0).writeEncryptedParameters();

		int index = customerList.getSelectionIndex();
		if (index >= 0 && index <= mCollect.getList().size())
		{
			String linkId = mCollect.getList().get(index).getLinkId();
			config.addParameter("LinkId", linkId);
		}
		// write custom data to additional file in sensor
		ConnectionManager.getInstance().currentSensor(0).writeCustomFile();

	}

	public void setTrayIcon()
	{
		// image for tray icon
		Image image = new Image(display, PropertyManager.getInstance().getProperty("ICON_FOLDER"));
		final Tray tray = display.getSystemTray();

		if (tray == null)
		{
			System.out.println("The system tray is not available");
		}
		else
		{
			final TrayItem item = new TrayItem(tray, SWT.NONE);
			item.setToolTipText(PropertyManager.getInstance().getProperty("SOFT_INFO"));

			final Menu menu = new Menu(shell, SWT.POP_UP);

			MenuItem mi1 = new MenuItem(menu, SWT.PUSH);
			MenuItem mi2 = new MenuItem(menu, SWT.PUSH);
			MenuItem mi3 = new MenuItem(menu, SWT.PUSH);
			MenuItem mi4 = new MenuItem(menu, SWT.PUSH);

			mi1.setText("Show");
			mi1.addListener(SWT.Selection, new Listener()
			{
				public void handleEvent(Event event)
				{
					shell.setVisible(true);
					System.out.println("selection " + event.widget);
				}
			});

			mi2.setText("Hide");
			mi2.addListener(SWT.Selection, new Listener()
			{
				public void handleEvent(Event event)
				{
					shell.setVisible(false);
					System.out.println("selection " + event.widget);
				}
			});

			mi3.setText("Change Operator");
			mi3.addListener(SWT.Selection, new Listener()
			{
				public void handleEvent(Event event)
				{
					login = "";
					password = "";
					InputDialog opDialog = new InputDialog(shell, SWT.DIALOG_TRIM);
					shell.setEnabled(!shell.getEnabled());
					shell.setCursor(new Cursor(display, SWT.CURSOR_WAIT));
					String credential = opDialog.createDialogArea();
					login = credential.substring(0, credential.indexOf(File.separator));
					password = credential.substring(credential.indexOf(File.separator));
					shell.setEnabled(true);
					shell.setCursor(new Cursor(display, SWT.CURSOR_ARROW));
				}
			});

			mi4.setText("Close");
			mi4.addListener(SWT.Selection, new Listener()
			{
				public void handleEvent(Event event)
				{
					System.exit(0);
					System.out.println("selection " + event.widget);
				}
			});

			item.addListener(SWT.MenuDetect, new Listener()
			{
				public void handleEvent(Event event)
				{
					menu.setVisible(true);
				}
			});

			item.setImage(image);
		}
	}

	public static void setCustomerData()
	{
		String sURL = PropertyManager.getInstance().getProperty("REST_PATH") + "measurements?state=WAIT_FOR_CONFIG";
		mCollect = new MeasurementCollection();
		mCollect.setList(sURL);

		for (int i = 0; i < mCollect.getList().size(); i++)
		{
			mObject = mCollect.getList().get(i);
			String mID = mObject.getID();
			aData = new AddressData(mID);
			if (mObject.getPriority().equals("HIGH"))
			{
				customerList.add("+ " + aData.guiAddressData());
			}
			else if (mObject.getPriority().equals("MEDIUM"))
			{
				customerList.add("- " + aData.guiAddressData());
			}
			else
			{
				customerList.add("o " + aData.guiAddressData());
			}

		}
		customerText.setText(aData.getCustomerData());
		measureText.setText(mObject.getMeasurementData());

	}

	public static void setSensorData() throws IOException
	{
		String sURL = PropertyManager.getInstance().getProperty("REST_PATH") + "sensors??state="+Constants.SENSOR_STATE_STOCK;
		sCollect = new SensorCollection();
		sCollect.setList(sURL);
		for (SensorData sData : sCollect.getList())
		{
			if (sData.getID().equals(ConnectionManager.getInstance().currentSensor(0).getSerialNumber()))
			{
				sensorText.setText(sData.getSensorData() + "\r\n" + "Device: "
						+ (ConnectionManager.getInstance().currentSensor(0).getDeviceName() + "\r\n" + "Manufacture: "
								+ ConnectionManager.getInstance().currentSensor(0).getManufacturerName() + "\r\n"
								+ "FlashDate: " + ConnectionManager.getInstance().currentSensor(0).getFlashDate()
								+ "\r\n" + "Battery: "
								+ ConnectionManager.getInstance().currentSensor(0).getBatteryVoltage()));
				break;
			}

		}

	}

	public void setTaskData(String mID) throws IOException
	{
		tCollect = new TaskCollection(mID, ConnectionManager.getInstance().currentSensor(0).getSerialNumber());
		if (tCollect.getMeasTask().isEmpty())
		{
			measurTaskText.setText(" ");
		}
		else
		{
			for (int i = 0; i < tCollect.getMeasTask().size(); i++)
			{
				measurTaskText.setText(tCollect.getMeasTask().get(i).getTaskDetails());
			}

		}
		// if sensor task is set skip
		if (!sensorTaskText.equals(""))
		{
			for (int i = 0; i < tCollect.getSensorTask().size(); i++)
			{
				sensorTaskText.setText(tCollect.getSensorTask().get(i).getTaskDetails());
			}
		}

	}

	public void openShell()
	{
		if (ConnectionManager.getInstance().connectionState)
			shell.open();
	}

	public static void resetGuiData()
	{
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

	public static void setData() throws IOException
	{
		setCustomerData();
		setSensorData();

	}

	public void checkFirmwareVersion()
	{
		for (SensorData sensorData : sCollect.getList())
		{
			if (sensorData.getID().equals(ConnectionManager.getInstance().currentSensor(0).getSerialNumber()))
			{
				if (!ConnectionManager.getInstance().currentSensor(0).getFirwareVersion()
						.equals(sensorData.getFirmware()))
				{
					restApiUpdate(ConnectionManager.getInstance().currentSensor(0).getSerialNumber(), "firmware");
				}
				break;
			}

		}

	}

}
