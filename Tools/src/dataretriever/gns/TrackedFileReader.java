package dataretriever.gns;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TrackedFileReader extends FileReader
{
    private long readedAmount;
    
    public TrackedFileReader(File file) throws FileNotFoundException 
    {
        super(file);
        readedAmount = 0;
    }

    public TrackedFileReader(FileDescriptor fd) 
    {
        super(fd);
        readedAmount = 0;
    }

    public TrackedFileReader(String fileName) throws FileNotFoundException 
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
