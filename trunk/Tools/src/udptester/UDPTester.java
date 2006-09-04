/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package udptester;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class UDPTester implements ActionListener
{
    private JFrame frame = null; // @jve:decl-index=0:visual-constraint="93,25"

    private JPanel contentPane = null;

    private JPanel buttonPanel = null;

    private JPanel textAreaPanel = null;

    private JScrollPane scrollPane = null;

    private JTextPane textPane = null;

    private JButton sendButton = null;

    private JTextField ipField = null;

    private JTextField portField = null;

    private JPanel ipPanel = null;

    private JPanel sendPanel = null;

    private JPanel panel1 = null;

    private JPanel panel2 = null;

    private DatagramSocket socket;

    public UDPTester() throws SocketException, UnknownHostException
    {
        socket = new DatagramSocket(3000, Inet4Address.getLocalHost());
    }

    /**
     * This method initializes panel1
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getPanel1()
    {
        if (panel1 == null)
        {
            panel1 = new JPanel();
            panel1.setLayout(new GridBagLayout());
            panel1.add(new JLabel("IP:"));
            panel1.add(getIpField());
        }
        return panel1;
    }

    /**
     * This method initializes panel2
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getPanel2()
    {
        if (panel2 == null)
        {
            panel2 = new JPanel();
            panel2.setLayout(new GridBagLayout());
            panel2.add(new JLabel("Port:"));
            panel2.add(getPortField());
        }
        return panel2;
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
            frame.setSize(new Dimension(300, 200));
            frame.setTitle("UDP Tester");
            frame.setContentPane(getContentPane());
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
            contentPane.setLayout(new BorderLayout());
            contentPane.add(getButtonPanel(), BorderLayout.SOUTH);
            contentPane.add(getTextAreaPanel(), BorderLayout.CENTER);
        }
        return contentPane;
    }

    /**
     * This method initializes buttonPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getButtonPanel()
    {
        if (buttonPanel == null)
        {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(1);
            gridLayout.setColumns(2);
            buttonPanel = new JPanel();
            buttonPanel.setLayout(gridLayout);
            buttonPanel.add(getIpPanel(), null);
            buttonPanel.add(getSendPanel(), null);
        }
        return buttonPanel;
    }

    /**
     * This method initializes textAreaPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getTextAreaPanel()
    {
        if (textAreaPanel == null)
        {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.weightx = 1.0;
            textAreaPanel = new JPanel();
            textAreaPanel.setLayout(new GridBagLayout());
            textAreaPanel.add(getScrollPane(), gridBagConstraints);
        }
        return textAreaPanel;
    }

    /**
     * This method initializes scrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getScrollPane()
    {
        if (scrollPane == null)
        {
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(getTextPane());
        }
        return scrollPane;
    }

    /**
     * This method initializes textPane
     * 
     * @return javax.swing.JTextPane
     */
    private JTextPane getTextPane()
    {
        if (textPane == null)
        {
            textPane = new JTextPane();
        }
        return textPane;
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
            sendButton = new JButton();
            sendButton.setText("Send");
            sendButton.addActionListener(this);
        }
        return sendButton;
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
            ipField.setInputVerifier(new InputVerifier()
            {
                public boolean verify(JComponent input)
                {
                    JTextField field = (JTextField) input;
                    String text = field.getText();
                    if (text != null && text.length() > 0 && !text.matches("(\\d{1,3}\\.){3}\\d{1,3}"))
                    {
                        field.setBackground(new Color(0xFFCCCC));
                        return false;
                    } else
                    {
                        field.setBackground(Color.WHITE);
                        return true;
                    }
                }
            });
        }
        return ipField;
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
            portField.setPreferredSize(new Dimension(40, 20));
            portField.setInputVerifier(new InputVerifier()
            {
                public boolean verify(JComponent input)
                {
                    JTextField field = (JTextField) input;
                    String text = field.getText();
                    if (text != null && text.length() > 0 && !text.matches("\\d+"))
                    {
                        field.setBackground(new Color(0xFFCCCC));
                        return false;
                    } else
                    {
                        field.setBackground(Color.WHITE);
                        return true;
                    }
                }
            });
        }
        return portField;
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
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(2);
            ipPanel = new JPanel();
            ipPanel.setLayout(gridLayout);
            ipPanel.add(getPanel1(), null);
            ipPanel.add(getPanel2(), null);
        }
        return ipPanel;
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
            sendPanel.setLayout(new GridBagLayout());
            sendPanel.add(getSendButton(), new GridBagConstraints());
        }
        return sendPanel;
    }

    public static void main(String[] args) throws Exception
    {
        new UDPTester().getFrame().setVisible(true);
    }

    public void actionPerformed(ActionEvent event)
    {
        String ip = getIpField().getText();
        String port = getPortField().getText();
        String text = getTextPane().getSelectedText();
        if (text == null)
            text = textPane.getText();
        if (text == null)
            text = "";
        byte[] data = text.getBytes();
        try
        {
            DatagramPacket packet = new DatagramPacket(data, data.length);
            packet.setAddress(Inet4Address.getByName(ip));
            packet.setPort(Integer.parseInt(port));
            socket.send(packet);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
