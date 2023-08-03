package uol.compass.cspcapi.application.api.generalPurposeDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uol.compass.cspcapi.application.api.generalPourposeDTO.ResponseErrorDTO;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResponseErrorDTOTest {
    private ResponseErrorDTO responseErrorDTO;

    @BeforeEach
    public void setup() {
        List<String> messages = Arrays.asList("Error 1", "Error 2");
        responseErrorDTO = new ResponseErrorDTO(messages);
    }

    @Test
    public void testGetTimestamp() throws Exception {
        Date mockDate = mock(Date.class);

        // when(mockDate.getTime()).thenReturn(123456789L);

        Field timestampField = ResponseErrorDTO.class.getDeclaredField("timestamp");
        timestampField.setAccessible(true);
        timestampField.set(responseErrorDTO, mockDate);

        assertSame(mockDate, responseErrorDTO.getTimestamp());
    }

    @Test
    public void testGetMessages() {
        List<String> messages = Arrays.asList("Error 1", "Error 2");
        assertIterableEquals(messages, responseErrorDTO.getMessages());
    }
}
