/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat;

import bincat.type.DayRange;
import bincat.type.Range;
import bincat.type.Type;

/**
 * Grouping of {@code Field}s, a "record" synonymous in concept 
 * with a Row in a database table.
 * 
 * @author M. Eric DeFazio eric@varcode.io
 */
public class Row
{
    /** fields in the row (containing {@code Type} and {@code Name}) */
    public Field[] fields;
    
    public int fieldCount()
    {
        return fields.length;
    }
    
    public Field getField( String name )
    {
        for( int i = 0; i < fields.length; i++)
        {
            if( fields[ i ].name.equals( name ) )
            {
                return fields[ i ];
            }
        }
        throw new RuntimeException( "Frame has no field named \"" + name + "\"" );
    }
    
    public int getFieldIndex( String name )
    {
        for( int i = 0; i < fields.length; i++)
        {
            if( fields[ i ].name.equals( name ) )
            {
                return i;
            }
        }
        throw new RuntimeException( "No field named \"" + name + "\"" );
    }
    
    public int bitCount()
    {
        int count = 0;
        for( int i = 0; i < fields.length; i++ )
        {
            count += fields[ i ].type.bitCount();
        }
        return count;
    }
    
    /**
     * Define a Row of Sequential Fields for each item 
     * in the bin catalog
     * 
     * 
     * @param parameters fieldName, type pairs
     * @return 
     */
    public static Row of( Object... parameters )
    {
        if( parameters.length % 2 == 1)
        {
            throw new BinCatException( 
                "Must be at least (2) Parameters and must be EVEN number Type/Values" );
        }        
        //0 and even parameters are types, odd parameters are names
        Field[] fields = new Field[ parameters.length / 2 ];
        for( int i = 0; i < parameters.length / 2; i++ )
        {
            fields[ i ] = Field.of( 
                typeFor( parameters[ i * 2 ] ),
                (String)parameters[ (i * 2) + 1 ] );                     
        }
        return new Row( fields );
    }    
    
    private static Type typeFor( Object obj )
    {
        if( obj instanceof Type )
        {
            return (Type)obj;
        }
        if( obj instanceof String && ((String)obj).contains( "..." ) )
        {
            String str = (String) obj;
            int index = str.indexOf("...");
            return typeFor( 
                 str.substring( 0 , str.indexOf( "..." ) ),
                 str.substring( index+3 ) );            
        }
        throw new BinCatException( "unknown  Type Object \"" + obj + "\"" );
    }
    
    private static Type typeFor( String min, String max )
    {
        if( min.contains( "-" ) )
        {
            return DayRange.of( min, max );
        }
        else
        {
            return Range.of( Integer.parseInt(min), Integer.parseInt( max ) );
        }
        //throw new BinCatException(" could not find type for "+min+"..."+max );
    }
    
    public Row( Field... fields )
    {
        this.fields = fields;
    }
}
