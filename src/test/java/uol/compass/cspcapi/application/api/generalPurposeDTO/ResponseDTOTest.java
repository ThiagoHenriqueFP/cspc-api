package uol.compass.cspcapi.application.api.generalPurposeDTO;

import org.junit.jupiter.api.Test;
import uol.compass.cspcapi.application.api.generalPourposeDTO.ResponseDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResponseDTOTest {
    @Test
    public void testGetData() {
        // Criação de um objeto de exemplo para testar a classe ResponseDTO
        String testData = "Exemplo de dados";
        ResponseDTO<String> responseDTO = new ResponseDTO<>(testData);

        // Verificação do método getData()
        assertEquals(testData, responseDTO.getData());
    }

    @Test
    public void testGetTimestamp() {
        // Criação de um objeto ResponseDTO para testar
        ResponseDTO<String> responseDTO = ResponseDTO.ok("Teste de data");

        // Verificação do método getTimestamp()
        assertNotNull(responseDTO.getTimestamp());
    }

    @Test
    public void testOk() {
        // Dados de exemplo
        String testData = "Dados de teste";

        // Chamada do método estático ok() para criar um objeto ResponseDTO
        ResponseDTO<String> responseDTO = ResponseDTO.ok(testData);

        // Verificação do método getData() e se o objeto não é nulo
        assertEquals(testData, responseDTO.getData());
        assertNotNull(responseDTO.getTimestamp());
    }
}
