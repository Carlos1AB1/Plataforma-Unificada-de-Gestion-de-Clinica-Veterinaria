package com.veterinary.appointmentservice;

import com.veterinary.appointmentservice.dto.CreateAppointmentRequest;
import com.veterinary.appointmentservice.dto.UpdateAppointmentRequest;
import com.veterinary.appointmentservice.entity.Appointment;
import com.veterinary.appointmentservice.repository.AppointmentRepository;
import com.veterinary.appointmentservice.service.AppointmentService;
import com.veterinary.appointmentservice.client.PatientServiceClient;
import com.veterinary.appointmentservice.client.UserServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AppointmentServiceApplicationTests {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private PatientServiceClient patientServiceClient;

    @Mock
    private UserServiceClient userServiceClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(patientServiceClient.getPatientById(any(Long.class), any(String.class)))
                .thenReturn(ResponseEntity.ok(Map.of("patient", Map.of("name", "Test Patient"))));
        when(userServiceClient.getUserById(any(Long.class), any(String.class)))
                .thenReturn(ResponseEntity.ok(Map.of("user", Map.of("firstName", "Test", "lastName", "Veterinarian"))));
    }

    @Test
    void testCreateAppointment() {
        CreateAppointmentRequest request = new CreateAppointmentRequest(1L, 1L, LocalDate.now().plusDays(1), LocalTime.of(10, 0), "Checkup");
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(i -> i.getArguments()[0]);

        var response = appointmentService.createAppointment(request, "admin", "Bearer token");

        assertNotNull(response);
        assertEquals("Appointment created successfully", response.getMessage());
    }

    @Test
    void testGetAppointmentById() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setPatientId(1L);
        appointment.setVeterinarianId(1L);
        appointment.setAppointmentDate(LocalDate.now().plusDays(1));
        appointment.setAppointmentTime(LocalTime.of(10, 0));
        appointment.setReason("Checkup");
        appointment.setStatus(Appointment.Status.SCHEDULED);
        appointment.setDurationMinutes(30);
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(LocalDateTime.now());
        appointment.setCreatedBy("admin");
        appointment.setUpdatedBy("admin");
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        var response = appointmentService.getAppointmentById(1L, "Bearer token");

        assertNotNull(response);
        assertEquals("Appointment found", response.getMessage());
    }

    @Test
    void testUpdateAppointment() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setPatientId(1L);
        appointment.setVeterinarianId(1L);
        appointment.setAppointmentDate(LocalDate.now().plusDays(1));
        appointment.setAppointmentTime(LocalTime.of(10, 0));
        appointment.setReason("Checkup");
        appointment.setStatus(Appointment.Status.SCHEDULED);
        appointment.setDurationMinutes(30);
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(LocalDateTime.now());
        appointment.setCreatedBy("admin");
        appointment.setUpdatedBy("admin");
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(i -> i.getArguments()[0]);

        UpdateAppointmentRequest request = new UpdateAppointmentRequest();
        request.setReason("Updated reason");

        var response = appointmentService.updateAppointment(1L, request, "admin", "Bearer token");

        assertNotNull(response);
        assertEquals("Appointment updated successfully", response.getMessage());
    }

    @Test
    void testCancelAppointment() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(Appointment.Status.SCHEDULED);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(i -> i.getArguments()[0]);

        var response = appointmentService.cancelAppointment(1L, "admin", "Bearer token");

        assertNotNull(response);
        assertEquals("Appointment cancelled successfully", response.getMessage());
    }
}