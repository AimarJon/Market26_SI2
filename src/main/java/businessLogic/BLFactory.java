package businessLogic;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import configuration.ConfigXML;
import dataAccess.DataAccess;

public class BLFactory {

    public enum Type { LOCAL, REMOTE }

    public static BLFacade createBLFacade(Type type) throws MalformedURLException {
        switch (type) {
            case LOCAL:
                return new BLFacadeImplementation(new DataAccess());

            case REMOTE:
                ConfigXML c = ConfigXML.getInstance();
                String serviceName = "http://" + c.getBusinessLogicNode() + ":" + c.getBusinessLogicPort() + "/ws/" + c.getBusinessLogicName() + "?wsdl";
                URL url = new URL(serviceName);

                // 1. argumentua: wsdl dokumentua; 2. argumentua: zerbitzuaren izena
                QName qname = new QName("http://businessLogic/", "BLFacadeImplementationService");
                Service service = Service.create(url, qname);
                return service.getPort(BLFacade.class);

            default:
                throw new IllegalArgumentException("Negozio logika mota ezezaguna: " + type);
        }
    }
}