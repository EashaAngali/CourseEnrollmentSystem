package com.example.courseenrollmentsystem.Service.services;
import com.example.courseenrollmentsystem.DTO.StudentDto.StudentCreateRequestDto;
import com.example.courseenrollmentsystem.DTO.StudentDto.StudentResponseDto;
import com.example.courseenrollmentsystem.DTO.StudentDto.StudentUpdateRequestDto;
import com.example.courseenrollmentsystem.Entity.Program;
import com.example.courseenrollmentsystem.Entity.Student;
import com.example.courseenrollmentsystem.Enum.StudentEnum.StudentGender;
import com.example.courseenrollmentsystem.Enum.StudentEnum.StudentSpringOrFallStatus;
import com.example.courseenrollmentsystem.Enum.StudentEnum.StudentStatus;
import com.example.courseenrollmentsystem.ExceptionHandling.DublicateNotAllowedException;
import com.example.courseenrollmentsystem.ExceptionHandling.InvalidResourceException;
import com.example.courseenrollmentsystem.ExceptionHandling.ResourceNotFoundException;
import com.example.courseenrollmentsystem.Repository.ProgramRepository;
import com.example.courseenrollmentsystem.Repository.StudentRepository;
import com.example.courseenrollmentsystem.Service.Interface.Studentinterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService implements Studentinterface {
     private final StudentRepository studentRepository;
     private  final ModelMapper modelMapper;
     private final ProgramRepository programRepository;
    @Override
    public StudentResponseDto addstudent(StudentCreateRequestDto studentCreateRequestDto) {
        Student student = modelMapper.map(studentCreateRequestDto, Student.class);
        Optional<Student> existingStudent = studentRepository.findByEmail(studentCreateRequestDto.getEmail());
        Optional<Student> existingCNIC = studentRepository.findByCnic(studentCreateRequestDto.getCnic());
        Program program = programRepository.findById(studentCreateRequestDto.getProgramId()).orElse(null);
        if(existingStudent.isPresent()){
            throw new DublicateNotAllowedException("Email already exists");
        } else if (existingCNIC.isPresent()) {
            throw new DublicateNotAllowedException("CNIC already exists");
        } else {

            student.setStudentId(null);
            student.setProgram(program);
            String Code= generateStudentNumber(studentCreateRequestDto.getAdmissionDate(), studentCreateRequestDto.getProgramId());
            student.setStudentNumber(Code);
            StudentSpringOrFallStatus status = SeasonStatus(studentCreateRequestDto.getAdmissionDate());
            student.setSpringOrFallStatus(status);
            Student newStudent = studentRepository.save(student);
            StudentResponseDto responseDto = modelMapper.map(newStudent, StudentResponseDto.class);
            responseDto.setProgramName(newStudent.getProgram().getProgramName());
            responseDto.setProgramCode(newStudent.getProgram().getProgramCode());
            responseDto.setStudentNumber(newStudent.getStudentNumber());
            return responseDto;
        }
    }

    @Override
    public List<StudentResponseDto> getAllStudent() {
        List<Student> students = studentRepository.findAll();
        return students.stream().map(student -> modelMapper.map(
                student,StudentResponseDto.class)).toList();
    }

    @Override
    public StudentResponseDto getStudentbyId(Long id) {
        Student student= Exception(id);
        return modelMapper.map(student,StudentResponseDto.class);
    }

    @Override
    public String deleteStudent(Long id) {
        Exception(id);
        studentRepository.deleteById(id);
        return "Student deleted";
    }

    @Override
    public StudentResponseDto updateStudent(Long id, StudentUpdateRequestDto studentupdateRequestDto) {
       Student student= Exception(id);
       modelMapper.map(studentupdateRequestDto, student);
        student=studentRepository.save(student);
        return modelMapper.map(student, StudentResponseDto.class);
    }

    @Override
    public StudentResponseDto updateStudentByCatagory(Long id, Map<String, Object> map) {
        Student student = Exception(id);

        map.forEach((field, value) -> {
            switch (field) {
                case "email":
                    student.setEmail(value.toString());
                    break;
                case "studentCNIC":
                    student.setCnic(value.toString());
                    break;
                case "firstName":
                    student.setFirstName(value.toString());
                    break;
                case "lastName":
                    student.setLastName(value.toString());
                    break;
                case "fatherName":
                    student.setFatherName(value.toString());
                    break;
                case "address":
                    student.setAddress(value.toString());
                    break;
                case "phone":
                    student.setPhone(value.toString());
                    break;
                case "dateOfBirth":
                    student.setDateOfBirth(LocalDate.parse(value.toString()));
                    break;
                case "gender":
                    student.setGender(StudentGender.valueOf(value.toString()));
                    break;
               case "status":
                   student.setStatus(StudentStatus.valueOf(value.toString()));
                    break;
            }
        });
        Student savedstudent= studentRepository.save(student);
        return modelMapper.map(savedstudent, StudentResponseDto.class);
    }


    public Student Exception(Long id){
        return studentRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Student not found")
        );
    }
      private StudentSpringOrFallStatus SeasonStatus( LocalDate date) {
        int month = date.getMonthValue();
          StudentSpringOrFallStatus status = null;
        if(month == 3){
            status = StudentSpringOrFallStatus.SPRING;
        }else if(month == 8){
            status  = StudentSpringOrFallStatus.FALL;
        }
        else {
            throw new InvalidResourceException("Invalid month");
        }
        return status;
      }
    private String generateStudentNumber(LocalDate joiningDate, Long id){
        int year = joiningDate.getYear();
       Program program = programRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Program Not exist")
        );
       String year1= Integer.toString(year).substring(2,4);
       String Season = SeasonStatus(joiningDate).toString().substring(0,1);
        String Prefix = program.getProgramCode().substring(1,4)+"-"+year1+Season+"-";
        Optional<Student> student = studentRepository.findTopByStudentNumberStartingWithOrderByStudentNumberDesc(Prefix);
        int nextNumber=1;
        if(student.isPresent()){
            String lastCode = student.get().getStudentNumber();
            String lastSerial = lastCode.substring(lastCode.lastIndexOf("-")+1);
            int lastNumber = Integer.parseInt(lastSerial);
            nextNumber = lastNumber + 1;
        }
        String FormatedCode = String.format("%03d", nextNumber);
        return Prefix+FormatedCode;
    }
}
