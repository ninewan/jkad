package dataretriever.gns;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class GNSDataRetriever implements Runnable {
	private File inputFile;
	private File outputFile;
	private MyFileReader fileReader;

	//Codes explanations: http://earth-info.nga.mil/gns/html/gis.html
	
	public static final int RC = 0;
	public static final int UFI = 1;
	public static final int UNI = 2;
	public static final int LAT = 3;
	public static final int LONG = 4;
	public static final int DMS_LAT = 5;
	public static final int DMS_LONG = 6;
	public static final int UTM = 7;
	public static final int JOG = 8;
	public static final int FC = 9;
	public static final int DSG = 10;
	public static final int PC = 11;
	public static final int CC1 = 12;
	public static final int ADM1 = 13;
	public static final int ADM2 = 14;
	public static final int DIM = 15;
	public static final int CC2 = 16;
	public static final int NT = 17;
	public static final int LC = 18;
	public static final int SHORT_FORM = 19;
	public static final int GENERIC = 20;
	public static final int SORT_NAME = 21;
	public static final int FULL_NAME = 22;
	public static final int FULL_NAME_ND = 23;
	public static final int MODIFY_DATE = 24;

	
	public GNSDataRetriever(File inputFile, File outputFile) 
	{
		this.inputFile = inputFile;
		this.outputFile = outputFile;
	}

	public void run() 
	{
		try 
		{
			if (inputFile.isFile()) 
			{
				fileReader = new MyFileReader(inputFile);
				BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
				BufferedReader reader = new BufferedReader(fileReader);
				String readed;
				while ((readed = reader.readLine()) != null)
				{
					String[] columns = readed.split("\\t");
					if(columns[FC].trim().equals("P"))
					{
						String country = columns[CC1];
						String name = columns[FULL_NAME_ND];
						String latitude = columns[LAT];
						String longitude = columns[LONG];
						writer.write(country + "#" + name + "#" + latitude + "#" + longitude + "\r\n");
					}
				}
				reader.close();
				writer.close();
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public synchronized long getReadedAmount()
	{
		return fileReader.getReadedAmount();
	}
	
	public long getInputFileSize()
	{
		return inputFile.length();
	}

	public static void main(String[] args)
	{
		GNSDataRetriever retriever = new GNSDataRetriever(new File(args[0]), new File(args[1]));
		Timer timer = new Timer(true);
		timer.schedule(new Meter(retriever), 5000, 5000);
		retriever.run();
	}
}

class Meter extends TimerTask
{
	private GNSDataRetriever retriever;
	
	public Meter(GNSDataRetriever retriever)
	{
		this.retriever = retriever;
	}
	
	public void run() 
	{
		long readed = retriever.getReadedAmount();
		long total = retriever.getInputFileSize();
		double ratio = ((double)readed / (double)total) * 100.0;
		System.out.println("Processed " + readed + " of " + total + " bytes (" + (Math.round(ratio * 100) / 100.0) + "%)");
	}
	
}

class MyFileReader extends FileReader
{
	private long readedAmount;
	
	public MyFileReader(File file) throws FileNotFoundException 
	{
		super(file);
		readedAmount = 0;
	}

	public MyFileReader(FileDescriptor fd) 
	{
		super(fd);
		readedAmount = 0;
	}

	public MyFileReader(String fileName) throws FileNotFoundException 
	{
		super(fileName);
		readedAmount = 0;
	}

	@Override
	public int read() throws IOException 
	{
		int readed = super.read();
		if(readed != -1)
			readedAmount++;
		return readed;
	}

	@Override
	public int read(char[] cbuf, int offset, int length) throws IOException 
	{
		int readed = super.read(cbuf, offset, length);
		if(readed != -1)
			readedAmount += readed;
		return readed;
	}
	
	public synchronized long getReadedAmount()
	{
		return this.readedAmount;
	}
}