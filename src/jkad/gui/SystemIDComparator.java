package jkad.gui;

import java.util.Comparator;

import jkad.controller.JKadSystem;

public class SystemIDComparator implements Comparator<JKadSystem>
{
    public int compare(JKadSystem o1, JKadSystem o2)
    {
        return o1.getSystemID().compareTo(o2.getSystemID());
    }
}
