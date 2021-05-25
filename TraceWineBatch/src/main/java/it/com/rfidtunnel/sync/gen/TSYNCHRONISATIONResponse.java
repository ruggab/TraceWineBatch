//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2021.05.25 alle 11:44:09 AM CEST 
//


package it.com.rfidtunnel.sync.gen;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per tSYNCHRONISATIONResponse complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="tSYNCHRONISATIONResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SYNCHRONISATIONResult" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="SYNCHRONISATIONMessage" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="SYNCHRONISATIONMessageId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tSYNCHRONISATIONResponse", propOrder = {
    "synchronisationResult",
    "synchronisationMessage",
    "synchronisationMessageId"
})
public class TSYNCHRONISATIONResponse {

    @XmlElement(name = "SYNCHRONISATIONResult")
    protected int synchronisationResult;
    @XmlElement(name = "SYNCHRONISATIONMessage", required = true)
    protected String synchronisationMessage;
    @XmlElement(name = "SYNCHRONISATIONMessageId")
    protected int synchronisationMessageId;

    /**
     * Recupera il valore della proprietà synchronisationResult.
     * 
     */
    public int getSYNCHRONISATIONResult() {
        return synchronisationResult;
    }

    /**
     * Imposta il valore della proprietà synchronisationResult.
     * 
     */
    public void setSYNCHRONISATIONResult(int value) {
        this.synchronisationResult = value;
    }

    /**
     * Recupera il valore della proprietà synchronisationMessage.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSYNCHRONISATIONMessage() {
        return synchronisationMessage;
    }

    /**
     * Imposta il valore della proprietà synchronisationMessage.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSYNCHRONISATIONMessage(String value) {
        this.synchronisationMessage = value;
    }

    /**
     * Recupera il valore della proprietà synchronisationMessageId.
     * 
     */
    public int getSYNCHRONISATIONMessageId() {
        return synchronisationMessageId;
    }

    /**
     * Imposta il valore della proprietà synchronisationMessageId.
     * 
     */
    public void setSYNCHRONISATIONMessageId(int value) {
        this.synchronisationMessageId = value;
    }

}
