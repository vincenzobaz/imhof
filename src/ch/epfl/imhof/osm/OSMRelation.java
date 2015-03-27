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
     * Le constructeur de la classe. Comme la classe doit être immuable, on fait
     * une copie des objets non-immuables et on la stocke sous forme de vue
     * non-modifiable
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
     * Accesseur des membres de la relation. L'objet fourni est une vue
     * non-modifiable, ce qui garantit l'immuabilité de la classe.
     * 
     * @return la liste des membres de la relation
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
         * Le constructeur de la classe.
         * 
         * @param type
         *            le type du membre
         * @param role
         *            le rôle du membre
         * @param member
         *            membre défini comme entité OSM
         */
        public Member(Type type, String role, OSMEntity member) {
            this.type = type;
            this.role = role;
            this.member = member;
        }

        /**
         * Accesseur du type du membre. La classe Type étant immuable, nous
         * n'avons pas besoin de copier type avant de le renvoyer.
         * 
         * @return le type du membre
         */
        public Type type() {
            return type;
        }

        /**
         * Accesseur de l'attribut role. Comme il s'agit d'un String, immuable
         * par définition, nous n'avons pas besoin de le copier avant de le
         * renvoyer.
         * 
         * @return le role du membre
         */
        public String role() {
            return role;
        }

        /**
         * Accesseur pour l'entité qu'on veut qualifier en tant que membre
         * 
         * @return une entité membre
         */
        public OSMEntity member() {
            return member;
        }

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
         * Constructeur du bâtisseur. Pour instancier on ne demande qu'un
         * identifiant unique.
         * 
         * @param id
         *            l'identifiant unique
         */
        public Builder(long id) {
            super(id);
            members = new ArrayList<>();
        }

        /**
         * Cette méthode permet d'ajouter un membre à la relation en le
         * qualifiant avec des attributs
         * 
         * @param type
         *            le type du membre
         * @param role
         *            le role dans la relation
         * @param newMember
         *            le membre
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
