package bincat.frame;

import bincat.Field;
import bincat.type.Type;

/**
 * A Field that exists within a Frame (i.e. assigned to specific bits
 * within a 64 bit Frame)
 * 
 * @author M. Eric DeFazio eric@varcode.io
 */
public class BitField
{
    public static BitField of( Type type, String name )
    {
        return new BitField( Field.of( type, name ), 0 );
    }
    
    public static BitField of( Type type, String name, int shift )
    {
        return new BitField( Field.of( type, name ), shift );
    }
    
    public final Field field;
    
    public final int shift;
    
    public final long bitMask64;
        
    public BitField( Field field, int shift )
    {
        this.field = field;
        this.shift = shift;        
        this.bitMask64 = ( -1L >>> ( 64 - field.type.bitCount() ) ) << shift;
    }
    
    /**
     * Describe the field as it exists within the row
     * @param row the low of data
     * @return a String representation of the bits field a
     */
    public String describeFrame( long row )
    {
        long bits = ( row & bitMask64 ) >> shift;
        String alignedBits = Frame.zeroPadToNBits( bits, field.type.bitCount() );
        alignedBits = Frame.shiftSpaces( alignedBits, shift );
        alignedBits = Frame.to64Bit( alignedBits );
        long bin = extractBits(row);
        return alignedBits + " " + field.name 
            + "[" + bin + "]->" + field.type.loadObject( bin );
    }
    
    public String getName()
    {
        return field.getName();
    }
    
    @Override
    public String toString()
    {
        return Frame.to64BitMask( bitMask64 ) + " "+ field.name + field.type;
    }
    
    public int bitCount()
    {
        return field.getType().bitCount();
    }
    
    public long longMask()
    {
        return bitMask64;
    }
    
    public long storeLong( Object value )
    {
        return field.getType().storeObject( value ) << shift;
    }
    
    public Object loadObject( long row )
    {
        return field.getType().loadObject( extractBits( row ) );
    }
    
    public long extractBits( long row )
    {
        return ( row & bitMask64 ) >>> shift;
    }
    
    public long synthesizeBin()
    {
        return field.getType().synthesizeBin() << shift;
    }
}
