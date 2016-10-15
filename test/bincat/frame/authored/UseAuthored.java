
package bincat.frame.authored;

public class UseAuthored
{
    public static void main(String[] args)
    {   
        long row = NetflixFrame.synthesize();
        
        System.out.println( NetflixFrame.describeRow( row ) );
        
        System.out.println( NetflixFrame.loadDay( row ) );
        System.out.println( NetflixFrame.loadMovieId(row ) );
        System.out.println( NetflixFrame.loadPersonId( row ) );
        System.out.println( NetflixFrame.loadRating( row ) );
        
    }
    
}
