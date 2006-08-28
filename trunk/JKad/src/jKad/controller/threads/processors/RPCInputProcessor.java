package jKad.controller.threads.processors;

import jKad.controller.threads.CyclicThread;
import jKad.tools.ToolBox;

import org.apache.log4j.Logger;

public class RPCInputProcessor extends CyclicThread
{
    private static Logger logger = Logger.getLogger(RPCInputProcessor.class);

    public RPCInputProcessor()
    {
        super(ToolBox.getReflectionTools().generateThreadName(RPCInputProcessor.class));
    }
    
    protected void cycleOperation() throws InterruptedException
    {
        // TODO Auto-generated method stub
        
    }

    protected void finalize()
    {
        // TODO Auto-generated method stub
    }

    protected Logger getLogger()
    {
        return logger;
    }
}
