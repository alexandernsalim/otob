package otob.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import otob.util.generator.IdGenerator;

import static org.junit.Assert.assertFalse;
import static org.mockito.MockitoAnnotations.initMocks;

public class IdGeneratorTest {

    @InjectMocks
    private IdGenerator idGenerator;

    private String checkoutDate;

    @Before
    public void setUp() {
        initMocks(this);

        checkoutDate = "2019/08/13 15:02:00";
    }

    @Test
    public void generateOrderIdTest() throws Exception {
        String result = idGenerator.generateOrderId(checkoutDate);

        assertFalse(result.isEmpty());
    }

    @After
    public void tearDown() {
        //Do Nothing
    }

}
