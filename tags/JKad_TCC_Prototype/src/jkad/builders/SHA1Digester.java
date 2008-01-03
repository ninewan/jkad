/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.builders;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

public class SHA1Digester
{
    private static Logger logger = Logger.getLogger(SHA1Digester.class);
    
    private static MessageDigest digester;
    
    private static MessageDigest getDigester()
    {
        if(digester == null)
        {
            try
            {
                digester = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e)
            {
                logger.fatal(e);
                throw new RuntimeException(e);
            }
        }
        return digester;
    }
    
    public static byte[] digest(String string)
    {
        byte[] data = string.getBytes();
        return getDigester().digest(data);
    }
    
    public static byte[] digest(byte[] data)
    {
        return getDigester().digest(data);
    }
    
    public static BigInteger hash(String string)
    {
        byte[] data = string.getBytes();
        byte[] hashed = getDigester().digest(data);
        return new BigInteger(hashed);
    }
    
    public static BigInteger hash(byte[] data)
    {
        byte[] hashed = getDigester().digest(data);
        return new BigInteger(hashed);
    }
}
