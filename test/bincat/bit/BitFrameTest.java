/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.bit;

import bincat.bit.BitFrame;
import bincat.Field;
import bincat.Record;
import bincat.type.DayRange;
import bincat.type.Range;
import junit.framework.TestCase;

/**
 *
 * @author eric
 */
public class BitFrameTest
    extends TestCase
{
    public void testLongRow()
    {
        Record Netflix = new Record(
            new Field( Range.of( 1, 17770 ), "movieId" ),
            new Field( Range.of( 1, 480196 ), "personId" ),
            new Field( Range.of( 1, 5 ), "rating" ),
            new Field( DayRange.of( "1996-01-01", "2008-12-31" ), "day" )                    
        );
        
        BitFrame netflixPrize = new BitFrame( Netflix );
        
        long first = netflixPrize.pack( 1,1,1,"1996-01-01" );
        
        System.out.println( netflixPrize );
        
        Object[] unpacked = netflixPrize.unpack( first );
        
        assertEquals( 1L, unpacked[ 0 ] );
        assertEquals( 1L, unpacked[ 1 ] );
        assertEquals( 1L, unpacked[ 2 ] );
        assertEquals( DayRange.dateOf( "1996-01-01" ), unpacked[ 3 ] );        
        
        
        System.out.println( Long.toBinaryString(netflixPrize.getField("movieId").mask ));
        System.out.println( Long.toBinaryString(netflixPrize.getField("personId").mask ));
        System.out.println( Long.toBinaryString(netflixPrize.getField("rating").mask ));
        System.out.println( Long.toBinaryString(netflixPrize.getField("day").mask ));
        
        long last = netflixPrize.pack( 17770,480196,5,"2008-12-31" );
        
        unpacked = netflixPrize.unpack( last );
        
        assertEquals( 17770L, unpacked[ 0 ] );
        assertEquals( 480196L, unpacked[ 1 ] );
        assertEquals( 5L, unpacked[ 2 ] );
        assertEquals( DayRange.dateOf( "2008-12-31" ), unpacked[ 3 ] );        
        
        
        
    }
}
