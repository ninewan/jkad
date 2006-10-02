package dataretriever.tgn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TGNDataRetriever implements Runnable
{
    private File rootPath;
    private File resultFile;
    private Set<String> cities;
    
    public TGNDataRetriever(File dirPath, File resultFile)
    {
        this.rootPath = dirPath;
        this.resultFile = resultFile;
        this.cities = new TreeSet<String>();
    }
    
    public void run()
    {
        try
        {
            this.processFile(rootPath, true);
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, false));
            for (String city : cities)
                writer.write(city + "\r\n");
            writer.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void processFile(File file, boolean recursive) throws IOException
    {
        if(file.isFile())
        {
            System.out.println("Processing file " + file.getPath());
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuffer sb = new StringBuffer();
            String readed;
            while((readed = reader.readLine()) != null)
                sb.append(readed + "\r\n");
            reader.close();
            readed = sb.toString();
            readed = readed.replaceAll("\\<[\\w\\s\\=\\\"\\\\\\/\\.]+\\>", "");
            readed = readed.replaceAll("\\<A\\s+HREF\\=\\\".+\\\"\\>", "");
            readed = readed.replaceAll("\\&nbsp;", " ");
            
            Pattern namePattern = Pattern.compile("Names:\\s+(.+)\\s+", Pattern.MULTILINE);
            Pattern latPattern = Pattern.compile("Lat\\:(.+)", Pattern.MULTILINE);
            Pattern longPattern = Pattern.compile("Long\\:(.+)", Pattern.MULTILINE);
            Matcher nameMatcher = namePattern.matcher(readed);
            Matcher latMatcher = latPattern.matcher(readed);
            Matcher longMatcher = longPattern.matcher(readed);
            
            String name = null;
            String latitude = null;
            String longitude = null;
            
            if(nameMatcher.find())
            {
                name = nameMatcher.group(1);
                name = name.replaceAll("\\s*\\(.+\\)\\s*", "");
                namePattern = Pattern.compile("\\&\\#x([A-F\\d]+)\\;", Pattern.MULTILINE);
                nameMatcher = namePattern.matcher(name);
                while(nameMatcher.find())
                {
                    int[] codePoint = new int[]{Integer.parseInt(nameMatcher.group(1),16)};
                    name = name.replaceAll(nameMatcher.group(), new String(codePoint, 0, codePoint.length));
                }
            }
            if(latMatcher.find())
            {
                latitude = latMatcher.group(1);
                latitude = latitude.replaceAll("\\s+", " ");
                latitude = latitude.replaceAll("^\\s+", "");
                latitude = latitude.replaceAll("\\s+$", "");
                latitude = latitude.replaceAll("\\s*degrees\\s*minutes\\s*", "");
            }
            if(longMatcher.find())
            {
                longitude = longMatcher.group(1);
                longitude = longitude.replaceAll("\\s+", " ");
                longitude = longitude.replaceAll("^\\s+", "");
                longitude = longitude.replaceAll("\\s+$", "");
                longitude = longitude.replaceAll("\\s*degrees\\s*minutes\\s*", "");
            }
            
            if(name != null && latitude != null && longitude != null)
                cities.add(name + "#" + latitude + "#" + longitude);
        } else if(recursive && file.isDirectory())
        {
            for (File child : file.listFiles(new HTMLFilenameFilter()))
                this.processFile(child, recursive);
        }
    }
    
    public static void main(String[] args)
    {
        TGNDataRetriever retriever = new TGNDataRetriever(new File(args[0]), new File(args[1]));
        retriever.run();
    }
}

class HTMLFilenameFilter implements FilenameFilter
{
    public boolean accept(File dir, String name)
    {
        return (!name.startsWith("#")) && ((!name.contains(".")) || name.endsWith(".html") || name.endsWith(".htm"));
    }
}