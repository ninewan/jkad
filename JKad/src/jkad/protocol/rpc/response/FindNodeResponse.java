/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.protocol.rpc.response;

import java.math.BigInteger;
import java.net.InetAddress;

import jkad.protocol.KadProtocol;
import jkad.protocol.KadProtocolException;
import jkad.protocol.rpc.RPC;

public class FindNodeResponse extends RPC
{
    /**
     * Estrutura da RPC a ser utilizada via reflection na constru��o do pacote
     * UDP em baixo n�vel
     */
    public static final String[][] DATA_STRUCTURE =
    {
        { "foundNodeID", "NODE_ID_LENGTH" },
        { "ipAddress", "IP_ADDRESS_LENGTH" },
        { "port", "PORT_LENGTH" }
    };

    /**
     * Tamanho de um endere�o IP em Bytes
     */
    public static final int IP_ADDRESS_LENGTH = Integer.SIZE / 8;

    /**
     * Tamanho de uma porta em Bytes
     */
    public static final int PORT_LENGTH = Integer.SIZE / 8;

    /**
     * Area de identifica��o do n� achado
     */
    public static final int FOUND_NODE_AREA = KadProtocol.TOTAL_AREA_LENGTH;

    /**
     * Area de identifica��o do ip do n� achado
     */
    public static final int IP_AREA = FOUND_NODE_AREA + NODE_ID_LENGTH;

    /**
     * Area de identifica��o da porta do n� achado
     */
    public static final int PORT_AREA = IP_AREA + IP_ADDRESS_LENGTH;

    /**
     * Area total desta RPC
     */
    public static final int TOTAL_AREA_LENGTH = PORT_AREA + PORT_LENGTH;

    private BigInteger foundNodeID;

    private BigInteger ipAddress;

    private BigInteger port;

    public BigInteger getFoundNodeID()
    {
        return foundNodeID;
    }

    public void setFoundNodeID(BigInteger foundNodeID) throws KadProtocolException
    {
        if (foundNodeID == null)
        {
            throw new KadProtocolException("Cannot set foundNodeID to null");
        } else if (foundNodeID.bitLength() > KadProtocol.NODE_ID_LENGTH * 8)
        {
            throw new KadProtocolException("foundNodeID must have " + (KadProtocol.NODE_ID_LENGTH * 8) + " bits, " + "found " + foundNodeID.bitLength() + " bits");
        } else
        {
            this.foundNodeID = foundNodeID;
        }
    }

    public BigInteger getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress(BigInteger ipAddress) throws KadProtocolException
    {
        if (ipAddress == null)
        {
            throw new KadProtocolException("Cannot set ipAddress to null");
        } else if (ipAddress.bitLength() > FindNodeResponse.IP_ADDRESS_LENGTH * 8)
        {
            throw new KadProtocolException("ipAddress must have " + (FindNodeResponse.IP_ADDRESS_LENGTH * 8) + " bits, " + "found " + ipAddress.bitLength() + " bits");
        } else
        {
            this.ipAddress = ipAddress;
        }
    }
    
    public void setIpAddress(InetAddress ip)
    {
        try
        {
            this.setIpAddress(new BigInteger(ip.getAddress()));
        } catch (KadProtocolException e)
        {
            e.printStackTrace();
        }
    }

    public BigInteger getPort()
    {
        return port;
    }

    public void setPort(BigInteger port) throws KadProtocolException
    {
        if (port == null)
        {
            throw new KadProtocolException("Cannot set port to null");
        } else if (port.bitLength() > FindNodeResponse.PORT_LENGTH * 8)
        {
            throw new KadProtocolException("port must have " + (FindNodeResponse.PORT_LENGTH * 8) + " bits, " + "found " + port.bitLength() + " bits");
        } else
        {
            this.port = port;
        }
    }
    
    public void setPort(int port)
    {
        try
        {
            this.setPort(BigInteger.valueOf(port));
        } catch (KadProtocolException e)
        {
            e.printStackTrace();
        }
    }

    public String[][] getDataStructure()
    {
        return DATA_STRUCTURE;
    }

    public int getInfoLength()
    {
        return TOTAL_AREA_LENGTH;
    }

    public byte getType()
    {
        return FIND_NODE;
    }
}
