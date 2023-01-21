package ru.netology.patient.service.medical;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.*;
import ru.netology.patient.entity.*;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.*;

import java.math.BigDecimal;

public class MedicalServiceImplTest {

    @BeforeAll
    static void init(){
        System.out.println("*start MedicalServiceImp tests*");
    }

    @AfterAll
    static void endClass(){
        System.out.println("*finished MedicalServiceImp tests*");
    }
    @BeforeEach
    void start() {
        System.out.println("---------------------------------");
    }

    @AfterEach
    void end() {
        System.out.println("*********************************");
    }

    @Mock
    PatientInfoRepository patientInfoRepository;
    HealthInfo healthInfo;
    PatientInfo patientInfo;
    SendAlertService alertService;

    private void mockitTest() {
        patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        healthInfo = Mockito.mock(HealthInfo.class);
        patientInfo = Mockito.mock(PatientInfo.class);
        alertService = Mockito.mock(SendAlertServiceImpl.class);
        Mockito.when(patientInfoRepository.getById(Mockito.any()))
                .thenReturn(patientInfo);
        Mockito.when(patientInfo.getHealthInfo())
                .thenReturn(healthInfo);
    }

    @Test
    void checkBloodPressureTest() {
        System.out.println("checkBloodPressure test started...");
        //arrange:
        BloodPressure bloodPressurePatient = new BloodPressure(120, 80);

        mockitTest();

        Mockito.when(healthInfo.getBloodPressure())
                .thenReturn(bloodPressurePatient);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        //act:
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        medicalService.checkBloodPressure("test",
                new BloodPressure(bloodPressurePatient.getLow(), bloodPressurePatient.getHigh()));

        //assert:
        Mockito.verify(alertService).send(argumentCaptor.capture());
        Assertions.assertNotNull(argumentCaptor.getValue());
        System.out.println();
        System.out.println("checkBloodPressure test finished...");
    }

    @Test
    void checkTemperatureTest() {
        System.out.println("checkTemperature test started...");
        //arrange:
        int temperature = 1;
        BigDecimal normalTemperature = new BigDecimal(temperature);
        BigDecimal patientTemperature = new BigDecimal(temperature + 1.6);

        mockitTest();

        Mockito.when(healthInfo.getNormalTemperature())
                .thenReturn(patientTemperature);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        //act:
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        medicalService.checkTemperature("test", normalTemperature);

        //assert:
        Mockito.verify(alertService).send(argumentCaptor.capture());
        Assertions.assertNotNull(argumentCaptor.getValue());
        System.out.println();
        System.out.println("checkTemperature test finished...");
    }

    @Test
    void checkNormalTest() {
        System.out.println("checkNormal test started...");
        //arrange:
        BloodPressure bloodPressureTest = new BloodPressure(120, 60);

        mockitTest();

        Mockito.when(healthInfo.getNormalTemperature())
                .thenReturn(new BigDecimal(37.0));
        Mockito.when(healthInfo.getBloodPressure())
                .thenReturn(new BloodPressure(120, 60));

        //act:
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        medicalService.checkTemperature("test", new BigDecimal(36.6));
        medicalService.checkBloodPressure("test", bloodPressureTest);

        //assert:
        Mockito.verify(alertService, Mockito.never()).send(Mockito.any());

        System.out.println("checkNormal test finished...");


    }

}
