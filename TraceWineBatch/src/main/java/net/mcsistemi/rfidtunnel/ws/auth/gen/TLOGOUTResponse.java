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
 * <p>Classe Java per tLOGOUTResponse complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="tLOGOUTResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LOGOUTResult" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="LOGOUTMessage" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="LOGOUTConnexionId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tLOGOUTResponse", propOrder = {
    "logoutResult",
    "logoutMessage",
    "logoutConnexionId"
})
@XmlRootElement(name = "LOGOUTResponse")
public class TLOGOUTResponse {

    @XmlElement(name = "LOGOUTResult")
    protected int logoutResult;
    @XmlElement(name = "LOGOUTMessage", required = true)
    protected String logoutMessage;
    @XmlElement(name = "LOGOUTConnexionId")
    protected int logoutConnexionId;

    /**
     * Recupera il valore della proprietà logoutResult.
     * 
     */
    public int getLOGOUTResult() {
        return logoutResult;
    }

    /**
     * Imposta il valore della proprietà logoutResult.
     * 
     */
    public void setLOGOUTResult(int value) {
        this.logoutResult = value;
    }

    /**
     * Recupera il valore della proprietà logoutMessage.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOGOUTMessage() {
        return logoutMessage;
    }

    /**
     * Imposta il valore della proprietà logoutMessage.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOGOUTMessage(String value) {
        this.logoutMessage = value;
    }

    /**
     * Recupera il valore della proprietà logoutConnexionId.
     * 
     */
    public int getLOGOUTConnexionId() {
        return logoutConnexionId;
    }

    /**
     * Imposta il valore della proprietà logoutConnexionId.
     * 
     */
    public void setLOGOUTConnexionId(int value) {
        this.logoutConnexionId = value;
    }

}
