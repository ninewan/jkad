package jkad.gui;

import java.util.Comparator;

import jkad.controller.JKadSystem;

public class SystemIPComparator implements Comparator<JKadSystem>
{
    public int compare(JKadSystem system1, JKadSystem system2)
    {
        int compare = 0;
        byte[] system1Address = system1.getIP().getAddress();
        byte[] system2Address = system2.getIP().getAddress();
        for(int i = 0; i < 4 && compare == 0; i++)
            compare = ((Byte)system1Address[i]).compareTo(system2Address[i]);
        if(compare == 0)
            compare = ((Integer)system1.getPort()).compareTo(system2.getPort());
        return compare;
    }

}
