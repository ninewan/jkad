/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkadgui;

import jKad.controller.JKadSystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class NodeListModel extends ArrayList<JKadSystem> implements ListModel
{
    private List<ListDataListener> listenersList;

    public NodeListModel()
    {
        super();
        this.listenersList = new ArrayList<ListDataListener>();
    }

    public NodeListModel(List<JKadSystem> systemList)
    {
        this();
        this.addAll(systemList);
    }

    public void addListDataListener(ListDataListener l)
    {
        this.listenersList.add(l);
    }

    public Object getElementAt(int index)
    {
        return get(index);
    }

    public int getSize()
    {
        return size();
    }

    public void removeListDataListener(ListDataListener l)
    {
        this.listenersList.remove(l);
    }

    public void add(int index, JKadSystem element)
    {
        super.add(index, element);
        this.notifyListeners(ListDataEvent.INTERVAL_ADDED, index, index);
    }

    @Override
    public boolean add(JKadSystem o)
    {
        int sizeBefore = size();
        boolean added = super.add(o);
        if (added)
            this.notifyListeners(ListDataEvent.INTERVAL_ADDED, sizeBefore, size());
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends JKadSystem> c)
    {
        int sizeBefore = size();
        boolean added = super.addAll(c);
        if (added)
            this.notifyListeners(ListDataEvent.INTERVAL_ADDED, sizeBefore, size());
        return added;
    }

    @Override
    public boolean addAll(int index, Collection<? extends JKadSystem> c)
    {
        int sizeBefore = size();
        boolean added = super.addAll(index, c);
        if (added)
            this.notifyListeners(ListDataEvent.INTERVAL_ADDED, index, index + (size() - sizeBefore));
        return added;
    }

    @Override
    public void clear()
    {
        int sizeBefore = size();
        super.clear();
        this.notifyListeners(ListDataEvent.INTERVAL_REMOVED, 0, sizeBefore);
    }

    @Override
    public JKadSystem remove(int index)
    {
        JKadSystem removed = super.remove(index);
        this.notifyListeners(ListDataEvent.INTERVAL_REMOVED, index, index);
        return removed;
    }

    @Override
    public boolean remove(Object o)
    {
        int index = indexOf(o);
        boolean removed = super.remove(o);
        if (removed)
            this.notifyListeners(ListDataEvent.INTERVAL_REMOVED, index, index);
        return removed;
    }

    @Override
    public JKadSystem set(int index, JKadSystem element)
    {
        JKadSystem replaced = super.set(index, element);
        this.notifyListeners(ListDataEvent.CONTENTS_CHANGED, index, index);
        return replaced;
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        boolean removed = false;
        for (Iterator<JKadSystem> iterator = iterator(); iterator.hasNext();)
        {
            JKadSystem system = iterator.next();
            if (c.contains(system))
            {
                int index = indexOf(system);
                iterator.remove();
                notifyListeners(ListDataEvent.INTERVAL_REMOVED, index, index);
                removed = true;
            }
        }
        return removed;
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        boolean removed = false;
        for (Iterator<JKadSystem> iterator = iterator(); iterator.hasNext();)
        {
            JKadSystem system = iterator.next();
            if (!c.contains(system))
            {
                int index = indexOf(system);
                iterator.remove();
                notifyListeners(ListDataEvent.INTERVAL_REMOVED, index, index);
                removed = true;
            }
        }
        return removed;
    }

    private void notifyListeners(int type, int index0, int index1)
    {
        switch (type)
        {
            case ListDataEvent.CONTENTS_CHANGED:
            {
                for (ListDataListener listener : listenersList)
                    listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index0, index1));
                break;
            }
            case ListDataEvent.INTERVAL_ADDED:
            {
                for (ListDataListener listener : listenersList)
                    listener.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index0, index1));
                break;
            }
            case ListDataEvent.INTERVAL_REMOVED:
            {
                for (ListDataListener listener : listenersList)
                    listener.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index0, index1));
                break;
            }
        }
    }
}
