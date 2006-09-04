/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkadgui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import jkad.controller.JKad;
import jkad.controller.JKadSystem;

public class JKadGUI implements ListDataListener
{
    private JFrame frame = null;  //  @jve:decl-index=0:visual-constraint="65,17"
    private JPanel frameContentPane = null;
    private JList nodeList = null;
    private JPanel controlPanel = null;
    private JPanel viewPanel = null;
    private JScrollPane listScrollPane = null;
    private NodeListModel systemList = null;
    
    public JKadGUI()
    {
        this.systemList = new NodeListModel();
    }
    
    public JKadGUI(List<JKadSystem> systemList)
    {
        this();
        this.systemList.addAll(systemList);
    }
    
    public JKadGUI(JKad jkad)
    {
        this(jkad.getSystems());
    }
    
    public void init()
    {
        this.getFrame().setVisible(true);
    }
    
    private JFrame getFrame()
    {
        if (frame == null)
        {
            frame = new JFrame();
            frame.setTitle("JKad GUI");
            frame.setSize(new Dimension(500, 400));
            frame.setContentPane(getFrameContentPane());
        }
        return frame;
    }

    private JPanel getFrameContentPane()
    {
        if (frameContentPane == null)
        {
            frameContentPane = new JPanel();
            frameContentPane.setLayout(new BorderLayout());
            frameContentPane.add(getViewPanel(), BorderLayout.CENTER);
            frameContentPane.add(getControlPanel(), BorderLayout.SOUTH);
        }
        return frameContentPane;
    }

    private JList getNodeList()
    {
        if (nodeList == null)
        {
            nodeList = new JList(systemList);
            systemList.addListDataListener(this);
        }
        return nodeList;
    }
    
    public void contentsChanged(ListDataEvent e)
    {
        this.getNodeList().update(getNodeList().getGraphics());
        System.out.println("JKadGUI.contentsChanged()");
    }

    public void intervalAdded(ListDataEvent e)
    {
        this.getNodeList().update(getNodeList().getGraphics());
        System.out.println("JKadGUI.intervalAdded()");
    }

    public void intervalRemoved(ListDataEvent e)
    {
        this.getNodeList().update(getNodeList().getGraphics());
        System.out.println("JKadGUI.intervalRemoved()");
    }

    private JPanel getControlPanel()
    {
        if (controlPanel == null)
        {
            controlPanel = new JPanel();
            controlPanel.setLayout(new GridBagLayout());
        }
        return controlPanel;
    }

    private JPanel getViewPanel()
    {
        if (viewPanel == null)
        {
            viewPanel = new JPanel();
            viewPanel.setLayout(new BorderLayout());
            viewPanel.add(getListScrollPane(), BorderLayout.CENTER);
        }
        return viewPanel;
    }
    
    private JScrollPane getListScrollPane()
    {
        if (listScrollPane == null)
        {
            listScrollPane = new JScrollPane();
            listScrollPane.setViewportView(getNodeList());
        }
        return listScrollPane;
    }

    public static void main(String[] args) throws InterruptedException
    {
        JKad jkad = new JKad();
        Thread jkadThread = new Thread(jkad);
        jkadThread.start();
        Thread.sleep(5000);
        JKadGUI gui = new JKadGUI(jkad);
        gui.init();
        
        while(jkadThread.isAlive())
            try { jkadThread.join(); } catch (InterruptedException e){ }
    }
}
