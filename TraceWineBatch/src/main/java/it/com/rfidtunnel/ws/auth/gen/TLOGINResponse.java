//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2021.05.22 alle 05:57:14 AM CEST 
//


package it.com.rfidtunnel.ws.auth.gen;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per tLOGINResponse complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="tLOGINResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LOGINResult" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="LOGINMessage" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="LOGINConnexionId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LOGINResponse", propOrder = {
    "loginResult",
    "loginMessage",
    "loginConnexionId"
})
@XmlRootElement(name = "LOGINResponse")
public class TLOGINResponse {

    @XmlElement(name = "LOGINResult")
    protected int loginResult;
    @XmlElement(name = "LOGINMessage", required = true)
    protected String loginMessage;
    @XmlElement(name = "LOGINConnexionId")
    protected int loginConnexionId;

    /**
     * Recupera il valore della proprietà loginResult.
     * 
     */
    public int getLOGINResult() {
        return loginResult;
    }

    /**
     * Imposta il valore della proprietà loginResult.
     * 
     */
    public void setLOGINResult(int value) {
        this.loginResult = value;
    }

    /**
     * Recupera il valore della proprietà loginMessage.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOGINMessage() {
        return loginMessage;
    }

    /**
     * Imposta il valore della proprietà loginMessage.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOGINMessage(String value) {
        this.loginMessage = value;
    }

    /**
     * Recupera il valore della proprietà loginConnexionId.
     * 
     */
    public int getLOGINConnexionId() {
        return loginConnexionId;
    }

    /**
     * Imposta il valore della proprietà loginConnexionId.
     * 
     */
    public void setLOGINConnexionId(int value) {
        this.loginConnexionId = value;
    }

}
