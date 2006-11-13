/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import jkad.controller.JKad;
import jkad.controller.JKadShutdownHook;
import jkad.controller.JKadSystem;
import jkad.facades.user.NetLocation;
import jkad.structures.kademlia.KadNode;
import javax.swing.JProgressBar;
import java.awt.Point;

public class JKadGUI extends JKad
{
    private JFrame frame = null;  //  @jve:decl-index=0:visual-constraint="11,6"
    private JPanel contentPane = null;
    private JComboBox systemsComboBox = null;
    private JList knowContactsListBox = null;
    private JPanel knowContactsPanel = null;
    private JPanel selectionPanel = null;
    private JLabel systemsLabel = null;
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
        this.getFrame().setVisible(true);
        this.populateSystems();
    }
    
    protected void populateSystems()
    {
        JComboBox combo = this.getSystemsComboBox();
        if(combo.getItemCount()> 0)
            combo.removeAllItems();
        for(JKadSystem system : this.getSystems())
            combo.addItem(system);
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
            frame.setSize(new Dimension(558, 573));
            frame.setTitle("JKad simple GUI");
            frame.setResizable(false);
            frame.setContentPane(getContentPane());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        }
        return contentPane;
    }

    /**
     * This method initializes systemsComboBox	
     * 	
     * @return javax.swing.JComboBox	
     */
    protected JComboBox getSystemsComboBox()
    {
        if (systemsComboBox == null)
        {
            systemsComboBox = new JComboBox();
            systemsComboBox.setBounds(new Rectangle(5, 20, 100, 20));
            systemsComboBox.addActionListener(new RefreshActionListener(this));
        }
        return systemsComboBox;
    }

    /**
     * This method initializes knowContactsListBox	
     * 	
     * @return javax.swing.JList	
     */
    protected JList getKnowContactsListBox()
    {
        if (knowContactsListBox == null)
        {
            knowContactsListBox = new JList();
        }
        return knowContactsListBox;
    }

    /**
     * This method initializes knowContactsPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    protected JPanel getKnowContactsPanel()
    {
        if (knowContactsPanel == null)
        {
            knowContactsPanel = new JPanel();
            knowContactsPanel.setName("knowContactsPanel");
            knowContactsPanel.setLayout(null);
            knowContactsPanel.setBorder(BorderFactory.createTitledBorder(null, "Know Contacts", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            knowContactsPanel.add(getKnowContactsScrollPane());
            knowContactsPanel.setBounds(new Rectangle(10, 383, 518, 159));
        }
        return knowContactsPanel;
    }

    /**
     * This method initializes selectionPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    protected JPanel getSelectionPanel()
    {
        if (selectionPanel == null)
        {
            selectionPanel = new JPanel();
            selectionPanel.setName("selectionPanel");
            selectionPanel.setBounds(new Rectangle(10, 10, 280, 46));
            selectionPanel.setLayout(null);
            selectionPanel.setBorder(BorderFactory.createTitledBorder(null, "Systems", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            selectionPanel.add(getSystemsComboBox());
            selectionPanel.add(getRefreshListButton(), null);
        }
        return selectionPanel;
    }
    
    /**
     * This method initializes knowContactsScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    protected JScrollPane getKnowContactsScrollPane()
    {
        if (knowContactsScrollPane == null)
        {
            knowContactsScrollPane = new JScrollPane();
            knowContactsScrollPane.setSize(new Dimension(500, 130));
            knowContactsScrollPane.setLocation(new Point(10, 20));
            knowContactsScrollPane.setViewportView(getKnowContactsListBox());
        }
        return knowContactsScrollPane;
    }

    /**
     * This method initializes loginPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    protected JPanel getLoginPanel()
    {
        if (loginPanel == null)
        {
            loginLabel = new JLabel();
            loginLabel.setText("Bootstrap Node IP");
            loginLabel.setBounds(new Rectangle(5, 20, 110, 16));
            portLabel = new JLabel();
            portLabel.setText("Port");
            portLabel.setBounds(new Rectangle(135, 20, 40, 16));
            loginPanel = new JPanel();
            loginPanel.setLayout(null);
            loginPanel.setBounds(new Rectangle(10, 62, 280, 65));
            loginPanel.setBorder(BorderFactory.createTitledBorder(null, "Login", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            loginPanel.add(loginLabel, null);
            loginPanel.add(getIpField(), null);
            loginPanel.add(portLabel, null);
            loginPanel.add(getPortField(), null);
            loginPanel.add(getLoginButton(), null);
        }
        return loginPanel;
    }

    /**
     * This method initializes loginButton	
     * 	
     * @return javax.swing.JButton	
     */
    protected JButton getLoginButton()
    {
        if (loginButton == null)
        {
            loginButton = new JButton();
            loginButton.setText("Login");
            loginButton.setBounds(new Rectangle(195, 40, 70, 20));
            loginButton.addActionListener( new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    JKadSystem system = getSelectedSystem();
                    try
                    {
                        system.login(new NetLocation(getIpField().getText(), Integer.parseInt(getPortField().getText())));
                    } catch (Exception error)
                    {
                        System.err.println(error.getMessage());
                    }
                }
            });
        }
        return loginButton;
    }

    /**
     * This method initializes ipField	
     * 	
     * @return javax.swing.JTextField	
     */
    protected JTextField getIpField()
    {
        if (ipField == null)
        {
            ipField = new JTextField();
            ipField.setPreferredSize(new Dimension(80, 20));
            ipField.setBounds(new Rectangle(5, 40, 114, 20));
        }
        return ipField;
    }

    /**
     * This method initializes portField	
     * 	
     * @return javax.swing.JTextField	
     */
    protected JTextField getPortField()
    {
        if (portField == null)
        {
            portField = new JTextField();
            portField.setPreferredSize(new Dimension(30, 20));
            portField.setBounds(new Rectangle(135, 40, 50, 20));
        }
        return portField;
    }

    /**
     * This method initializes storePanel	
     * 	
     * @return javax.swing.JPanel	
     */
    protected JPanel getStorePanel()
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
            storePanel.setBounds(new Rectangle(10, 135, 520, 70));
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
    protected JButton getStoreButton()
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
    protected JTextField getStoreKeyField()
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
    protected JTextField getStoreValueField()
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
    protected JPanel getFindPanel()
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
            findPanel.setBounds(new Rectangle(10, 214, 520, 70));
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
    protected JTextField getFindKeyField()
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
    protected JButton getFindButton()
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
    protected JTextField getFindResultField()
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
    protected JPanel getStatisticsPanel()
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
            statisticsPanel.setBounds(new Rectangle(10, 295, 520, 77));
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
    protected JTextField getReceivedPacketsField()
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
    protected JTextField getSentPacketsField()
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
    protected JTextField getReceveidRPCsField()
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
    protected JTextField getSentRPCsField()
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
    protected JPanel getInfoPanel()
    {
        if (infoPanel == null)
        {
            jkadLabel2 = new JLabel();
            jkadLabel2.setBounds(new Rectangle(10, 61, 191, 27));
            jkadLabel2.setText("author: Bruno C. A. Penteado");
            jkadLabel1 = new JLabel();
            jkadLabel1.setBounds(new Rectangle(10, 32, 190, 27));
            jkadLabel1.setText("https://jkad.googlecode.com/svn");
            jkadLabel1.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
            jkadLabel = new JLabel();
            jkadLabel.setBounds(new Rectangle(10, 7, 156, 23));
            jkadLabel.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
            jkadLabel.setText("JKad Simple GUI");
            infoPanel = new JPanel();
            infoPanel.setLayout(null);
            infoPanel.setBounds(new Rectangle(307, 12, 225, 114));
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
    protected JButton getRefreshButton()
    {
        if (refreshButton == null)
        {
            refreshButton = new JButton();
            refreshButton.setText("Refresh");
            refreshButton.setBounds(new Rectangle(383, 27, 109, 26));
            refreshButton.addActionListener(new RefreshActionListener(this));
        }
        return refreshButton;
    }

    /**
     * This method initializes refreshListButton	
     * 	
     * @return javax.swing.JButton	
     */
    protected JButton getRefreshListButton()
    {
        if (refreshListButton == null)
        {
            refreshListButton = new JButton();
            refreshListButton.setText("Refresh List");
            refreshListButton.setBounds(new Rectangle(145, 20, 120, 20));
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
    protected JProgressBar getFindProgressBar()
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
    protected JProgressBar getStoreProgressBar()
    {
        if (storeProgressBar == null)
        {
            storeProgressBar = new JProgressBar();
            storeProgressBar.setBounds(new Rectangle(445, 45, 70, 20));
            storeProgressBar.setVisible(false);
        }
        return storeProgressBar;
    }
    
    protected JKadSystem getSelectedSystem()
    {
        JComboBox combo = getSystemsComboBox();
        JKadSystem system = (JKadSystem)combo.getSelectedItem();
        if(system == null)
            system = this.getSystems().get(0);
        return system;
    }
    
    protected void startProgressBar(JProgressBar bar)
    {
        bar.setIndeterminate(true);
        bar.setVisible(true);
    }
    
    protected void endProgressBar(JProgressBar bar)
    {
        bar.setIndeterminate(true);
        bar.setVisible(false);
    }

    public static void main(String[] args)
    {
        JKadGUI jkad = new JKadGUI(args.length > 0 ? args[0] : DEFAULT_PROPERTY_FILE);
        Thread thread = new Thread(jkad);
        thread.start();
        jkad.init();
        Runtime.getRuntime().addShutdownHook(new JKadShutdownHook(thread, jkad));
    }
}   

class RefreshActionListener implements ActionListener
{
    private JKadGUI gui;
    
    protected RefreshActionListener(JKadGUI gui)
    {
        this.gui = gui;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        JKadSystem system = gui.getSelectedSystem();
        gui.getReceivedPacketsField().setText("" + system.countReceivedPackets());
        gui.getReceveidRPCsField().setText("" + system.countReceivedRPCs());
        gui.getSentPacketsField().setText("" + system.countSentPackets());
        gui.getSentRPCsField().setText("" + system.countSentRPCs());
        gui.getKnowContactsListBox().setModel(new KnowContactsListModel(system.listKnowContacts()));
    }
}

class KnowContactsListModel extends AbstractListModel
{
    private List<KadNode> nodes;

    protected KnowContactsListModel(List<KadNode> nodes)
    {
        this.nodes = nodes;
    }
    
    public Object getElementAt(int index)
    {
        return nodes.get(index);
    }

    public int getSize()
    {
        return nodes.size();
    }
}
