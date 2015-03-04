package ch.epfl.imhof.osm;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import ch.epfl.imhof.osm.OSMEntity;
import ch.epfl.imhof.Attributes;

/**
 * Cette classe répresente une relation OSM. La classe est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class OSMRelation extends OSMEntity {
    private List<Member> members;

    /**
     * Le constructeur de la classe. Comme la classe doit être immuable, on fait
     * une copie des objets non-immuables et on la stocke sous forme de vue
     * non-modifialbe
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
     * Accesseur de membres de la realtion. L'objet founri est une vue
     * non-modifiable, ce qui garantit l'immuabilité de la classe
     * 
     * @return la liste des membres de la relation
     */
    public List<Member> members() {
        return members;
    }

    /**
     * Cette classe static imbriquée représente un membre d'une relation OSM. La
     * classe est immuable
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static final class Member {
        private Type type;
        private String role;
        private OSMEntity member;

        /**
         * Le cosntructeur de la classe.
         * 
         * @param type
         *            l
         * @param role
         *            le role du membre
         * @param member
         *            membre défini comme entité osm
         */
        public Member(Type type, String role, OSMEntity member) {
            this.type = type;
            this.role = role;
            this.member = member;
        }

        /**
         * accesseur du type du membre. La classe Type étant immuable, nous
         * n'avons pas besoin de copier type avant le renvoyer
         * 
         * @return le type du membre
         */
        public Type type() {
            return type;
        }

        /**
         * Accesseur de l'attribut role. Comme il s'agit d'un String, immuable
         * par délfinition, nous n'avons pas besoin de le copier avant de le
         * renvoyer
         * 
         * @return le role du membre.
         */
        public String role() {
            return role;
        }

        /**
         * accesseur pour l'entité qu'on veut qualifier en tant que membre
         * 
         * @return une entité membre
         */
        public OSMEntity member() {
            return member;
        }

        static class Type {
            enum type {
                NODE, WAY, RELATION
            };
        }
    }

    /**
     * Bâtisseur de la classe OSMRelation. Il hérité du bâtisseur de la classe
     * OSMEntity vu que cell-ci hérite de OSMEntity
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static final class Builder extends OSMEntity.Builder {
        private List<Member> members;

        /**
         * le constructeur de la classe. Pour instancier on ne demande qu'un
         * identificateur unique
         * 
         * @param id
         *            l'identificateur unique
         */
        public Builder(long id) {
            super(id);
            members = new ArrayList<>();
        }

        /**
         * Cette méthode permet d'ajouter un membre à la rélation en le
         * qualifiant avec des attributs
         * 
         * @param type
         *            du membre
         * @param role
         *            le role dans la relation
         * @param newMember
         *            le membre.
         */
        public void addMember(Member.Type type, String role, OSMEntity newMember) {
            members.add(new Member(type, role, newMember));
        }

        /**
         * Méthode permettant enfin de construire une OSMRelation à partir de
         * l'id passé au constructeur combiné avec les membres ajoutés avec la
         * méthode addMember
         * 
         * @return la relation
         */
        public OSMRelation build() {
            if (isIncomplete()) {
                throw new IllegalStateException(
                        "La relation en cours de construction est incomplète.");
            }
            return new OSMRelation(idBuild(), members, attributesBuild());
        }
    }
}
