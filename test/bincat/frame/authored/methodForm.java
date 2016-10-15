/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.frame.authored;

import java.util.Set;
import varcode.context.VarContext;
import varcode.doc.Compose;
import varcode.doc.DocState;
import varcode.dom.Dom;
import varcode.java.code._code;
import varcode.java.code._methods._method;
import varcode.markup.bindml.BindML;

/**
 *
 * @author eric
 */
public class methodForm
{
    public static methodForm of( String signature, String... body )
    {
        return new methodForm( signature, body );
    }
    
    private final Dom signature; 
    private final Dom body;
    
    public methodForm( String signature, String... body )
    {
        StringBuilder bodyLines = new StringBuilder();
        for( int i = 0; i < body.length; i++ )
        {
            if( i > 0 )
            {
                bodyLines.append( System.lineSeparator() );
            }
            bodyLines.append( body[ i ] );
        }
        this.signature = BindML.compile( signature );
        this.body = BindML.compile( bodyLines.toString() );
    }
    
    public Set<String> getVars()
    {
        Set<String> vars = signature.getAllVarNames( VarContext.of( ) );
        vars.addAll( body.getAllVarNames(VarContext.of( ) ) );
        return vars;
    }
    
    public _method compose( Object...keyValues )
    {
        VarContext vc = VarContext.of( keyValues );
        _method m = _method.of( 
            Compose.asString( this.signature, vc ), 
            _code.of( Compose.asString( this.body, vc ) ) );
        return m;
    }
    
    public _method inline( Object...values )
    {
        DocState state = Compose.inlineToState( this.signature, values );
        //get the signature after composing
        String sig = state.getTranslateBuffer().docAsString();    
        
        //create a nwe docState USING the 
        DocState ds = new DocState( this.body, state.getContext() );
        Compose.toState( ds );
        String bod = ds.getTranslateBuffer().docAsString();
        
        return _method.of( sig, 
            _code.of( bod ) );
    }
    
    public static void main( String[] args )
    {
        methodForm mf = methodForm.of( 
            "public static {+returnType+} load{+$^(name)+}( long row )",
                "return {+$^(name)+}.load( row );" );
        
        System.out.println( mf.getVars() );
        
        System.out.println( 
            mf.compose( "returnType", int.class, "name", "movieId" ) );
        
        System.out.println( 
            mf.inline( int.class, "movieId" ) );
        
        mf = methodForm.of( 
            "public static {+returnType+} load{+$^(name)+}( long row )",
                "return {+string+}.load( row );" );
        
        System.out.println( 
            mf.inline( int.class, "movieId", "str" ) );
        
        mf = methodForm.of( 
            "public static {+returnType+} load{+$^(name)+}( long row )",
                "//do something here",   
                "return {+string+}.load( row );",
                "// new {+param+}");
        
        System.out.println( 
            mf.inline( int.class, "movieId", "str", "Rise of the machines" ) );
    }
}
