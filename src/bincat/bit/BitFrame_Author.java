/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.bit;

import bincat.BinCatException;
import java.util.List;
import varcode.java.code._class;
import varcode.java.code._methods._method;
import varcode.markup.codeml.code._Method;

/**
 * Takes a Frame, authors an Enum that 
 * Can be used for the Frame
 * 
 * @author M. Eric DeFazio eric@varcode.io
 */
public class BitFrame_Author
{   
    public static class Synthesize
        extends _Method
    {
        public _method composeWith( String[] fieldNames )
        {
            return compose( "fieldName", fieldNames );
        }
        
        /*$*/
        /** synthesizes a valid frame of values */
        public static long synthesize()
        {
            long synthesized = 0L;
            /*{{+:synthesized |= $FieldName$.synthesize();            
            +}}*/
            return synthesized;            
        }
        /*$*/
    }
    
    public static class IsValid
        extends _Method
    {
        public _method composeWith( String[] fieldNames )
        {
            return compose( "fieldName", fieldNames );
        }
        
        /*$*/
        public static boolean isValid( long word )
        {
            return /*{-*/true/*-}*/
            /*{{+:$FieldName$.isValid( word ) && 
            +}}*/; 
        }
        /*$*/
    }
            
    public static class Mask
        extends _Method
    {
        public _method composeWith( String[] fieldNames )
        {
            return compose( "fieldName", fieldNames );
        }
        
        /*$*/
        /** calculates and returns the Frame's mask  */
        public static long mask()
        {
            long mask = 0L;
            /*{{+:mask |= $FieldName$.mask;            
            +}}*/
            return mask;
        }
        /*$*/
    }
    
    public static class BitCount
        extends _Method
    {
        public _method composeWith( String[] fieldNames )
        {
            return compose( "fieldName", fieldNames );
        }
        
        /*$*/
        public static int bitCount()
        {
            int bitCount = 0;
            /*{{+:bitCount += $FieldName$.bitCount;            
            +}}*/
            return bitCount;
        }
        /*$*/
    }
    public static class DescribeRow
        extends _Method
    {
        public _method composeWith( String[] $fieldNames$ )
        {
            return compose( "fieldName", $fieldNames$ );
        }
                
        static long mask() { return 0L; }
        static long bitCount() { return 0; }
        
        /*$*/
        public static String describeRow( long row )
        {
            StringBuilder sb = new StringBuilder();
            String frameBits = BitAlign.zeroPadToNBits( row & mask(), bitCount() );
            frameBits = BitAlign.to64Bit( frameBits, '-' );
            sb.append( frameBits );
            sb.append( " (");
            sb.append( bitCount() );
            sb.append( " of 64-bits)" );
            sb.append( System.lineSeparator() ); 
            
            //describe each individual field
            /*{{+:sb.append( $FieldName$.describeFrame( row ) );
            sb.append( System.lineSeparator() );
            +}}*/    
            return sb.toString();
        }
        /*$*/
    }
    
    public static class Unpack
        extends _Method
    {
        public _method composeWith( String[] fieldNames )
        {
            return compose( "fieldName", fieldNames );
        }
        
        /*$*/
        public static Object[] unpack( long row )
        {   /*{#index:$[#](fieldName)#}*/
            Object[] unpacked = new Object[ /*{+$#(fieldName)*/ 0 /*+}*/ ]; 
            
            /*{{+:unpacked[ {+index+} ] = $FieldName$.load( row );
            +}}*/
            return unpacked;
        }
        /*$*/
    }
    
    public static class Pack
        extends _Method
    {
        public _method composeWith( String[] elementTypes, String[] fieldNames )
        {
            return compose( 
                "fieldElementType", elementTypes, "fieldName", fieldNames );
        }
        /*$*/
        public static long pack( /*{{+:$fieldElementType$ $fieldName$, +}}*/ )
        {
            long packed = 0L;
            /*{{+:packed |= $FieldName$.store( $fieldName$ );            
            +}}*/
            return packed;
        }
        /*$*/
    }
    
    public static class LoadField
        extends _Method
    {
        public _method composeWith( String $fieldName$, Object $loadType$ )
        {
            return compose( 
                "fieldName", $fieldName$, 
                "loadType", $loadType$ );
        }
        
        static class $loadType$ {}
        static class $FieldName$
        {
            static $loadType$ load( long row )
            {
                return null;
            }
        }
        
        /*$*/
        /** load the value of $fieldName$ from the row */
        public static $loadType$ load$FieldName$( long row )
        {
            return $FieldName$.load( row );
        }
        /*$*/
    }
    
    public static LoadField _LOAD_FIELD = new LoadField();
    public static Pack _PACK = new Pack();
    public static Unpack _UNPACK = new Unpack();
    public static IsValid _IS_VALID = new IsValid();
    public static BitCount _BITCOUNT = new BitCount();
    public static DescribeRow _DESCRIBE_ROW = new DescribeRow();
    public static Mask _MASK = new Mask();
    public static Synthesize _SYNTHESIZE = new Synthesize();
        
    public static _class of( 
        String packageName, String className, BitFrame longFrame )
    {
        _class c = _class.of( 
            packageName, className );
        c.imports( BitAlign.class );
        
        String[] fieldNames = new String[ longFrame.bitFields.length ];
        
        String[] elementTypes = new String[ longFrame.bitFields.length ];
        
        for( int i = 0; i < longFrame.bitFields.length; i++) 
        {
            //add all of the Fields as Nested Classes
            _class _field = BitField_Author.of( longFrame.getFieldAt( i ) );            
            
            //make the fields static inner classes
            _field.getSignature().getModifiers().set( "static" );
            c.nest( _field );
            
            //collect all of the names
            fieldNames[ i ] = longFrame.bitFields[ i ].getName();            
            
            //determine the load type for each field
            List<_method> methods = _field.getMethods().getByName( "load" );
            
            if( methods.size() != 1 )
            {
                throw new BinCatException( 
                    "Must be exactly (1) \"load\" method, was " + methods.size() );
            }
            String loadType = methods.get( 0 ).getSignature().getReturnType();
            
            //add load() methods to read each field from the row
            c.method( _LOAD_FIELD.composeWith( fieldNames[ i ], loadType ) );
            elementTypes[ i ] = loadType;
        }
        c.method( _PACK.composeWith( elementTypes, fieldNames ) );
        c.method( _UNPACK.composeWith( fieldNames ) );
        c.method( _IS_VALID.composeWith( fieldNames ) );
        c.method( _BITCOUNT.composeWith( fieldNames ) );
        c.method( _DESCRIBE_ROW.composeWith( fieldNames ) );
        c.method( _MASK.composeWith( fieldNames ) );
        c.method( _SYNTHESIZE.composeWith( fieldNames ) );
            
        return c;
    }    
}
