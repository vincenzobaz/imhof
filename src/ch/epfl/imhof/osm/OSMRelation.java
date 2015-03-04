package ch.epfl.imhof.osm;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import ch.epfl.imhof.osm.OSMEntity;
import ch.epfl.imhof.Attributes;

public final class OSMRelation extends OSMEntity {
    private List<Member> members;

    public OSMRelation(long id, List<Member> members, Attributes attributes) {
        super(id, attributes);
        this.members = Collections.unmodifiableList(new ArrayList<Member>(
                members));
    }

    public List<Member> members() {
        return members;
    }

    public static final class Member {
        private Type type;
        private String role;
        private OSMEntity member;

        public Member(Type type, String role, OSMEntity member) {
            this.type = type;
            this.role = role;
            this.member = member;
        }

        public Type type() {
            return type;
        }

        public String role() {
            return role;
        }

        public OSMEntity member() {
            return member;
        }

        static class Type {
            enum type {
                NODE, WAY, RELATION
            };
        }
    }

    public static final class Builder extends OSMEntity.Builder {
        private List<Member> members;

        public Builder(long id) {
            super(id);
            members = new ArrayList<>();
        }

        public void addMember(Member.Type type, String role, OSMEntity newMember) {

        }

        public OSMRelation build() {
            if (isIncomplete()) {
                throw new IllegalStateException();
            }
            return new OSMRelation(idBuild(), members, attributesBuild());
        }
    }
}
