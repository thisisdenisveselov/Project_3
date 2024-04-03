package ru.veselov.springcourse.Project3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.veselov.springcourse.Project3.models.Measurement;
import ru.veselov.springcourse.Project3.models.Sensor;

import java.util.Optional;

@Repository
public interface MeasurementsRepository extends JpaRepository<Measurement, Integer> {
    int countMeasurementByRainingIsTrue();
}
