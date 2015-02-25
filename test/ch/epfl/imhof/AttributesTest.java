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
package ch.epfl.imhof;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public class AttributesTest {
    private HashMap<String, String> newHashMap(String... args) {
        HashMap<String, String> newHashMap = new HashMap<>();
        for (int i = 0; i < args.length - 1; ++i) {
            newHashMap.put(args[i], args[i + 1]);
        }
        return newHashMap;
    }
    
    private Attributes newAttributes(String... args) {
        return new Attributes(newHashMap(args));
    }
    
    @Test
    public void returnsTrueForEmptyMap() {
        Attributes test = new Attributes(new HashMap<String, String>());
        assertTrue(test.isEmpty());
    }
    
    @Test
    public void returnsFalseForNotEmptyMap() {
        Attributes test = newAttributes("name", "Léman");
        assertFalse(test.isEmpty());
    }
    
    @Test
    public void returnsTrueForKeyPresent() {
        Attributes test = newAttributes("name", "Léman", "color", "blue");
        assertTrue(test.contains("name"));
        assertTrue(test.contains("color"));
    }
    
    @Test
    public void returnsFalseForKeyNotPresent() {
        Attributes test = newAttributes("name", "Léman", "color", "blue");
        assertFalse(test.contains("jambon"));
        assertFalse(test.contains("pourquoi?"));
    }
}
