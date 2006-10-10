package dataretriever.gns;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

/**
 * Codes Explanations at <a href="http://earth-info.nga.mil/gns/html/gis.html">http://earth-info.nga.mil/gns/html/gis.html</a>
 * @author Bruno C. A. Penteado
 *
 */
public class GNSDataRetriever implements Runnable {
    private File countriesFile;
    private File admCodesFile;
    private File geonamesFile;
	private File outputFile;
	private TrackedFileReader fileReader;

	public static final int GEO_RC = 0; 
    public static final int GEO_UFI = 1; 
    public static final int GEO_UNI = 2; 
    public static final int GEO_LAT = 3; 
    public static final int GEO_LONG = 4;    
    public static final int GEO_DMS_LAT = 5; 
    public static final int GEO_DMS_LONG = 6;    
    public static final int GEO_UTM = 7; 
    public static final int GEO_JOG = 8; 
    public static final int GEO_FC = 9;  
    public static final int GEO_DSG = 10; 
    public static final int GEO_PC = 11;  
    public static final int GEO_CC1 = 12; 
    public static final int GEO_ADM1 = 13;    
    public static final int GEO_ADM2 = 14;    
    public static final int GEO_DIM = 15; 
    public static final int GEO_CC2 = 16; 
    public static final int GEO_NT = 17;  
    public static final int GEO_LC = 18;  
    public static final int GEO_SHORT_FORM = 19;  
    public static final int GEO_GENERIC = 20; 
    public static final int GEO_SORT_NAME = 21;   
    public static final int GEO_FULL_NAME = 22;   
    public static final int GEO_FULL_NAME_ND = 23;    
    public static final int GEO_MODIFY_DATE = 24;
    
    public static final int ADM_CODE_COUNTRY = 0;
    public static final int ADM_CODE_REGION = 1;
    public static final int ADM_NAME = 2;
    public static final int ADM_CONTINENT = 3;
    
    public static final int COUNTRY_NAME = 0;
    public static final int COUNTRY_CODE = 1;
    public static final int COUNTRY_PRIMARY_REGION = 2;
    public static final int COUNTRY_SECONDARY_REGION = 3;    
    
    public static final String SEPARATOR = "#";
    public static final String LINE_TERMINATOR = "\r\n";

	public GNSDataRetriever(File gnsDir, File outputFile) throws FileNotFoundException
	{
        this.countriesFile = new File(gnsDir, "countries.txt");
        if(!countriesFile.exists())
            throw new FileNotFoundException(countriesFile.getAbsolutePath());  
        
        this.admCodesFile = new File(gnsDir, "admcodes.txt");
        if(!admCodesFile.exists())
            throw new FileNotFoundException(admCodesFile.getAbsolutePath());
        
        this.geonamesFile = new File(gnsDir, "geonames.txt");
        if(!geonamesFile.exists())
            throw new FileNotFoundException(geonamesFile.getAbsolutePath());
        
		this.outputFile = outputFile;
	}

	public void run() 
	{
		try 
		{
            System.out.print("Processing countries...");
			Map<String, Country> countryMap = this.processCountries();
            System.out.println("OK");
            
            System.out.print("Processing region names...");
            Map<AdmRegion, AdmRegion> regionNameMap = this.processAdmRegions();
            System.out.println("OK");
            
            System.out.print("Processing geofile...");
            this.processGeofile(countryMap, regionNameMap);
            System.out.println("OK");
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private void processGeofile(Map<String, Country> countryMap, Map<AdmRegion, AdmRegion> regionNameMap) throws IOException
    {
        if (geonamesFile.isFile()) 
        {
            fileReader = new TrackedFileReader(geonamesFile);
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            BufferedReader reader = new BufferedReader(fileReader);
            AdmRegion admRegion = new AdmRegion();
            //Skips header
            String readed = reader.readLine();
            while ((readed = reader.readLine()) != null)
            {
                String[] columns = readed.split("\\t");
                if(columns[GEO_FC].trim().equals("P"))
                {
                    admRegion.setCountryCode(columns[GEO_CC1]);
                    admRegion.setAdmCode(columns[GEO_ADM1]);
                    String country = countryMap.get(columns[GEO_CC1]).getName();
                    AdmRegion region = regionNameMap.get(admRegion);
                    String state = region != null && !region.getName().contains("(general)") ? region.getName() : "";
                    String name = columns[GEO_FULL_NAME_ND];
                    String latitude = columns[GEO_LAT];
                    String longitude = columns[GEO_LONG];
                    writer.write(country + SEPARATOR + state + SEPARATOR + name + SEPARATOR + latitude + SEPARATOR + longitude + LINE_TERMINATOR);
                }
            }
            reader.close();
            writer.close();
        }
    }

    private Map<AdmRegion, AdmRegion> processAdmRegions() throws IOException
    {
        HashMap<AdmRegion, AdmRegion> map = new HashMap<AdmRegion, AdmRegion>();
        if (admCodesFile.isFile()) 
        {
            fileReader = new TrackedFileReader(admCodesFile);
            BufferedReader reader = new BufferedReader(fileReader);
            //Skips header
            String readed = reader.readLine();
            while ((readed = reader.readLine()) != null)
            {
                String[] columns = readed.split("\\t");
                String country = columns[ADM_CODE_COUNTRY].trim();
                String admCode = columns[ADM_CODE_REGION].trim();
                String name = columns[ADM_NAME].trim();
                String continent = columns[ADM_CONTINENT].trim();
                AdmRegion region = new AdmRegion(country, admCode, name, continent);
                map.put(region, region);
            }
            reader.close();
        }
        return map;
    }

    private Map<String, Country> processCountries() throws IOException
    {
        HashMap<String, Country> map = new HashMap<String, Country>();
        if (countriesFile.isFile()) 
        {
            fileReader = new TrackedFileReader(countriesFile);
            BufferedReader reader = new BufferedReader(fileReader);
            String readed;
            while ((readed = reader.readLine()) != null)
            {
                String[] columns = readed.split("\\t");
                String code = columns[COUNTRY_CODE].trim();
                String name = columns[COUNTRY_NAME].trim();
                String pRegion = columns[COUNTRY_PRIMARY_REGION];
                String sRegion = columns[COUNTRY_SECONDARY_REGION];
                Country country = new Country(code, name, pRegion, sRegion);
                map.put(code, country);
            }
            reader.close();
        }
        return map;
    }
    

    public synchronized long getReadedAmount()
	{
		return fileReader.getReadedAmount();
	}
	
	public long getGeoFileSize()
	{
		return geonamesFile.length();
	}

	public static void main(String[] args) throws Exception
	{
		File gnsDir = new File(args[0]);
        File output = new File(args[1]);
        GNSDataRetriever retriever = new GNSDataRetriever(gnsDir, output);
		JFrame frame = new JFrame("Processing files from " + gnsDir.getName() + " to " + output.getName());
        JProgressBar progressBar = new JProgressBar();
        progressBar.setMaximum(1000);
        progressBar.setBackground(new Color(102, 102, 102));
        progressBar.setForeground(new Color(255, 204, 51));
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(progressBar, BorderLayout.CENTER);
        frame.setSize(new Dimension(500,80));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        Timer timer = new Timer(true);
		timer.schedule(new ProgressMeter(retriever, progressBar), 500, 500);
		retriever.run();
        timer.cancel();
	}
}