/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.bit;

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
public class BitField_AuthorTest
    extends TestCase
{
    public static final Logger LOG = 
        LoggerFactory.getLogger( BitField_AuthorTest.class );
    
    public void testNF()
    {
        BitFrame lf = BitFrame.of(
            "1...17770", "movieId",
            "1...5", "rating",
            "1...480196", "personId",
            "01-01-1996...12-31-2008", "day" );
        
        _class movieId = BitField_Author.of( lf.getFieldAt( 0 ) );
        _class rating = BitField_Author.of( lf.getFieldAt( 1 ) );
        _class personId = BitField_Author.of( lf.getFieldAt( 2 ) );
        _class day = BitField_Author.of( lf.getFieldAt( 3 ) );
        
        //load MovieId
        LOG.debug( movieId.toString() );
        
        Class movieIdClass = movieId.loadClass();
        assertEquals( "MovieId", movieIdClass.getSimpleName() );
        String describedFrame = (String)
            Java.invoke( movieIdClass, "describeFrame",  Java.invoke( movieIdClass, "synthesize") );
        
        LOG.debug( describedFrame );
        /*
        System.out.println( movieId );
        System.out.println( rating );
        System.out.println( personId );
        System.out.println( day );
        */
    }
    
    public void atestAuthor()
    {
        BitField bf = BitField.of(Range.of( 0, 100 ), "count" );
        _class c = BitField_Author.of( bf );
        
        LOG.debug( c.toString() );
        
        Object instance = c.instance( );
        
        LOG.debug( Java.invoke( instance.getClass(), "describeFrame", 100L ).toString() );
        
        //I can modify the class to make it static 
        //c.getSignature().getModifiers().set( "static" );
        //System.out.println( c );
        
        BitField day = BitField.of( 
            DayRange.of( "1980-01-01", "1980-12-31" ), "date", 3 );
        
        _class drbf = BitField_Author.of( day );
        
        instance = drbf.instance( );
        
        //System.out.println( drbf );
        LOG.debug( instance.toString() );
        long store = (long)Java.invoke( instance, "store", "12-31-1980" );        
        LOG.debug( Java.invoke( instance, "describeFrame", store ).toString() );
        
        /*
        System.out.println( 
            Java.invoke( instance, "load", store ) );
        */
    }
}
