package ch.epfl.imhof;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Map;
import java.util.HashMap;
import org.junit.Test;

public class AttributesTest {
    public @Test public void isEmptyTest() {
        Map<String, String> emptyMap = new HashMap<String,String>();
        assertTrue(isEmpty(new Attributes(emptyMap)));
    }
    
    public 
    
    
}
