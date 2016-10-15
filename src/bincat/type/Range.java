/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.type;

import bincat.BinCatException;
import bincat.synthesize.Synthesize;

/**
 * this is a 0, or positive int range, that MUST have at least (2) states
 * (0, or 1)
 * 
 * @author eric
 */
public class Range
    implements Type
{
    public static Range of( long min, long max )
    {
        return new Range( min, max );
    }
    
    public final long min;
    public final long max;
    
    @Override
    public int bitCount()
    {
        return 64 - Long.numberOfLeadingZeros( max - min );
    }
    
    public Range( long min, long max )
    {
        this.min = min;
        this.max = max;
    }
       
    @Override
    public String toString()
    {
        return "[0..." + ( max - min ) + "]<->[" + min + "..." + max + "]";
    }
    
    public long store( long value )
    {
        long res = value - min;
        if( res < 0 )
        {
            throw new BinCatException(
                "Bad value " + value + " not in [0..." + ( max - min ) + "]" );
        }
        return res;
    }
    
    public long load( long bin )
    {
        return bin + min;
    }

    @Override
    public long storeObject( Object value )
    {
        if( value instanceof Integer )
        {
            return store( ((Integer)value).longValue() );
        }
        return store( (Long)value );        
    }

    @Override
    public Object loadObject( long bin )
    {
        return load( bin );
    }
    
    @Override
    public long synthesizeBin()
    {
        return Synthesize.RANDOM.nextLong( ( max - min ) + 1 );
    }
    
    @Override
    public String selfAuthor()
    {
        return "Range.of( " + min + ", " + max + " )";
    }
}
