/*
 * Created on 20/03/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jKad.tools;

import java.nio.ByteBuffer;

import junit.framework.TestCase;

public class DataToolsTest extends TestCase {

	/*
	 * Test method for 'jKad.tools.DataTools.convertArray(Byte[])'
	 */
	public void testConvertArrayByteArray() {
		byte[] array = new byte[256];
		for(int i = 0; i < array.length; i++){
			array[i] = (byte)(i*i);
		}
		Byte[] newArray = ToolBox.getDataTools().convertArray(array);
		assertNotNull(newArray);
		assertEquals(array.length, newArray.length);
		for(int i = 0; i < newArray.length; i++){
			assertEquals(array[i], newArray[i].byteValue());
		}
	}

	/*
	 * Test method for 'jKad.tools.DataTools.convertArray(byte[])'
	 */
	public void testConvertArrayByteArray1() {
		Byte[] array = new Byte[256];
		for(int i = 0; i < array.length; i++){
			array[i] = (byte)(i*i);
		}
		byte[] newArray = ToolBox.getDataTools().convertArray(array);
		assertNotNull(newArray);
		assertEquals(array.length, newArray.length);
		for(int i = 0; i < newArray.length; i++){
			assertEquals(array[i].byteValue(), newArray[i]);
		}
	}

    /*
     * Test method for 'jKad.tools.DataTools.subByteBuffer(ByteBuffer, int, int)'
     */
    public void testSubByteBuffer() {
        ByteBuffer bf = ByteBuffer.allocate(512);
        for(int i = 0; i < 512/4; i++) bf.putInt(i);
        ByteBuffer bf2 = ToolBox.getDataTools().subByteBuffer(bf, 128, 256);
        bf.position(128);
        assertNotSame(bf, bf2);
        for(int i = 0; i < 256/4; i++) assertEquals(bf.getInt(), bf2.getInt());
        bf.putInt(128, Integer.MAX_VALUE);
        bf.position(128);
        bf2.position(128);
        assertEquals(bf.getInt(), bf2.getInt());
    }
    
	/*
	 * Test method for 'jKad.tools.DataTools.copyByteBuffer(ByteBuffer, int, int)'
	 */
	public void testCopyByteBuffer() {
		ByteBuffer bf = ByteBuffer.allocate(512);
		for(int i = 0; i < 512/4; i++) bf.putInt(i);
		ByteBuffer bf2 = ToolBox.getDataTools().copyByteBuffer(bf, 128, 256);
		bf.position(128);
		assertNotSame(bf, bf2);
		for(int i = 0; i < 256/4; i++) assertEquals(bf.getInt(), bf2.getInt());
		bf.putInt(128, Integer.MAX_VALUE);
		bf.position(128);
		bf2.position(128);
		assertNotSame(bf.getInt(), bf2.getInt());
		bf.position(128);
		bf2.position(0);
		assertNotSame(bf.getInt(), bf2.getInt());
	}

	/*
	 * Test method for 'jKad.tools.DataTools.copyByteArray(Byte[], int, int)'
	 */
	public void testCopyByteArrayByteArrayIntInt() {
		Byte[] array = new Byte[256];
		for(int i = 0; i < array.length; i++) array[i] = (byte)i;
		Byte[] newArray = ToolBox.getDataTools().copyByteArray(array, 128, 64);
		assertNotNull(newArray);
		assertEquals(newArray.length, 64);
		for(int i = 0; i < newArray.length; i++){
			assertEquals(array[i + 128], newArray[i]);
		}
		array[128] = Byte.MAX_VALUE;
		assertNotSame(array[128], newArray[0]);
	}

	/*
	 * Test method for 'jKad.tools.DataTools.copyByteArray(byte[], int, int)'
	 */
	public void testCopyByteArrayByteArrayIntInt1() {
		byte[] array = new byte[256];
		for(int i = 0; i < array.length; i++) array[i] = (byte)i;
		byte[] newArray = ToolBox.getDataTools().copyByteArray(array, 128, 64);
		assertNotNull(newArray);
		assertEquals(newArray.length, 64);
		for(int i = 0; i < newArray.length; i++){
			assertEquals(array[i + 128], newArray[i]);
		}
		array[128] = Byte.MAX_VALUE;
		assertNotSame(array[128], newArray[0]);
	}

}
