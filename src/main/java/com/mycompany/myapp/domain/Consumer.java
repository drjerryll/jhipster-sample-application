package com.mycompany.myapp.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Consumer.
 */
@Entity
@Table(name = "consumer")
public class Consumer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "update_date")
    private ZonedDateTime updateDate;

    @Column(name = "procedura")
    private String procedura;

    @Column(name = "enabled")
    private Boolean enabled;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Consumer creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getUpdateDate() {
        return updateDate;
    }

    public Consumer updateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getProcedura() {
        return procedura;
    }

    public Consumer procedura(String procedura) {
        this.procedura = procedura;
        return this;
    }

    public void setProcedura(String procedura) {
        this.procedura = procedura;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public Consumer enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Consumer)) {
            return false;
        }
        return id != null && id.equals(((Consumer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Consumer{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", procedura='" + getProcedura() + "'" +
            ", enabled='" + isEnabled() + "'" +
            "}";
    }
}
