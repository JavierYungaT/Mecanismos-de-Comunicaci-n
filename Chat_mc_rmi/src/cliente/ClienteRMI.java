package cliente;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import cliente.ClienteRMI;

public class ClienteRMI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel textPanel, inputPanel;
	private JTextField textField;
	private String name, message;
	private Font meiryoFont = new Font("Meiryo", Font.PLAIN, 14);
	private Border blankBorder = BorderFactory.createEmptyBorder(10, 10, 20, 10);// top,r,b,l
	private ChatCliente chatClient;
	private JList<String> list;
	private DefaultListModel<String> listModel;

	protected JTextArea textArea, userArea;
	protected JFrame frame;
	protected JButton privateMsgButton, startButton, sendButton;
	protected JPanel clientPanel, userPanel;

	/**
	 * Main method to start client GUI app.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		}
		new ClienteRMI();
	}

	/**
	 * GUI Constructor
	 */
	public ClienteRMI() {

		frame = new JFrame("Consola de chat del cliente");

		// -----------------------------------------
		/*
		 * interceptar el m�todo de cierre, informar al servidor que nos vamos y luego
		 * dejar que el sistema salga.
		 */
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {

				if (chatClient != null) {
					try {
						sendMessage("Adi�s a todos, me voy");
						chatClient.serverIF.leaveChat(name);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				System.exit(0);
			}
		});

		Container c = getContentPane();
		JPanel outerPanel = new JPanel(new BorderLayout());

		outerPanel.add(getInputPanel(), BorderLayout.CENTER);
		outerPanel.add(getTextPanel(), BorderLayout.NORTH);

		c.setLayout(new BorderLayout());
		c.add(outerPanel, BorderLayout.CENTER);
		c.add(getUsersPanel(), BorderLayout.WEST);

		frame.add(c);
		frame.pack();
		frame.setAlwaysOnTop(true);
		frame.setLocation(150, 150);
		textField.requestFocus();

		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * Method to set up the JPanel to display the chat text
	 * 
	 * @return
	 */
	public JPanel getTextPanel() {
		String welcome = "Bienvenido ingrese su nombre y presione Iniciar para comenzar\n";
		textArea = new JTextArea(welcome, 14, 34);
		textArea.setMargin(new Insets(10, 10, 10, 10));
		textArea.setFont(meiryoFont);

		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		textPanel = new JPanel();
		textPanel.add(scrollPane);

		textPanel.setFont(new Font("Meiryo", Font.PLAIN, 14));
		return textPanel;
	}

	/**
	 * Method to build the panel with input field
	 * 
	 * @return inputPanel
	 */
	public JPanel getInputPanel() {
		inputPanel = new JPanel(new GridLayout(1, 1, 5, 5));
		inputPanel.setBorder(blankBorder);
		textField = new JTextField();
		textField.setFont(meiryoFont);
		inputPanel.add(textField);
		return inputPanel;
	}

	/**
	 * Method to build the panel displaying currently connected users with a call to
	 * the button panel building method
	 * 
	 * @return
	 */
	public JPanel getUsersPanel() {

		userPanel = new JPanel(new BorderLayout());
		String userStr = " Usuarios actuales      ";

		JLabel userLabel = new JLabel(userStr, JLabel.CENTER);
		userPanel.add(userLabel, BorderLayout.NORTH);
		userLabel.setFont(new Font("Meiryo", Font.PLAIN, 16));

		String[] noClientsYet = { "No hay otros usuarios" };
		setClientPanel(noClientsYet);

		clientPanel.setFont(meiryoFont);
		userPanel.add(makeButtonPanel(), BorderLayout.SOUTH);
		userPanel.setBorder(blankBorder);

		return userPanel;
	}

	/**
	 * Populate current user panel with a selectable list of currently connected
	 * users
	 * 
	 * @param currClients
	 */
	public void setClientPanel(String[] currClients) {
		clientPanel = new JPanel(new BorderLayout());
		listModel = new DefaultListModel<String>();

		for (String s : currClients) {
			listModel.addElement(s);
		}
		if (currClients.length > 1) {
			privateMsgButton.setEnabled(true);
		}

		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setVisibleRowCount(8);
		list.setFont(meiryoFont);
		JScrollPane listScrollPane = new JScrollPane(list);

		clientPanel.add(listScrollPane, BorderLayout.CENTER);
		userPanel.add(clientPanel, BorderLayout.CENTER);
	}

	/**
	 * Make the buttons and add the listener
	 * 
	 * @return
	 */
	public JPanel makeButtonPanel() {
		sendButton = new JButton("Enviar ");
		sendButton.addActionListener(this);
		sendButton.setEnabled(false);

		privateMsgButton = new JButton("Enviar PM");
		privateMsgButton.addActionListener(this);
		privateMsgButton.setEnabled(false);

		startButton = new JButton("Iniciar ");
		startButton.addActionListener(this);

		JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
		buttonPanel.add(privateMsgButton);
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(startButton);
		buttonPanel.add(sendButton);

		return buttonPanel;
	}

	/**
	 * Action handling on the buttons
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		try {
			// get connected to chat service
			if (e.getSource() == startButton) {
				name = textField.getText();
				if (name.length() != 0) {
					frame.setTitle(name + "'s console ");
					textField.setText("");
					textArea.append("username : " + name + " conect�ndose al chat...\n");
					getConnected(name);
					if (!chatClient.connectionProblem) {
						startButton.setEnabled(false);
						sendButton.setEnabled(true);
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Ingrese su nombre para comenzar");
				}
			}

			// get text and clear textField
			if (e.getSource() == sendButton) {
				message = textField.getText();
				textField.setText("");
				sendMessage(message);
				System.out.println("Enviando mensaje : " + message);
			}

			// send a private message, to selected users
			if (e.getSource() == privateMsgButton) {
				int[] privateList = list.getSelectedIndices();

				for (int i = 0; i < privateList.length; i++) {
					System.out.println("�ndice seleccionado :" + privateList[i]);
				}
				message = textField.getText();
				textField.setText("");
				sendPrivate(privateList);
			}

		} catch (RemoteException remoteExc) {
			remoteExc.printStackTrace();
		}
	}

	/**
	 * Send a message, to be relayed to all chatters
	 * 
	 * @param chatMessage
	 * @throws RemoteException
	 */
	private void sendMessage(String chatMessage) throws RemoteException {
		chatClient.serverIF.updateChat(name, chatMessage);
	}

	/**
	 * Send a message, to be relayed, only to selected chatters
	 * 
	 * @param chatMessage
	 * @throws RemoteException
	 */
	private void sendPrivate(int[] privateList) throws RemoteException {
		String privateMessage = "[PM de " + name + "] :" + message + "\n";
		chatClient.serverIF.sendPM(privateList, privateMessage);
	}

	/**
	 * Make the connection to the chat server
	 * 
	 * @param userName
	 * @throws RemoteException
	 */
	private void getConnected(String userName) throws RemoteException {
		// remove whitespace and non word characters to avoid malformed url
		String cleanedUserName = userName.replaceAll("\\s+", "_");
		cleanedUserName = userName.replaceAll("\\W+", "_");
		try {
			chatClient = new ChatCliente(this, cleanedUserName);
			chatClient.startClient();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}