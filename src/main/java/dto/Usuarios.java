/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ABNER
 */
@Entity
@Table(name = "usuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuarios.findAll", query = "SELECT u FROM Usuarios u"),
    @NamedQuery(name = "Usuarios.findByIduser", query = "SELECT u FROM Usuarios u WHERE u.iduser = :iduser"),
    @NamedQuery(name = "Usuarios.findByCoduni", query = "SELECT u FROM Usuarios u WHERE u.coduni = :coduni"),
    @NamedQuery(name = "Usuarios.validar", query = "SELECT u FROM Usuarios u WHERE u.coduni = :coduni AND u.passuni = :passuni"),
    @NamedQuery(name = "Usuarios.findByPassuni", query = "SELECT u FROM Usuarios u WHERE u.passuni = :passuni")})
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iduser")
    private Integer iduser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "coduni")
    private String coduni;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "passuni")
    private String passuni;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iduser")
    private List<Perfilusuario> perfilusuarioList;

    public Usuarios() {
    }

    public Usuarios(Integer iduser) {
        this.iduser = iduser;
    }

    public Usuarios(Integer iduser, String coduni, String passuni) {
        this.iduser = iduser;
        this.coduni = coduni;
        this.passuni = passuni;
    }

    public Integer getIduser() {
        return iduser;
    }

    public void setIduser(Integer iduser) {
        this.iduser = iduser;
    }

    public String getCoduni() {
        return coduni;
    }

    public void setCoduni(String coduni) {
        this.coduni = coduni;
    }

    public String getPassuni() {
        return passuni;
    }

    public void setPassuni(String passuni) {
        this.passuni = passuni;
    }

    @XmlTransient
    public List<Perfilusuario> getPerfilusuarioList() {
        return perfilusuarioList;
    }

    public void setPerfilusuarioList(List<Perfilusuario> perfilusuarioList) {
        this.perfilusuarioList = perfilusuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iduser != null ? iduser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.iduser == null && other.iduser != null) || (this.iduser != null && !this.iduser.equals(other.iduser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Usuarios[ iduser=" + iduser + " ]";
    }
    
}
