package bincat.type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import junit.framework.TestCase;

/**
 *
 * @author eric
 */
public class DayRangeTest
    extends TestCase
{
    
    public void testSelfAuthor()
    {
        DayRange dr = DayRange.of( "2016-01-01", "2016-12-31" );
        assertEquals( "DayRange.of( \"2016-01-01\", \"2016-12-31\" )", dr.selfAuthor() );
    }
    public void testDayRange() throws ParseException
    {
        DayRange dr = DayRange.of( "2016-01-01", "2016-12-31" );
        assertEquals( 9, dr.bitCount() );
        
        SimpleDateFormat sdf = new SimpleDateFormat( DayRange.DATE_FORMAT  );
        
        Date startDate = sdf.parse( "2016-01-01" );
        Date endDate = sdf.parse( "2016-12-31" );
        
        //2016 is a leap year
        
        //storing will return the binary that 
        assertEquals( 0, dr.store( startDate ) );
        assertEquals( 365, dr.store( endDate ) );        
        
        //loading will read from the binary to return the value
        assertEquals( startDate, dr.load( 0 ) );
        assertEquals( endDate, dr.load( 365 ) );        
    } 
    
    public void testSingleDay()
    {
        DayRange dr = DayRange.of( "2016-01-01", "2016-01-02" );
        assertEquals( 1, dr.bitCount() ); 
        
        assertEquals( DayRange.dateOf("2016-01-01"), dr.load(0) ); 
        assertEquals( DayRange.dateOf("2016-01-02"), dr.load(1) ); 
        
        assertEquals( 0, dr.store( DayRange.dateOf("2016-01-01") ) );
        assertEquals( 1, dr.store( DayRange.dateOf("2016-01-02") ) );
    }
    
    public void testSynthesize()
    {
        DayRange dr = DayRange.of( "2016-01-01", "2016-01-02" );
        assertEquals( 1, dr.bitCount() );
        
        int mask = 0;
        int oneCount = 0;
        int iterations = 100;
        
        //lets synthesize a bunch and make sure
        // we get between 40 and 60 % 1s
        for(int i=0; i< iterations; i++)
        {
            if( dr.synthesizeBin() == 1L)
            {
                oneCount ++;
            }
        }
        double pct1es = oneCount * 1.0d / iterations * 1.0d;
        
        //should be about 50 %
        assertTrue( pct1es < 0.6d);
        assertTrue( pct1es > 0.4d);
    }
    
}
