/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.frame;

/**
 *
 * @author eric
 */
public class Frame
{
    public static final String[] SPACES = new String[ 65 ];
    public static final String[] ZEROS = new String[ 65 ];
    
    static
    {
        SPACES[ 0 ] = "";
        ZEROS[ 0 ] = "";
        for( int i = 1; i < SPACES.length; i++ )
        {
            SPACES[ i ] = SPACES[ i - 1 ] + " ";
            ZEROS[ i ] = ZEROS[ i - 1] + "0";
        }
    }
                    
    protected Frame()
    {        
    }
        
    
    public static final String shiftSpaces( String binary, int spaces )
    {
        return binary + SPACES[ spaces ];
    }
    
    /** 
     * Converts the Left-padding (most significant bits) a long value to 64 bits 
     * and returns the String representation
     * 
     * @param binary
     * @return a String of 64 characters that represents the binary value padded 
     * to 64 spaces
     */
    public static final String to64BitMask( long bitMask )
    {
        String str = Long.toBinaryString( bitMask ).replace('0', ' ');
        return SPACES[ 64 - str.length() ] + str;        
    }

    public static final String zeroPadToNBits( long value, long bitCount )
    {
        String str = Long.toBinaryString( value );
        return ZEROS[ (int)bitCount - str.length() ] + str;
    }
    
    public static final String zeroPadToNBits( long value, int bitCount )
    {
        String str = Long.toBinaryString( value );
        return ZEROS[ bitCount - str.length() ] + str;
    }
    
    public static final String zeroPadToNBits( int value, int bitCount )
    {
        String str = Integer.toBinaryString( value );
        return ZEROS[ bitCount - str.length() ] + str;
    }
        
    /** 
     * Converts the Left-padding (most significant bits) a long value to 64 bits 
     * and returns the String representation
     * 
     * @param binary
     * @return a String of 64 characters that represents the binary value padded 
     * to 64 spaces
     */
    public static final String to64Bit( long binary )
    {
        String str = Long.toBinaryString( binary );
        return to64Bit( str );
    }
    public static final String to64Bit( long binary, char padChar )
    {
         String str = Long.toBinaryString( binary );
        return to64Bit( str , padChar);
    }
    
    public static final String to32Bit( int binary )
    {
        String str = Integer.toBinaryString( binary );
        return to32Bit( str );
    }
    
    public static final String to32Bit( int binary, char padChar )
    {
        String str = Integer.toBinaryString( binary );
        return to32Bit( str, padChar );
    }
    
    public static final String to32Bit( String binary )
    {
        return SPACES[ 32 - binary.length() ] + binary;
    }
    
    public static final String to32Bit( String binary, char padChar )
    {
        return SPACES[ 32 - binary.length() ].replace( ' ', padChar) + binary;
    }
    
    public static final String to64Bit( String binary )
    {
        return SPACES[ 64 - binary.length() ] + binary;
    }
    
    public static final String to64Bit( String binary, char padChar )
    {
        return SPACES[ 64 - binary.length() ].replace(' ', padChar) + binary;
    }
}
