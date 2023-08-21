package helpers;

import resources.generated.PRDWorld;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class XmlDeserializer {
    public static PRDWorld deserialeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(PRDWorld.class);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }
}
