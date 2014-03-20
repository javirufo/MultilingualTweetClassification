//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.6 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: AM.06.03 a las 09:06:24 AM CEST 
//


package xml.es.daedalus.tass.tweets;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para enumTypePolarity.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTypePolarity">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="AGREEMENT"/>
 *     &lt;enumeration value="DISAGREEMENT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumTypePolarity")
@XmlEnum
public enum EnumTypePolarity {

    AGREEMENT,
    DISAGREEMENT;

    public String value() {
        return name();
    }

    public static EnumTypePolarity fromValue(String v) {
        return valueOf(v);
    }

}
