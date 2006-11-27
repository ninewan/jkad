/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import jkad.controller.JKad;
import jkad.controller.JKadShutdownHook;
import jkad.controller.JKadSystem;
import jkad.gui.actions.AddSystemAction;
import jkad.gui.actions.ComboScrollAction;
import jkad.gui.actions.DeleteSystemAction;
import jkad.gui.actions.LoginAction;
import jkad.gui.actions.PauseAction;
import jkad.gui.actions.PlayAction;
import jkad.gui.actions.RandomLoginAction;
import jkad.gui.actions.RefreshAction;
import jkad.gui.actions.SelectNodesAction;
import java.awt.GridBagLayout;
import javax.swing.SwingConstants;

public class JKadGUI extends JKad
{
    private JFrame frame = null;  //  @jve:decl-index=0:visual-constraint="11,6"
    private JPanel contentPane = null;
    private JComboBox systemsComboBox = null;
    private JList knowContactsListBox = null;
    private JPanel knowContactsPanel = null;
    private JPanel selectionPanel = null;
    private JScrollPane knowContactsScrollPane = null;
    private JPanel loginPanel = null;
    private JLabel loginLabel = null;
    private JLabel portLabel = null;
    private JButton loginButton = null;
    private JTextField ipField = null;
    private JTextField portField = null;
    private JPanel storePanel = null;
    private JLabel storeKeyLabel = null;
    private JButton storeButton = null;
    private JTextField storeKeyField = null;
    private JLabel storeValueLabel = null;
    private JTextField storeValueField = null;
    private JPanel findPanel = null;
    private JLabel findKeyLabel = null;
    private JTextField findKeyField = null;
    private JButton findButton = null;
    private JTextField findResultField = null;
    private JLabel resultLabel = null;
    private JPanel statisticsPanel = null;
    private JLabel receivedPacketsLabel = null;
    private JTextField receivedPacketsField = null;
    private JLabel sentPacketsLabel = null;
    private JTextField sentPacketsField = null;
    private JLabel sentRPCsLabel = null;
    private JLabel receivedRPCsLabel = null;
    private JTextField receveidRPCsField = null;
    private JTextField sentRPCsField = null;
    private JPanel infoPanel = null;
    private JLabel jkadLabel = null;
    private JLabel jkadLabel1 = null;
    private JLabel jkadLabel2 = null;
    private JButton refreshButton = null;
    private JButton refreshListButton = null;
    private JProgressBar findProgressBar = null;
    private JProgressBar storeProgressBar = null;
    private JButton addSystemButton = null;
    private JButton removeSystemButton = null;
    private JButton pauseButton = null;
    private JButton playButton = null;
    private JPanel idPanel = null;
    private JTextField systemID = null;
    private JLabel orderLabel = null;
    private JComboBox orderBox = null;
    private JFrame nodeListFrame = null;  //  @jve:decl-index=0:visual-constraint="621,62"
    private JPanel nodeListContentPane = null;  //  @jve:decl-index=0:visual-constraint="628,66"
    private JScrollPane nodeListScrollPane = null;
    private JList nodeList = null;
    private JLabel nodeListOrderByLabel = null;
    private JComboBox nodesListOrderByCombo = null;
    private JButton selectNodesButton = null;
    private JButton randomLoginButton = null;
    private JTextField randomLoginIntervalField = null;
    private JLabel randomLoginIntervalLabel = null;
    private JProgressBar randomLoginProgressBar = null;
    public JKadGUI()
    {
        super();
    }
    
    public JKadGUI(String propertiesFileName)
    {
        super(propertiesFileName);
    }
    
    protected void init()
    {
        this.populateSystems();
        this.getFrame().setVisible(true);
        this.getNodeListFrame().setVisible(true);
    }
    
    /**
     * This method initializes frame	
     * 	
     * @return javax.swing.JFrame	
     */
    protected JFrame getFrame()
    {
        if (frame == null)
        {
            frame = new JFrame();
            frame.setSize(new Dimension(593, 664));
            frame.setTitle("JKad GUI");
            frame.setResizable(false);
            frame.setContentPane(getContentPane());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocation(5, 30);

        }
        return frame;
    }

    /**
     * This method initializes contentPane	
     * 	
     * @return javax.swing.JPanel	
     */
    protected JPanel getContentPane()
    {
        if (contentPane == null)
        {
            contentPane = new JPanel();
            contentPane.setLayout(null);
            contentPane.add(getSelectionPanel(), null);
            contentPane.add(getLoginPanel(), null);
            contentPane.add(getStorePanel(), null);
            contentPane.add(getFindPanel(), null);
            contentPane.add(getKnowContactsPanel(), null);
            contentPane.add(getStatisticsPanel(), null);
            contentPane.add(getInfoPanel(), null);
            contentPane.add(getIdPanel(), null);
            InputMap inputMap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            inputMap.put(KeyStroke.getKeyStroke("I"), "listScroll");
            inputMap.put(KeyStroke.getKeyStroke("K"), "listScroll");
            inputMap.put(KeyStroke.getKeyStroke("L"), "login");
            inputMap.put(KeyStroke.getKeyStroke("A"), "addSystem");
            inputMap.put(KeyStroke.getKeyStroke("shift D"), "deleteSystem");
            inputMap.put(KeyStroke.getKeyStroke("shift P"), "pauseSystem");
            inputMap.put(KeyStroke.getKeyStroke("P"), "playSystem");
            inputMap.put(KeyStroke.getKeyStroke("R"), "refresh");
            inputMap.put(KeyStroke.getKeyStroke("S"), "selectNodes");
            ActionMap actionMap = contentPane.getActionMap();
            actionMap.put("listScroll", new ComboScrollAction(this));
            actionMap.put("login", new LoginAction(this));
            actionMap.put("addSystem", new AddSystemAction(this));
            actionMap.put("deleteSystem", new DeleteSystemAction(this));
            actionMap.put("playSystem", new PlayAction(this));
            actionMap.put("pauseSystem", new PauseAction(this));
            actionMap.put("refresh", new RefreshAction(this));
            actionMap.put("selectNodes", new SelectNodesAction(this));
        }
        return contentPane;
    }

    /**
     * This method initializes systemsComboBox	
     * 	
     * @return javax.swing.JComboBox	
     */
    public JComboBox getSystemsComboBox()
    {
        if (systemsComboBox == null)
        {
            systemsComboBox = new JComboBox();
            systemsComboBox.setBounds(new Rectangle(10, 20, 120, 20));
            systemsComboBox.addActionListener(new RefreshAction(this));
        }
        return systemsComboBox;
    }

    /**
     * This method initializes knowContactsListBox	
     * 	
     * @return javax.swing.JList	
     */
    public JList getKnowContactsListBox()
    {
        if (knowContactsListBox == null)
        {
            knowContactsListBox = new JList();
            knowContactsListBox.setFont(new Font("Courier New", Font.PLAIN, 12));
        }
        return knowContactsListBox;
    }

    /**
     * This method initializes knowContactsPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    public JPanel getKnowContactsPanel()
    {
        if (knowContactsPanel == null)
        {
            orderLabel = new JLabel();
            orderLabel.setBounds(new Rectangle(10, 20, 60, 16));
            orderLabel.setText("Order by: ");
            knowContactsPanel = new JPanel();
            knowContactsPanel.setName("knowContactsPanel");
            knowContactsPanel.setLayout(null);
            TitledBorder knowContactsBorder = BorderFactory.createTitledBorder(null, "Know Contacts", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51));
            knowContactsPanel.setBorder(knowContactsBorder);
            knowContactsPanel.add(getKnowContactsScrollPane());
            knowContactsPanel.setBounds(new Rectangle(10, 395, 570, 185));
            knowContactsPanel.add(orderLabel, null);
            knowContactsPanel.add(getOrderBox(), null);
            knowContactsPanel.add(getSelectNodesButton(), null);
        }
        return knowContactsPanel;
    }

    /**
     * This method initializes selectionPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    public JPanel getSelectionPanel()
    {
        if (selectionPanel == null)
        {
            selectionPanel = new JPanel();
            selectionPanel.setName("selectionPanel");
            selectionPanel.setBounds(new Rectangle(10, 10, 290, 76));
            selectionPanel.setLayout(null);
            selectionPanel.setBorder(BorderFactory.createTitledBorder(null, "Systems", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            selectionPanel.add(getSystemsComboBox());
            selectionPanel.add(getRefreshListButton(), null);
            selectionPanel.add(getAddSystemButton(), null);
            selectionPanel.add(getRemoveSystemButton(), null);
            selectionPanel.add(getPauseButton(), null);
            selectionPanel.add(getPlayButton(), null);
        }
        return selectionPanel;
    }
    
    /**
     * This method initializes knowContactsScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    public JScrollPane getKnowContactsScrollPane()
    {
        if (knowContactsScrollPane == null)
        {
            knowContactsScrollPane = new JScrollPane();
            knowContactsScrollPane.setSize(new Dimension(550, 130));
            knowContactsScrollPane.setLocation(new Point(10, 45));
            knowContactsScrollPane.setViewportView(getKnowContactsListBox());
        }
        return knowContactsScrollPane;
    }

    /**
     * This method initializes loginPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    public JPanel getLoginPanel()
    {
        if (loginPanel == null)
        {
            randomLoginIntervalLabel = new JLabel();
            randomLoginIntervalLabel.setBounds(new Rectangle(300, 16, 218, 19));
            randomLoginIntervalLabel.setText("Interval between logins (miliseconds)");
            loginLabel = new JLabel();
            loginLabel.setText("Bootstrap IP");
            loginLabel.setBounds(new Rectangle(10, 20, 82, 16));
            portLabel = new JLabel();
            portLabel.setText("Port");
            portLabel.setBounds(new Rectangle(140, 20, 32, 16));
            loginPanel = new JPanel();
            loginPanel.setLayout(null);
            loginPanel.setBounds(new Rectangle(10, 92, 570, 65));
            loginPanel.setBorder(BorderFactory.createTitledBorder(null, "Login", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            loginPanel.add(loginLabel, null);
            loginPanel.add(getIpField(), null);
            loginPanel.add(portLabel, null);
            loginPanel.add(getPortField(), null);
            loginPanel.add(getLoginButton(), null);
            loginPanel.add(getRandomLoginProgressBar(), null);
            loginPanel.add(getRandomLoginButton(), null);
            loginPanel.add(getRandomLoginIntervalField(), null);
            loginPanel.add(randomLoginIntervalLabel, null);
        }
        return loginPanel;
    }

    /**
     * This method initializes loginButton	
     * 	
     * @return javax.swing.JButton	
     */
    public JButton getLoginButton()
    {
        if (loginButton == null)
        {
            loginButton = new JButton();
            loginButton.setText("Login");
            loginButton.setBounds(new Rectangle(210, 40, 70, 20));
            loginButton.addActionListener(new LoginAction(this));
        }
        return loginButton;
    }

    /**
     * This method initializes ipField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getIpField()
    {
        if (ipField == null)
        {
            ipField = new JTextField();
            ipField.setBounds(new Rectangle(10, 40, 120, 20));
        }
        return ipField;
    }

    /**
     * This method initializes portField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getPortField()
    {
        if (portField == null)
        {
            portField = new JTextField();
            portField.setBounds(new Rectangle(140, 40, 60, 20));
        }
        return portField;
    }

    /**
     * This method initializes storePanel	
     * 	
     * @return javax.swing.JPanel	
     */
    public JPanel getStorePanel()
    {
        if (storePanel == null)
        {
            storeValueLabel = new JLabel();
            storeValueLabel.setBounds(new Rectangle(5, 45, 35, 16));
            storeValueLabel.setText("Value");
            storeKeyLabel = new JLabel();
            storeKeyLabel.setText("Key");
            storeKeyLabel.setBounds(new Rectangle(5, 20, 30, 16));
            storePanel = new JPanel();
            storePanel.setLayout(null);
            storePanel.setBounds(new Rectangle(10, 160, 570, 70));
            storePanel.setBorder(BorderFactory.createTitledBorder(null, "Store", TitledBorder.LEFT, TitledBorder.TOP, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            storePanel.add(storeKeyLabel, null);
            storePanel.add(getStoreKeyField(), null);
            storePanel.add(getStoreButton(), null);
            storePanel.add(storeValueLabel, null);
            storePanel.add(getStoreValueField(), null);
            storePanel.add(getStoreProgressBar(), null);
        }
        return storePanel;
    }

    /**
     * This method initializes storeButton	
     * 	
     * @return javax.swing.JButton	
     */
    public JButton getStoreButton()
    {
        if (storeButton == null)
        {
            storeButton = new JButton();
            storeButton.setText("Store");
            storeButton.setBounds(new Rectangle(445, 20, 70, 20));
            storeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    JKadSystem system = getSelectedSystem();
                    startProgressBar(getStoreProgressBar());
                    getStoreKeyField().setEditable(false);
                    getStoreValueField().setEditable(false);
                    getStoreButton().setEnabled(false);
                    
                    system.store(getStoreKeyField().getText(), getStoreValueField().getText());
                    
                    endProgressBar(getStoreProgressBar());
                    getStoreKeyField().setEditable(true);
                    getStoreValueField().setEditable(true);
                    getStoreButton().setEnabled(true);
                }}
            );
        }
        return storeButton;
    }

    /**
     * This method initializes storeKeyField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getStoreKeyField()
    {
        if (storeKeyField == null)
        {
            storeKeyField = new JTextField();
            storeKeyField.setBounds(new Rectangle(55, 20, 383, 20));
        }
        return storeKeyField;
    }

    /**
     * This method initializes storeValueField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getStoreValueField()
    {
        if (storeValueField == null)
        {
            storeValueField = new JTextField();
            storeValueField.setBounds(new Rectangle(55, 45, 383, 20));
        }
        return storeValueField;
    }

    /**
     * This method initializes findPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    public JPanel getFindPanel()
    {
        if (findPanel == null)
        {
            resultLabel = new JLabel();
            resultLabel.setBounds(new Rectangle(10, 45, 40, 16));
            resultLabel.setText("Result");
            findKeyLabel = new JLabel();
            findKeyLabel.setText("Key");
            findKeyLabel.setBounds(new Rectangle(10, 20, 26, 16));
            findPanel = new JPanel();
            findPanel.setLayout(null);
            findPanel.setBounds(new Rectangle(10, 235, 570, 70));
            findPanel.setBorder(BorderFactory.createTitledBorder(null, "Find", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            findPanel.add(findKeyLabel, null);
            findPanel.add(getFindKeyField(), null);
            findPanel.add(getFindButton(), null);
            findPanel.add(getFindResultField(), null);
            findPanel.add(resultLabel, null);
            findPanel.add(getFindProgressBar(), null);
        }
        return findPanel;
    }

    /**
     * This method initializes findKeyField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getFindKeyField()
    {
        if (findKeyField == null)
        {
            findKeyField = new JTextField();
            findKeyField.setBounds(new Rectangle(55, 20, 385, 20));
        }
        return findKeyField;
    }

    /**
     * This method initializes findButton	
     * 	
     * @return javax.swing.JButton	
     */
    public JButton getFindButton()
    {
        if (findButton == null)
        {
            findButton = new JButton();
            findButton.setText("Find");
            findButton.setBounds(new Rectangle(445, 20, 70, 20));
            findButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    JKadSystem system = getSelectedSystem();
                    JTextField keyField = getFindKeyField();
                    
                    startProgressBar(getFindProgressBar());
                    keyField.setEditable(false);
                    getFindButton().setEnabled(false);
                    
                    String value = system.findValue(keyField.getText());
                    
                    endProgressBar(getFindProgressBar());
                    keyField.setEditable(true);
                    getFindButton().setEnabled(true);
                    
                    getFindResultField().setText(value != null ? value : "<no value found>");
                }}
            );
        }
        return findButton;
    }

    /**
     * This method initializes findResultField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getFindResultField()
    {
        if (findResultField == null)
        {
            findResultField = new JTextField();
            findResultField.setBounds(new Rectangle(55, 45, 385, 20));
            findResultField.setEditable(false);
        }
        return findResultField;
    }

    /**
     * This method initializes statisticsPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    public JPanel getStatisticsPanel()
    {
        if (statisticsPanel == null)
        {
            receivedRPCsLabel = new JLabel();
            receivedRPCsLabel.setBounds(new Rectangle(180, 20, 100, 16));
            receivedRPCsLabel.setText("Received RPCs:");
            sentRPCsLabel = new JLabel();
            sentRPCsLabel.setBounds(new Rectangle(180, 45, 100, 16));
            sentRPCsLabel.setText("Sent RPCs:");
            sentPacketsLabel = new JLabel();
            sentPacketsLabel.setBounds(new Rectangle(10, 45, 108, 16));
            sentPacketsLabel.setText("Sent Packets:");
            receivedPacketsLabel = new JLabel();
            receivedPacketsLabel.setBounds(new Rectangle(10, 20, 108, 16));
            receivedPacketsLabel.setText("Received Packets: ");
            statisticsPanel = new JPanel();
            statisticsPanel.setLayout(null);
            statisticsPanel.setBounds(new Rectangle(10, 310, 570, 77));
            statisticsPanel.setBorder(BorderFactory.createTitledBorder(null, "Statistics", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            statisticsPanel.add(receivedPacketsLabel, null);
            statisticsPanel.add(getReceivedPacketsField(), null);
            statisticsPanel.add(sentPacketsLabel, null);
            statisticsPanel.add(getSentPacketsField(), null);
            statisticsPanel.add(sentRPCsLabel, null);
            statisticsPanel.add(receivedRPCsLabel, null);
            statisticsPanel.add(getReceveidRPCsField(), null);
            statisticsPanel.add(getSentRPCsField(), null);
            statisticsPanel.add(getRefreshButton(), null);
        }
        return statisticsPanel;
    }

    /**
     * This method initializes receivedPacketsField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getReceivedPacketsField()
    {
        if (receivedPacketsField == null)
        {
            receivedPacketsField = new JTextField();
            receivedPacketsField.setBounds(new Rectangle(122, 18, 40, 20));
            receivedPacketsField.setEditable(false);
        }
        return receivedPacketsField;
    }

    /**
     * This method initializes sentPacketsField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getSentPacketsField()
    {
        if (sentPacketsField == null)
        {
            sentPacketsField = new JTextField();
            sentPacketsField.setBounds(new Rectangle(122, 43, 40, 20));
            sentPacketsField.setEditable(false);
        }
        return sentPacketsField;
    }

    /**
     * This method initializes receveidRPCsField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getReceveidRPCsField()
    {
        if (receveidRPCsField == null)
        {
            receveidRPCsField = new JTextField();
            receveidRPCsField.setBounds(new Rectangle(290, 18, 40, 20));
            receveidRPCsField.setEditable(false);
        }
        return receveidRPCsField;
    }

    /**
     * This method initializes sentRPCsField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getSentRPCsField()
    {
        if (sentRPCsField == null)
        {
            sentRPCsField = new JTextField();
            sentRPCsField.setBounds(new Rectangle(290, 43, 40, 20));
            sentRPCsField.setEditable(false);
        }
        return sentRPCsField;
    }

    /**
     * This method initializes infoPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    public JPanel getInfoPanel()
    {
        if (infoPanel == null)
        {
            jkadLabel2 = new JLabel();
            jkadLabel2.setBounds(new Rectangle(10, 50, 190, 20));
            jkadLabel2.setForeground(new Color(187, 49, 49));
            jkadLabel2.setHorizontalAlignment(SwingConstants.CENTER);
            jkadLabel2.setText("by Bruno C. A. Penteado");
            jkadLabel1 = new JLabel();
            jkadLabel1.setBounds(new Rectangle(10, 25, 190, 20));
            jkadLabel1.setText("https://jkad.googlecode.com/svn");
            jkadLabel1.setForeground(new Color(187, 49, 49));
            jkadLabel1.setHorizontalAlignment(SwingConstants.CENTER);
            jkadLabel1.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
            jkadLabel = new JLabel();
            jkadLabel.setBounds(new Rectangle(10, 0, 190, 20));
            jkadLabel.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
            jkadLabel.setForeground(new Color(187, 49, 49));
            jkadLabel.setHorizontalAlignment(SwingConstants.CENTER);
            jkadLabel.setText("JKad GUI");
            infoPanel = new JPanel();
            infoPanel.setLayout(null);
            infoPanel.setBounds(new Rectangle(343, 10, 211, 70));
            infoPanel.add(jkadLabel, null);
            infoPanel.add(jkadLabel1, null);
            infoPanel.add(jkadLabel2, null);
        }
        return infoPanel;
    }

    /**
     * This method initializes refreshButton	
     * 	
     * @return javax.swing.JButton	
     */
    public JButton getRefreshButton()
    {
        if (refreshButton == null)
        {
            refreshButton = new JButton();
            refreshButton.setText("Refresh");
            refreshButton.setBounds(new Rectangle(383, 27, 109, 26));
            refreshButton.addActionListener(new RefreshAction(this));
        }
        return refreshButton;
    }

    /**
     * This method initializes refreshListButton	
     * 	
     * @return javax.swing.JButton	
     */
    public JButton getRefreshListButton()
    {
        if (refreshListButton == null)
        {
            refreshListButton = new JButton();
            refreshListButton.setText("Refresh List");
            refreshListButton.setBounds(new Rectangle(140, 20, 145, 20));
            refreshListButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    populateSystems();
                }
            });
        }
        return refreshListButton;
    }
    
    /**
     * This method initializes findProgressBar  
     *  
     * @return javax.swing.JProgressBar 
     */
    public JProgressBar getFindProgressBar()
    {
        if (findProgressBar == null)
        {
            findProgressBar = new JProgressBar();
            findProgressBar.setBounds(new Rectangle(445, 45, 70, 20));
            findProgressBar.setVisible(false);
        }
        return findProgressBar;
    }

    /**
     * This method initializes storeProgressBar 
     *  
     * @return javax.swing.JProgressBar 
     */
    public JProgressBar getStoreProgressBar()
    {
        if (storeProgressBar == null)
        {
            storeProgressBar = new JProgressBar();
            storeProgressBar.setBounds(new Rectangle(445, 45, 70, 20));
            storeProgressBar.setVisible(false);
        }
        return storeProgressBar;
    }
    
    /**
     * This method initializes addSystemButton  
     *  
     * @return javax.swing.JButton  
     */
    public JButton getAddSystemButton()
    {
        if (addSystemButton == null)
        {
            addSystemButton = new JButton();
            addSystemButton.setText("Add");
            addSystemButton.setBounds(new Rectangle(70, 45, 60, 20));
            addSystemButton.addActionListener(new AddSystemAction(this));
        }
        return addSystemButton;
    }

    /**
     * This method initializes removeSystemButton   
     *  
     * @return javax.swing.JButton  
     */
    private JButton getRemoveSystemButton()
    {
        if (removeSystemButton == null)
        {
            removeSystemButton = new JButton();
            removeSystemButton.setText("Del");
            removeSystemButton.setBounds(new Rectangle(10, 45, 55, 20));
            removeSystemButton.addActionListener(new DeleteSystemAction(this));
        }
        return removeSystemButton;
    }
    
    /**
     * This method initializes pauseButton  
     *  
     * @return javax.swing.JButton  
     */
    private JButton getPauseButton()
    {
        if (pauseButton == null)
        {
            pauseButton = new JButton();
            pauseButton.setText("Pause");
            pauseButton.setBounds(new Rectangle(140, 45, 70, 20));
            pauseButton.addActionListener(new PauseAction(this));
        }
        return pauseButton;
    }

    /**
     * This method initializes playButton   
     *  
     * @return javax.swing.JButton  
     */
    private JButton getPlayButton()
    {
        if (playButton == null)
        {
            playButton = new JButton();
            playButton.setText("Play");
            playButton.setBounds(new Rectangle(215, 45, 70, 20));
            playButton.addActionListener(new PlayAction(this));
        }
        return playButton;
    }

    public JKadSystem getSelectedSystem()
    {
        JComboBox combo = getSystemsComboBox();
        JKadSystem system = (JKadSystem)combo.getSelectedItem();
        if(system == null && this.getSystems().size() > 0 )
            system = this.getSystems().get(0);
        return system;
    }
    
    public void startProgressBar(JProgressBar bar)
    {
        bar.setIndeterminate(true);
        bar.setVisible(true);
    }
    
    public void endProgressBar(JProgressBar bar)
    {
        bar.setIndeterminate(true);
        bar.setVisible(false);
    }
    
    public void populateSystems()
    {
        JComboBox combo = this.getSystemsComboBox();
        if(combo.getItemCount()> 0)
            combo.removeAllItems();
        for(JKadSystem system : this.getSystems())
            combo.addItem(system);
    }
    
    /**
     * This method initializes idPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    public JPanel getIdPanel()
    {
        if (idPanel == null)
        {
            idPanel = new JPanel();
            idPanel.setLayout(null);
            idPanel.setBounds(new Rectangle(9, 591, 570, 40));
            idPanel.setBorder(BorderFactory.createTitledBorder(null, "System ID", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            idPanel.add(getSystemID(), null);
        }
        return idPanel;
    }

    /**
     * This method initializes systemID	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getSystemID()
    {
        if (systemID == null)
        {
            systemID = new JTextField();
            systemID.setBounds(new Rectangle(9, 15, 420, 20));
            systemID.setEditable(false);
            systemID.setBorder(null);
            systemID.setFont(getKnowContactsListBox().getFont());
        }
        return systemID;
    }

    /**
     * This method initializes orderBox	
     * 	
     * @return javax.swing.JComboBox	
     */
    public JComboBox getOrderBox()
    {
        if (orderBox == null)
        {
            orderBox = new JComboBox();
            orderBox.setBounds(new Rectangle(80, 20, 110, 20));
            orderBox.addItem("IP");
            orderBox.addItem("System ID");
            orderBox.addActionListener(new RefreshAction(this));
        }
        return orderBox;
    }

    /**
     * This method initializes nodeListFrame	
     * 	
     * @return javax.swing.JFrame	
     */
    private JFrame getNodeListFrame()
    {
        if (nodeListFrame == null)
        {
            nodeListFrame = new JFrame("JKad nodes");
            nodeListFrame.setSize(new Dimension(546, 621));
            nodeListFrame.setContentPane(getNodeListContentPane());
            nodeListFrame.setLocation(605, 30);
        }
        return nodeListFrame;
    }

    /**
     * This method initializes nodeListContentPane	
     * 	
     * @return javax.swing.JPanel	
     */
    public JPanel getNodeListContentPane()
    {
        if (nodeListContentPane == null)
        {
            nodeListOrderByLabel = new JLabel();
            nodeListOrderByLabel.setBounds(new Rectangle(15, 5, 64, 16));
            nodeListOrderByLabel.setText("Order By:");
            nodeListContentPane = new JPanel();
            nodeListContentPane.setLayout(null);
            nodeListContentPane.add(getNodeListScrollPane());
            nodeListContentPane.add(nodeListOrderByLabel, null);
            nodeListContentPane.add(getNodesListOrderByCombo(), null);
        }
        return nodeListContentPane;
    }

    /**
     * This method initializes nodeListScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getNodeListScrollPane()
    {
        if (nodeListScrollPane == null)
        {
            nodeListScrollPane = new JScrollPane();
            nodeListScrollPane.setBounds(10, 30, 520, 550);
            nodeListScrollPane.setViewportView(getNodeList());
        }
        return nodeListScrollPane;
    }

    /**
     * This method initializes nodeList	
     * 	
     * @return javax.swing.JList	
     */
    public JList getNodeList()
    {
        if (nodeList == null)
        {
            nodeList = new JList();
            nodeList.setFont(getKnowContactsListBox().getFont());
        }
        return nodeList;
    }

    /**
     * This method initializes nodesListOrderByCombo	
     * 	
     * @return javax.swing.JComboBox	
     */
    public JComboBox getNodesListOrderByCombo()
    {
        if (nodesListOrderByCombo == null)
        {
            nodesListOrderByCombo = new JComboBox();
            nodesListOrderByCombo.setBounds(new Rectangle(80, 5, 110, 20));
            nodesListOrderByCombo.addItem("IP");
            nodesListOrderByCombo.addItem("System ID");
            nodesListOrderByCombo.addActionListener(new RefreshAction(this));
        }
        return nodesListOrderByCombo;
    }

    /**
     * This method initializes selectNodesButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getSelectNodesButton()
    {
        if (selectNodesButton == null)
        {
            selectNodesButton = new JButton("Select nodes");
            selectNodesButton.setBounds(new Rectangle(448, 20, 110, 20));
            selectNodesButton.addActionListener(new SelectNodesAction(this));
        }
        return selectNodesButton;
    }

    /**
     * This method initializes randomLoginButton	
     * 	
     * @return javax.swing.JButton	
     */
    public JButton getRandomLoginButton()
    {
        if (randomLoginButton == null)
        {
            randomLoginButton = new JButton();
            randomLoginButton.setText("Random Login");
            randomLoginButton.setBounds(new Rectangle(390, 40, 120, 20));
            randomLoginButton.addActionListener(new RandomLoginAction(this));
        }
        return randomLoginButton;
    }

    /**
     * This method initializes randomLoginIntervalField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getRandomLoginIntervalField()
    {
        if (randomLoginIntervalField == null)
        {
            randomLoginIntervalField = new JTextField();
            randomLoginIntervalField.setBounds(new Rectangle(300, 40, 80, 20));
        }
        return randomLoginIntervalField;
    }

    /**
     * This method initializes randomLoginProgressBar	
     * 	
     * @return javax.swing.JProgressBar	
     */
    public JProgressBar getRandomLoginProgressBar()
    {
        if (randomLoginProgressBar == null)
        {
            randomLoginProgressBar = new JProgressBar(0, 100000);
            randomLoginProgressBar.setString("Random Login");
            randomLoginProgressBar.setStringPainted(true);
            randomLoginProgressBar.setBounds(new Rectangle(385, 40, 180, 20));
            randomLoginProgressBar.setVisible(false);
        }
        return randomLoginProgressBar;
    }

    public static void main(String[] args)
    {
        JKadGUI jkad = new JKadGUI(args.length > 0 ? args[0] : DEFAULT_PROPERTY_FILE);
        Thread thread = new Thread(jkad);
        thread.start();
        try
        {
            Thread.sleep(2000);
        } catch (InterruptedException e) { }
        jkad.init();
        Runtime.getRuntime().addShutdownHook(new JKadShutdownHook(thread, jkad));
    }
}   