/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.type;

import junit.framework.TestCase;

/**
 *
 * @author M. Eric DeFazio 
 */
public class IntRangeTest
    extends TestCase
{
    public void testIntRange()
    {
        Range ir = new Range(1, 100);
        assertEquals( 7, ir.bitCount() );
        
        //loading will read from the binary to return the value
        assertEquals( 1, ir.load( 0 ) );
        assertEquals( 100, ir.load( 99 ) );
        
        //storing will return the binary that 
        assertEquals( 0, ir.store( 1 ) );
        assertEquals( 99, ir.store( 100 ) );        
    }    
    
    public void testSynthesize()
    {
        Range ir = new Range( 0, 1 );
        assertEquals( 1, ir.bitCount() );
        
        int mask = 0;
        int oneCount = 0;
        int iterations = 100;
        
        //lets synthesize a bunch and make sure
        // we get between 40 and 60 % 1s
        for(int i=0; i< iterations; i++)
        {
            if( ir.synthesizeBin() == 1L)
            {
                oneCount ++;
            }
        }
        double pct1es = oneCount * 1.0d / iterations * 1.0d;
        
        //should be about 50 %
        assertTrue( pct1es < 0.6d);
        assertTrue( pct1es > 0.4d);
    }
}
