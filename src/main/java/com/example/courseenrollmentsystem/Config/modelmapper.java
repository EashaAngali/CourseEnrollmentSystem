package com.example.courseenrollmentsystem.Config;

import com.example.courseenrollmentsystem.DTO.StudentDto.StudentCreateRequestDto;
import com.example.courseenrollmentsystem.Entity.Student;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class modelmapper {
    @Bean
    public ModelMapper ModelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(
                StudentCreateRequestDto.class,
                Student.class
        ).addMappings(mapper -> {
            mapper.skip(Student::setStudentId);
        });
        return new ModelMapper();
    }

}
