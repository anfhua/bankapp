package atmproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AtmGUI {

    private JFrame frame;
    private JButton withdrawalButton;
    private JButton depositButton;
    private JButton newAccountButton;
    private JButton balanceButton;
    private JButton quitButton;
    private JButton deleteAccountButton;
    protected final int MAX_NUM = 3;
  	public int[] accountsList = new int[MAX_NUM];
  	public double[] balancesList = new double[MAX_NUM];
  	public int numAccts = 0;
    StringBuilder finalReceipt = new StringBuilder();
    StringBuilder receipt = new StringBuilder();


    public AtmGUI() {
        frame = new JFrame("Anthony's Bank");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        // Create buttons
        withdrawalButton = new JButton("Withdrawal");
        depositButton = new JButton("Deposit");
        newAccountButton = new JButton("New Account");
        balanceButton = new JButton("Balance");
        deleteAccountButton = new JButton("Delete Account");
        quitButton = new JButton("Quit");

        // Add action listeners to the buttons
        withdrawalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                withdraw();
            }
        });

        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deposit();
            }
        });

        newAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newAccount();
            }
        });

        balanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                balance();
            }
        });

        deleteAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteAccount();
            }
        });
        

        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleQuit();
            }
        });

        // Create a panel and add components to it
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(withdrawalButton);
        panel.add(depositButton);
        panel.add(newAccountButton);
        panel.add(balanceButton);
        panel.add(deleteAccountButton);
        panel.add(quitButton);

        // Set up the main frame
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setTitle("Anthony's Bank Menu");
        frame.setVisible(true);
    }
    
    
    // QUIT PROGRAM
    public void handleQuit() {
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy @ HH:mm:ss");
        int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
        	finalReceipt.append("Thank you for banking with us!\nHere is Your Receipt:\n" + dtf.format(LocalDateTime.now())+"\n");
        	receipt();
            frame.dispose();
            System.exit(0);
        }
    } //end handleQuit()
    
    
    // display receipt after quitting
    public void receipt() {

    	finalReceipt.append(receipt);
        JOptionPane.showMessageDialog(frame, finalReceipt.toString(), "Anthony's Bank", JOptionPane.INFORMATION_MESSAGE);
   
    } // end receipt()

    
    // create a new account
    public void newAccount() {
    	if (numAccts >= MAX_NUM) {
    		JOptionPane.showMessageDialog(null, "Error: You have opened the Maximum Accounts", "Error", JOptionPane.ERROR_MESSAGE);
    	}
    	else {
    	  String accountNumberStr = JOptionPane.showInputDialog("Please enter a 4-digit account number:");
    	  int accountNumber = Integer.parseInt(accountNumberStr);
    	  boolean accountExists = false;
    	  for (int i : accountsList) {
    	      if (i == accountNumber) {
    	          accountExists = true;
    	          break;
    	      }
    	  }
    	  
    	  if (accountExists) {
    	      JOptionPane.showMessageDialog(null, "The account number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
    	  } else if(accountNumber > 9999 || accountNumber < 1000) {
    		  JOptionPane.showMessageDialog(null, "The account number must be between 1000 and 9999", "INVALID",JOptionPane.ERROR_MESSAGE);
    	  } else {
    		  accountsList[numAccts] = accountNumber;
    		  balancesList[numAccts] = 0.0;
    		  JOptionPane.showMessageDialog(null, "New account created successfully.");
    		  receipt.append("\nTransaction Type: New Account\nAccount "+accountNumber+" created.\n");
    		  numAccts++;
    	  }
    	}
    } //end newAccount()
    
    
    // delete an existing account
    public void deleteAccount() {
    	 String accountNumberStr = JOptionPane.showInputDialog("Please enter an account number:");
    	 int accountNumber = Integer.parseInt(accountNumberStr);
    	 
    	 boolean accountExists = false;
    	 int index = -1;
    	 for (int i = 0; i < accountsList.length; i++) {
    		 if (accountsList[i] == accountNumber) {
    			 accountExists = true;
    			 index = i;
    			 break;
    			 }
    		 }
    	 
    	 if (!accountExists) {
    	     JOptionPane.showMessageDialog(null, "The account number does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
    	 } else if (index != -1 && balancesList[index] > 0) {
    		 String results = String.format("You stll have: $%,.2f in the account.",balancesList[index]);
    	     JOptionPane.showMessageDialog(null, results, "Error", JOptionPane.ERROR_MESSAGE);
    	 } else {
    	     accountsList[index] = -1;
    	     JOptionPane.showMessageDialog(null, "Account deleted successfully.");
    	     receipt.append("\nTransaction Type: Delete Account\nAccount "+accountNumber+" deleted.\n");
    	     numAccts--;
    	 }
    } //end deleteAccount()

    
    // put money into account
    public void deposit() {
    	String accountNumberStr = JOptionPane.showInputDialog("Please enter an account number:");
    	int accountNumber = Integer.parseInt(accountNumberStr);
    	
    	 boolean accountExists = false;
    	 int index = -1;
    	 for (int i = 0; i < accountsList.length; i++) {
    		 if (accountsList[i] == accountNumber) {
    			 accountExists = true;
    			 index = i;
    			 break;
    			 }
    		 }
    	 
    	 if (!accountExists) {
    		 JOptionPane.showMessageDialog(null, "The account number does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
    	 }
    	 else {
    		 double money = balancesList[index];
    		 String depositAmountStr = JOptionPane.showInputDialog("Please enter your deposit amount:");
    		 double depositAmount = Double.parseDouble(depositAmountStr);
    		 
    		 if(depositAmount > 0) {
        		 receipt.append("\nTransaction Type: Deposit\nOld Balance: $").append(String.format("%,.2f", money)).append("\n");
    			 money += depositAmount;
    			 balancesList[index] = money;
    			 String results = String.format("New Balance: $%,.2f", money);
    			 JOptionPane.showMessageDialog(null, results);
    			 receipt.append("New Balance: $").append(String.format("%,.2f", money)).append("\n");
    		 }
    		 else {
    			 JOptionPane.showMessageDialog(null, "Your deposit must be greater than 0", "INVALID", JOptionPane.ERROR_MESSAGE);
    		 }
    	 }
    } //end deposit
    
    
    // Take out money if you have it
    public void withdraw() {
    	String accountNumberStr = JOptionPane.showInputDialog("Please enter an account number:");
    	int accountNumber = Integer.parseInt(accountNumberStr);
    	
    	 boolean accountExists = false;
    	 int index = -1;
    	 for (int i = 0; i < accountsList.length; i++) {
    		 if (accountsList[i] == accountNumber) {
    			 accountExists = true;
    			 index = i;
    			 break;
    			 }
    		 }
    	 
    	 if (!accountExists) {
    		 JOptionPane.showMessageDialog(null, "The account number does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
    	 }
    	 else {
    		 double money = balancesList[index];
    		 String withdrawAmountStr = JOptionPane.showInputDialog(String.format("You have: $%,.2f\nPlease enter your withdraw amount:",money));
    		 double withdrawAmount = Double.parseDouble(withdrawAmountStr);
    		 receipt.append("\nTransaction Type: Withdraw\nOld Balance: $").append(String.format("%,.2f", money)).append("\n");
    		 
    		 if(withdrawAmount <= money) {
    			 money -= withdrawAmount;
    			 balancesList[index] = money;
    			 String results = String.format("New Balance: $%,.2f", money);
    			 JOptionPane.showMessageDialog(null, results);
    			 receipt.append("New Balance: $").append(String.format("%,.2f", money)).append("\n");
    		 }
    		 else {
    			 JOptionPane.showMessageDialog(null, "You dont have that much money!", "ERROR", JOptionPane.ERROR_MESSAGE);
    		 } 
    	 }
    } //end withdraw()
    
    
    public void balance() {
    	String accountNumberStr = JOptionPane.showInputDialog("Please enter an account number:");
    	int accountNumber = Integer.parseInt(accountNumberStr);
    	
    	 boolean accountExists = false;
    	 int index = -1;
    	 for (int i = 0; i < accountsList.length; i++) {
    		 if (accountsList[i] == accountNumber) {
    			 accountExists = true;
    			 index = i;
    			 break;
    			 }
    		 }
    	 
    	 if (!accountExists) {
    		 JOptionPane.showMessageDialog(null, "The account number does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
    	 }
    	 else {
    		 String results = String.format("Your balance is: $%,.2f",balancesList[index]);
    		 JOptionPane.showMessageDialog(null, results);
    		 receipt.append("\nTransaction Type: Balance\nCurrent Balance: $").append(String.format("%,.2f", balancesList[index])).append("\n");
    	 }
    }
    
    // MAIN METHOD
    public static void main(String[] args) {
                new AtmGUI();
            } //end main()
} //END PROGRAM
