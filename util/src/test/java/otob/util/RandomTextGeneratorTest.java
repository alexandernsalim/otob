package otob.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import otob.util.generator.RandomTextGenerator;

import static org.junit.Assert.assertFalse;
import static org.mockito.MockitoAnnotations.initMocks;

public class RandomTextGeneratorTest {

    @InjectMocks
    private RandomTextGenerator textGenerator;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void generateRandomUserIdTest() {
        String result = textGenerator.generateRandomUserId();

        assertFalse(result.isEmpty());
    }

    @Test
    public void generateRandomPasswordTest() {
        String result = textGenerator.generateRandomUserId();

        assertFalse(result.isEmpty());
    }

}
