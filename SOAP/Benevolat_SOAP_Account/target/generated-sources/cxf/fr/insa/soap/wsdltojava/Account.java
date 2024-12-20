package fr.insa.soap.wsdltojava;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.4.2
 * 2024-11-13T11:46:12.607+01:00
 * Generated source version: 3.4.2
 *
 */
@WebServiceClient(name = "account",
                  wsdlLocation = "file:/C:/Users/aure3/eclipse-workspace/Benevolat_SOAP_Account/src/main/resources/wsdl/account.wsdl",
                  targetNamespace = "http://soap.insa.fr/")
public class Account extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://soap.insa.fr/", "account");
    public final static QName AccountWSPort = new QName("http://soap.insa.fr/", "AccountWSPort");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/Users/aure3/eclipse-workspace/Benevolat_SOAP_Account/src/main/resources/wsdl/account.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(Account.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:/C:/Users/aure3/eclipse-workspace/Benevolat_SOAP_Account/src/main/resources/wsdl/account.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public Account(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public Account(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public Account() {
        super(WSDL_LOCATION, SERVICE);
    }

    public Account(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public Account(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public Account(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns AccountWS
     */
    @WebEndpoint(name = "AccountWSPort")
    public AccountWS getAccountWSPort() {
        return super.getPort(AccountWSPort, AccountWS.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AccountWS
     */
    @WebEndpoint(name = "AccountWSPort")
    public AccountWS getAccountWSPort(WebServiceFeature... features) {
        return super.getPort(AccountWSPort, AccountWS.class, features);
    }

}
