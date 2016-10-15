/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.match;

import bincat.Field;
import bincat.frame.BitField;
import bincat.type.Range;
import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

/**
 *
 * @author eric
 */
public enum RowMatch
{
    ;
    
   
    
    
    public enum LongRow 
    {
        ;            
        
        public static final boolean arrayContains( long[] array, long target )
        {
            for(int i=0; i< array.length; i++ )
            {
                if( array[i] == target )
                {
                    return true;
                }
            }
            return false;
        }
        
        public static LongFunction<Boolean> oneOf( BitField field, Object...values )
        {
            final long[] targets = new long[ values.length ];
            for( int i = 0; i < values.length; i++)
            {
                targets[ i ] = field.storeLong( values[ i ] );
            }
            Arrays.sort( targets );
            //return (it) -> arrayContains( targets, (it & field.longMask() ) ); 
            return it -> Arrays.binarySearch( targets, it & field.bitMask64 ) >= 0;
            /*
            for( int i = 0; i < targets.length; i++ )
                {
                    if( targets[ i ] == ( it & longMask) )
                    {
                        return true;
                    }
                }
                return false;
            */
            //Arrays.binarySearch ( targets, it & field.longMask ) >= 0;             
        }
        
        public static LongFunction<Boolean> equal( 
            BitField field, Object value )
        {
            final long target = field.storeLong( value );
            return it -> ( ( it & field.bitMask64 ) ) == target;
        }
                
        public static LongFunction<Boolean> equal( 
            Field field, int shift, Object value )
        {
            final long mask = ( -1L >>> ( 64 - field.getType().bitCount() ) ) << shift;
            final long target = field.getType().storeObject( value );
            return it -> ( ( it & mask ) >>> shift ) == target;
        }
        
        public static LongFunction<Boolean> lessThan( 
            Field field, final int shift, Object value )
        {
            final long mask = ( -1L >>> ( 64 - field.getType().bitCount() ) ) << shift;
            final long target = field.getType().storeObject( value );
            return it -> ( ( it & mask ) >>> shift ) < target;
        }
        
        public static LongFunction<Boolean> lessThanEqualTo( 
            Field field, int shift, Object value )
        {
            final long mask = ( -1L >>> ( 64 - field.getType().bitCount() ) ) << shift;
            final long target = field.getType().storeObject( value );
            return it -> ( ( it & mask ) >>> shift) <= target;
        }
        
        public static LongFunction<Boolean> greaterThan( 
            Field field, int shift, Object value )
        {
            final long mask = ( -1L >>> ( 64 - field.getType().bitCount() ) ) << shift;
            final long target = field.getType().storeObject( value );
            return it -> ( ( it & mask ) >>> shift ) > target;
        }
        
        public static LongFunction<Boolean> greaterThanEqualTo( 
            Field field, int shift, Object value )
        {
            final long mask = ( -1L >>> ( 64 - field.getType().bitCount() ) ) << shift;
            final long target = field.getType().storeObject( value );
            return it -> ( ( it & mask ) >>> shift ) >= target;
        }
        
        public static LongFunction<Boolean> between( 
            Field field, int shift, Object value )
        {
            final long mask = ( -1L >>> ( 64 - field.getType().bitCount() ) ) << shift;
            final long target = field.getType().storeObject( value );
            return it -> (
                ( ( it & mask ) >>> shift ) >= target &&
                ( ( it & mask ) >>> shift ) <= target );                           
        }
    }
}
