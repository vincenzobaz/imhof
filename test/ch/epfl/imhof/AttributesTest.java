package ch.epfl.imhof;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

public class AttributesTest {
    @Test
    public void isEmptyReturnsTrueForEmptyMap() {
        Attributes test = new Attributes(new HashMap<String, String>());
        assertTrue(test.isEmpty());
    }

    @Test
    public void isEmptyReturnsFalseForNotEmptyMap() {
        Attributes test = BuildAll.newAttributes("name", "Léman");
        assertFalse(test.isEmpty());
    }

    @Test
    public void containsReturnsTrueForKeyPresent() {
        Attributes test = BuildAll.newAttributes("name", "Léman", "color", "blue");
        assertTrue(test.contains("name"));
        assertTrue(test.contains("color"));
    }

    @Test
    public void containsReturnsFalseForKeyNotPresent() {
        Attributes test = BuildAll.newAttributes("name", "Léman", "color", "blue");
        assertFalse(test.contains("jambon"));
        assertFalse(test.contains("pourquoi?"));
    }

    @Test
    public void isEmptyTest() {
        Map<String, String> emptyMap = new HashMap<String, String>();
        assertTrue(emptyMap.isEmpty());
        Map<String, String> notEmptyMap = new HashMap<String, String>();
        notEmptyMap.put("nature", "forest");
        assertFalse(notEmptyMap.isEmpty());
    }

    @Test
    public void containsTest() {
        Attributes notEmpty = BuildAll.newAttributes("nature", "forest", "highways",
                "motorway");
        assertTrue(notEmpty.contains("forest"));
    }

    @Test
    public void getReturnsCorrectValue() {
        Attributes test = BuildAll.newAttributes("je", "ne", "sais", "pas", "quoi",
                "mettre", "ici", ":(");
        assertEquals("mettre", test.get("quoi"));
    }

    @Test
    public void getReturnsNullForKeyNotValid() {
        Attributes test = BuildAll.newAttributes("je", "ne", "sais", "pas", "quoi",
                "mettre", "ici", ":(");
        assertEquals(null, test.get("rien"));
    }

    @Test
    public void getReturnsCorrectValueBis() {
        Attributes test = BuildAll.newAttributes("je", "ne", "sais", "pas", "quoi",
                "mettre", "ici", ":(");
        assertEquals(":(", test.get("ici", "defaultValue"));
    }

    @Test
    public void getReturnsDefaultValue() {
        Attributes test = BuildAll.newAttributes("je", "ne", "sais", "pas", "quoi",
                "mettre", "ici", ":(");
        assertEquals("defaultValue", test.get("fake", "defaultValue"));
    }

    @Test
    public void getReturnsCorrectInt() {
        Attributes test = BuildAll.newAttributes("je", "ne", "sais", "pas", "quoi",
                "mettre", "ici", "2014");
        assertEquals(2014, test.get("ici", 12));
    }

    @Test
    public void getReturnsDefaultValueBis() {
        Attributes test = BuildAll.newAttributes("je", "ne", "sais", "pas", "quoi",
                "mettre", "ici", "2014");
        assertEquals(42, test.get("sais", 42));
        assertEquals(42, test.get("faaaake", 42));
    }

    @Test
    public void keepOnlyKeysTest() {
        List<String> list = new ArrayList<>();
        list.add("sais");
        list.add("ici");
        HashSet<String> keysToKeep = new HashSet<>(list);
        Attributes toFilter = BuildAll.newAttributes("je", "ne", "sais", "pas", "quoi",
                "mettre", "ici", "2014");
        Attributes test = toFilter.keepOnlyKeys(keysToKeep);
        assertFalse(test.isEmpty());
        assertTrue(test.contains("sais"));
        assertEquals("pas", test.get("sais"));
        assertTrue(test.contains("ici"));
        assertEquals(2014, test.get("ici", 24));
        assertFalse(test.contains("je"));
        assertFalse(test.contains("quoi"));
    }
}
