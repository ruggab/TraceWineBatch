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
 * <p>Classe Java per tCHECKLOGINResponse complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="tCHECKLOGINResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CHECKLOGINResult" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="CHECKLOGINMessage" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CHECKLOGINConnexionId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tCHECKLOGINResponse", propOrder = {
    "checkloginResult",
    "checkloginMessage",
    "checkloginConnexionId"
})
@XmlRootElement(name = "CHECKLOGINResponse")
public class TCHECKLOGINResponse {

    @XmlElement(name = "CHECKLOGINResult")
    protected int checkloginResult;
    @XmlElement(name = "CHECKLOGINMessage", required = true)
    protected String checkloginMessage;
    @XmlElement(name = "CHECKLOGINConnexionId")
    protected int checkloginConnexionId;

    /**
     * Recupera il valore della proprietà checkloginResult.
     * 
     */
    public int getCHECKLOGINResult() {
        return checkloginResult;
    }

    /**
     * Imposta il valore della proprietà checkloginResult.
     * 
     */
    public void setCHECKLOGINResult(int value) {
        this.checkloginResult = value;
    }

    /**
     * Recupera il valore della proprietà checkloginMessage.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCHECKLOGINMessage() {
        return checkloginMessage;
    }

    /**
     * Imposta il valore della proprietà checkloginMessage.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCHECKLOGINMessage(String value) {
        this.checkloginMessage = value;
    }

    /**
     * Recupera il valore della proprietà checkloginConnexionId.
     * 
     */
    public int getCHECKLOGINConnexionId() {
        return checkloginConnexionId;
    }

    /**
     * Imposta il valore della proprietà checkloginConnexionId.
     * 
     */
    public void setCHECKLOGINConnexionId(int value) {
        this.checkloginConnexionId = value;
    }

}
