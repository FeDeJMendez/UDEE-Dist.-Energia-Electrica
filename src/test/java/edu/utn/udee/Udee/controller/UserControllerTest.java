package edu.utn.udee.Udee.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.utn.udee.Udee.TestUtils.UserTestUtils;
import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.dto.LoginRequestDto;
import edu.utn.udee.Udee.dto.LoginResponseDto;
import edu.utn.udee.Udee.dto.UserDto;
import edu.utn.udee.Udee.service.UserService;
import org.hibernate.boot.model.source.spi.AnyDiscriminatorSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static edu.utn.udee.Udee.TestUtils.UserTestUtils.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Conf.class)
public class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserController userController;

    @Test
    public void testLoginOk() throws JsonProcessingException {
        String Json = "{\"id\":1,\"username\":\"employee1\",\"rol\":\"BACKOFFICE\"}";
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().username("employee1").password("1234").build();
        PowerMockito.mockStatic(Conf.class);
        when(userService.login(anyString(), anyString())).thenReturn(getUser());
        when(objectMapper.writeValueAsString(any())).thenReturn(Json);
        ResponseEntity<LoginResponseDto> responseEntity = userController.login(loginRequestDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testLoginUnauthorized(){
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().username("employee1").password("1234").build();
        PowerMockito.mockStatic(Conf.class);
        when(userService.login(anyString(), anyString())).thenReturn(null);
        ResponseEntity<LoginResponseDto> responseEntity = userController.login(loginRequestDto);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    /*@Test
    public void testGenerateTokenOk(){
        String Json = "{\"id\":1,\"username\":\"employee1\",\"rol\":\"ROLE_BACKOFFICE\"}";
        String token = userController.ge
    }*/
}
