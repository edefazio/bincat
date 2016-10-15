package bincat;

import bincat.type.Type;

public class Field
{
    public static Field of( Type type, String name )
    {
        return new Field( type, name );
    }
    
    public static Field of( String name, Type type )
    {
        return new Field( type, name );
    }
    
    public String name;
    public Type type;    
    
    public Field( Type type, String name )
    {
        this.name = name;
        this.type = type;
    }
    
    public Type getType()
    {
        return type;
    }
    
    public String getName()
    {
        return name;
    }
}
