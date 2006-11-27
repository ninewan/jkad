package jkad.gui;

import java.math.BigInteger;

import jkad.controller.JKadSystem;

public class ListNode
{
    private JKadSystem system;
    
    public ListNode(JKadSystem system)
    {
        this.system = system;
    }
    
    public JKadSystem getSystem()
    {
        return system;
    }
    
    public String toString()
    {
        String ip = system.getIP() != null ? system.getIP().toString() : "<ip not bound>";
        String port = system.getPort() != -1 ? system.getPort() + "" : "<port not bound>";
        String id = system.getSystemID() != null ? idToString(system.getSystemID()) : " <no id>";
        return padSystemName(7) + " | " + ip + ":" + port + " | " + id;
    }
    
    private String padSystemName(int size)
    {
        String name = system.toString();
        while(name.length() < size)
            name += " ";
        return name;
    }
    
    private String idToString(BigInteger id)
    {
        StringBuffer idString = new StringBuffer(id.toString(16).toUpperCase());
        idString.ensureCapacity(21);
        if(id.compareTo(BigInteger.ZERO) >= 0)
            idString.insert(0, "+");
        for(int i = idString.length(); i <= 40; i++)
            idString.insert(1, "0");
        return idString.toString();
    }
}