/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author bradl
 */
@Entity
@Table(name = "PORTFOLIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Portfolio.findAll", query = "SELECT p FROM Portfolio p"),
    @NamedQuery(name = "Portfolio.findById", query = "SELECT p FROM Portfolio p WHERE p.id = :id"),
    @NamedQuery(name = "Portfolio.findByUserId", query = "SELECT p FROM Portfolio p WHERE p.userId = :userId"),
    @NamedQuery(name = "Portfolio.findByStockname", query = "SELECT p FROM Portfolio p WHERE p.stockname = :stockname"),
    @NamedQuery(name = "Portfolio.findByStockticker", query = "SELECT p FROM Portfolio p WHERE p.stockticker = :stockticker"),
    @NamedQuery(name = "Portfolio.findByStockexchange", query = "SELECT p FROM Portfolio p WHERE p.stockexchange = :stockexchange"),
    @NamedQuery(name = "Portfolio.findByShareprice", query = "SELECT p FROM Portfolio p WHERE p.shareprice = :shareprice"),
    @NamedQuery(name = "Portfolio.findBySharesowned", query = "SELECT p FROM Portfolio p WHERE p.sharesowned = :sharesowned"),
    @NamedQuery(name = "Portfolio.findByEquitycap", query = "SELECT p FROM Portfolio p WHERE p.equitycap = :equitycap")})
public class Portfolio implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "USER_ID")
    private Integer userId;
    @Column(name = "STOCKNAME")
    private String stockname;
    @Column(name = "STOCKTICKER")
    private String stockticker;
    @Column(name = "STOCKEXCHANGE")
    private String stockexchange;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "SHAREPRICE")
    private Double shareprice;
    @Column(name = "SHARESOWNED")
    private Double sharesowned;
    @Column(name = "EQUITYCAP")
    private Double equitycap;

    public Portfolio() {
    }

    public Portfolio(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Integer oldId = this.id;
        this.id = id;
        changeSupport.firePropertyChange("id", oldId, id);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        Integer oldUserId = this.userId;
        this.userId = userId;
        changeSupport.firePropertyChange("userId", oldUserId, userId);
    }

    public String getStockname() {
        return stockname;
    }

    public void setStockname(String stockname) {
        String oldStockname = this.stockname;
        this.stockname = stockname;
        changeSupport.firePropertyChange("stockname", oldStockname, stockname);
    }

    public String getStockticker() {
        return stockticker;
    }

    public void setStockticker(String stockticker) {
        String oldStockticker = this.stockticker;
        this.stockticker = stockticker;
        changeSupport.firePropertyChange("stockticker", oldStockticker, stockticker);
    }

    public String getStockexchange() {
        return stockexchange;
    }

    public void setStockexchange(String stockexchange) {
        String oldStockexchange = this.stockexchange;
        this.stockexchange = stockexchange;
        changeSupport.firePropertyChange("stockexchange", oldStockexchange, stockexchange);
    }

    public Double getShareprice() {
        return shareprice;
    }

    public void setShareprice(Double shareprice) {
        Double oldShareprice = this.shareprice;
        this.shareprice = shareprice;
        changeSupport.firePropertyChange("shareprice", oldShareprice, shareprice);
    }

    public Double getSharesowned() {
        return sharesowned;
    }

    public void setSharesowned(Double sharesowned) {
        Double oldSharesowned = this.sharesowned;
        this.sharesowned = sharesowned;
        changeSupport.firePropertyChange("sharesowned", oldSharesowned, sharesowned);
    }

    public Double getEquitycap() {
        return equitycap;
    }

    public void setEquitycap(Double equitycap) {
        Double oldEquitycap = this.equitycap;
        this.equitycap = equitycap;
        changeSupport.firePropertyChange("equitycap", oldEquitycap, equitycap);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Portfolio)) {
            return false;
        }
        Portfolio other = (Portfolio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Portfolio[ id=" + id + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
