package helpers;

import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DataProvider {
    public static Stream<Arguments> provideTest(){
        List<String> vendors = new ArrayList<>();
        vendors.add("ASUS");
        vendors.add("Lenovo");
        return Stream.of(
                Arguments.of("Яндекс Маркет", "Ноутбуки и компьютеры", "Ноутбуки",
                        "10000", "70000", vendors, "12")
        );
    }
}
