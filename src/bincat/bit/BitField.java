package bincat.bit;

import bincat.Field;
import bincat.type.Type;

/**
 * {@code Field} whos bits are assigned to assigned to specific bits
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
    
    public final long mask;
        
    public BitField( Field field, int shift )
    {
        this.field = field;
        this.shift = shift;        
        this.mask = ( -1L >>> ( 64 - field.type.bitCount() ) ) << shift;
    }
    
    /**
     * Describe the field as it exists within the row
     * @param row the low of data
     * @return a String representation of the bits field a
     */
    public String describeFrame( long row )
    {
        long bits = ( row & mask ) >> shift;
        String alignedBits = BitAlign.zeroPadToNBits( bits, field.type.bitCount() );
        alignedBits = BitAlign.shiftSpaces( alignedBits, shift );
        alignedBits = BitAlign.to64Bit( alignedBits );
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
        return BitAlign.to64BitMask(mask ) + " "+ field.name + field.type;
    }
    
    public int bitCount()
    {
        return field.getType().bitCount();
    }
    
    public long mask()
    {
        return mask;
    }
    
    public long storeObject( Object value )
    {
        return field.getType().storeObject( value ) << shift;
    }
    
    public Object loadObject( long row )
    {
        return field.getType().loadObject( extractBits( row ) );
    }
    
    public long extractBits( long row )
    {
        return ( row & mask ) >>> shift;
    }
    
    /**
     * Is the field within this row valid
     * @param row the row
     * @return true if valid, false otherwise
     */
    public boolean isValid( long row )
    {
        return this.field.getType().isValidBin( extractBits( row ) );
    }
    
    public long synthesizeBin()
    {
        return field.getType().synthesizeBin() << shift;
    }
}
