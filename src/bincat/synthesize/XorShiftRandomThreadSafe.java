/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bincat.synthesize;

import java.util.Random;

/**
 * In Java 8+ land I should consider delegating to a 
 * <A HREF="http://cs.oswego.edu/pipermail/concurrency-interest/2013-July/011579.html">SplittableRandom</A>
 * 
 * A lightning Fast Thread-safe (the seed is stored in Thread-Local) 
 * Pseudo Random Number Generator that uses bit shifting
 * (The Mutable long "seed" is maintained on ThreadLocal) 
 * 
 * NOTE: this is built for speed not to run Monte Carlo simulations from or anything
 * involving security or token-identity
 * 
 * This is used so that old JVMs (which have somewhat slow and/or non-threadsafe 
 * implementation of java.util.Random) can create Pseudo random numbers fast without 
 * thread contention.
 * 
 * Uses an Xor Shift variant to produce random numbers
 *  
 * @author M. Eric DeFazio eric@typefra.me
 */
public class XorShiftRandomThreadSafe 
	extends Random 
{
	private static final long serialVersionUID = 2937829177291436361L;

	private ThreadLocal<Long> seed;
	
	public XorShiftRandomThreadSafe() 
	{
		this( System.nanoTime() );		
	}
	
	/**
	 * creates a Pseudo non-cryptographic fast thread safe RNG  
	 * @param initialSeed
	 */
	public XorShiftRandomThreadSafe( final long initialSeed ) 
	{
		super();
		seed = new ThreadLocal<Long>(){
			protected Long initialValue() {
				return initialSeed;
			}
		};		
	}
	
	public int next( int nbits ) 
	{
		long x = randomLong();
		x &= ( ( 1L << nbits ) - 1 );
		return (int) x;
	}	
	
	public long randomLong() 
	{
		long x = seed.get();
		x ^= ( x << 21 ); 
		x ^= ( x >>> 35 );
		x ^= ( x << 4 );
		seed.set( x ); //sets the seed back on ThreadLocal
		return x;
	}
	
	public long nextLong( long maxValueExclusive ) 
	{
		if( maxValueExclusive <= 0 )
		{
			throw new RuntimeException (" Expected positive value >0  for maxValueExclusive ");			
		}
		for(;;) 
		{
			final long bits = randomLong() >>> 1;
			final long value = bits % maxValueExclusive;
			if( bits - value +( maxValueExclusive - 1 ) >= 0 ) return value;
		}
	}
}
