package com.algo4chris.algo4chrisweb.junit5util;

import com.algo4chris.algo4chrisweb.AlgoBaseTest;
import com.algo4chris.algo4chrisweb.argsprovider_test.AlgoTestArgsProvider;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Junit5 工具測試與練習類<br>
 * 文章參考: https://www.baeldung.com/parameterized-tests-junit-5
 *
 * @author chris
 * */
@EnabledIfSystemProperty(named = "algo.test",matches = "true")
@DisplayName("[Junit5Util]测试")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class Junit5Util extends AlgoBaseTest {

    private List<String> in = new ArrayList<>(Arrays.asList("Hello", "Yes", "No"));
    private List<String> out = new ArrayList<>(Arrays.asList("Cześć", "Tak", "Nie"));

    @Override
    public String userName() {
        return null;
    }

    @Override
    public String sellerName() {
        return null;
    }

    @Test
    @DisplayName("assertAll")
    void groupAssertions() {
        int[] numbers = {0, 1, 2, 3, 4};
        assertAll("numbers",
                () -> assertEquals(numbers[0], 0),
                () -> assertEquals(numbers[3], 3),
                () -> assertEquals(numbers[4], 1)
        );
    }

    @Test
    @DisplayName("assumeTrue")
    void trueAssumption() {
        assumeTrue(5 > 1);
        assertEquals(5 + 2, 7);
    }

    @Test
    @DisplayName("assumeFalse")
    void falseAssumption() {
        assumeFalse(5 < 1);
        assertEquals(5 + 2, 7);
    }

    @Test
    @DisplayName("assumingThat")
    void assumptionThat() {
        String someString = "Just a string";
        assumingThat(
                someString.equals("Just a string"),
                () -> assertEquals(2 + 2, 4)
        );
    }

    @Test
    void shouldThrowException() {
        Throwable exception = assertThrows(UnsupportedOperationException.class, () -> {
            throw new UnsupportedOperationException("Not supported");
        });
        assertEquals(exception.getMessage(), "Not supported");
    }

    @Test
    void assertThrowsException() {
        String str = null;
        assertThrows(IllegalArgumentException.class, () -> {
            Integer.valueOf(str);
        });
    }

    @TestFactory
    public Stream<DynamicTest> translateDynamicTestsFromStream() {
        return in.stream()
                .map(word ->
                        DynamicTest.dynamicTest("Test translate " + word, () -> {
                            int id = in.indexOf(word);
                            assertEquals(out.get(id), translate(word));
                        })
                );
    }

    private String translate(String word) {
        if ("Hello".equalsIgnoreCase(word)) {
            return "Cześć";
        } else if ("Yes".equalsIgnoreCase(word)) {
            return "Tak";
        } else if ("No".equalsIgnoreCase(word)) {
            return "Nie";
        }
        return "Error";
    }

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({
            "0,    1,   1",
            "1,    2,   3",
            "49,  51, 100",
            "1,  100, 101"
    })
    void add(int first, int second, int expectedResult) {
        Calculator calculator = new Calculator();
        assertEquals(expectedResult, calculator.add(first, second),
                () -> first + " + " + second + " should equal " + expectedResult);
    }


    public class Calculator {

        public int add(int a, int b) {
            return a + b;
        }

    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  "})
    void isBlank_ShouldReturnTrueForNullOrBlankStrings(String input) {
        assertTrue(StringUtils.isBlank(input));
    }

//    @ParameterizedTest
//    @EnumSource(PayOrderStatus.class) //注入所有的狀態
//    void getValueFor_PayOrderStatus(PayOrderStatus payOrderStatus) {
//        int statusCode = payOrderStatus.getStatusCode();
//        System.out.println(statusCode);
//        assertTrue(statusCode >= 0 && statusCode <= 18);
//    }
//
//    @ParameterizedTest
//    @EnumSource(value = PayOrderStatus.class,
//                names = {"STATUS_CREATED","STATUS_CONFIRMING","STATUS_PENDING","STATUS_CURRENCY_PAYING"},//注入所有的狀態
//                mode = EnumSource.Mode.EXCLUDE)//排除以上names的enums
//    void getFilteredValueFor_PayOrderStatus(PayOrderStatus payOrderStatus) {
//        int statusCode = payOrderStatus.getStatusCode();
//        String msg     = payOrderStatus.getStatusMsg();
//        System.out.println(statusCode + " ==> " + msg);
//        assertTrue(statusCode >= 0 && statusCode <= 18);
//    }

    @ParameterizedTest
    @CsvSource(value = {"test:test", "tEst:test", "Java:java"}, delimiter = ':')
    void toLowerCase_ShouldGenerateTheExpectedLowercaseValue(String input, String expected) {
//        String actualValue = input.toLowerCase();
        assertEquals(expected, input);
    }

    @ParameterizedTest
    @MethodSource("provideStringsForIsBlank")
    void isBlank_ShouldReturnTrueForNullOrBlankStrings(String input, boolean expected) {
        System.out.println("input: "+input+",expected: "+expected);
        assertEquals(expected, StringUtils.isBlank(input));
    }

    private static Stream<Arguments> provideStringsForIsBlank() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of("", true),
                Arguments.of("  ", true),
                Arguments.of("not blank", false)
        );
    }


    @ParameterizedTest
    @ArgumentsSource(AlgoTestArgsProvider.class)
    void isBlank_ShouldReturnTrueForNullOrBlankStringsArgProvider(String input) {
        assertTrue(StringUtils.isBlank(input));
    }
}
