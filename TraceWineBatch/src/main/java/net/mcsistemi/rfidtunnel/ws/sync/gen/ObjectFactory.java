//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2021.05.23 alle 07:28:03 AM CEST 
//


package net.mcsistemi.rfidtunnel.ws.sync.gen;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.mcsistemi.rfidtunnel.ws.sync.gen package. 
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

    private final static QName _SYNCHRONISATION_QNAME = new QName("urn:WS_TRC_SYNCHRONISATION", "SYNCHRONISATION");
    private final static QName _SYNCHRONISATIONResponse_QNAME = new QName("urn:WS_TRC_SYNCHRONISATION", "SYNCHRONISATIONResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.mcsistemi.rfidtunnel.ws.sync.gen
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TSYNCHRONISATION }
     * 
     */
    public TSYNCHRONISATION createTSYNCHRONISATION() {
        return new TSYNCHRONISATION();
    }

    /**
     * Create an instance of {@link TSYNCHRONISATIONResponse }
     * 
     */
    public TSYNCHRONISATIONResponse createTSYNCHRONISATIONResponse() {
        return new TSYNCHRONISATIONResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSYNCHRONISATION }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TSYNCHRONISATION }{@code >}
     */
    @XmlElementDecl(namespace = "urn:WS_TRC_SYNCHRONISATION", name = "SYNCHRONISATION")
    public JAXBElement<TSYNCHRONISATION> createSYNCHRONISATION(TSYNCHRONISATION value) {
        return new JAXBElement<TSYNCHRONISATION>(_SYNCHRONISATION_QNAME, TSYNCHRONISATION.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSYNCHRONISATIONResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TSYNCHRONISATIONResponse }{@code >}
     */
    @XmlElementDecl(namespace = "urn:WS_TRC_SYNCHRONISATION", name = "SYNCHRONISATIONResponse")
    public JAXBElement<TSYNCHRONISATIONResponse> createSYNCHRONISATIONResponse(TSYNCHRONISATIONResponse value) {
        return new JAXBElement<TSYNCHRONISATIONResponse>(_SYNCHRONISATIONResponse_QNAME, TSYNCHRONISATIONResponse.class, null, value);
    }

}
