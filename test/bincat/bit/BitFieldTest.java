/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.bit;

import bincat.type.Range;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author eric
 */
public class BitFieldTest
    extends TestCase
{
    public static final Logger LOG = 
        LoggerFactory.getLogger( BitFieldTest.class );
    
    public void testNotShifted()
    {
        //note right now the fields dont validate, if I add this in, could break
        BitField bf = BitField.of(Range.of( 0, 100 ), "a" );
        //assertEquals( 0b1111111, bf.intMask() );
        assertEquals( 0b1111111, bf.mask() );
        //int bits = bf.extractIntBits( -1 );
        //assertEquals( 0b1111111, bits );
        long bitsL = bf.extractBits( -1L );
        assertEquals( 0b1111111L, bitsL ); 
        
        assertEquals( 0b1111111L, bf.loadObject( -1 ) );
        assertEquals( 0b1111111L, bf.loadObject( -1L ) );
        
        //assertEquals(0, bf.storeInt(0));
        assertEquals(0L, bf.storeObject(0));
        
        //assertEquals(100, bf.storeInt(100));
        assertEquals(100L, bf.storeObject(100));
        
        for(int i=0; i < 1000; i++)
        {
            assertTrue( bf.synthesizeBin() <= 100 );
            assertTrue( bf.synthesizeBin() >= 0 );
        }
        for(int i=0; i < 1000; i++)
        {
            assertTrue( bf.synthesizeBin() <= 100 );
            assertTrue( bf.synthesizeBin() >= 0 );
        }        
    }
    
    public void testShift1()
    {
        BitField bf = BitField.of(Range.of(0,1), "a", 1 );
        //assertEquals( 0b10, bf.intMask() );
        assertEquals( 0b10L, bf.mask() );
        //assertEquals( 0b1, bf.extractIntBits(-1) );
        assertEquals( 0b1L, bf.extractBits(-1L) );
        
        //assertEquals( 0b0, bf.storeInt( 0 ) );
        //assertEquals( 0b10, bf.storeInt( 1 ) );
        
        assertEquals( 0b0, bf.storeObject( 0 ) );
        assertEquals( 0b10, bf.storeObject( 1 ) );
        
    }
}
