/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.tools;

public class ToolBox
{
    private static ToolBox toolBox;

    private NetTools[] netTools;
    private ReflectionTools[] reflectionTools;
    private DataTools[] dataTools;
    private int netToolsCounter;
    private int reflectionToolsCounter;
    private int dataToolsCounter;

    protected ToolBox()
    {
        Integer amount;
        amount = Integer.parseInt(System.getProperty("jkad.toolBox.data.instances", "10"));
        dataTools = new DataTools[amount];
        amount = Integer.parseInt(System.getProperty("jkad.toolBox.net.instances", "10"));
        netTools = new NetTools[amount];
        amount = Integer.parseInt(System.getProperty("jkad.toolBox.reflection.instances", "10"));
        reflectionTools = new ReflectionTools[amount];
    }

    private static ToolBox getToolBox()
    {
        if (toolBox == null)
            toolBox = new ToolBox();
        return toolBox;
    }

    public static DataTools getDataTools()
    {
        return ToolBox.getToolBox().getNextDataTools();
    }

    public static NetTools getNetTools()
    {
        return ToolBox.getToolBox().getNextNetTools();
    }

    public static ReflectionTools getReflectionTools()
    {
        return ToolBox.getToolBox().getNextReflectionTools();
    }

    protected synchronized DataTools getNextDataTools()
    {
        if (dataTools[dataToolsCounter] == null)
            dataTools[dataToolsCounter] = new DataTools();
        DataTools instance = dataTools[dataToolsCounter];
        dataToolsCounter++;
        dataToolsCounter %= dataTools.length;
        return instance;
    }

    protected synchronized NetTools getNextNetTools()
    {
        if (dataTools[netToolsCounter] == null)
            netTools[netToolsCounter] = new NetTools();
        NetTools instance = netTools[netToolsCounter];
        netToolsCounter++;
        netToolsCounter %= netTools.length;
        return instance;
    }

    protected synchronized ReflectionTools getNextReflectionTools()
    {
        if (reflectionTools[reflectionToolsCounter] == null)
            reflectionTools[reflectionToolsCounter] = new ReflectionTools();
        ReflectionTools instance = reflectionTools[reflectionToolsCounter];
        reflectionToolsCounter++;
        reflectionToolsCounter %= reflectionTools.length;
        return instance;
    }
}
