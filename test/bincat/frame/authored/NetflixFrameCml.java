package bincat.frame.authored;

import bincat.frame.Frame;
import bincat.type.DayRange;
import bincat.type.Range;
import varcode.java.code._methods._method;
import varcode.markup.codeml.code._Method;

public class NetflixFrameCml
    extends Frame
{   
    public static class LoadField
        extends _Method
    {
        public static class $elementType${}
        
        public static class $Name$
        { 
            public static $elementType$ load( long row ) 
            {
                return null;
            }
        }
        
        public _method composeWith( Object $elementType$, String name )
        {
            return compose( "elenmentType", $elementType$, "name", name );
        }
                
        /*$*/
        public static $elementType$ load$Name$( long row )
        {
            return $Name$.load( row );
        }
        /*$*/
    }
    
    public static class DescribeRow
        extends _Method
    {
        public static long mask() { return 0L; }
        public static int bitCount() { return 0; }
        
        /*$*/
        public static String describeRow( long row )
        {
            StringBuilder sb = new StringBuilder();
            String frameBits = Frame.zeroPadToNBits( row & mask(), bitCount() );
            frameBits = Frame.to64Bit( frameBits, '-' );
            sb.append( frameBits );
            sb.append( " (" );
            sb.append( bitCount() );
            sb.append( " of 64-bits)" );
            sb.append( System.lineSeparator() );
            
            //now describe each field in the Frame
            /*{{+:sb.append( {+$^(name)+}.describeFrame( row ) );
            sb.append( System.lineSeparator() );
            +}}*/
            return sb.toString();
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
            /*{{+:bitCount+={+$(^fieldName)+}.bitCount;            
            }}*/
            return bitCount;
        }
        /*$*/
    }
    
    public static class Synthesize
        extends _Method
    {
        public _method composeWith( String[] fieldNames )
        {
            return compose( "fieldName", fieldNames );
        }
        
        /*$*/
        public static long synthesize()
        {
            long synthesized = 0L;
            /*{{+:synthesized |= {+$(^fieldName)+}.synthesize();
            +}}*/
            return synthesized;
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
        public static long mask()
        {
            long mask = 0L;
            /*{{+:mask |= {+$(^name)+}.bitMask64;
            +}}*/
            return mask;
        }
        /*$*/
    }
    
    /**
     * varcode Authored bitField Class
     */
    public static class MovieId
    {
        public static final String name = "movieId";
        public static final int bitCount = 15;
        public static final int shift = 35;
        public static final bincat.type.Range type = Range.of( 1, 17770 );
        /**
         * 64-bit (long) bitmask for the field bits
         */
        public static final long bitMask64 = 0b0000000000000011111111111111100000000000000000000000000000000000L;

        public static long load( long row )
        {
            return type.load( extractLongBits( row ) );
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
        /**
         * describes the BitField as it is stored in bits of the Frame
         */
        public static String describeFrame(  )
        {
            return Frame.to64BitMask( bitMask64 ) + " " + name + type;
        }
        public static long store( long value1 )
        {
            return type.store( value1 ) << shift;
        }
        public static long extractLongBits( long row )
        {
            return ( row & bitMask64 ) >>> shift;
        }
        public static long synthesize(  )
        {
            return type.synthesizeBin() << shift;
        }
        public String toString(  )
        {
            return getClass().getCanonicalName() + "{" + type.getClass().getSimpleName() + "}";
        }
    }
    /**
     * varcode Authored bitField Class
     */
    public static class Rating
    {
        public static final String name = "rating";
        public static final int bitCount = 3;
        public static final int shift = 32;
        public static final bincat.type.Range type = Range.of( 1, 5 );
        /**
         * 64-bit (long) bitmask for the field bits
         */
        public static final long bitMask64 = 0b0000000000000000000000000000011100000000000000000000000000000000L;

        public static long load( long row )
        {
            return type.load( extractLongBits( row ) );
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
        /**
         * describes the BitField as it is stored in bits of the Frame
         */
        public static String describeFrame(  )
        {
            return Frame.to64BitMask( bitMask64 ) + " " + name + type;
        }
        public static long store( long value1 )
        {
            return type.store( value1 ) << shift;
        }
        public static long extractLongBits( long row )
        {
            return ( row & bitMask64 ) >>> shift;
        }
        public static long synthesize(  )
        {
            return type.synthesizeBin() << shift;
        }
        public String toString(  )
        {
            return getClass().getCanonicalName() + "{" + type.getClass().getSimpleName() + "}";
        }
    }
    /**
     * varcode Authored bitField Class
     */
    public static class PersonId
    {
        public static final String name = "personId";
        public static final int bitCount = 19;
        public static final int shift = 13;
        public static final bincat.type.Range type = Range.of( 1, 480196 );
        /**
         * 64-bit (long) bitmask for the field bits
         */
        public static final long bitMask64 = 0b0000000000000000000000000000000011111111111111111110000000000000L;

        public static long load( long row )
        {
            return type.load( extractLongBits( row ) );
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
        /**
         * describes the BitField as it is stored in bits of the Frame
         */
        public static String describeFrame(  )
        {
            return Frame.to64BitMask( bitMask64 ) + " " + name + type;
        }
        public static long store( long value1 )
        {
            return type.store( value1 ) << shift;
        }
        public static long extractLongBits( long row )
        {
            return ( row & bitMask64 ) >>> shift;
        }
        public static long synthesize(  )
        {
            return type.synthesizeBin() << shift;
        }
        public String toString(  )
        {
            return getClass().getCanonicalName() + "{" + type.getClass().getSimpleName() + "}";
        }
    }
    /**
     * varcode Authored bitField Class
     */
    public static class Day
    {
        public static final String name = "day";
        public static final int bitCount = 13;
        public static final int shift = 0;
        public static final bincat.type.DayRange type = DayRange.of( "0006-06-19", "0020-01-02" );
        /**
         * 64-bit (long) bitmask for the field bits
         */
        public static final long bitMask64 = 0b0000000000000000000000000000000000000000000000000001111111111111L;

        public static java.util.Date load( long row )
        {
            return type.load( extractLongBits( row ) );
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
        /**
         * describes the BitField as it is stored in bits of the Frame
         */
        public static String describeFrame(  )
        {
            return Frame.to64BitMask( bitMask64 ) + " " + name + type;
        }
        public static long store( String value1 )
        {
            return type.store( value1 ) << shift;
        }
        public static long store( java.util.Date value1 )
        {
            return type.store( value1 ) << shift;
        }
        public static long extractLongBits( long row )
        {
            return ( row & bitMask64 ) >>> shift;
        }
        public static long synthesize(  )
        {
            return type.synthesizeBin() << shift;
        }
        public String toString(  )
        {
            return getClass().getCanonicalName() + "{" + type.getClass().getSimpleName() + "}";
        }
    }
}