//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2021.05.22 alle 05:57:14 AM CEST 
//


package it.com.rfidtunnel.ws.auth.gen;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.mcsistemi.rfidtunnel.ws.auth.gen package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _LOGIN_QNAME = new QName("urn:WS_TRC_AUTHENTICATION", "LOGIN");
    private final static QName _LOGINResponse_QNAME = new QName("urn:WS_TRC_AUTHENTICATION", "LOGINResponse");
    private final static QName _LOGOUT_QNAME = new QName("urn:WS_TRC_AUTHENTICATION", "LOGOUT");
    private final static QName _LOGOUTResponse_QNAME = new QName("urn:WS_TRC_AUTHENTICATION", "LOGOUTResponse");
    private final static QName _CHECKLOGIN_QNAME = new QName("urn:WS_TRC_AUTHENTICATION", "CHECKLOGIN");
    private final static QName _CHECKLOGINResponse_QNAME = new QName("urn:WS_TRC_AUTHENTICATION", "CHECKLOGINResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.mcsistemi.rfidtunnel.ws.auth.gen
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TLOGIN }
     * 
     */
    public TLOGIN createTLOGIN() {
        return new TLOGIN();
    }

    /**
     * Create an instance of {@link TLOGINResponse }
     * 
     */
    public TLOGINResponse createTLOGINResponse() {
        return new TLOGINResponse();
    }

    /**
     * Create an instance of {@link TLOGOUT }
     * 
     */
    public TLOGOUT createTLOGOUT() {
        return new TLOGOUT();
    }

    /**
     * Create an instance of {@link TLOGOUTResponse }
     * 
     */
    public TLOGOUTResponse createTLOGOUTResponse() {
        return new TLOGOUTResponse();
    }

    /**
     * Create an instance of {@link TCHECKLOGIN }
     * 
     */
    public TCHECKLOGIN createTCHECKLOGIN() {
        return new TCHECKLOGIN();
    }

    /**
     * Create an instance of {@link TCHECKLOGINResponse }
     * 
     */
    public TCHECKLOGINResponse createTCHECKLOGINResponse() {
        return new TCHECKLOGINResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TLOGIN }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TLOGIN }{@code >}
     */
    @XmlElementDecl(namespace = "urn:WS_TRC_AUTHENTICATION", name = "LOGIN")
    public JAXBElement<TLOGIN> createLOGIN(TLOGIN value) {
        return new JAXBElement<TLOGIN>(_LOGIN_QNAME, TLOGIN.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TLOGINResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TLOGINResponse }{@code >}
     */
    @XmlElementDecl(namespace = "urn:WS_TRC_AUTHENTICATION", name = "LOGINResponse")
    public JAXBElement<TLOGINResponse> createLOGINResponse(TLOGINResponse value) {
        return new JAXBElement<TLOGINResponse>(_LOGINResponse_QNAME, TLOGINResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TLOGOUT }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TLOGOUT }{@code >}
     */
    @XmlElementDecl(namespace = "urn:WS_TRC_AUTHENTICATION", name = "LOGOUT")
    public JAXBElement<TLOGOUT> createLOGOUT(TLOGOUT value) {
        return new JAXBElement<TLOGOUT>(_LOGOUT_QNAME, TLOGOUT.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TLOGOUTResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TLOGOUTResponse }{@code >}
     */
    @XmlElementDecl(namespace = "urn:WS_TRC_AUTHENTICATION", name = "LOGOUTResponse")
    public JAXBElement<TLOGOUTResponse> createLOGOUTResponse(TLOGOUTResponse value) {
        return new JAXBElement<TLOGOUTResponse>(_LOGOUTResponse_QNAME, TLOGOUTResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TCHECKLOGIN }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TCHECKLOGIN }{@code >}
     */
    @XmlElementDecl(namespace = "urn:WS_TRC_AUTHENTICATION", name = "CHECKLOGIN")
    public JAXBElement<TCHECKLOGIN> createCHECKLOGIN(TCHECKLOGIN value) {
        return new JAXBElement<TCHECKLOGIN>(_CHECKLOGIN_QNAME, TCHECKLOGIN.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TCHECKLOGINResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TCHECKLOGINResponse }{@code >}
     */
    @XmlElementDecl(namespace = "urn:WS_TRC_AUTHENTICATION", name = "CHECKLOGINResponse")
    public JAXBElement<TCHECKLOGINResponse> createCHECKLOGINResponse(TCHECKLOGINResponse value) {
        return new JAXBElement<TCHECKLOGINResponse>(_CHECKLOGINResponse_QNAME, TCHECKLOGINResponse.class, null, value);
    }

}
