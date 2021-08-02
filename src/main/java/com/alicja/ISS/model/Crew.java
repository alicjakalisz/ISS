package com.alicja.ISS.model;

import javax.persistence.*;

@Entity
public class Crew {
    @Id
    @SequenceGenerator(name = "crew_sequence",
            sequenceName = "crew_sequence",
            allocationSize = 1) //how much would the sequence increase from
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "crew_sequence")
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "iss_id", referencedColumnName = "id")
    private ISS iss;


    public Crew() {
    }

    public Crew( String name, ISS iss) {

        this.name = name;
        this.iss = iss;
    }

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ISS getIss() {
        return iss;
    }

    public void setIss(ISS iss) {
        this.iss = iss;
    }


}
