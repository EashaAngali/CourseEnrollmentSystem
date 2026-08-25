package com.example.courseenrollmentsystem.Service.services;
import com.example.courseenrollmentsystem.DTO.TeacherDto.TeacherRequestDto;
import com.example.courseenrollmentsystem.DTO.TeacherDto.TeacherResponseDto;
import com.example.courseenrollmentsystem.Entity.Teacher;
import com.example.courseenrollmentsystem.Enum.TeacherEnum.TeacherDesignation;
import com.example.courseenrollmentsystem.Enum.TeacherEnum.TeacherStatus;
import com.example.courseenrollmentsystem.ExceptionHandling.DublicateNotAllowedException;
import com.example.courseenrollmentsystem.ExceptionHandling.InvalidDateException;
import com.example.courseenrollmentsystem.ExceptionHandling.ResourceNotFoundException;
import com.example.courseenrollmentsystem.Repository.TeacherRepository;
import com.example.courseenrollmentsystem.Service.Interface.TeacherInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService implements TeacherInterface {
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;
    @Override
    public TeacherResponseDto saveTeacher(TeacherRequestDto teacherRequestDto) {
        Optional<Teacher> teacherExist=teacherRepository.findByTeacherEmail(teacherRequestDto.getTeacherEmail());
        if(teacherExist.isPresent()) {
            throw new DublicateNotAllowedException("Teacher email already exists");
        }

        Teacher teacher = modelMapper.map(teacherRequestDto, Teacher.class);
        if(teacherRequestDto.getJoiningDate().isAfter(LocalDate.now())) {
            throw new InvalidDateException("joining Date cannot be in future");
        }
        String employeeCode = generateEmployeeCode(teacherRequestDto.getJoiningDate());
        teacher.setEmployeeCode(employeeCode);
        Teacher savedTeacher = teacherRepository.save(teacher);
        return modelMapper.map(savedTeacher, TeacherResponseDto.class);
    }

    @Override
    public List<TeacherResponseDto> fetchAllTeachersData() {
        List<Teacher> teacher = teacherRepository.findAll();
        return teacher.stream().map(
                teacher1 -> modelMapper.map(
                        teacher1,TeacherResponseDto.class)).toList();
    }

    @Override
    public TeacherResponseDto fetchTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Teacher with "+id+" not found")
        );
        return modelMapper.map(teacher, TeacherResponseDto.class);
    }

    @Override
    public TeacherResponseDto upadteTeacher(Long id, Map<String, Object> map) {
        Teacher teacher =  teacherRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Teacher with "+id+" not found")
        );
        map.forEach((field,value)->{
            switch (field) {
                case "TeacherFirstName":
                    teacher.setTeacherFirstName(value.toString());
                    break;
                case "TeacherLastName":
                    teacher.setTeacherLastName(value.toString());
                    break;
                case "teacherEmail":
                    Optional<Teacher> teacherExist=teacherRepository.findByTeacherEmail(teacher.getTeacherEmail());
                    if(teacherExist.isPresent()) {
                        throw new DublicateNotAllowedException("Teacher email already exists");
                    }
                    teacher.setTeacherEmail(value.toString());
                    break;
                case "TeacherPhone":
                    teacher.setTeacherPhone(value.toString());
                    break;
                case "joiningDate":
                    teacher.setJoiningDate(LocalDate.parse(value.toString()));
                    break;
                case "teacherDesignation":
                    teacher.setTeacherDesignation(TeacherDesignation.valueOf(value.toString()));
                    break;
                case "teacherStatus":
                    teacher.setTeacherStatus(TeacherStatus.valueOf(value.toString()));
                    break;
            }
        });
        Teacher savedTeacher = teacherRepository.save(teacher);
        return modelMapper.map(savedTeacher, TeacherResponseDto.class);
    }

    @Override
    public List<TeacherResponseDto> fetchTeacherBydepartmentId(Long id) {
            List<Teacher> teacher = teacherRepository.findByDepartment_DepartmentId(id);
            return teacher.stream().map(
                    teacher1 -> modelMapper.map(
                    teacher1, TeacherResponseDto.class)).toList();
    }

    @Override
    public List<TeacherResponseDto> fetchTeacherByStatus(TeacherStatus status) {
        return teacherRepository.findAll().stream().filter(
                teacher1 -> teacher1.getTeacherStatus().equals(status)
        ).map(teacher ->  modelMapper.map(teacher, TeacherResponseDto.class)).toList();

    }

    @Override
    public List<TeacherResponseDto> fetchTeacherByDepAndStatus(Long id, TeacherStatus status) {
        return teacherRepository.findByDepartment_DepartmentId(id).stream().filter(
                teacher1 -> teacher1.getTeacherStatus().equals(status)
        ).map(teacher1 -> modelMapper.map(teacher1, TeacherResponseDto.class)).toList();
    }

    @Override
    public List<TeacherResponseDto> fetchTeacherByName(String name) {
        return teacherRepository.findAll().stream().filter(teacher ->
                teacher.getTeacherFirstName().equals(name)).map(teacher -> modelMapper
                .map(teacher,TeacherResponseDto.class)).toList();
    }

    @Override
    public List<TeacherResponseDto> fetchTeacherByEmail(String email) {
        return teacherRepository.findAll().stream().filter(teacher ->
                teacher.getTeacherFirstName().equals(email)).map(teacher -> modelMapper
                .map(teacher,TeacherResponseDto.class)).toList();
    }

    @Override
    public TeacherResponseDto getTeacherByEmployeeCode(String id) {
        Teacher teacher = teacherRepository.findByEmployeeCode(id).orElseThrow(
                () -> new ResourceNotFoundException("Teacher with "+id+" not found")
        );
        return modelMapper.map(teacher, TeacherResponseDto.class);
    }

    @Override
    public List<TeacherResponseDto> page(PageRequest pageRequest) {
        List<Teacher> teacher = teacherRepository.findAll(pageRequest).getContent();
        return teacher.stream().map(teacher1 -> modelMapper.map(
                teacher1, TeacherResponseDto.class)).toList();
    }


    private String generateEmployeeCode(LocalDate joiningDate){
        int year = joiningDate.getYear();
        String year1= Integer.toString(year).substring(2,4);
        String prefix= "TCH-"+year1+"-";

        Optional<Teacher> teacher=teacherRepository.findTopByEmployeeCodeStartingWithOrderByEmployeeCodeDesc(prefix);
        int nextNumber = 1;
        if(teacher.isPresent()){
            String lastCode = teacher.get().getEmployeeCode();
            String lastSerial = lastCode.substring(lastCode.lastIndexOf("-")+1);
            int lastNumber = Integer.parseInt(lastSerial);
            nextNumber = lastNumber + 1;
        }
        String formattedNumber = String.format("%03d", nextNumber);
        return prefix+formattedNumber;
    }


}
