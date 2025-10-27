/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ABNER
 */
@Entity
@Table(name = "perfilusuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Perfilusuario.findAll", query = "SELECT p FROM Perfilusuario p"),
    @NamedQuery(name = "Perfilusuario.findByIdperfil", query = "SELECT p FROM Perfilusuario p WHERE p.idperfil = :idperfil"),
    @NamedQuery(name = "Perfilusuario.findByNombre", query = "SELECT p FROM Perfilusuario p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Perfilusuario.findByApellido", query = "SELECT p FROM Perfilusuario p WHERE p.apellido = :apellido"),
    @NamedQuery(name = "Perfilusuario.findByFechnaci", query = "SELECT p FROM Perfilusuario p WHERE p.fechnaci = :fechnaci"),
    @NamedQuery(name = "Perfilusuario.findByCoduni", query = "SELECT p FROM Perfilusuario p WHERE p.coduni = :coduni"),
    @NamedQuery(name = "Perfilusuario.findByFacultad", query = "SELECT p FROM Perfilusuario p WHERE p.facultad = :facultad"),
    @NamedQuery(name = "Perfilusuario.findByEscuela", query = "SELECT p FROM Perfilusuario p WHERE p.escuela = :escuela"),
    @NamedQuery(name = "Perfilusuario.findByDireccion", query = "SELECT p FROM Perfilusuario p WHERE p.direccion = :direccion"),
    @NamedQuery(name = "Perfilusuario.findByCorreo", query = "SELECT p FROM Perfilusuario p WHERE p.correo = :correo"),
    @NamedQuery(name = "Perfilusuario.findByCelular", query = "SELECT p FROM Perfilusuario p WHERE p.celular = :celular"),
    @NamedQuery(name = "Perfilusuario.findByDni", query = "SELECT p FROM Perfilusuario p WHERE p.dni = :dni"),
    @NamedQuery(name = "Perfilusuario.findByUrlfoto", query = "SELECT p FROM Perfilusuario p WHERE p.urlfoto = :urlfoto")})
public class Perfilusuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idperfil")
    private Integer idperfil;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "apellido")
    private String apellido;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechnaci")
    @Temporal(TemporalType.DATE)
    private Date fechnaci;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "coduni")
    private String coduni;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "facultad")
    private String facultad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "escuela")
    private String escuela;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "correo")
    private String correo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "celular")
    private String celular;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "dni")
    private String dni;
    @Size(max = 300)
    @Column(name = "urlfoto")
    private String urlfoto;
    @JoinColumn(name = "iduser", referencedColumnName = "iduser")
    @ManyToOne(optional = false)
    private Usuarios iduser;

    public Perfilusuario() {
    }

    public Perfilusuario(Integer idperfil) {
        this.idperfil = idperfil;
    }

    public Perfilusuario(Integer idperfil, String nombre, String apellido, Date fechnaci, String coduni, String facultad, String escuela, String direccion, String correo, String celular, String dni) {
        this.idperfil = idperfil;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechnaci = fechnaci;
        this.coduni = coduni;
        this.facultad = facultad;
        this.escuela = escuela;
        this.direccion = direccion;
        this.correo = correo;
        this.celular = celular;
        this.dni = dni;
    }

    public Integer getIdperfil() {
        return idperfil;
    }

    public void setIdperfil(Integer idperfil) {
        this.idperfil = idperfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Date getFechnaci() {
        return fechnaci;
    }

    public void setFechnaci(Date fechnaci) {
        this.fechnaci = fechnaci;
    }

    public String getCoduni() {
        return coduni;
    }

    public void setCoduni(String coduni) {
        this.coduni = coduni;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public String getEscuela() {
        return escuela;
    }

    public void setEscuela(String escuela) {
        this.escuela = escuela;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getUrlfoto() {
        return urlfoto;
    }

    public void setUrlfoto(String urlfoto) {
        this.urlfoto = urlfoto;
    }

    public Usuarios getIduser() {
        return iduser;
    }

    public void setIduser(Usuarios iduser) {
        this.iduser = iduser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idperfil != null ? idperfil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Perfilusuario)) {
            return false;
        }
        Perfilusuario other = (Perfilusuario) object;
        if ((this.idperfil == null && other.idperfil != null) || (this.idperfil != null && !this.idperfil.equals(other.idperfil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Perfilusuario[ idperfil=" + idperfil + " ]";
    }
    
}
