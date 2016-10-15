
import bincat.synthesize.XorShiftRandomThreadSafe;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author eric
 */
public class TestFastBoundedIntRandom
{
    
    public static void main( String[ ] args )
    {
        int count = 1000000000;
        oldBoundWay( count );
        newBoundWay( count );
    }
    
    public static int fastBounded( XorShiftRandomThreadSafe xor, int bound )
    {
        return (int)( ( ( xor.randomLong() >>> 32 ) * bound ) >>> 32 );
    }
    
    public static void newBoundWay( int count )
    {
        long odds = 0;
        XorShiftRandomThreadSafe xor = new XorShiftRandomThreadSafe();
        long start = System.currentTimeMillis();
        for( int i=0; i < count; i++ )
        {
            odds += fastBounded( xor, 3287612 )  | 1;
        }
        long end = System.currentTimeMillis();
        
        System.out.println( "TOOK "+ (end - start) +" FOUND "+ odds );
    }
    
    public static void oldBoundWay( int count )
    {
        long odds = 0;
        XorShiftRandomThreadSafe xor = new XorShiftRandomThreadSafe();
        long start = System.currentTimeMillis();
        for( int i=0; i < count; i++ )
        {
            odds += ( xor.nextInt( 3287612 ) | 1 );
        }
        long end = System.currentTimeMillis();
        
        System.out.println( "TOOK "+ (end - start) +" FOUND "+ odds );
    }
}
