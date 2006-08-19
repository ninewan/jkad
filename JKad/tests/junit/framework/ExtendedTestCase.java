/*
 * Created on 20/03/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package junit.framework;

public class ExtendedTestCase extends TestCase {
	
	public void assertEquals(byte[] arg0, byte[]arg1){
		this.assertEquals(arg0, arg1, true);
	}
	
	public void assertEquals(byte[] arg0, byte[] arg1, boolean lookValue){
		if(lookValue){
			assertEquals(arg0.length, arg1.length);
			for(int i = 0; i < arg0.length; i++) assertEquals(arg0[i], arg1[i]);
		} else {
			super.assertEquals(arg0, arg1);
		}
	}
}
