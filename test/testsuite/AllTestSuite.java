/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testsuite;

import bincat.RowTest;
import bincat.bit.BitFieldTest;
import bincat.bit.BitField_AuthorTest;
import bincat.bit.BitFrameTest;
import bincat.bit.BitFrame_AuthorTest;
import bincat.bit.BitRecordTest;
import bincat.match.RowMatchTest;
import bincat.match.TypeMatchTest;
import bincat.type.DayRangeTest;
import bincat.type.RangeTest;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author eric
 */
public class AllTestSuite
{
    public static Test suite() 
    {
        TestSuite suite = new TestSuite( AllTestSuite.class.getName() );
        
        suite.addTestSuite( RowTest.class );
        suite.addTestSuite( BitFieldTest.class );
        suite.addTestSuite( BitField_AuthorTest.class );
        suite.addTestSuite( BitFrameTest.class );
        suite.addTestSuite( BitFrame_AuthorTest.class );
        suite.addTestSuite( BitRecordTest.class );
        suite.addTestSuite( DayRangeTest.class );
        suite.addTestSuite(RangeTest.class );
        suite.addTestSuite( RowMatchTest.class );
        suite.addTestSuite( TypeMatchTest.class );
        
        return suite;
    }
}
