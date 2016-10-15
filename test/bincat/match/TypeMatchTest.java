/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.match;

import bincat.type.Range;
import java.util.function.LongFunction;
import junit.framework.TestCase;

/**
 *
 * @author eric
 */
public class TypeMatchTest
    extends TestCase
{
    public void testMatchEquals()
    {
        Range range = Range.of( 0, 1 );
        LongFunction<Boolean> match0 = 
            TypeMatch.LongType.equal( range, 0 );
        
        assertTrue( match0.apply( 0 ) );
        assertFalse( match0.apply( 1 ) );
        
        LongFunction<Boolean> match1 = 
            TypeMatch.LongType.equal( range, 1 );
        
        assertFalse( match1.apply( 0 ) );
        assertTrue( match1.apply( 1 ) );        
    }
     
    public void testMatchGreaterThan()
    {
        Range range = Range.of( 0, 100 );
        LongFunction<Boolean> matchGt = TypeMatch.LongType.greaterThan( range, 50 );
        assertFalse( matchGt.apply( 50 ) ); 
        assertTrue( matchGt.apply( 51 ) ); 
        
    }
    
    public void testMatchBetween()
    {
        Range range = Range.of( 0, 100 );
        LongFunction<Boolean> matchBetween = TypeMatch.LongType.between( range, 50, 75 );
        assertTrue( matchBetween.apply(50));
        assertTrue( matchBetween.apply(75));
        assertFalse( matchBetween.apply(76));
        assertFalse( matchBetween.apply(78));
        assertFalse( matchBetween.apply( 49 ) );
    }
    
}
