package bincat.bit;

import bincat.BinCatException;
import bincat.type.Type;
import java.lang.reflect.Method;
import java.util.List;
import varcode.doc.translate.JavaTranslate;
import varcode.java.Java;
import varcode.java.JavaNaming;
import varcode.java.code._class;
import varcode.java.code._methods._method;
import varcode.markup.codeml.code._Method;

/**
 * Given a BitField, uses varcode to author a Specific API for the
 * BitField
 * 
 * @author M. Eric DeFazio eric@varcode.io
 */
public class BitField_Author
{
    public static final JavaTranslate T = JavaTranslate.INSTANCE;
    
    public static _class of( BitField bitField )
    {
        String name = bitField.field.getName();
        Type type = bitField.field.type;
        
        Class typeClass = type.getClass();
        
        _class c = _class.of( 
            JavaNaming.ClassName.convertName(
                Character.toUpperCase( name.charAt( 0 ) ) 
                + name.substring( 1 ) ) );
        
        c.javadoc( "varcode authored bitField for " );
        c.imports( typeClass );
        c.imports( BitAlign.class );
        
        c.field( "public static final String name = \""+ bitField.field.getName() + "\";" );
        c.field( "public static final int bitCount = "+ bitField.bitCount() + ";" );
        c.field( "public static final int shift = "+ bitField.shift + ";" );
        c.field( "public static final " + 
            T.translate( typeClass ) + " type = "+ bitField.field.type.selfAuthor() + ";" );

        
        c.field( "64-bit (long) bitmask for the field bits",
            "public static final long mask = 0b" 
                + BitAlign.to64Bit( bitField.mask, '0' ) + "L;" );
        
        c.method( _DESCRIBE_FRAME_ROW.compose( ) );
        c.method( _DESCRIBE_FRAME.compose( ) );
        c.method( _TO_STRING.compose( ) );
        c.method( _EXTRACT_BIN.compose( ) );
        c.method( _SYNTHESIZE.compose( ) );
        c.method( _IS_VALID.compose( ) );
        
        c.imports( BitAlign.class );
        
        //c = buildStoreMethods( c, typeClass );
        c = authorStoreMethods( c, typeClass );
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
        _bitFieldClass.imports( (Object[]) Java.classesFromType( t ) );
        
        String loadType = T.translate( t );
        
        _bitFieldClass.method( _LOAD.composeWith( loadType ) );        
        return _bitFieldClass;        
    }
    
    /**
     * 
     * @param c the BitField class being authored
     * @param typeClass the type (Class) mediating the storage
     * @return the modified _class
     */
    public static _class authorStoreMethods( _class c, Class typeClass )
    {
        List<Method> typeStoreMethods = Java.getMethods( typeClass, "store" );
        for( int i = 0; i < typeStoreMethods.size(); i++ )
        {
            Method m = typeStoreMethods.get( i );
            java.lang.reflect.Type[] paramTypes = m.getGenericParameterTypes();   
            for( int j = 0; j < paramTypes.length; j++ )
            {
                //the class needs to import the parameter types
                c.imports( (Object[])Java.classesFromType( paramTypes[ j ] ) );                                                       
            }
            c.method( STORE.composeWith( (Object[])paramTypes ) );
        }
        return c;
    }
    
    public static final Store STORE = new Store();
    
    public static class Store
        extends _Method
    {
        static class type
        {
            static long store()
            {
                return 0L;
            }
        }
        static int shift = 0;
        
        /**
         * TODO I need to look into compiling with -parameter
         * and extracting the actual parameter names
         * http://docs.oracle.com/javase/tutorial/reflect/member/methodparameterreflection.html
         * (At least TRYING) if we are using JDK 8 or better
         * 
         * otherwise I'll  just fall back into using "argN"
         * @param elementTypes
         * @return 
         */
        public _method composeWith( Object...elementTypes )
        {
            String[] elementName = new String[ elementTypes.length ];
            
            for( int i = 0; i < elementName.length; i++ )
            {
                elementName[ i ] = "arg" + i;
            }
            return compose( 
                "elementType", elementTypes, "elementName", elementName );
        }
        
        /*$*/
        public static long store( /*{{+:{+elementType+} {+elementName+}, +}}*/ )
        {            
            return type.store( /*{{+:{+elementName+}, 
            +}}*/ ) << shift;
        }
        /*$*/
    }
    
    public static class _ExtractBin
        extends _Method
    {
        public static long mask = 0L;
        public static int shift = 0;
        
        /*$*/
        public static long extractBin( long row ) 
        {
            return ( row & mask ) >>> shift;
        }
        /*$*/
    } 
   
    private static final _DescribeFrame _DESCRIBE_FRAME = new _DescribeFrame();
    
    private static final _DescribeFrameRow _DESCRIBE_FRAME_ROW 
        = new _DescribeFrameRow();
    
    private static final _ExtractBin _EXTRACT_BIN 
        = new _ExtractBin();
    
    private static final _Synthesize _SYNTHESIZE = new _Synthesize();
    
    private static final _ToString _TO_STRING = new _ToString();
    
    private static final _Load _LOAD = new _Load();
    
    private static final IsValid _IS_VALID = new IsValid();
    
    private static class _Load
         extends _Method
    {
        public static class $loadType$ {}
        
        public static long extractBin( long word ) { return 0L; }
        
        public static class type 
        { 
            public static $loadType$ load( long bin ) { return null; } 
        } 
        
        public _method composeWith( Object $loadType$ )
        {
            return compose( "loadType", $loadType$ );
        }
        
        /*$*/
        public static $loadType$ load( long word )
        {
            return type.load( extractBin( word ) );
        }
        /*$*/
    }
    
    public static class _ToString
        extends _Method
    {
        public static String type;
        
        /*$*/
        public String toString()
        {
            return getClass().getCanonicalName() + "{" 
                + type.getClass().getSimpleName() + "}"; 
        }   
        /*$*/
    }
    
    public static final class _DescribeFrameRow
        extends _Method
    {
        public static long mask = 0L;
        public static int shift = 0;
        public static int bitCount = 0;
        public static long extractBin( long word ) { return 0L; }
        public static String name = "";
        public static class type 
        { 
            public static String loadObject( long bin ) { return ""; } 
        } 
        
        /*$*/
        public static String describeFrame( long word )
        {
            long bits = ( word & mask ) >>> shift;
            String alignedBits = BitAlign.zeroPadToNBits( bits, bitCount );
            alignedBits = BitAlign.shiftSpaces( alignedBits, shift );
            alignedBits = BitAlign.to64Bit( alignedBits );
            long bin = extractBin( word );
            return alignedBits + " " + name 
                + "[" + bin + "]->" + type.loadObject( bin );
        }
        /*$*/        
    }    
    
    public static final class _DescribeFrame
        extends _Method
    {
        public static long mask = 0L;
        public static String name = "";
        public static String type = "type";
        
        /*$*/
        public static String describeFrame()
        {
            return BitAlign.to64BitMask( mask ) + " " + name + type;
        }
        /*$*/
    }
    
    public static class IsValid
        extends _Method
    {
        static class type{ 
            public static boolean isValidBin( long bin ) { return true;} 
        }
        static long extractBin( long row )
        {
            return 0L;
        }
        
        /*$*/
        public static boolean isValid( long word )
        {
            return type.isValidBin( extractBin( word ) );
        }
        /*$*/
    }        
            
    public static class _Synthesize
        extends _Method
    {
        public static class type { 
            public static long synthesizeBin()
            {
                return 0L;
            }
        }
        public static int shift = 0;
        
        /*$*/
        public static long synthesize()
        {
            return type.synthesizeBin() << shift;
        }
        /*$*/
    }
}
