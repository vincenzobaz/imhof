package ch.epfl.imhof;

public final class Attributes {
    private HashMap<String, String> attributes;
    
    public Attributes(Map<String, String> attributes) {
        this.attributes = new HashMap(Map<String, String> attributes);
    }
    
    public boolean isEmpty() {
        return attributes.isEmpty();
    }
    
    public boolean contains(String key) {
        return (attributes.containsKey(key) || attributes.containsValue(key));
    }
    
    public String get(String key) {
        return attributes.get(key);
    }
    
    public String get(String key, String defaultValue) {
        return attributes.getOrDefault(key, defaultValue);
    }
    
    public int get(String key, int defaultValue) {
        int temp;
        try {
            temp = Integer.parseInt(get(key));
        } catch (NumberFormatException e) {
            temp = defaultValue;
        }
        finally {
            return temp;
        }
    }
    
    public Attributes keepOnlyKeys(Set<String> keysToKeep) {
        Attributes filteredAttributes = new Builder();
        for (int i = 0; i < keysToKeep.size(); ++i) {
            if (contains(keysToKeep.get(i))) {
                filteredAttributes.put(keysToKeep.get(i), get(keysToKeep.get(i)));
            }
        }
        return filteredAttributes.build();
    }
    
    public static final class Builder {
        private HashMap<String, String> attributes;
        
        public void put(String key, String value) {
            attributes.put(key, value);
        }
        
        public Attributes build() {
            return new Attributes(attributes);
        }
    }
}
