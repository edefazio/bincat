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
        
        c.method( DESCRIBE_FRAME_ROW.compose( ) );
        
        c.method( DESCRIBE_FRAME.compose( ) );
        //c.method("describes the BitField as it is stored in bits of the Frame",
        //    "public static String describeFrame()",
        //    _code.of( "return Frame.to64BitMask( bitMask64 ) + \" \" + name + type;" ) );
        
        c.method( TO_STRING.compose( ) );
        //c.method("public String toString()",
        //    "return getClass().getCanonicalName() + \"{\" + type.getClass().getSimpleName() + \"}\";" );    
        
        c.method( EXTRACT_LONG_BITS.compose( ) );
        //c.method(
        //    "public static long extractLongBits( long row )", 
        //    "return ( row & bitMask64 ) >>> shift;" );
        c.method( SYNTHESIZE.compose( ) );
        //c.method( "public static long synthesize()",
        //   "return type.synthesizeBin() << shift;" );
        c.imports( Frame.class );
        
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
        
        _bitFieldClass.method( LOAD.composeWith( loadType ) );        
        return _bitFieldClass;        
    }
    
    
    /**
     * Modifies a (BitField) _class model that contains the appropriate store()
     * methods...
     * this includes adding any imports based on the types
     * @param c the (_BitField) class
     * @param typeClass the Type class of the field
     * @return the modified _class (with the store methods and imports)
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
    
    /** 
     * given a String that represents a Class, returns an Array of Strings representing all
     * Class names that are referenced in the String
     * <PRE>
     *    String gen = "Map<BigDecimal, ValueObject>";
     *    String[] classes = {"Map", "BigDecimal", "ValueObject"};
     * </PRE>
     * 
     * @param genericType String representation of a Generic Type
     * @return an array of Strings for all types of the generic Type
     */
    public static String[] parseClassesFromGenericString( String genericType )
    {
        genericType = genericType.replace( "<", " ");
        genericType = genericType.replace( ">", " ");
        genericType = genericType.replace( ",", " ");
        return genericType.split( " " );        
    }
    
    /*    
    //upon construction, this will read the text inside 
    //parse a _method (signature and code lines) and return
    private static final class _DescribeLongField
        extends _Method
    {
        static long bitMask64 = 0L;
        static int shift = 0;
        static int bitCount = 0;
        private static long extractLongBits( long row ) { return 0L; }
        static String name = "";
        static class type 
        {
            static String loadObject( long bin )
            {
                return null;
            }
        }
        
        public _method composeWith()
        {
            return compose();
        }
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
        
    }   
    */
    
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
    
    //private static final _DescribeLongField _DESCRIBE_LONG_FIELD 
    //    = new _DescribeLongField();
    
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
            String alignedBits = Frame.zeroPadToNBits( bits, bitCount );
            alignedBits = Frame.shiftSpaces( alignedBits, shift );
            alignedBits = Frame.to64Bit( alignedBits );
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
            return Frame.to64BitMask( bitMask64 ) + " " + name + type;
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
