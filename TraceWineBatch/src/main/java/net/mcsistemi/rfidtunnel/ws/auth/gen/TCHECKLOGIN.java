//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2021.05.22 alle 05:57:14 AM CEST 
//


package net.mcsistemi.rfidtunnel.ws.auth.gen;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per tCHECKLOGIN complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="tCHECKLOGIN"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IdConnexion" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="Application" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Token" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tCHECKLOGIN", propOrder = {
    "idConnexion",
    "application",
    "token"
})
@XmlRootElement(name = "CHECKLOGIN")
public class TCHECKLOGIN {

    @XmlElement(name = "IdConnexion")
    protected int idConnexion;
    @XmlElement(name = "Application", required = true)
    protected String application;
    @XmlElement(name = "Token", required = true)
    protected String token;

    /**
     * Recupera il valore della proprietà idConnexion.
     * 
     */
    public int getIdConnexion() {
        return idConnexion;
    }

    /**
     * Imposta il valore della proprietà idConnexion.
     * 
     */
    public void setIdConnexion(int value) {
        this.idConnexion = value;
    }

    /**
     * Recupera il valore della proprietà application.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplication() {
        return application;
    }

    /**
     * Imposta il valore della proprietà application.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplication(String value) {
        this.application = value;
    }

    /**
     * Recupera il valore della proprietà token.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Imposta il valore della proprietà token.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

}
