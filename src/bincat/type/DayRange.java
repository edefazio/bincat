/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.type;

import bincat.BinCatException;
import bincat.synthesize.Synthesize;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * a Day Range is something like "01-01-1970" to "12-31-1970" 
 * basically day 0 is the start day
 * where we store 
 * <PRE>
 * bin    "actual" date
 * 0    = 01-01-1970
 * 1    = 01-02-1970
 * 2    = 01-03-1970
 * ...
 * 364    12-31-1970
 * </PRE>
 * 
 * @author eric
 */
public class DayRange
    implements Type
{
    public static final Date dateOf( String string )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( DATE_FORMAT );
        try 
        {
            return sdf.parse( string );
        }
        catch( ParseException ex ) 
        {
            throw new BinCatException( "Bad data format \""+ string+"\"");
        }
    }
    public static final String DATE_FORMAT = "yyyy-MM-dd";
            
    public final Date startDate;
    public final int days;
    
    public static DayRange of( String startDate, String endDate )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( DATE_FORMAT );
        Date start = null;
        Date end = null;
        try 
        {
            start = sdf.parse( startDate );
        }
        catch( ParseException ex ) 
        {
            throw new RuntimeException( ex ); 
        }        
        try 
        {
            end = sdf.parse( endDate );
        }
        catch( ParseException ex ) 
        {
            throw new RuntimeException( ex ); 
        }   
        //Calendar cal = GregorianCalendar.getInstance();
        //cal.setTime( start );
        return new DayRange( 
            start, 
            (int)daysBetween( start, end ) );
    }
    
    public DayRange( Date startDate, int days )
    {
        this.startDate = startDate;
        this.days = days;
    }
    
    private static long daysBetween( Date startDate, Date endDate )            
    {
        //time is always 00:00:00 so rounding should help to ignore the missing 
        //hour when going from winter to summer time as well as the extra 
        //hour in the other direction
        return Math.round(
            (endDate.getTime() - startDate.getTime()) / (double) 86400000 );
    }
 
    public boolean isValidBin( long bin )
    {
        return bin <= days && bin >= 0;
    }
    
    public Date load( long bin )
    {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime( startDate );
        c.add( Calendar.DAY_OF_YEAR, (int)bin);
        return c.getTime();
    }
    
    public int store( Date value )
    {
        return (int)daysBetween( this.startDate, value );
    }
    
    public int store( String value )
    {
        return (int)daysBetween( this.startDate, dateOf( value ) );
    }
    
    @Override
    public int bitCount()
    {
        return 32 - Integer.numberOfLeadingZeros( days );
    }

    @Override
    public long storeObject( Object value )
    {
        if( value instanceof Date)
        {
            return store( (Date)value );
        }
        if( value instanceof String )
        {
            SimpleDateFormat sdf = new SimpleDateFormat( DATE_FORMAT );
            try 
            {
                return store( sdf.parse( (String)value ) );
            }
            catch( ParseException ex ) 
            {
                throw new BinCatException( "Bad input \""+ value +"\"" );
            }
        }
        throw new BinCatException( "Bad input \""+ value +"\"" );        
    }
    
    @Override
    public String toString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat( DATE_FORMAT );
        return "[0..." + days + "]<->[" 
            + sdf.format( startDate ) + "..." + sdf.format( load( days ) ) + "]";
    }

    @Override
    public Object loadObject( long bin )
    {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime( startDate );
        c.add( Calendar.DAY_OF_YEAR, (int)bin);
        return c.getTime();
    }

    @Override
    public long synthesizeBin()
    {
        return Synthesize.RANDOM.nextInt( days  + 1 );
    }
    
    public String formatDate( Date date )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( DATE_FORMAT );
        return sdf.format( date );
    }
    
    @Override
    public String selfAuthor()
    {
        return "DayRange.of( \"" 
            + formatDate( startDate )+"\", \""+ formatDate( load( days ) )+"\" )";
    }
}
