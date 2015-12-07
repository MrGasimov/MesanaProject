package com.corvolution.mesana.gui;

import java.io.File;
import java.io.IOException;

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
import com.corvolution.cm2.SensorNotFoundException;
import com.corvolution.cm2.connection.ConnectionManager;
import com.corvolution.cm2.connection.DisconnectionEvent;
import com.corvolution.cm2.connection.ConnectionEvent;
import com.corvolution.mesana.configurator.Constants;
import com.corvolution.mesana.configurator.PropertyManager;
import com.corvolution.mesana.data.Measurement;
import com.corvolution.mesana.data.MeasurementCollection;
import com.corvolution.mesana.rest.RestApiConnector;

public class ReaderGui
{
	double copySize, size;
	String readOutDest;
	private static String login = null;
	private static String password = null;
	private Display display;
	private static Shell shell;
	private static Button button;
	private static ProgressBar bar;
	private GridData gridData;
	private static Text text;
	MeasurementCollection mCollect;
	private String measurementId;
	RestApiConnector restApi;

	public ReaderGui()
	{
		restApi = new RestApiConnector();
		mCollect = new MeasurementCollection();
		String url = PropertyManager.getInstance().getProperty("REST_PATH") + "/measurements?state="
				+ Constants.MEASUREMENT_STATE_MEASURING;
		mCollect.setList(url);

		setGui();

		setTrayIcon();
		if (!ConnectionManager.getInstance().getConnectedSensorsList().isEmpty())
		{
			text.setText(ConnectionManager.getInstance().getNumberOfConnectedSensors() + " sensor has been connected");
		}
		else
		{
			text.setText("Please connect Sensor!");
		}

		
		readOutDest = PropertyManager.getInstance().getProperty("READOUT_DEST");

		button.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				text.setText("Reading data from sensors...");
				size = (int) ConnectionManager.getInstance().measurementDataSize("all");
				readData();
				
				new Thread()
				{
					public void run()
					{ 
						
						display.asyncExec(new Runnable(){
							public void run()
							{ 	
								boolean state = true;
								while(state)
								{	
									copySize = (double) FileUtils.sizeOf(new File("Z:/measurementData/"));
									System.out.println("copySize "+copySize);
									int i =  (int) ((copySize*100)/size);
									System.out.println("progressbar "+i);
									bar.setSelection(i);
									
									try
									{
										Thread.sleep(1500);
									}
									catch (InterruptedException e)
									{
										e.printStackTrace();
									}
									
									if(copySize == size)
									{	
										state = false;
										text.setText("All sensors have been readout");
										button.setEnabled(false);
									}
								}
							}
						});
					}
				}.start();
			}
		});

		shell.pack();
		operatorDialog();

		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public static void connection(ConnectionEvent cEvent)
	{
		Display.getDefault().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				if (cEvent.getState())
				{	
					button.setEnabled(true);
					text.setText(cEvent.getNumOfConnectedSensors() + " sensors connected");
					System.out.println("sensor " + cEvent.getSensorPath() + " connected");
				}

			}

		});
	}

	public static void disconnection(DisconnectionEvent dEvent)
	{
		Display.getDefault().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				if (!dEvent.getState() && dEvent.getNumOfConnectedSensors() == 0)
				{
					button.setEnabled(false);
					text.setText("All sensors have been disconnected");
					bar.setSelection(0);
					
				}
				else if (!dEvent.getState())
				{
					text.setText(dEvent.getNumOfConnectedSensors() + " sensors connected");
					bar.setSelection(0);
					System.out.println("sensor " + dEvent.getSensorPath() + " disconnected");
				}
			}

		});

	}

	public void restApiUpdate(String deviceNumber, String mId)
	{
		JSONObject json = new JSONObject();
		json.put("state", Constants.MEASUREMENT_STATE_SENSOR_READOUT);
		json.put("user", login);
		json.put("sensorId", deviceNumber);
		String jsonString = json.toJSONString();
		String sURL = PropertyManager.getInstance().getProperty("REST_PATH") + "measurements/" + mId + "/";
		try
		{
			restApi.putMethod(jsonString, sURL);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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

	public void setGui()
	{
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
		gridData.heightHint = 50;
		gridData.widthHint = 100;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;
		text.setLayoutData(gridData);

		bar = new ProgressBar(shell, SWT.SMOOTH);
		bar.setMaximum(100);
		button = new Button(shell, SWT.PUSH);
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

	}

	public static void operatorDialog()
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
	}
	
	public void readData()
	{
		Thread thread = new Thread (new Runnable(){
			@Override
			public void run()
			{
				if (!ConnectionManager.getInstance().getConnectedSensorsList().isEmpty())
				{
					for (Sensor device : ConnectionManager.getInstance().getConnectedSensorsList())
					{
						for (Measurement element : mCollect.getList())
						{
							if (element.getLinkId().equals(device.getReadConfiguration().getParameter("LinkId")))
							{
								measurementId = element.getID();
								restApiUpdate(device.getSerialNumber(), measurementId);
							}
						}
							try
							{
								device.readMeasurement(readOutDest + measurementId);
							}
							catch (SensorNotFoundException e)
							{
								e.printStackTrace();
							}
					}
				}
			}
		});
		thread.start();
	
	}
}
