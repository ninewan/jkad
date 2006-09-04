/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package rpctester;

import jKad.builders.OutputBuilder;
import jKad.protocol.KadProtocolException;
import jKad.protocol.rpc.RPC;
import jKad.protocol.rpc.request.FindNodeRPC;
import jKad.protocol.rpc.request.FindValueRPC;
import jKad.protocol.rpc.request.PingRPC;
import jKad.protocol.rpc.request.StoreRPC;
import jKad.protocol.rpc.response.FindNodeResponse;
import jKad.protocol.rpc.response.FindValueResponse;
import jKad.protocol.rpc.response.PingResponse;
import jKad.protocol.rpc.response.StoreResponse;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RPCTester implements ActionListener
{
    private JFrame frame = null; // @jve:decl-index=0:visual-constraint="77,27"

    private JPanel contentPane = null;

    private JPanel ipPanel = null;

    private JTextField ipField = null;

    private JLabel ipLabel = null;

    private JPanel portPanel = null;

    private JLabel portLabel = null;

    private JTextField portField = null;

    private JPanel rpcPanel = null;

    private JLabel rpcLabel = null;

    private JComboBox rpcCombo = null;

    private JPanel sendPanel = null;

    private JButton sendButton = null;

    private DatagramSocket socket = null;

    public RPCTester() throws SocketException
    {
        this(3001);
    }

    public RPCTester(int port) throws SocketException
    {
        socket = new DatagramSocket(port);
    }

    /**
     * This method initializes ipLabel
     * 
     * @return javax.swing.JLabel
     */
    private JLabel getIpLabel()
    {
        if (ipLabel == null)
        {
            ipLabel = new JLabel();
            ipLabel.setText("IP");
        }
        return ipLabel;
    }

    /**
     * This method initializes portPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getPortPanel()
    {
        if (portPanel == null)
        {
            portPanel = new JPanel();
            portPanel.add(getPortLabel());
            portPanel.add(getPortField());
        }
        return portPanel;
    }

    /**
     * This method initializes portLabel
     * 
     * @return javax.swing.JLabel
     */
    private JLabel getPortLabel()
    {
        if (portLabel == null)
        {
            portLabel = new JLabel();
            portLabel.setText("Port");
        }
        return portLabel;
    }

    /**
     * This method initializes portField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getPortField()
    {
        if (portField == null)
        {
            portField = new JTextField();
            portField.setPreferredSize(new Dimension(50, 20));
        }
        return portField;
    }

    /**
     * This method initializes rpcPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getRpcPanel()
    {
        if (rpcPanel == null)
        {
            rpcPanel = new JPanel();
            rpcPanel.add(getRpcLabel());
            rpcPanel.add(getRpcCombo());
        }
        return rpcPanel;
    }

    /**
     * This method initializes rpcLabel
     * 
     * @return javax.swing.JLabel
     */
    private JLabel getRpcLabel()
    {
        if (rpcLabel == null)
        {
            rpcLabel = new JLabel();
            rpcLabel.setText("RPC");
        }
        return rpcLabel;
    }

    /**
     * This method initializes rpcCombo
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getRpcCombo()
    {
        if (rpcCombo == null)
        {
            rpcCombo = new JComboBox();
            this.setComboOptions(rpcCombo);
        }
        return rpcCombo;
    }

    private void setComboOptions(JComboBox rpcCombo)
    {
        try
        {
            PingRPC ping = new PingRPC();
            ping.setRPCID(BigInteger.ZERO);
            ping.setDestinationNodeID(BigInteger.ZERO);
            ping.setSenderNodeID(BigInteger.ZERO);

            StoreRPC store = new StoreRPC();
            store.setDestinationNodeID(BigInteger.ZERO);
            store.setKey(BigInteger.ZERO);
            store.setPiece((byte) 0);
            store.setPieceTotal((byte) 0);
            store.setRPCID(BigInteger.ZERO);
            store.setSenderNodeID(BigInteger.ZERO);
            store.setValue(BigInteger.ZERO);

            FindValueRPC findValue = new FindValueRPC();
            findValue.setDestinationNodeID(BigInteger.ZERO);
            findValue.setKey(BigInteger.ZERO);
            findValue.setRPCID(BigInteger.ZERO);
            findValue.setSenderNodeID(BigInteger.ZERO);

            FindNodeRPC findNode = new FindNodeRPC();
            findNode.setDestinationNodeID(BigInteger.ZERO);
            findNode.setRPCID(BigInteger.ZERO);
            findNode.setSearchedNodeID(BigInteger.ZERO);
            findNode.setSenderNodeID(BigInteger.ZERO);

            PingResponse pingResponse = new PingResponse();
            pingResponse.setDestinationNodeID(BigInteger.ZERO);
            pingResponse.setRPCID(BigInteger.ZERO);
            pingResponse.setSenderNodeID(BigInteger.ZERO);

            StoreResponse storeResponse = new StoreResponse();
            storeResponse.setDestinationNodeID(BigInteger.ZERO);
            storeResponse.setRPCID(BigInteger.ZERO);
            storeResponse.setSenderNodeID(BigInteger.ZERO);

            FindValueResponse findValueResponse = new FindValueResponse();
            findValueResponse.setDestinationNodeID(BigInteger.ZERO);
            findValueResponse.setPiece((byte) 0);
            findValueResponse.setPieceTotal((byte) 0);
            findValueResponse.setRPCID(BigInteger.ZERO);
            findValueResponse.setSenderNodeID(BigInteger.ZERO);
            findValueResponse.setValue(BigInteger.ZERO);

            FindNodeResponse findNodeResponse = new FindNodeResponse();
            findNodeResponse.setDestinationNodeID(BigInteger.ZERO);
            findNodeResponse.setFoundNodeID(BigInteger.ZERO);
            findNodeResponse.setIpAddress(BigInteger.ZERO);
            findNodeResponse.setPort(BigInteger.ZERO);
            findNodeResponse.setRPCID(BigInteger.ZERO);
            findNodeResponse.setSenderNodeID(BigInteger.ZERO);

            rpcCombo.addItem(new RPCOption("RPC: Ping", ping));
            rpcCombo.addItem(new RPCOption("RPC: Store", store));
            rpcCombo.addItem(new RPCOption("RPC: Find Value", findValue));
            rpcCombo.addItem(new RPCOption("RPC: Find Node", findNode));
            rpcCombo.addItem(new RPCOption("Response: Ping", pingResponse));
            rpcCombo.addItem(new RPCOption("Response: Store", storeResponse));
            rpcCombo.addItem(new RPCOption("Response: Find Value", findValueResponse));
            rpcCombo.addItem(new RPCOption("Response: Find Node", findNodeResponse));
        } catch (KadProtocolException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method initializes sendPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getSendPanel()
    {
        if (sendPanel == null)
        {
            sendPanel = new JPanel();
            sendPanel.setComponentOrientation(ComponentOrientation.UNKNOWN);
            sendPanel.add(getSendButton());
        }
        return sendPanel;
    }

    /**
     * This method initializes sendButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getSendButton()
    {
        if (sendButton == null)
        {
            sendButton = new JButton("Send");
            sendButton.addActionListener(this);
        }
        return sendButton;
    }

    public static void main(String[] args) throws SocketException
    {
        RPCTester tester = new RPCTester();
        tester.init();
    }

    public void init()
    {
        getFrame().setVisible(true);
    }

    /**
     * This method initializes frame
     * 
     * @return javax.swing.JFrame
     */
    private JFrame getFrame()
    {
        if (frame == null)
        {
            frame = new JFrame();
            frame.setSize(new Dimension(600, 80));
            frame.setTitle("RPC Tester");
            frame.setContentPane(getContentPane());
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.addWindowListener(new WindowListener()
            {
                public void windowClosing(WindowEvent e)
                {
                    if (socket != null)
                        socket.close();
                }

                public void windowActivated(WindowEvent e)
                {
                }

                public void windowClosed(WindowEvent e)
                {
                }

                public void windowDeactivated(WindowEvent e)
                {
                }

                public void windowDeiconified(WindowEvent e)
                {
                }

                public void windowIconified(WindowEvent e)
                {
                }

                public void windowOpened(WindowEvent e)
                {
                }
            });
        }
        return frame;
    }

    /**
     * This method initializes contentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getContentPane()
    {
        if (contentPane == null)
        {
            contentPane = new JPanel();
            GridBagLayout layout = new GridBagLayout();
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weightx = 1.0;
            layout.setConstraints(getRpcPanel(), constraints);
            layout.setConstraints(getIpPanel(), constraints);
            layout.setConstraints(getPortPanel(), constraints);
            layout.setConstraints(getSendPanel(), constraints);
            contentPane.setLayout(layout);
            contentPane.add(getRpcPanel());
            contentPane.add(getIpPanel());
            contentPane.add(getPortPanel());
            contentPane.add(getSendPanel());
        }
        return contentPane;
    }

    /**
     * This method initializes ipPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getIpPanel()
    {
        if (ipPanel == null)
        {
            ipPanel = new JPanel();
            ipPanel.add(getIpLabel());
            ipPanel.add(getIpField());
        }
        return ipPanel;
    }

    /**
     * This method initializes ipField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getIpField()
    {
        if (ipField == null)
        {
            ipField = new JTextField();
            ipField.setPreferredSize(new Dimension(100, 20));
        }
        return ipField;
    }

    public void actionPerformed(ActionEvent event)
    {
        String ip = getIpField().getText();
        String port = getPortField().getText();
        RPCOption option = (RPCOption) getRpcCombo().getSelectedItem();
        RPC rpc = option.getRPC();
        OutputBuilder builder = OutputBuilder.getInstance();
        try
        {
            InetAddress ipAddress = Inet4Address.getByName(ip);
            Integer portNumber = Integer.parseInt(port);
            DatagramPacket packet = builder.buildPacket(rpc, ipAddress, portNumber);
            socket.send(packet);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
