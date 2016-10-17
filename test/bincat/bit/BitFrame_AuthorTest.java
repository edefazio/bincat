/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.bit;

import bincat.Field;
import bincat.Record;
import bincat.type.DayRange;
import bincat.type.Range;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import varcode.java.Java;
import varcode.java.code._class;

/**
 *
 * @author eric
 */
public class BitFrame_AuthorTest
    extends TestCase
{
    public static final Logger LOG = 
        LoggerFactory.getLogger( BitFrame_AuthorTest.class );
    
    public void testLongFrame_Author()
    {
         Record Netflix = new Record(
            new Field( Range.of( 1, 17770 ), "movieId" ),
            new Field( Range.of( 1, 480196 ), "personId" ),
            new Field( Range.of( 1, 5 ), "rating" ),
            new Field( DayRange.of( "1996-01-01", "2008-12-31" ), "day" )                    
        );
        
        BitFrame netflixPrize = new BitFrame( Netflix );
        
        _class c = BitFrame_Author.of(
            "bitcat.frame.authored", "NetflixPrizeFrame", netflixPrize );
        
        LOG.debug( c.toString() );
        
        Class nfClass = c.loadClass();
        
        String described = 
            (String) Java.invoke( nfClass, "describeRow", 
                Java.invoke( nfClass, "synthesize" ) );
        
        LOG.debug( described );
        
        long packed = (long)Java.invoke(
            nfClass, "pack", 17770L, 480196L, 5L, DayRange.dateOf("2008-12-31")
        );
        
        
    }
}
