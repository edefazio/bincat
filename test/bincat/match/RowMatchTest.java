/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.match;

import bincat.match.RowMatch;
import bincat.Field;
import bincat.frame.BitFrame;
import bincat.Row;
import bincat.type.DayRange;
import bincat.type.Range;
import java.util.function.LongFunction;
import junit.framework.TestCase;

/**
 *
 * @author eric
 */
public class RowMatchTest
     extends TestCase
{
    static Row n = new Row(
        new Field( Range.of( 1, 17770 ), "movieId" ),        
        new Field( Range.of( 1, 480196 ), "personId" ),                
        new Field( DayRange.of( "01-01-1996", "12-31-2012" ), "day" ),
        new Field( Range.of( 1, 5 ), "rating" ) );
    
        
    static BitFrame NetflixRow = new BitFrame( n );
    
    public void testSynthesizeMatch()
    {
        
        /*
        LongFunction<Boolean> matcher = 
            RowMatch.LongRowField.equal( n.getField( "rating" ), 0, 1 );
        */
        
        LongFunction<Boolean> matchRatingEqual5 = 
            RowMatch.LongRow.equal( 
                NetflixRow.getField( "rating" ), 5 );
        
        
        LongFunction<Boolean> matchDayOneOf = 
            RowMatch.LongRow.oneOf( 
                NetflixRow.getField( "day" ), 
                    "01-02-1996", "12-25-2006", "10-31-2009" );
        
        /*
        LongFunction<Integer> whereDayOneOf = 
            Where.LongRow.oneOf( 
                NetflixRow.getField( "day" ), 
                    "01-02-1996", "12-25-2006", "10-31-2009" );
                //"means" "WHERE rating = 1"
        */        
        int ratingMatches = 0;
        int dayMatches = 0;
        int iterations = 10000000;
        
        for( int i = 0; i < iterations; i++ )
        {
            long row = NetflixRow.synthesize();
            /*
            if( matchRatingEqual5.apply( row ) )
            {
                ratingMatches++;
            } 
            */
            //dayMatches += whereDayOneOf.apply( row );
            
            if( matchDayOneOf.apply( row ) )
            {
                dayMatches++;
            }

        }
        
        //we expect matching about 20% matching for rating , since it 
        // with there is a 20% chance of choosing between 5 options randomly
        System.out.println("Matched Ratings " + ratingMatches + " of "+ iterations );
        System.out.println("Matched Ratings " + ratingMatches * 1.0d / iterations * 1.0d + "% ");
        
        System.out.println("Matched DAY " + dayMatches + " of "+ iterations );
        System.out.println("Matched DAY " + dayMatches * 1.0d / iterations * 1.0d + "% ");
    }
    
    
}
