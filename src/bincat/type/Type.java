/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.type;

/**
 *
 * I would consider having categories for types
 * 
 * Some Types have Order (and you can get first, getLast)
 * 
 * Some Types have multiple values 
 * 
 * @author M. Eric DeFazio eric@bitc.at
 */
public interface Type
{   
    public int bitCount();    
    
    public long storeObject( Object value );
    
    public Object loadObject( long bin );
    
    
    public long synthesizeBin();
    
    /** is the [Bin] valid for this Type instance */
    public boolean isValidBin( long bin );
    
    /** 
     * Each instance knows how to describe in Java source code 
     * how to construct a new one
     * 
     * This allows us to "author"
     * 
     * so for instance, a Date class that was selfAuthored might return String:
     * "new Date( "+this.timeMillis+")"
     * 
     * It would be nice that I could get rid of this by reflectively looking at 
     * static factory methods and the fields within the Type itself
     * but for the time being, lets just do this
     * ....
     * 
     * MAYBE, I could look at the non static member fields, 
     * I could do some annotation nonsense
     * 
     */
    public String selfAuthor();
}
