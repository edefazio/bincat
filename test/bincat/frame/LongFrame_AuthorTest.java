/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.frame;

import bincat.Field;
import bincat.Row;
import bincat.type.DayRange;
import bincat.type.Range;
import junit.framework.TestCase;
import varcode.java.Java;
import varcode.java.code._class;

/**
 *
 * @author eric
 */
public class LongFrame_AuthorTest
    extends TestCase
{
    public void testLongFrame_Author()
    {
         Row Netflix = new Row(
            new Field( Range.of( 1, 17770 ), "movieId" ),
            new Field( Range.of( 1, 480196 ), "personId" ),
            new Field( Range.of( 1, 5 ), "rating" ),
            new Field( DayRange.of( "1996-01-01", "2008-12-31" ), "day" )                    
        );
        
        LongFrame netflixPrize = new LongFrame( Netflix );
        
        _class c = LongFrame_Author.of(
            "bitcat.frame.authored", "NetflixPrizeFrame", netflixPrize );
        
        System.out.println( c );
        
        Class nfClass = c.loadClass();
        
        String described = 
            (String) Java.invoke( nfClass, "describeRow", 
                Java.invoke( nfClass, "synthesize" ) );
        
        System.out.println( described );
    }
}
