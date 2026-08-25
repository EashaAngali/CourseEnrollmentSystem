package com.example.courseenrollmentsystem.Service.services;
import com.example.courseenrollmentsystem.DTO.AcademicSemeterDTO.AcademicSemsterResponseDto;
import com.example.courseenrollmentsystem.DTO.AcademicSemeterDTO.AcadmeicSemsterRequestDto;
import com.example.courseenrollmentsystem.Entity.AcademicSemster;
import com.example.courseenrollmentsystem.Enum.AcadmeicSemsterEnum.AcademicSemsterStatus;
import com.example.courseenrollmentsystem.ExceptionHandling.DublicateNotAllowedException;
import com.example.courseenrollmentsystem.ExceptionHandling.InvalidDateException;
import com.example.courseenrollmentsystem.ExceptionHandling.ResourceNotFoundException;
import com.example.courseenrollmentsystem.Repository.AcademicSemsterRepository;
import com.example.courseenrollmentsystem.Service.Interface.AcademicSemsterInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AcademicSemsterService implements AcademicSemsterInterface {
    private final ModelMapper modelMapper;
    private final AcademicSemsterRepository academicSemsterRepository;
    @Override
    public AcademicSemsterResponseDto createAcademicSemster(AcadmeicSemsterRequestDto academicSemsterRequestDto) {
        AcademicSemster academicSemster= modelMapper.map(academicSemsterRequestDto, AcademicSemster.class);
       Optional<AcademicSemster> exist =academicSemsterRepository.findBysemesterName(academicSemsterRequestDto.getSemesterName());
        if (exist.isPresent()){
            throw new DublicateNotAllowedException("Semster already exists");
        }
            SemsterDateValidationsConditions(academicSemsterRequestDto);
            AcademicSemster academicSemster1 = academicSemsterRepository.save(academicSemster);
        return modelMapper.map(academicSemster1, AcademicSemsterResponseDto.class);
    }

    @Override
    public List<AcademicSemsterResponseDto> fetchAllAcademicSemsterdata() {
        List<AcademicSemster> academicSemster=academicSemsterRepository.findAll();
        return academicSemster.stream().map(
                academicSemster1 -> modelMapper.map(
                        academicSemster1,AcademicSemsterResponseDto.class)).toList();
    }

    @Override
    public AcademicSemsterResponseDto fetchAllAcademicSemsterdataById(Long id) {
        AcademicSemster academicSemster=academicSemsterRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Semster not found")
        );
        return modelMapper.map(academicSemster, AcademicSemsterResponseDto.class);
    }

    @Override
    public List<AcademicSemsterResponseDto> getAcademicSemsterByCurrent() {
       List<AcademicSemster> academicSemster = academicSemsterRepository.
               findBystartDateLessThanEqualAndEndDateGreaterThanEqual(
                       LocalDate.now(),LocalDate.now()).stream().toList();
                 return academicSemster.stream().map(semster -> modelMapper.map(
                         semster, AcademicSemsterResponseDto.class)).toList();
    }

    @Override
    public List<AcademicSemsterResponseDto> getAcademicSemsterByStatus(AcademicSemsterStatus status) {
       List<AcademicSemster> semster=  academicSemsterRepository.findAll().stream().filter(
                 academicSemster -> academicSemster.getAcademicSemsterStatus().equals(status)).toList();
return semster.stream().map(semster1-> modelMapper.map(semster1,AcademicSemsterResponseDto.class)).toList();
    }

    @Override
    public List<AcademicSemsterResponseDto> fetchEnrollmentOpen() {
        List<AcademicSemster> academicSemster = academicSemsterRepository.
                findByenrollmentStartDateLessThanEqualAndEnrollmentEndDateGreaterThanEqual(
                        LocalDate.now(),LocalDate.now()).stream().toList();
        return academicSemster.stream().map(semster -> modelMapper.map(
                semster, AcademicSemsterResponseDto.class)).toList();
    }

    @Override
    public AcademicSemsterResponseDto updateAcademicSesmter(Long id, Map<String, Object> map) {
        AcademicSemster academicSemster = academicSemsterRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Academic Semster not found")
        );
        map.forEach((field,value)->{
            switch (field) {
                case "semesterName":
                    academicSemster.setSemesterName((String) value);
                    break;
                case "startDate":
                    academicSemster.setStartDate(LocalDate.parse(value.toString()));
                    break;
                case "endDate":
                    academicSemster.setEndDate(LocalDate.parse(value.toString()));
                    break;
                case "enrollmentStartDate":
                    academicSemster.setEnrollmentStartDate(LocalDate.parse(value.toString()));
                    break;
                case "enrollmentEndDate":
                    academicSemster.setEnrollmentEndDate(LocalDate.parse(value.toString()));
                    break;
                case "academicSemsterStatus":
                    academicSemster.setAcademicSemsterStatus(
                            AcademicSemsterStatus.valueOf(value.toString())
                    );
                    break;
                case "dropDeadline":
                    academicSemster.setDropDeadline(LocalDate.parse(value.toString()));
                    break;
            }
        });
        AcademicSemster academicSemster1 = academicSemsterRepository.save(academicSemster);
        return modelMapper.map(academicSemster1, AcademicSemsterResponseDto.class);
    }


    public void SemsterDateValidationsConditions(
            AcadmeicSemsterRequestDto academicSemsterRequestDto
    ) {
        boolean startDate = academicSemsterRequestDto.getStartDate().isBefore(LocalDate.now());
        if (startDate) {
            throw new InvalidDateException("Start date must be after end date");
        }
        if (academicSemsterRequestDto.getEndDate().isBefore(academicSemsterRequestDto.getStartDate())) {
            throw new InvalidDateException("End date must be After start date");
        }
        if (academicSemsterRequestDto.getEndDate().equals(academicSemsterRequestDto.getStartDate())) {
            throw new InvalidDateException("End date equal to start date 'Not Allowed'");
        }
        if (academicSemsterRequestDto.getEnrollmentStartDate().isBefore(academicSemsterRequestDto.getStartDate())) {
            throw new InvalidDateException("Enrollment start date must be After start date");
        }
        if (academicSemsterRequestDto.getEnrollmentEndDate().isAfter(academicSemsterRequestDto.getEndDate())
                || academicSemsterRequestDto.getEnrollmentEndDate().isBefore(academicSemsterRequestDto.getEnrollmentStartDate())) {
            throw new InvalidDateException("Enrollment end date must be After start date/Enrollment start date");
        }
        if (academicSemsterRequestDto.getDropDeadline().isAfter(academicSemsterRequestDto.getEndDate())) {
            throw new InvalidDateException("Drop deadline must be before end date");
        }

    }
}
