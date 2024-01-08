package si.fri.rso.seznam.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "seznam_metadata")
@SequenceGenerator(name = "sequence", sequenceName = "mySequence")
@NamedQueries(value =
        {
                @NamedQuery(name = "SeznamMetadataEntity.getAll",
                        query = "SELECT im FROM SeznamMetadataEntity im")
        })
public class SeznamMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Integer id;

    @Column(name = "cocktail_id")
    private String cocktailId;

    @Column(name = "name")
    private String name;

    @Column(name = "user_name")
    private String user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCocktailId() {
        return cocktailId;
    }

    public void setCocktailId(String cocktailId) {
        this.cocktailId = cocktailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String description) {
        this.user = description;
    }

}