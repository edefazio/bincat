/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.frame;

import bincat.BinCatException;
import bincat.type.Type;
import java.lang.reflect.Method;
import java.util.List;
import varcode.doc.translate.JavaTranslate;
import varcode.java.Java;
import varcode.java.JavaNaming;
import varcode.java.code._class;
import varcode.java.code._code;
import varcode.java.code._methods._method;
import varcode.markup.codeml.code._Method;

/**
 * Given a BitField, uses varcode to author a Specific API for the
 * BitField
 * 
 * @author M. Eric DeFazio eric@varcode.io
 */
public class BitFieldAuthor
{
    public static final JavaTranslate T = JavaTranslate.INSTANCE;
    
    /** Parse the method inside the {$ $} above as a _method */
    private static final _DescribeLongField DESCRIBE_LONG_FIELD 
        = new _DescribeLongField();
     
    public static _class of( BitField bitField )
    {
        String name = bitField.field.getName();
        Type type = bitField.field.type;
        Class typeClass = type.getClass();
        
        _class c = _class.of( 
            JavaNaming.ClassName.convertName(
                Character.toUpperCase( name.charAt( 0 ) ) 
                + name.substring( 1 ) ) );
        
        c.javadoc( "varcode Authored bitField Class" );
        c.imports( typeClass );
        c.imports( Frame.class );
        
        c.field( "public static final String name = \""+ bitField.field.getName() + "\";" );
        c.field( "public static final int bitCount = "+ bitField.bitCount() + ";" );
        c.field( "public static final int shift = "+ bitField.shift + ";" );
        c.field( "public static final " + 
            T.translate( typeClass ) + " type = "+ bitField.field.type.selfAuthor() + ";" );

        
        c.field( "64-bit (long) bitmask for the field bits",
            "public static final long bitMask64 = 0b" 
                + Frame.to64Bit( bitField.bitMask64, '0' ) + "L;" );
        
        c.method( DESCRIBE_LONG_FIELD.compose() );
        
        c.imports( Frame.class );
        
        c.method("public String toString()",
            //"return Frame.to64BitMask( bitMask64 ) + \" \" + name + type;" );
            "return getClass().getCanonicalName() + \"{\" + type.getClass().getSimpleName() + \"}\";" );    
        
        c.method("describes the BitField as it is stored in bits of the Frame",
            "public static String describeFrame()",
            _code.of( "return Frame.to64BitMask( bitMask64 ) + \" \" + name + type;" ) );
        
        c.method(
            "public static long extractLongBits( long row )", 
            "return ( row & bitMask64 ) >>> shift;" );
        
        c.method( "public static long synthesize()",
            "return type.synthesizeBin() << shift;" );
    
        //build all the Store methods based on the Fields Type Store methods
        c = buildStoreMethods( c, typeClass );
        c = buildLoadMethod( c, bitField, typeClass );
        return c;        
    }
    
    public static _class buildLoadMethod( 
        _class _bitFieldClass, BitField bitField, Class typeClass )
    {
        List<Method> typeLoadMethods = Java.getMethods( typeClass, "load" );    
        
        if( typeLoadMethods.size() > 1 )
        {
            throw new BinCatException( "Type has more than one load() method" );
        }
        java.lang.reflect.Type t = typeLoadMethods.get( 0 ).getGenericReturnType();
        
        //make sure to import anythign even generics
        _bitFieldClass.imports( (Object[])parseClassesFromGenericString( t.toString() ) );
        
        String loadType = T.translate( t );
        
        //_bitFieldClass.method( "public static " + loadType + " load ( long row )",
        //    "return type.load( extractLongBits( row ) );" );
        
        _bitFieldClass.method( $LOAD.composeWith( loadType ) );
                
        return _bitFieldClass;        
    }
    
    public static final _Load $LOAD = new _Load();
    
    public static class _Load
        extends _Method
    {
        static class $typeLoad${}
        
        static class type
        {
            static $typeLoad$ load( long row )
            {
                return null;
            }            
        }
        static long extractLongBits( long row )
        {
            return 0L;
        }
        /** plug with the $typeLoad$ */
        public _method composeWith( Object $typeLoad$ )
        {
            return compose( "typeLoad", $typeLoad$ );
        }
        
        /*$*/
        public static $typeLoad$ load( long row )
        {
            return type.load( extractLongBits( row ) ); 
        }
        /*$*/
    }
    
    /**
     * Builds and returns a class 
     * @param c
     * @param typeClass
     * @return 
     */
    public static _class buildStoreMethods( _class c, Class typeClass )
    {
        List<Method> typeStoreMethods = Java.getMethods( typeClass, "store" );
        
        for( int i = 0; i < typeStoreMethods.size(); i++ )
        {
            Method m = typeStoreMethods.get( i );
        
            StringBuilder storeSignature = new StringBuilder();
            StringBuilder storeBody = new StringBuilder();
            storeSignature.append( "public static long store( " );
            storeBody.append( "return type.store(" );
            
            java.lang.reflect.Type[] paramTypes = m.getGenericParameterTypes();
            for( int j = 0; j< paramTypes.length; j++ )
            {
                //the class needs to import the parameter types
                c.imports( 
                    (Object[]) parseClassesFromGenericString( paramTypes[ j ].toString() ) ); 
                
                if( j > 0 )
                {
                    storeSignature.append( ", " );
                    storeBody.append( ", " );
                }
                
                storeSignature.append( T.translate( paramTypes[ j ] ) );
                storeSignature.append( " value");
                storeSignature.append( j + 1 );             
                storeBody.append( " value");
                storeBody.append( j + 1  );                
            }
            storeSignature.append( " )" );
            storeBody.append( " ) << shift;" );
            
            //System.out.println( methodSignature.toString() );
            
            _method theStoreMethod = 
                _method.of(storeSignature.toString(), 
                    _code.of(storeBody.toString() ) );
            
            c.method( theStoreMethod );
            //System.out.println( theStoreMethod );
            
        }
        return c;
    }
    
    public static String[] parseClassesFromGenericString( String genericString )
    {
        genericString = genericString.replace("<", " ");
        genericString = genericString.replace(">", " ");
        genericString = genericString.replace(",", " ");
        return genericString.split(" ");        
    }
    
        
    //upon construction, this will read the text inside /*{$  ...  $}*/ 
    //parse a _method (signature and code lines) and return
    private static final class _DescribeLongField
        extends _Method 
   {
        static long bitMask64 = 0l;
        static int shift = 0;
        static int bitCount = 0;
        static long extractLongBits( long row )
        {
            return 0L;
        }
        static String name;
        static class type
        {
            public static Object loadObject( long bin )
            {
                return null;
            }
        }
        
        /*$*/
        public static String describeFrame( long row )
        {
            long bits = ( row & bitMask64 ) >> shift;
            String alignedBits = Frame.zeroPadToNBits( bits, bitCount );
            alignedBits = Frame.shiftSpaces( alignedBits, shift );
            alignedBits = Frame.to64Bit( alignedBits );
            long bin = extractLongBits( row );
            return alignedBits + " " + name 
                + "[" + bin + "]->" + type.loadObject( bin );
        }
        /*$*/        
    }    
}
