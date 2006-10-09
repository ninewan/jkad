package dataretriever.gns;

public class AdmRegion
{
    private String countryCode;
    private String admCode;
    private String name;
    private String region;
    
    public AdmRegion(){}
    
    public AdmRegion(String countryCode, String admCode, String name, String region)
    {
        this.countryCode = countryCode;
        this.admCode = admCode;
        this.name = name;
        this.region = region;
    }

    public String getAdmCode()
    {
        return admCode;
    }

    public void setAdmCode(String admCode)
    {
        this.admCode = admCode;
    }
    
    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof AdmRegion)
        {
            AdmRegion region = (AdmRegion) obj;
            return region.getCountryCode().equals(countryCode) && region.getAdmCode().equals(admCode);
        } else
            return false;
    }

    public int hashCode()
    {
        return (countryCode + "#" + admCode).hashCode();
    }
}
