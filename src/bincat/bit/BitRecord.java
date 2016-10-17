package bincat.bit;

import bincat.BinCatException;

/**
 * Assignment of a {@code Row} of {@code Field}s to  
 * one or more {@code BitFrames}.
 * 
 * an example {@code BitRecord} is a
 * <A HREF="https://en.wikipedia.org/wiki/IPv6_packet#Fixed_header">
 * IPv6 Fixed Packet Header</A>.
 * that defines the bitwise layout of multiple fields in a IPv6 packet
 * 
 * @author M. Eric DeFazio eric@varcode.io
 */
public class BitRecord
{
    /** 
     * What each record represents 
     * synonymous with Table Name/ Object name (i.e. "Person")
     */
    public String name;
    
    /**
     * Description | Detail about the contents of each record
     */
    public String description;
    
    /** 
     * The Mapping of {@code Field}s to {@code BitFrames} for the {@code BitRecord}
     */
    public BitFrame[] frames; 
    
    
    public static BitRecord of( BitFrame...frames )
    {
        return new BitRecord( frames );
    }
    
    public BitRecord( BitFrame...frames )
    {
        this.frames = frames;
    }
    
    /** is the Record valid?*/
    public boolean isValid( long[] record )
    {
        if( record.length != frames.length )
        {
            return false;
        }
        for( int i = 0; i < frames.length; i++ )
        {
            if( !frames[ i ].isValid( record[ i ] ) )
            {
                return false;
            }
        }
        return true;
    }
    
    // the number of fields
    public int fieldCount()
    {
        int fieldCount = 0;
        for( int i = 0; i < frames.length; i++ )
        {
            fieldCount += frames[ i ].fieldCount();
        }
        return fieldCount;
    }
    
    /** Synthesize a new record based on the {@code BitFrame}s*/
    public long[] synthesize()
    {
        long[] row = new long[ this.frames.length ];
        for( int i = 0; i < this.frames.length; i++ )
        {
            row[ i ] = frames[ i ].synthesize();
        }
        return row;
    }
    
    /** describe the Layout of fields within the Frames for this Record */
    public String describe()
    {
        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < frames.length; i++ )
        {
              sb.append( "Frame(")
                .append( i + 1 )
                .append( " of ") 
                .append( frames.length )
                .append( ")" );
            
            sb.append( System.lineSeparator() );
            sb.append( frames[ i ].toString() );
        }
        return sb.toString();
    }
    
    public String describe( long[] record )
    {
        if( record.length != frames.length )
        {
            throw new BinCatException(
                "expected (" + frames.length + ") words, to describe Record : " 
                    + System.lineSeparator() 
                    + describe() + System.lineSeparator() + "... instead, got (" 
                    + record.length + ")" );
        }
        
        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < frames.length; i++ )
        {
              sb.append( "Frame(")
                .append( i + 1 )
                .append( " of ") 
                .append( frames.length )
                .append( ")" );
            
            sb.append( System.lineSeparator() );
            sb.append( frames[ i ].describe( record[ i ] ) );
        }
        return sb.toString();
    }
    
    /** 
     * Pack the BitRecord's {@code BitFrame}s with [Bin]s 
     * for all fields
     * @param values in the order they are described in the BitRecord
     * @return the record as an array of 64-bit long words
     */
    public long[] pack( Object... values )
    {
        if( values.length < fieldCount() )
        {
            throw new BinCatException(
                "expected (" + fieldCount() + ") values, to pack Record : " + System.lineSeparator() 
                    + describe() + System.lineSeparator() + "... instead, got (" 
                    + values.length + ")" );
        }
        long[] packed = new long[ this.frames.length ];
        
        int valueIndex = 0;
        for( int i = 0; i < frames.length; i++ )
        {
            int fieldsThisFrame = frames[ i ].fieldCount();
            
            packed[ i ] = 0L; //this should be done automatically...for clarity
            for( int j = 0; j < fieldsThisFrame; j++ )
            {
                packed[ i ] |= 
                    frames[ i ].getFieldAt( j ).storeObject( values[ valueIndex ] );
                valueIndex ++;
            }
        }
        return packed;
    }
    
    public Object[] unpack( long...record )
    {
        if( record.length < frames.length )
        {
            throw new BinCatException(
                "expected record of (" + frames.length + ") 64-bit words, "
                    + " for : " + System.lineSeparator() 
                    + describe() + System.lineSeparator() + "... instead, got (" 
                    + record.length + ") 64-bit words" );
        }
        Object[] values = new Object[ fieldCount() ];
        int valueIndex = 0;
        for( int i = 0; i < frames.length; i++ )
        {
            int fieldsThisFrame = frames[ i ].fieldCount();
            //for( int j = 0; j < fieldsThisFrame; j++ )
            //{                
            Object[] valuesThisFrame = 
                frames[ i ].unpack( record[ i ] );
            
            System.arraycopy(
                valuesThisFrame,       //source
                0,                     //source index
                values,                //dest
                valueIndex,            //dest index
                valuesThisFrame.length //length
            );                
            valueIndex += fieldsThisFrame;            
        }
        return values;
    }
    
    
    /**
     * Gets the value of a BitField from within the record
     * @param fieldName the name of the field
     * @param record the record as an array of words
     * 
     * @return the value of the field within the record
     */
    public Object loadObject( String fieldName, long[] record )
    {
        for( int i = 0; i < frames.length; i++ )
        {
            BitField bitField = frames[ i ].getField( fieldName );
            if( bitField != null )
            {
                return bitField.loadObject( record[ i ] );
            }
        }
        throw new BinCatException(
            "No field \"" + fieldName + "\" in Record " + describe() );
    }
}
