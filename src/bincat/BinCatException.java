/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat;

/**
 * Base Exception for BinCat Framework (a Runtime Variant)
 * @author eric
 */
public class BinCatException
    extends RuntimeException
{
    
    public BinCatException( String message )
    {
        super( message );
    }
    
    public BinCatException( String message, Throwable t  )
    {
        super( message, t );
    }
    
    public BinCatException( Throwable t )
    {
        super( t );
    }
}
