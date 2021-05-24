//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2021.05.24 alle 02:21:54 PM CEST 
//


package net.mcsistemi.rfidtunnel.sync.gen;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per tSYNCHRONISATION complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="tSYNCHRONISATION"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IdConnexion" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="Application" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Token" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Subject" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Function" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Params" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tSYNCHRONISATION", propOrder = {
    "idConnexion",
    "application",
    "token",
    "subject",
    "function",
    "params"
})
public class TSYNCHRONISATION {

    @XmlElement(name = "IdConnexion")
    protected int idConnexion;
    @XmlElement(name = "Application", required = true)
    protected String application;
    @XmlElement(name = "Token", required = true)
    protected String token;
    @XmlElement(name = "Subject", required = true)
    protected String subject;
    @XmlElement(name = "Function", required = true)
    protected String function;
    @XmlElement(name = "Params", required = true)
    protected String params;

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

    /**
     * Recupera il valore della proprietà subject.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Imposta il valore della proprietà subject.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubject(String value) {
        this.subject = value;
    }

    /**
     * Recupera il valore della proprietà function.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFunction() {
        return function;
    }

    /**
     * Imposta il valore della proprietà function.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFunction(String value) {
        this.function = value;
    }

    /**
     * Recupera il valore della proprietà params.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParams() {
        return params;
    }

    /**
     * Imposta il valore della proprietà params.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParams(String value) {
        this.params = value;
    }

}
