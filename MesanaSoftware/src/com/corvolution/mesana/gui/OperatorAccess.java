package com.corvolution.mesana.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class OperatorAccess {
	
	Display display;
	Shell shell;
	Label userLabel, passwordLabel;
	Text loginText, passwordText, errorText;
	Button okButton, cancelButton;
	private String login, password;
	
	public OperatorAccess(){
		display = new Display();
        shell = new Shell(display,SWT.CLOSE | SWT.TITLE|SWT.MIN);
        shell.setText("Security control!");  
      	
        // create a new GridLayout with 3 columns of same size
        GridLayout layout = new GridLayout(2, false);        
        shell.setLayout(layout);
        
        
        Group group = new Group(shell, SWT.SHADOW_OUT);
        group.setLayout(new GridLayout(2, false));
        group.setText("Log in!");
        userLabel = new Label(group, SWT.NONE);
        userLabel.setText("Username:");
        userLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,false,1,1));
        
        //Login Text
        loginText = new Text(group, SWT.BORDER|SWT.SINGLE);
        GridData gridData = new GridData();
        gridData.widthHint = 80;
        gridData.horizontalAlignment = GridData.CENTER;        
        gridData.verticalAlignment = GridData.BEGINNING;   
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalSpan = 1;
        gridData.verticalSpan = 1;
        loginText.setLayoutData(gridData);
        
        passwordLabel = new Label(group, SWT.NONE);
        passwordLabel.setText("Password:");
        passwordLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false,false,1,1));
        
        //Login Text
        passwordText = new Text(group, SWT.BORDER|SWT.SINGLE | SWT.PASSWORD);
        gridData = new GridData();
        gridData.widthHint = 80;
        gridData.horizontalAlignment = GridData.CENTER;
        gridData.verticalAlignment = GridData.BEGINNING;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalSpan = 1;
        gridData.verticalSpan = 1;
        passwordText.setLayoutData(gridData);
             
        okButton = new Button(group, SWT.PUSH);
        okButton.setText("OK");
        gridData = new GridData();
        gridData.verticalAlignment = GridData.CENTER;
        gridData.horizontalAlignment = GridData.CENTER;
        gridData.widthHint = 70;
        gridData.heightHint = 30;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalSpan = 1;
        gridData.verticalSpan = 1;
        okButton.setLayoutData(gridData);
        
        cancelButton = new Button(group, SWT.PUSH);
        cancelButton.setText("Cancel");
        gridData = new GridData();
        gridData.verticalAlignment = GridData.CENTER;
        gridData.horizontalAlignment = GridData.CENTER;
        gridData.widthHint = 70;
        gridData.heightHint = 30;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalSpan = 1;
        gridData.verticalSpan = 1;
        cancelButton.setLayoutData(gridData);
        
        errorText = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.SINGLE);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 2;
		gridData.verticalSpan = 1;
		errorText.setLayoutData(gridData);
		
		
        passwordText.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.CR){
        			checkCredits();
        			
					
        			
        	}
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        }) ;   	
        
      
        okButton.addSelectionListener(new SelectionAdapter(){
        	public void widgetSelected(SelectionEvent e){
        		checkCredits();       		
        		
        		
        	}
        });
        
        cancelButton.addSelectionListener(new SelectionAdapter(){
        	public void widgetSelected(SelectionEvent e){
        		
        	System.exit(0);
        		
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
	
	
	public void checkCredits() {
		login = loginText.getText();
		password = passwordText.getText();
		
		if(login.equals("user") && password.equals("1234")){
			errorText.setText("Logging in, please wait a second!");
			shell.dispose();
	}else{
		errorText.setText("Wrong login or password");    		
		loginText.setText("");
		passwordText.setText("");
		
	}
	}
	

	public String getLogin(){
		return login;
	}
	
	
	public String getPassword(){
		return password;
	}
	
		
}
