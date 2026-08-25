package com.example.courseenrollmentsystem.Service.services;

import com.example.courseenrollmentsystem.DTO.CourseDto.CourseCreateRequestDto;
import com.example.courseenrollmentsystem.DTO.CourseDto.CourseUpdateRequestDto;
import com.example.courseenrollmentsystem.Entity.Course;
import com.example.courseenrollmentsystem.Entity.Department;
import com.example.courseenrollmentsystem.ExceptionHandling.ResourceNotFoundException;
import com.example.courseenrollmentsystem.Repository.CourseRepository;
import com.example.courseenrollmentsystem.Repository.DepartmentRepository;
import com.example.courseenrollmentsystem.Service.Interface.CourseInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CourseService implements CourseInterface {
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CourseCreateRequestDto> viewAllCourse() {
        List<Course> course = courseRepository.findAll();
        return course.stream().map(courseDto
                -> modelMapper.map(courseDto, CourseCreateRequestDto.class)).toList();
    }

    @Override
    public CourseCreateRequestDto addCourse(CourseCreateRequestDto createRequestDto) {
        Course course = modelMapper.map(createRequestDto, Course.class);
        Department department = departmentRepository.findById(createRequestDto.getDepartmentID()).orElseThrow(
                ()-> new ResourceNotFoundException("Department not found")
        );
        course.setDepartment(department);
        Course newCourse = courseRepository.save(course);
        CourseCreateRequestDto courseResponseDto= modelMapper.map(newCourse, CourseCreateRequestDto.class);
        courseResponseDto.setDepartmentID(newCourse.getDepartment().getDepartmentId());
        return courseResponseDto;
    }

    @Override
    public CourseCreateRequestDto getCoursebyId(Long id) {
        Course course = findbyid(id);
        return modelMapper.map(course, CourseCreateRequestDto.class);
    }

    @Override
    public String deleteCourse(Long id) {
        Course course = findbyid(id);
        courseRepository.delete(course);
        return "Course have been deleted";
    }

    @Override
    public CourseUpdateRequestDto updateCourse(Long id, CourseUpdateRequestDto courseupdateRequestDto) {
        Course course = findbyid(id);
       modelMapper.map(courseupdateRequestDto, course);
       course = courseRepository.save(course);
        return modelMapper.map(course, CourseUpdateRequestDto.class);
    }

    @Override
    public CourseUpdateRequestDto updateCourseByCatagory(Long id, Map<String, Object> map) {
        Course course = findbyid(id);
        map.forEach((field,value)->{
            switch (field) {
                case "courseName":
                    course.setCourseName(value.toString());
                    break;
                case "courseDescription":
                    course.setCourseDescription(value.toString());
                    break;
                case "courseCategory":
                    course.setCourseCategory(value.toString());
                    break;
                case "courseFee":
                    course.setCourseFee(value.toString());
                    break;
                case "courseStatus":
                    course.setCourseStatus(value.toString());
                    break;
                case "duration":
                    course.setDuration(value.toString());
                    break;
                    default:
                        System.out.println("Enter Valid field this field not exist");
                        break;
            }
        });
        Course updatedCourse = courseRepository.save(course);
        return modelMapper.map(updatedCourse, CourseUpdateRequestDto.class);
    }

     public Course findbyid(Long id){
         return courseRepository.findById(id).orElseThrow(
                 ()-> new ResourceNotFoundException("Course with id: " + id + " not found")
         );
    }
}
