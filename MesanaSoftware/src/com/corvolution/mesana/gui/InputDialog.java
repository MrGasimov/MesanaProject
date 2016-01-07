package com.corvolution.mesana.gui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**This class creates dialog for user input.It is used for security checking and access control.
 * @author Suleyman Gasimov
 */
public class InputDialog extends Dialog
{
	private Label userLabel, passwordLabel, msgLabel;
	private Text userField, passwordField;
	private String userString, passwordString;
	private Shell parentShell;
	private int arg;

	/**
	 * Constructor for Initialization of the dialog.
	 *
	 * @param shell the shell
	 * @param arg the arg
	 */
	public InputDialog(Shell shell, int arg)
	{
		super(shell,arg);
		this.parentShell = shell;
		this.arg = arg;
		
	}

	/**
	 * This method creates dialog area.
	 *
	 * @return String, user input
	 */
	protected String createDialogArea()
	{
		Shell shell = new Shell(this.parentShell, arg);
		//shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		shell.setText("Operator credentials");

		Group group = new Group(shell, SWT.SHADOW_OUT);
		group.setLayout(new GridLayout());
		group.setText("Operator Access");
		group.setLayout(new GridLayout(2, false));

		msgLabel = new Label(group, SWT.NONE);
		msgLabel.setText("Please enter operator credentials!");
		msgLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));

		userLabel = new Label(group, SWT.RIGHT);
		userLabel.setText("Login: ");
		userField = new Text(group, SWT.SINGLE | SWT.BORDER);

		passwordLabel = new Label(group, SWT.RIGHT);
		passwordLabel.setText("Password: ");
		passwordField = new Text(group, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);

		GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
		userField.setLayoutData(data);
		passwordField.setLayoutData(data);

		Button okButton = new Button(group, SWT.PUSH);
		okButton.setText("OK");
		data = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		data.heightHint = 30;
		data.widthHint = 60;
		okButton.setLayoutData(data);

		Button cancelButton = new Button(group, SWT.PUSH);
		cancelButton.setText("Cancel");
		data = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		data.heightHint = 30;
		data.widthHint = 60;
		cancelButton.setLayoutData(data);

		okButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				userString = userField.getText();
				passwordString = passwordField.getText();
				if (userString.equals("user") && passwordString.equals("1234"))
				{	
					parentShell.open();
					shell.close();
					
				}
				else
				{
					userField.setText("");
					passwordField.setText("");
					msgLabel.setText("Login or password is wrong");
				}
			}
		});

		cancelButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				System.exit(0);
				shell.close();
			}
		});

		shell.setDefaultButton(okButton);

		shell.pack();
		shell.open();

		Display display = this.parentShell.getDisplay();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
		return userString +File.separator+passwordString;
	}
}
