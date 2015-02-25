package ch.epfl.imhof;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Map;
import java.util.HashMap;
import org.junit.Test;

public class AttributesTest {
    @Test
    public void isEmptyTest() {
        Map<String, String> emptyMap = new HashMap<String,String>();
        assertTrue(emptyMap.isEmpty());
        Map<String, String> notEmptyMap = new HashMap<String,String>();
        notEmptyMap.put("nature", "forest");
        assertFalse(notEmptyMap.isEmpty());  
    }
    
    @Test
    public void containsTest(){
        Map<String, String> notEmptyMap = new HashMap<String,String>();
        notEmptyMap.put("nature", "forest");
        assertTrue(notEmptyMap.contains("nature", "forest"));  
        
    }
    
    
}
