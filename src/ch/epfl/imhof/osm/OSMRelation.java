package ch.epfl.imhof.osm;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import ch.epfl.imhof.Attributes;

/**
 * Cette classe répresente une relation OSM. La classe est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class OSMRelation extends OSMEntity {
    private final List<Member> members;

    /**
     * Construit une relation ayant l'identifiant, les membres et les attributs
     * donnés.
     * 
     * @param id
     *            l'identifiant unique de la relation.
     * @param members
     *            les membres de la relation
     * @param attributes
     *            les attributs de la relation
     */
    public OSMRelation(long id, List<Member> members, Attributes attributes) {
        super(id, attributes);
        this.members = Collections.unmodifiableList(new ArrayList<>(members));
    }

    /**
     * Retourne la liste des membres de la relation.
     * 
     * @return les membres de la relation, sous forme de List
     */
    public List<Member> members() {
        return members;
    }

    /**
     * Cette classe statique imbriquée représente un membre d'une relation OSM.
     * La classe est immuable
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static final class Member {
        private final Type type;
        private final String role;
        private final OSMEntity member;

        /**
         * Construit un membre ayant le type, le rôle et la valeur donnés.
         * 
         * @param type
         *            le type du membre
         * @param role
         *            le rôle du membre
         * @param member
         *            l'entité OSM qui constitue le membre
         */
        public Member(Type type, String role, OSMEntity member) {
            this.type = type;
            this.role = role;
            this.member = member;
        }

        /**
         * Retourne le type du membre.
         * 
         * @return le type du membre
         */
        public Type type() {
            return type;
        }

        /**
         * Retourne le rôle du membre.
         * 
         * @return le rôle du membre
         */
        public String role() {
            return role;
        }

        /**
         * Retourne le membre lui-même.
         * 
         * @return le membre, une OSMEntity
         */
        public OSMEntity member() {
            return member;
        }

        /**
         * Énumération des trois types de membres qu'une relation peut
         * comporter: NODE pour les noeuds, WAY pour les chemins, RELATION pour
         * les relations.
         * 
         * @author Vincenzo Bazzucchi (249733)
         * @author Nicolas Phan Van (239293)
         *
         */
        public static enum Type {
            NODE, WAY, RELATION;
        }
    }

    /**
     * Bâtisseur de la classe OSMRelation. Il hérite du bâtisseur de la classe
     * OSMEntity.
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static final class Builder extends OSMEntity.Builder {
        private final List<Member> members;

        /**
         * Construit un bâtisseur pour une relation ayant l'identifiant donné.
         * 
         * @param id
         *            l'identifiant unique de la relation
         */
        public Builder(long id) {
            super(id);
            members = new ArrayList<>();
        }

        /**
         * Ajoute un nouveau membre de type et de rôle donnés à la relation.
         * 
         * @param type
         *            le type du membre
         * @param role
         *            le rôle du membre
         * @param newMember
         *            le membre
         */
        public void addMember(Member.Type type, String role, OSMEntity newMember) {
            members.add(new Member(type, role, newMember));
        }

        /**
         * Construit et retourne la relation ayant l'identifiant passé au
         * constructeur ainsi que les membres et les attributs ajoutés au
         * bâtisseur.
         * 
         * @return la relation
         * @throws IllegalStateException
         *             lève une exception si la relation en cours de
         *             construction est incomplète
         */
        public OSMRelation build() throws IllegalStateException {
            if (isIncomplete()) {
                throw new IllegalStateException(
                        "La relation en cours de construction est incomplète.");
            }
            return new OSMRelation(this.id, members,
                    this.attributesInProgress.build());
        }
    }
}
