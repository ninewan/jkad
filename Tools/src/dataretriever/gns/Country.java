package dataretriever.gns;

public class Country
{
    private String code;
    private String name;
    private String primaryRegion;
    private String secondaryRegion;
    
    public Country(String code, String name, String primaryRegion, String secondaryRegion)
    {
        this.code = code;
        this.name = name;
        this.primaryRegion = primaryRegion;
        this.secondaryRegion = secondaryRegion;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPrimaryRegion()
    {
        return primaryRegion;
    }

    public void setPrimaryRegion(String primaryRegion)
    {
        this.primaryRegion = primaryRegion;
    }

    public String getSecondaryRegion()
    {
        return secondaryRegion;
    }

    public void setSecondaryRegion(String secondaryRegion)
    {
        this.secondaryRegion = secondaryRegion;
    }
    
}
