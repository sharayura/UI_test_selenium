/**
 * Класс для установки параметров теста
 *
 * @author Sharapov Yuri
 */
package helpers;

import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DataProvider {
    public static Stream<Arguments> provideTest() {
        List<String> vendors = new ArrayList<>();
        vendors.add("HP");
        vendors.add("Lenovo");
        return Stream.of(
                Arguments.of("Яндекс Маркет", "Ноутбуки и компьютеры", "Ноутбуки",
                        "10000", "30000", vendors, "12")
        );
    }
}
