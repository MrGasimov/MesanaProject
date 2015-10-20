package gui;
import java.io.IOException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
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
		Label listLabel, customerLabel, sensorLabel, commentLabel, measLabel, measTaskLabel, sensorTaskLabel; 
	    List customerList;
	    Text customerText, sensorText, measureText, commentText, measurTaskText, sensorTaskText;
	    Button updateButton, configButton;
	    Combo priorityCombo, electrodeCombo;
	    MeasurementCollection mCollect;
	    SensorCollection sCollect;
	    Measurement mObject;
	    AddressData aData;
	    SensorData sData;
	   
	    
	    
	    
	public GuiBuilder(String log, String pass) throws IOException{
		setOperatorData(log,pass);
		setGui();		
		//set data to customer list
	    setCustomerData();  
	    setSensorData();
	    
	    //register listener for customerList.
        customerList.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
            	int index = customerList.getSelectionIndex();
                measureText.setText(mCollect.measList.get(index).getMeasurementData());
                mObject = mCollect.measList.get(index);
                aData = new AddressData(mObject.getID());
                customerText.setText(aData.getCustomerData());
            	}
           
          });
        
        //register listener for combo selection
        priorityCombo.addSelectionListener(new SelectionAdapter(){
        	 @Override
	          public void widgetSelected(SelectionEvent e) {
        		 if(priorityCombo.getText().equals("1.High")){       			 
        			 mCollect.priorityFilter();
        			customerList.removeAll();
        			 for(Measurement mObject:mCollect.measList){
        				 aData = new AddressData(mObject.getID());
        				 customerList.add(aData.guiAddressData());        				 
        			 }       			 	        			 
        		 }
	          }
        	
        });
        
        //register listener for the electrode combo
        electrodeCombo.addSelectionListener(new SelectionAdapter(){
        	public void widgetSelected(SelectionEvent e){
        		
        	}

		});
        
        //delete all data on gui, update list and set text fields to first selected item
        updateButton.addSelectionListener(new SelectionAdapter(){
        	public void widgetSelected(SelectionEvent e){
        		resetData();
        		setCustomerData();
        		try {
					setSensorData();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        });
        
        //register listener for the selection event
	    configButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	     public void widgetSelected(SelectionEvent e) {
	       	try {	
	       		 	String text = commentText.getText();
		    		ConfigSensor.streamCopy();
		    		
		    		//save text in database
			} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
			}
	        	  
	     }
	    }); 
	        
	   
	    shell.pack();	      
	    setShell();
	        
	        
	    while (!shell.isDisposed()) {
	    	if (!display.readAndDispatch())            	
                display.sleep();
        }
        display.dispose();
			
	}
	
	
	public void setGui(){
		display = new Display();
        shell = new Shell(display,SWT.CLOSE | SWT.TITLE|SWT.MIN);
        shell.setText("Configurator");
      	// create a new GridLayout with 3 columns of same size
        GridLayout layout = new GridLayout(4, false);
        shell.setLayout(layout);
        // shell.setSize(1024, 600);
        
        setTrayIcon();
        
              
        listLabel = new Label(shell, SWT.NONE);
        listLabel.setText("List of Customers");
        listLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false,false,1,1));
      
        customerLabel = new Label(shell, SWT.NONE);
        customerLabel.setText("Customer Info");
        customerLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false,1,1));
        
        //Measurement Label
        measLabel = new Label(shell, SWT.NONE);
        measLabel.setText("Measurement Info");
        measLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true,false,1,1));   
               
        sensorLabel = new Label(shell, SWT.NONE);
        sensorLabel.setText("Sensor Info");
        sensorLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false,1,1));
        
        customerList = new List(shell, SWT.BORDER| SWT.V_SCROLL);
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.BEGINNING;
        gridData.heightHint = 170;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = false;
        gridData.horizontalSpan = 1;
        gridData.verticalSpan = 3;
        customerList.setLayoutData(gridData);
        //customerList.select(1);
        //customerList.showSelection();
        
        customerText = new Text(shell, SWT.BORDER | SWT.V_SCROLL);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.BEGINNING;
        gridData.heightHint = 140;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = false;
        gridData.horizontalSpan = 1;
        gridData.verticalSpan = 1;
        customerText.setLayoutData(gridData);
        
        //Measurement Text
        measureText = new Text(shell, SWT.BORDER|SWT.V_SCROLL);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.BEGINNING;
        gridData.heightHint = 140;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalSpan = 1;
        gridData.verticalSpan = 1;
        measureText.setLayoutData(gridData);
        
        sensorText = new Text(shell, SWT.BORDER | SWT.V_SCROLL);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.BEGINNING;
        gridData.heightHint = 140;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = false;
        gridData.horizontalSpan = 1;
        gridData.verticalSpan = 1;
        sensorText.setLayoutData(gridData);
         
        //Comment Label
    	commentLabel = new Label(shell, SWT.NONE);
        commentLabel.setText("Comment Here!");
        commentLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,false,1,1));
        
        //Measurement Task Label
    	measTaskLabel = new Label(shell, SWT.NONE);
    	measTaskLabel.setText("Measurement Tasks!");
    	measTaskLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,false,1,1));
    	
    	//sensor Task label
    	sensorTaskLabel = new Label(shell, SWT.NONE);
    	sensorTaskLabel.setText("Sensor Tasks");
    	sensorTaskLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,false,1,1));
    	
        //Comment Text
        commentText = new Text(shell, SWT.BORDER|SWT.V_SCROLL);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.CENTER;
        gridData.verticalAlignment = GridData.BEGINNING;
        gridData.heightHint = 80;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalSpan = 1;
        gridData.verticalSpan = 1;
        commentText.setLayoutData(gridData);
        
        //Measurement Task Text
        measurTaskText = new Text(shell, SWT.BORDER|SWT.V_SCROLL);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.CENTER;
        gridData.verticalAlignment = GridData.BEGINNING;
        gridData.heightHint = 80;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalSpan = 1;
        gridData.verticalSpan = 1;
        measurTaskText.setLayoutData(gridData);
        
        //Sensor Task Text
        sensorTaskText = new Text(shell, SWT.BORDER|SWT.V_SCROLL);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.CENTER;
        gridData.verticalAlignment = GridData.BEGINNING;
        gridData.heightHint = 80;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalSpan = 1;
        gridData.verticalSpan = 1;
        sensorTaskText.setLayoutData(gridData);
        
        //Combo selection        
        priorityCombo = new Combo (shell, SWT.READ_ONLY);
        priorityCombo.setText("Filter by Priority");
        priorityCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,true,1,1));
        priorityCombo.setItems (new String [] {"1.High", "2.Medium", "3.Low"});
    	
    	//Electrode selection        
        electrodeCombo = new Combo (shell, SWT.READ_ONLY);
        electrodeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,true,1,1));
        electrodeCombo.setItems (new String [] {"Simple", "Test", "New"});
    	
    	//updateButton 
    	updateButton =  new Button(shell, SWT.PUSH);
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
        
    	//configButton
        configButton =  new Button(shell, SWT.PUSH);
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
          }
	
	public void setTrayIcon(){
		 //image for tray icon
        Image image =  new Image(display,"C:/Users/gasimov/Documents/Repo/HttpTest-3/res/images/bulb.gif");
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
            	setShell();
                System.out.println("selection"); 
            	
            	  
              }
            });
            item.addListener(SWT.DefaultSelection, new Listener() {
              public void handleEvent(Event event) {
                System.out.println("default selection");
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
              
              mi3.setText("Info");
              mi3.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event event) {
                  
                	System.out.println("selection " + event.widget);
                }
              });
              
              //menu.setDefaultItem(mi1);
            
            item.addListener(SWT.MenuDetect, new Listener() {
              public void handleEvent(Event event) {
                menu.setVisible(true);
              }
            });
            
            item.setImage(image);
            }
	}
	public void setCustomerData(){
		mCollect = new MeasurementCollection();
        mCollect.getMethod(null);	    
        
       for(int i= mCollect.measList.size()-1; i>=0; i--){
        	mObject = mCollect.measList.get(i); 
        	String mID =  mObject.getID();
        	aData = new AddressData(mID);       	
      	    customerList.add(aData.guiAddressData());
        }       
       customerText.setText(aData.getCustomerData());
  	   measureText.setText(mObject.getMeasurementData());
	}
	
	public void setSensorData() throws IOException{
		if(ConfigSensor.checkSensorConnected()){
			SensorCollection sCollect = new SensorCollection();
			sCollect.getMethod(null);
			String sID = ConfigSensor.readConfigFile("E:/SensorInfo.txt").substring(5, 10).trim();		
			for(SensorData sData:sCollect.getList()){			
				if(sData.getID().equalsIgnoreCase(sID) )				
					sensorText.setText(sData.getSensorData());
				
			}
		}
		
		
	}
	
	public void setShell(){
		boolean status = ConfigSensor.checkSensorConnected();
		if(status == true){
			shell.open();
		}
		  
	}

	public void resetData(){
		customerText.setText("");
		sensorText.setText("");
		measureText.setText("");
		measurTaskText.setText("");
		sensorTaskText.setText("");
		customerList.removeAll();
	}

	public void setOperatorData(String login, String password){
		this.login = login;
		this.password = password;
	}
}
