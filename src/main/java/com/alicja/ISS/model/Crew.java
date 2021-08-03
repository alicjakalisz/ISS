package com.alicja.ISS.model;

import javax.persistence.*;

@Entity
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
