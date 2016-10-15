package bincat.frame.authored;

import bincat.frame.Frame;
import bincat.type.DayRange;
import bincat.type.Range;
import varcode.markup.codeml.code._Method;

public class NetflixFrameForm
    extends Frame
{

    // elementType to load
    // name[]
    
    public static class LoadField 
        extends _Method
    {
        class $elementType$ {};
        static class $Name${ 
            public static $elementType$ load( long row ) 
            { return null; } 
        }
        
        /*$*/
        public static $elementType$ load$Name$( long row )
        {
            return $Name$.load( row );
        }
        /*$*/
    }
    
    public static long loadRating( long row )
    {
        return Rating.load( row );
    }
    public static long loadMovieId( long row )
    {
        return MovieId.load( row );
    }
    public static java.util.Date loadDay( long row )
    {
        return Day.load( row );
    }
    public static long loadPersonId( long row )
    {
        return PersonId.load( row );
    }
    
    /**
     * This is a MethodForm
     */
    public static class DescribeRow
        extends _Method
    {
        private static long mask() { return 1L; }
        private static int bitCount() { return 1; }
        public static class $Name$ {
            public static String describeFrame( long row ) { return ""; }
        }
        
        /*$*/
        public static String describeRow( long row )
        {
            StringBuilder sb = new StringBuilder();
            String frameBits = zeroPadToNBits( row & mask(), bitCount() );
            frameBits = Frame.to64Bit( frameBits, '-' );
            sb.append( frameBits );
            sb.append( " (");
            sb.append( bitCount() );
            sb.append( " of 64-bits)" );
            sb.append( System.lineSeparator() );
            
            //describe each Field in the Frame
            /*{{+:*/sb.append( $Name$.describeFrame( row ) );
            sb.append( System.lineSeparator() );
            /*+}}*/
            return sb.toString();
        }
        /*$*/
    }        
    
    public static methodForm DESCRIBE_ROW = 
        methodForm.of( "public static String describeRow( long row )",
          "StringBuilder sb = new StringBuilder();",
          "String frameBits = Frame.zeroPadToNBits( row & mask(), bitCount() );",
          "frameBits = Frame.to64Bit( frameBits, '-' );",
          "sb.append( frameBits );",
          "sb.append( \" (\");sb.append( bitCount() );",
          "sb.append( \" of 64-bits)\" );",
          "sb.append( System.lineSeparator() );",
          
          "//now describe each Field in the Frame",          
          "{{+:sb.append( {+$^(name)+}.describeFrame( row ) );",
          "sb.append( System.lineSeparator() );",
          "+}}",
          "return sb.toString();"
        );
        
    public static String describeRow( long row )
    {
        StringBuilder sb = new StringBuilder();
        String frameBits = Frame.zeroPadToNBits( row & mask(), bitCount() );
        frameBits = Frame.to64Bit( frameBits, '-' );
        sb.append( frameBits );
        sb.append( " (");sb.append( bitCount() );
        sb.append( " of 64-bits)" );
        sb.append( System.lineSeparator() );
        
        sb.append( MovieId.describeFrame( row ) );
        sb.append( System.lineSeparator() );
        sb.append( Rating.describeFrame( row ) );
        sb.append( System.lineSeparator() );
        sb.append( PersonId.describeFrame( row ) );
        sb.append( System.lineSeparator() );
        sb.append( Day.describeFrame( row ) );
        sb.append( System.lineSeparator() );
        return sb.toString();
    }

    public static final methodForm BITCOUNT = methodForm.of( 
        "public static long bitCount()",
        
        "int bitCount = 0;",
        "{{+:bitCount+={+$(^name)+}.bitCount;",
        "+}}",
        "return bitCount;" );
            
    public static long bitCount(  )
    {
        int bitCount = 0;
        bitCount+=MovieId.bitCount;
        bitCount+=Rating.bitCount;
        bitCount+=PersonId.bitCount;
        bitCount+=Day.bitCount;
        return bitCount;
    }
    
    public static final methodForm SYNTHESIZE = methodForm.of( 
        "public static long synthesize()",
        
        "long synthesized = 0L;",
        "{{+:synthesized |= {+$(^name)+}.synthesize();",
        "+}}",
        "return synthesized;" );
        
    public static long synthesize(  )
    {
        long synthesized = 0L;
        synthesized |= MovieId.synthesize();
        synthesized |= Rating.synthesize();
        synthesized |= PersonId.synthesize();
        synthesized |= Day.synthesize();
        return synthesized;
    }
    
    public static final methodForm MASK = methodForm.of( 
        "public static long mask()",
        
        "long mask = 0L;",
        "{{+:mask |= {+$(^name)+}.bitMask64;",
        "+}}",
        "return mask;" );
    
    public static long mask(  )
    {
        long mask = 0L;
        mask |= MovieId.bitMask64;
        mask |= Rating.bitMask64;
        mask |= PersonId.bitMask64;
        mask |= Day.bitMask64;
        return mask;
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