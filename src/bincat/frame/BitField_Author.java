package bincat.frame;

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
        c.imports( Align.class );
        
        c.field( "public static final String name = \""+ bitField.field.getName() + "\";" );
        c.field( "public static final int bitCount = "+ bitField.bitCount() + ";" );
        c.field( "public static final int shift = "+ bitField.shift + ";" );
        c.field( "public static final " + 
            T.translate( typeClass ) + " type = "+ bitField.field.type.selfAuthor() + ";" );

        
        c.field( "64-bit (long) bitmask for the field bits",
            "public static final long bitMask64 = 0b" 
                + Align.to64Bit( bitField.bitMask64, '0' ) + "L;" );
        
        c.method( DESCRIBE_FRAME_ROW.compose( ) );
        c.method( DESCRIBE_FRAME.compose( ) );
        c.method( TO_STRING.compose( ) );
        c.method( EXTRACT_LONG_BITS.compose( ) );
        c.method( SYNTHESIZE.compose( ) );
        c.imports( Align.class );
        
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
        
        _bitFieldClass.method( LOAD.composeWith( loadType ) );        
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
    
    public static class _ExtractLongBits
        extends _Method
    {
        public static long bitMask64 = 0L;
        public static int shift = 0;
        
        /*$*/
        public static long extractLongBits( long row ) 
        {
            return ( row & bitMask64 ) >>> shift;
        }
        /*$*/
    } 
   
    private static final _DescribeFrame DESCRIBE_FRAME = new _DescribeFrame();
    
    private static final _DescribeFrameRow DESCRIBE_FRAME_ROW 
        = new _DescribeFrameRow();
    
    private static final _ExtractLongBits EXTRACT_LONG_BITS 
        = new _ExtractLongBits();
    
    private static final _Synthesize SYNTHESIZE = new _Synthesize();
    
    private static final _ToString TO_STRING = new _ToString();
    
    private static final _Load LOAD = new _Load();
    
    private static class _Load
         extends _Method
    {
        public static class $loadType$ {}
        
        public static long extractLongBits( long row ) { return 0L; }
        
        public static class type 
        { 
            public static $loadType$ load( long bin ) { return null; } 
        } 
        
        public _method composeWith( Object $loadType$ )
        {
            return compose( "loadType", $loadType$ );
        }
        
        /*$*/
        public static $loadType$ load( long row )
        {
            return type.load( extractLongBits( row ) );
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
        public static long bitMask64 = 0L;
        public static int shift = 0;
        public static int bitCount = 0;
        public static long extractLongBits( long row ) { return 0L; }
        public static String name = "";
        public static class type 
        { 
            public static String loadObject( long bin ) { return ""; } 
        } 
        
        /*$*/
        public static String describeFrame( long row )
        {
            long bits = ( row & bitMask64 ) >>> shift;
            String alignedBits = Align.zeroPadToNBits( bits, bitCount );
            alignedBits = Align.shiftSpaces( alignedBits, shift );
            alignedBits = Align.to64Bit( alignedBits );
            long bin = extractLongBits( row );
            return alignedBits + " " + name 
                + "[" + bin + "]->" + type.loadObject( bin );
        }
        /*$*/        
    }    
    
    public static final class _DescribeFrame
        extends _Method
    {
        public static long bitMask64 = 0L;
        public static String name = "";
        public static String type = "type";
        
        /*$*/
        public static String describeFrame()
        {
            return Align.to64BitMask( bitMask64 ) + " " + name + type;
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
