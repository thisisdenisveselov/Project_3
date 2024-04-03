package ru.veselov.springcourse.Project3.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.veselov.springcourse.Project3.dto.MeasurementDTO;
import ru.veselov.springcourse.Project3.dto.MeasurementsResponse;
import ru.veselov.springcourse.Project3.models.Measurement;
import ru.veselov.springcourse.Project3.services.MeasurementsService;
import ru.veselov.springcourse.Project3.services.SensorsService;
import ru.veselov.springcourse.Project3.util.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {
    private final MeasurementsService measurementsService;
    private final ModelMapper modelMapper;
    private final SensorsService sensorsService;

    private final MeasurementValidator measurementValidator;

    @Autowired
    public MeasurementsController(MeasurementsService measurementsService, ModelMapper modelMapper,
                                  SensorsService sensorsService, MeasurementValidator measurementValidator) {
        this.measurementsService = measurementsService;
        this.modelMapper = modelMapper;
        this.sensorsService = sensorsService;
        this.measurementValidator = measurementValidator;
    }

   /* @GetMapping
    public List<MeasurementDTO> findAll() {
        List<Measurement> measurements = measurementsService.findAll();
        List<MeasurementDTO> measurementDTOs = measurements.stream()
                .map(this::convertToMeasurementDTO)
                .collect(Collectors.toList());
        return measurementDTOs;
    }*/

    @GetMapping()
    public MeasurementsResponse getMeasurements() {
        // Обычно список из элементов оборачивается в один объект для пересылки
        return new MeasurementsResponse(measurementsService.findAll().stream().map(this::convertToMeasurementDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/rainyDaysCount")
    public ResponseEntity<RainyDaysCountResponse> rainyDaysCount() {
        //return measurementsService.findRainyDays();
        RainyDaysCountResponse response = new RainyDaysCountResponse(measurementsService.findRainyDays());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult) {

        Measurement measurement = convertToMeasurement(measurementDTO);

        measurementValidator.validate(measurement, bindingResult);
        if (bindingResult.hasErrors())
            ErrorsUtil.returnErrorsToClient(bindingResult);

        measurementsService.save(measurement);

        return ResponseEntity.ok(HttpStatus.OK);
    }

/*    @ExceptionHandler  //catch exception and return json with exception
    private ResponseEntity<SensorErrorResponse> handlerException(SensorNotFoundException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                "Measurement wasn't added because there is bo sensor with such name in database",
                System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }*/

    @ExceptionHandler  //catch exception and return json with exception
    private ResponseEntity<MeasurementErrorResponse> handlerException(MeasurementException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(),
                System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }
}
