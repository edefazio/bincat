/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.match;

import bincat.frame.BitField;
import java.util.Arrays;
import java.util.function.LongFunction;

/**
 * 
 * @author eric
 */
public enum WhereLongRow
{
    ;
        
    /**
     * Accepts a Binary Long "Row" as input and determines whether
     * a given Row matches based on some criteria
     */
    public interface LongRowMatch
    {
        /** 
         * A Function that can be used to Match a LongRow based on criteria
         * 
         * @return the Lambda function for matching a row
         */
        public LongFunction<Boolean> fn();
    }

    /** Matches a Field within the 64-bit Row is equal to the value */
    public static class MatchEqual
        implements LongRowMatch
    {
        public final BitField field;
        public final long targetBin;
        
        public MatchEqual( BitField field, Object value )
        {
            this.field = field;
            targetBin = field.storeLong( value );            
        }
        
        @Override
        public LongFunction<Boolean> fn()
        {
            return it -> targetBin == ( it & field.bitMask64 );
        }
    }
    
    /** Matches a Field within the 64-bit Row is less (bitwise) than the value */
    public static class MatchLessThan
        implements LongRowMatch
    {
        public final BitField field;
        public final long targetBin;
        
        public MatchLessThan( BitField field, Object value )
        {
            this.field = field;
            //get the normalized value, since we have to deal with 2s compliment
            targetBin = field.field.type.storeObject( value );            
        }
        
        @Override
        public LongFunction<Boolean> fn()
        {
            return it -> ( it & field.bitMask64 ) >> field.shift < targetBin;
        }
    }
    
    /** Matches a Field within the 64-bit Row is less (bitwise) than the value */
    public static class MatchLessThanEqual
        implements LongRowMatch
    {
        public final BitField field;
        public final long targetBin;
        
        public MatchLessThanEqual( BitField field, Object value )
        {
            this.field = field;
            //get the normalized value, since we have to deal with 2s compliment
            targetBin = field.field.type.storeObject( value );            
        }
        
        @Override
        public LongFunction<Boolean> fn()
        {
            return it -> ( it & field.bitMask64 ) >> field.shift <= targetBin;
        }
    }
    
    /** Matches a Field within the 64-bit Row is less (bitwise) than the value */
    public static class MatchGreaterThan
        implements LongRowMatch
    {
        public final BitField field;
        public final long targetBin;
        
        public MatchGreaterThan( BitField field, Object value )
        {
            this.field = field;
            //get the normalized value, since we have to deal with 2s compliment
            targetBin = field.field.type.storeObject( value );            
        }
        
        @Override
        public LongFunction<Boolean> fn()
        {
            return it -> ( it & field.bitMask64 ) >> field.shift > targetBin;
        }
    }
    
        /** Matches a Field within the 64-bit Row is less (bitwise) than the value */
    public static class MatchGreaterThanEqual
        implements LongRowMatch
    {
        public final BitField field;
        public final long targetBin;
        
        public MatchGreaterThanEqual( BitField field, Object value )
        {
            this.field = field;
            //get the normalized value, since we have to deal with 2s compliment
            targetBin = field.field.type.storeObject( value );            
        }
        
        @Override
        public LongFunction<Boolean> fn()
        {
            return it -> ( it & field.bitMask64 ) >> field.shift >= targetBin;
        }
    }
    
    /** Matches a Field within the 64-bit Row is less (bitwise) than the value */
    public static class MatchBetween
        implements LongRowMatch
    {
        public final BitField field;
        public final long minBin;
        public final long maxBin;
        
        public MatchBetween( BitField field, Object min, Object max )
        {
            this.field = field;
            //get the normalized value, since we have to deal with 2s compliment
            minBin = field.field.type.storeObject( min );            
            maxBin = field.field.type.storeObject( max );            
        }
        
        @Override
        public LongFunction<Boolean> fn()
        {
            return it -> ( it & field.bitMask64 ) >> field.shift >= minBin
               && ( it & field.bitMask64 ) >> field.shift <= maxBin;
        }
    }
    
    /** Match a BitField within a row is one of these target values */
    public static class MatchOneOf
        implements LongRowMatch
    {
        public final BitField field;
        
        public final long[] targetBins;
        
        public MatchOneOf( BitField field, Object...values )
        {
            this.field = field;
            targetBins = new long[ values.length ];
            for( int i = 0; i < values.length; i++ ) 
            {
                targetBins[ i ] = field.storeLong( values[ i ] );
            }
            Arrays.sort(targetBins );
        }
        
        @Override
        public LongFunction<Boolean> fn()
        {
            return it -> Arrays.binarySearch( targetBins, it & field.bitMask64 ) >= 0;
        }
    }    
}
