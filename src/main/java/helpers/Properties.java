/**
 * Класс для работы с константами из проперти файла
 *
 * @author Sharapov Yuri
 */
package helpers;

import org.aeonbits.owner.ConfigFactory;

public class Properties {
    public static TestProperties testProperties = ConfigFactory.create(TestProperties.class);
}
