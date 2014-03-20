//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.6 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: AM.06.03 a las 09:06:24 AM CEST 
//


package xml.es.daedalus.tass.tweets;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tweetid" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;element name="user" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lang" type="{http://www.w3.org/2001/XMLSchema}language"/>
 *         &lt;element ref="{http://www.daedalus.es/TASS/tweets}sentiments" minOccurs="0"/>
 *         &lt;element ref="{http://www.daedalus.es/TASS/tweets}topics" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "tweetid",
    "user",
    "content",
    "date",
    "lang",
    "sentiments",
    "topics"
})
@XmlRootElement(name = "tweet")
public class Tweet {

    @XmlElement(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger tweetid;
    @XmlElement(required = true)
    protected String user;
    @XmlElement(required = true)
    protected String content;
    @XmlElement(required = true)
    protected String date;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "language")
    protected String lang;
    protected Sentiments sentiments;
    protected Topics topics;

    /**
     * Obtiene el valor de la propiedad tweetid.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTweetid() {
        return tweetid;
    }

    /**
     * Define el valor de la propiedad tweetid.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTweetid(BigInteger value) {
        this.tweetid = value;
    }

    /**
     * Obtiene el valor de la propiedad user.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUser() {
        return user;
    }

    /**
     * Define el valor de la propiedad user.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUser(String value) {
        this.user = value;
    }

    /**
     * Obtiene el valor de la propiedad content.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * Define el valor de la propiedad content.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
    }

    /**
     * Obtiene el valor de la propiedad date.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDate() {
        return date;
    }

    /**
     * Define el valor de la propiedad date.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDate(String value) {
        this.date = value;
    }

    /**
     * Obtiene el valor de la propiedad lang.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        return lang;
    }

    /**
     * Define el valor de la propiedad lang.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }

    /**
     * Obtiene el valor de la propiedad sentiments.
     * 
     * @return
     *     possible object is
     *     {@link Sentiments }
     *     
     */
    public Sentiments getSentiments() {
        return sentiments;
    }

    /**
     * Define el valor de la propiedad sentiments.
     * 
     * @param value
     *     allowed object is
     *     {@link Sentiments }
     *     
     */
    public void setSentiments(Sentiments value) {
        this.sentiments = value;
    }

    /**
     * Obtiene el valor de la propiedad topics.
     * 
     * @return
     *     possible object is
     *     {@link Topics }
     *     
     */
    public Topics getTopics() {
        return topics;
    }

    /**
     * Define el valor de la propiedad topics.
     * 
     * @param value
     *     allowed object is
     *     {@link Topics }
     *     
     */
    public void setTopics(Topics value) {
        this.topics = value;
    }

}
