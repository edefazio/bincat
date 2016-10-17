/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat;

import bincat.bit.BitFieldTest;
import bincat.bit.BitFrame;
import bincat.type.Range;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author eric
 */
public class RowTest
     extends TestCase
{
    public static final Logger LOG = 
        LoggerFactory.getLogger( RowTest.class );
    
    public void testRowEZ()
    {
        //This is how I'd like to do it' 
        /**    
         * Define the row of constrained fields
         */
        Record row = Record.of(
            "1...480196", "personId",  //these are fieldConstraint
            "1...17770","movieId", 
            "1995-01-01...2009-01-01", "date", 
            "1...5", "rating" );
        
        BitFrame lf = BitFrame.of( row );
        LOG.debug( lf.describe() );
    }
    
    public void testRow()
    {
        Record r = new Record( 
            new Field( Range.of( 0, 100 ), "a" ) ); 
        
        assertEquals( 7, r.bitCount() );
        assertEquals( 1, r.fieldCount() );        
    }
    
}
