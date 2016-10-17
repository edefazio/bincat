/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.bit;

import bincat.BinCatException;
import bincat.Record;

/**
 * LongRow is a contiguous 64-bit description of fields
 * 
 * @author M. Eric DeFazio eric@varcode.io
 */
public class BitFrame
{
    public static BitFrame of( Record row )
    {
        return new BitFrame( row );
    }
    
    public static BitFrame of( Object...typeNamePairs )
    {
        return new BitFrame( Record.of( typeNamePairs ) ); 
    }
    
    public final BitField[] bitFields;
    
    public BitFrame( Record row )
    {
        int shift = 0;
        
        this.bitFields = new BitField[ row.fields.length ];
        for( int i = row.fieldCount() -1; i >= 0; i -- )    
        {
            bitFields[ i ] = new BitField( row.fields[ i ], shift );            
            shift += row.fields[ i ].type.bitCount();    
            //System.out.println( "[" + i + "]"+bitFields[ i ] );
        }        
    }
    
    /** Mask for the significant bits of the row */
    public final long mask()
    {
        long mask = 0L;
        for( int i = 0; i < bitFields.length; i++ )
        {
            mask |= bitFields[ i ].mask();
        }
        return mask;
    }
    
    public final int fieldCount()
    {
        return bitFields.length;
    }
    
    /**
     * Describe the contents of the Row 
     * @param row the row
     * @return a String description of how each of the Fields 
     * in the row are laid out physically
     */
    public String describe( long row )
    {
        StringBuilder sb = new StringBuilder();
        String frameBits = BitAlign.zeroPadToNBits( row & mask(), bitCount() );
        frameBits = BitAlign.to64Bit( frameBits, '-' );
        sb.append( frameBits );
        //sb.append( Frame.to64Bit( mask() ).replace(' ', '-') );
        
        sb.append( " (");
        sb.append( bitCount() );
        sb.append( " of 64-bits)" );
        sb.append( System.lineSeparator() );
        for( int i = 0; i < bitFields.length; i++ )
        {
            sb.append( bitFields[ i ].describeFrame( row ) );
            sb.append( System.lineSeparator() );
        }
        return sb.toString();        
    }
    
    public String describe()
    {
        StringBuilder sb = new StringBuilder();
        sb.append( BitAlign.to64Bit( mask() ).replace(' ', '-') );
        sb.append( " (");
        sb.append( bitCount() );
        sb.append( " of 64-bits)" );
        
        sb.append( System.lineSeparator() );
        for( int i = 0; i < bitFields.length; i++ )
        {
            sb.append( bitFields[ i ] );
            sb.append( System.lineSeparator() );
        }
        return sb.toString();
    }
    
    @Override
    public String toString()
    {
        return describe();
    }
    
    public int bitCount()
    {
        int bitCount = 0;
        for( int i = 0; i < bitFields.length; i++ )
        {
            bitCount += bitFields[ i ].field.type.bitCount();
        }
        return bitCount;
    }
    
    public boolean isValid( long row )
    {
        for( int i = 0; i < bitFields.length; i++ )
        {
            if( ! bitFields[ i ].isValid( row ) )
            {
                return false;
            }
        }
        return true;
    }
    
    /** 
     * synthesize a row containing valid values
     * @return a valid synthesized row
     */ 
    public long synthesize()
    {
        long packed = 0;
        for( int i = 0; i < bitFields.length; i++ )
        {
            packed |= bitFields[ i ].synthesizeBin();
        }        
        return packed;
    }
 
    /**
     * Converts the Row of Data to Bins and binary packs the row in the frame
     * @param rowData
     * @return 
     */
    public long pack( Object...rowData )
    {
        long packed = 0;
        for( int i = 0; i < this.bitFields.length; i++ )
        {
            packed |= this.bitFields[ i ].storeObject( rowData[ i ] );
        }
        return packed;
    }
    
    
    /** returns the {@code BitField} by name 
     * 
     * @param fieldName
     * @return the BitField with that name or null if a field with this name 
     * doesn't exist within the frame
     */
    public BitField getField( String fieldName )
        throws BinCatException
    {
        for( int i = 0; i < this.bitFields.length; i++ )
        {
            if( this.bitFields[ i ].field.getName().equals( fieldName ) )
            {
                return this.bitFields[ i ];
            }
        }
        return null;        
    }
    
    /**
     * returns the BitField at the given index
     * @param index the index of the Field
     * @return the Field
     */
    public BitField getFieldAt( int index )
    {
        if( index < 0 || index > this.bitFields.length -1 )
        {
            throw new BinCatException( "Index [" + index + "] out of bounds" );
        }
        return this.bitFields[ index ];
    }
    
    public Object load( long row, String name )
    {
        BitField bf = getField( name );
        return bf.loadObject( row );
    }
    
    public Object[] unpack( long row )
    {
        Object[] rowValues = new Object[ this.bitFields.length ];
        for( int i = 0; i < rowValues.length; i++ )
        {
            rowValues[ i ] = this.bitFields[ i ].loadObject( row );
        }
        return rowValues;
    }    
}
