package com.example.courseenrollmentsystem.Service.services;
import com.example.courseenrollmentsystem.DTO.ProgramCourseDto.PCResponseDto;
import com.example.courseenrollmentsystem.DTO.ProgramCourseDto.PCcreateRequestDto;
import com.example.courseenrollmentsystem.Entity.Course;
import com.example.courseenrollmentsystem.Entity.Program;
import com.example.courseenrollmentsystem.Entity.ProgramCourse;
import com.example.courseenrollmentsystem.Enum.ProgramCourseEnum.CourseType;
import com.example.courseenrollmentsystem.Enum.ProgramCourseEnum.ProgramCourseStatus;
import com.example.courseenrollmentsystem.ExceptionHandling.DublicateNotAllowedException;
import com.example.courseenrollmentsystem.ExceptionHandling.ResourceNotFoundException;
import com.example.courseenrollmentsystem.ExceptionHandling.ValidStatusRequiredException;
import com.example.courseenrollmentsystem.Repository.CourseRepository;
import com.example.courseenrollmentsystem.Repository.ProgramRepository;
import com.example.courseenrollmentsystem.Repository.programCourseRepository;
import com.example.courseenrollmentsystem.Service.Interface.ProgramCourseInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class ProgramCourseService implements ProgramCourseInterface {
    private final ModelMapper modelMapper;
    private final programCourseRepository programCourseRepository;
    private final CourseRepository courseRepository;
    private final ProgramRepository programRepository;
    @Override
    public PCResponseDto addprogramcourse(PCcreateRequestDto pccreateRequestDto) {
        ProgramCourse programCourse = modelMapper.map(pccreateRequestDto, ProgramCourse.class);
        Course course = courseRepository.findById(pccreateRequestDto.getId()).orElseThrow(
                ()->new ResourceNotFoundException("ProgramCourse not found with id: " + pccreateRequestDto.getId()));
        Program program= programRepository.findById(pccreateRequestDto.getProgramId()).orElseThrow(
                ()->new ResourceNotFoundException("Program not found with id: " + pccreateRequestDto.getProgramId())
        );
        boolean exists = programCourseRepository
                .existsByPrograms_programIdAndCourses_Id(program.getProgramId(), course.getId());
        if(exists){
              throw new DublicateNotAllowedException("ProgramCourse already exists");
        }
        if((pccreateRequestDto.getRecommendedSemester() <= 0 || pccreateRequestDto.getRecommendedSemester() >= 8)){
            throw new IllegalArgumentException("ProgramCourse recommendedSemester must be between 0 and 8");
        }
            programCourse.setCourses(course);
            programCourse.setPrograms(program);
            ProgramCourse newprogramCourse=programCourseRepository.save(programCourse);
            PCResponseDto responseDto = modelMapper.map(newprogramCourse, PCResponseDto.class);
            responseDto.setCourseName(newprogramCourse.getCourses().getCourseName());
            responseDto.setCourseCode(newprogramCourse.getCourses().getCourseCode());
            responseDto.setProgramName(newprogramCourse.getPrograms().getProgramName());
            responseDto.setId(newprogramCourse.getCourses().getId());
            responseDto.setProgramId(newprogramCourse.getPrograms().getProgramId());
            return responseDto;
    }

    @Override
    public List<PCResponseDto> getAllProgramCourse() {
        List<ProgramCourse> programCourse = programCourseRepository.findAll();
        List<PCResponseDto> responseDto = new ArrayList<>();
       for (ProgramCourse programCourse1 : programCourse) {
           PCResponseDto dto = modelMapper.map(programCourse1, PCResponseDto.class);
           dto.setCourseName(programCourse1.getCourses().getCourseName());
           dto.setCourseCode(programCourse1.getCourses().getCourseCode());
           dto.setProgramName(programCourse1.getPrograms().getProgramName());
           responseDto.add(dto);
       }
        return responseDto;
    }

    @Override
    public PCResponseDto getProgramCourseById(Long id) {
        ProgramCourse programCourse = programCourseRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("ProgramCourse not found with id: " + id)
        );
        PCResponseDto responseDto = modelMapper.map(programCourse, PCResponseDto.class);
        responseDto.setCourseName(programCourse.getCourses().getCourseName());
        responseDto.setCourseCode(programCourse.getCourses().getCourseCode());
        responseDto.setProgramName(programCourse.getPrograms().getProgramName());
        return responseDto;
    }

    @Override
    public List<PCResponseDto> getCourseByProgramId(Long programId) {
        List<ProgramCourse> programCourse = programCourseRepository.findAll();
        List<PCResponseDto> responseDto = new ArrayList<>();

            for (ProgramCourse programCourse1 : programCourse){
              if(programCourse1.getPrograms().getProgramId().equals(programId)){
                  FetchCompleteData(responseDto, programCourse1);
              }
            }

        return responseDto;
    }

    @Override
    public List<PCResponseDto> getCourseByProgramIdAndSemster(Long programId, Integer semesterNumber) {
        List<ProgramCourse> programCourse = programCourseRepository.findAll();
        List<PCResponseDto> responseDto = new ArrayList<>();
        for (ProgramCourse programCourse1 : programCourse){
            if((programCourse1.getPrograms().getProgramId().equals(programId))
                    &&(programCourse1.getRecommendedSemester().equals(semesterNumber))){
                FetchCompleteData(responseDto, programCourse1);
            }
        }

        return responseDto;
    }
    @Override
    public List<PCResponseDto> getCourseByCourseType(Long programId,CourseType courseType) {
        List<ProgramCourse> programCourse = programCourseRepository.findAll();
        List<PCResponseDto> responseDto = new ArrayList<>();
              programCourse.stream().filter(pc-> pc.getPrograms().getProgramId().equals(programId)).filter(
                      pc->pc.getCourseType().equals(courseType)
              ).forEach(pc->{
                  FetchCompleteData(responseDto, pc);
              });
        return responseDto;
    }
    @Override
    public PCResponseDto updateprogramcourse(Long id, Map<String, Object> updateRequestDto) {
        ProgramCourse programCourse = programCourseRepository.findById(id).orElseThrow(
                ()-> new  ResourceNotFoundException("ProgramCourse not found with id: " + id)
        );
        updateRequestDto.forEach((field,value)->{
            switch (field) {
                case "recommendedSemester":
                    if(programCourse.getRecommendedSemester()<=0||programCourse.getRecommendedSemester()>=8){
                        programCourse.setRecommendedSemester(Integer.parseInt(value.toString()));
                    }else{
                        throw new IllegalArgumentException("ProgramCourse recommendedSemester must be between 0 and 8");
                    }
                    break;
                case "courseType":
                     if((programCourse.getCourseType()==null) ||
                             (programCourse.getCourseType()==CourseType.CORE||
                                     programCourse.getCourseType()==CourseType.ELECTIVE)){
                         programCourse.setCourseType(CourseType.valueOf(value.toString()));
                     }  else{
                throw new ValidStatusRequiredException("ProgramCourse CourseType must be CORE or ELECTIVE");
            }
                    break;
                case "creditHours":
                    programCourse.setCreditHours(Integer.parseInt(value.toString()));
                    break;
                case "status":
                    if((programCourse.getStatus()==null) ||
                            (programCourse.getStatus()==ProgramCourseStatus.ACTIVE||
                                    programCourse.getStatus()==ProgramCourseStatus.INACTIVE)){
                        programCourse.setStatus(ProgramCourseStatus.valueOf(value.toString()));
                    }else{
                        throw new ValidStatusRequiredException("ProgramCourse Status must be ACTIVE or INACTIVE");
                    }

                    break;
               default:
                    throw new ValidStatusRequiredException("Unknown field: " + field);
            }
        });
        ProgramCourse programCourse1 = programCourseRepository.save(programCourse);
        PCResponseDto pcResponseDto=modelMapper.map(programCourse1, PCResponseDto.class);
        pcResponseDto.setCourseName(programCourse.getCourses().getCourseName());
        pcResponseDto.setCourseCode(programCourse.getCourses().getCourseCode());
        pcResponseDto.setProgramName(programCourse.getPrograms().getProgramName());
        return pcResponseDto;
    }

    private void FetchCompleteData(List<PCResponseDto> responseDto, ProgramCourse programCourse1) {
        PCResponseDto dto = modelMapper.map(programCourse1, PCResponseDto.class);
        ProgramCourse programCourse2 = modelMapper.map(programCourse1, ProgramCourse.class);
        dto.setCourseName(programCourse2.getCourses().getCourseName());
        dto.setCourseCode(programCourse2.getCourses().getCourseCode());
        dto.setProgramName(programCourse2.getPrograms().getProgramName());
        responseDto.add(dto);
    }
}
