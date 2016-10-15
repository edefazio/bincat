/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.match;

import bincat.type.Type;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

/**
 *
 * Creates and returns Match functions
 * 
 * @author M. Eric DeFazio eric@varcode.io
 */
public enum TypeMatch
{
    ;
    
    
    public enum LongType
    {
        ;
            
        /** 
         * return me a function that accepts an int and determines 
         * the value at this ints BIN matches the target value  
         */
        public static LongFunction<Boolean> equal( final Type type, final Object value )
        {
            final long target = type.storeObject( value );
            return it -> it == target;
        }        
        
        /** 
         * return me a function that accepts an int and determines 
         * the value at this ints BIN matches the target value  
         */
        public static LongFunction<Boolean> lessThan( final Type type, final Object value )
        {
            final long target = type.storeObject( value );
            return it -> it < target;
        }
        
        /** 
         * return me a function that accepts an int and determines 
         * the value at this ints BIN matches the target value  
         */
        public static LongFunction<Boolean> lessThanEqualTo( final Type type, final Object value )
        {
            final long target = type.storeObject( value );
            return it -> it <= target;
        }
        /** 
         * return me a function that accepts an int and determines 
         * the value at this ints BIN matches the target value  
         */
        public static LongFunction<Boolean> greaterThan( final Type type, final Object value )
        {
            final long target = type.storeObject( value );
            return it -> it > target;
        }        
        
        /** 
         * return a function that accepts an int and determines 
         * the value at this ints BIN matches the target value  
         */
        public static LongFunction<Boolean> greaterThanEqualTo( final Type type, final Object value )
        {
            final long target = type.storeObject( value );
            return it -> it >= target;
        }
        
        public static LongFunction<Boolean> between( Type type, Object min, Object max )
        {
            final long targetMin = type.storeObject( min );
            final long targetMax = type.storeObject( max );
            return it -> it >= targetMin && it <= targetMax;
        }
    }
    
    

    
}
