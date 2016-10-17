/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.bit;

import bincat.type.Range;
import java.util.Arrays;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author eric
 */
public class BitRecordTest
    extends TestCase
{
    public static final Logger LOG = 
        LoggerFactory.getLogger( BitRecordTest.class );
    
    public void testBitRecord()
    {
        BitFrame a = BitFrame.of(
            Range.of( 0, Integer.MAX_VALUE ), "A",
            Range.of( 0, Integer.MAX_VALUE ), "B"       
        );
        
        BitFrame b = BitFrame.of(
            Range.of( 0, Integer.MAX_VALUE ), "C",
            Range.of( 0, Integer.MAX_VALUE ), "D"       
        );
        
        BitRecord abcd = BitRecord.of( a, b );
        
        LOG.debug( abcd.describe() );
        
        //verify pack and unpack are reversable
        long[] packed0123 = abcd.pack( 0, 1, 2, 3 );
        LOG.debug( abcd.describe( packed0123 ) );
        
        Object[] values = abcd.unpack( packed0123 );
        assertEquals( 0L, values[ 0 ] );
        assertEquals( 1L, values[ 1 ] );
        assertEquals( 2L, values[ 2 ] );
        assertEquals( 3L, values[ 3 ] );
        
        //OK lets saturation test synthesize, isValid, load, 
        //pack and unpack verify they are reversable
        int iterations = 10000;
        for( int i = 0; i < iterations; i++ )
        {
            long[] synthesized = abcd.synthesize();
            assertTrue( abcd.isValid( synthesized ) );
            long A = (long)abcd.loadObject( "A", synthesized );
            long B = (long)abcd.loadObject( "B", synthesized );
            long C = (long)abcd.loadObject( "C", synthesized );
            long D = (long)abcd.loadObject( "D", synthesized );
            
            //now pack everything back in
            long[] packed = abcd.pack( A, B, C, D );
            
            if( ! Arrays.equals( packed, synthesized ) )
            {
                LOG.debug( abcd.describe( synthesized ) );
                LOG.debug( abcd.describe( packed ) );
            }
            //verify that I got the same result
            //assertTrue(  );
            
            Object[] unpackedValues = abcd.unpack( packed );
            
            assertEquals( A, unpackedValues[ 0 ] );
            assertEquals( B, unpackedValues[ 1 ] );
            assertEquals( C, unpackedValues[ 2 ] );
            assertEquals( D, unpackedValues[ 3 ] );            
        }
    }
}
