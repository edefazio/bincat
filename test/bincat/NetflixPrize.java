/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat;

import bincat.frame.BitField;
import bincat.frame.LongFrame;
import bincat.frame.LongFrame_Author;
import bincat.type.DayRange;
import bincat.type.Range;
import junit.framework.TestCase;
import varcode.java.code._class;

/**
 *
 * @author eric
 */
public class NetflixPrize
    extends TestCase
{
    static Row n = new Row(
        new Field( Range.of(1, 17770), "movieId" ),
        new Field( Range.of(1, 5), "rating" ),    
        new Field( Range.of(1, 480196), "personId" ),                
        new Field( DayRange.of("01-01-1996", "12-31-2012" ), "day" ) );
    
    static LongFrame NetflixFrame = new LongFrame( n );
        
    
    public void testAuthor()
    {
        _class netflixFrame = LongFrame_Author.of( 
            "bincat.frame.authored", "NetflixFrame", NetflixFrame );
        
        System.out.println( netflixFrame );
        
        Class c = netflixFrame.loadClass();
    }
    
    public void testNetflix()
    {        
        //System.out.println( NetflixRow.bitCount() );
        
        long firstRow = NetflixFrame.pack( 1, 1, 1, "01-01-1996" );
        
        System.out.println( NetflixFrame.describe( firstRow ) );
        
        //verify the first row (being the first element 
        assertEquals( 0L, firstRow );
        assertEquals(1L, NetflixFrame.load( firstRow, "movieId" ) );
        assertEquals(1L, NetflixFrame.load( firstRow, "rating" ) );
        assertEquals(1L, NetflixFrame.load( firstRow, "personId" ) );
        assertEquals(DayRange.dateOf( "01-01-1996" ), NetflixFrame.load( firstRow, "day" ) );
        
        long lastRow = NetflixFrame.pack( 17770, 5, 480196, "12-31-2012" );
        
        System.out.println(NetflixFrame.describe( lastRow ) );
        /*
        System.out.println( "ROW : "  + Long.toBinaryString( lastRow ) );
        
        System.out.println( "MOVIEID   : " + NetflixRow.getField("movieId").extractLongBits(lastRow) );
        System.out.println( "RATING    : " + NetflixRow.getField("rating").extractLongBits(lastRow) );
        System.out.println( "PERSONID  : " + NetflixRow.getField("personId").extractLongBits(lastRow) );
        System.out.println( "DAY       : " + NetflixRow.getField("day").extractLongBits(lastRow) );
        */
        
        Object[] values = NetflixFrame.unpack( lastRow );        
        assertEquals( 17770L, values[ 0 ] );
        assertEquals( 5L, values[ 1 ] );
        assertEquals( 480196L, values[ 2 ] );
        assertEquals( DayRange.dateOf( "12-31-2012" ), values[ 3 ] );

        System.out.println(NetflixFrame.describe(NetflixFrame.synthesize() ) ); 
        
        assertEquals(17770L, NetflixFrame.load( lastRow, "movieId" ) );
        assertEquals(5L, NetflixFrame.load( lastRow, "rating" ) );
        assertEquals(480196L, NetflixFrame.load( lastRow, "personId" ) );
        assertEquals(DayRange.dateOf( "12-31-2012" ), NetflixFrame.load( lastRow, "day" ) );
        
        
        BitField f0 = NetflixFrame.getField( "movieId" );
        BitField f1 = NetflixFrame.getField( "rating" );
        BitField f2 = NetflixFrame.getField( "personId" );
        BitField f3 = NetflixFrame.getField( "day" );
        
        //synthesize 10,000 netflix rows, load them individually 
        //describe them, then unpack them
        for( int i = 0; i < 10000; i++ )
        {
            long synth = NetflixFrame.synthesize();
            f0.loadObject( synth );
            f1.loadObject( synth );
            f2.loadObject( synth );
            f3.loadObject( synth );
            
            NetflixFrame.describe( synth );
            
            Object[] unpacked = NetflixFrame.unpack( synth );
        }
        
    }    
}
