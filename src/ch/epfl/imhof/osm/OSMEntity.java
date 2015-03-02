package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import java.util.Map;
import java.util.HashMap;

public abstract class OSMEntity {
    private final long id;
    private final Attributes attributes;

    public OSMEntity(long id, Attributes attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    public long id() {
        return id;
    }

    public Attributes attributes() {
        return attributes;
    }

    public boolean hasAttribute(String key) {
        return attributes.contains(key);
    }

    public String attributeValue(String key) {
        return attributes.get(key);
    }

    public abstract static class Builder {
        private final long id;
        private boolean isIncomplete;
        private Attributes.Builder attributesInProgress;

        public Builder(long id) {
            this.id = id;
            attributesInProgress = new Attributes.Builder();
        }

        public void setAttribute(String key, String value) {
            attributesInProgress.put(key, value);
        }

        public void setIncomplete() {
            isIncomplete = true;
        }

        public boolean isIncomplete() {
            return isIncomplete;
        }

        public Attributes attributesBuild() {
            return attributesInProgress.build();
        }

        public long idBuild() {
            return id;
        }
    }
}
