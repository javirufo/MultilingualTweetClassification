//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.6 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: AM.06.03 a las 09:06:24 AM CEST 
//


package xml.es.daedalus.tass.tweets;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.daedalus.tass.tweets package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.daedalus.tass.tweets
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Sentiments }
     * 
     */
    public Sentiments createSentiments() {
        return new Sentiments();
    }

    /**
     * Create an instance of {@link TypePolarity }
     * 
     */
    public TypePolarity createTypePolarity() {
        return new TypePolarity();
    }

    /**
     * Create an instance of {@link Topics }
     * 
     */
    public Topics createTopics() {
        return new Topics();
    }

    /**
     * Create an instance of {@link Tweet }
     * 
     */
    public Tweet createTweet() {
        return new Tweet();
    }

    /**
     * Create an instance of {@link Tweets }
     * 
     */
    public Tweets createTweets() {
        return new Tweets();
    }

}
