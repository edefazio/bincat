/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat;

import bincat.frame.BitFrame;
import bincat.type.Range;
import junit.framework.TestCase;

/**
 *
 * @author eric
 */
public class RowTest
     extends TestCase
{
    public void testRowEZ()
    {
        //This is how I'd like to do it' 
        /**    
         * Define the row of constrained fields
         */
        Row row = Row.of(
            "1...480196", "personId",  //these are fieldConstraint
            "1...17770","movieId", 
            "1995-01-01...2009-01-01", "date", 
            "1...5", "rating" );
        
        BitFrame lf = BitFrame.of( row );
        System.out.println( lf );
    }
    
    public void testRow()
    {
        Row r = new Row( 
            new Field( Range.of( 0, 100 ), "a" ) ); 
        
        assertEquals( 7, r.bitCount() );
        assertEquals( 1, r.fieldCount() );        
    }
    
}
