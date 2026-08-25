package com.example.courseenrollmentsystem.DTO.CourseOffreingDto;
import com.example.courseenrollmentsystem.Enum.CourseOfferingEnum.CourseOfferingStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCourseOfferingRequestDto {

    @NotNull(message = "Teacher id is required")
    private Long teacherId;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be greater than zero")
    private Integer capacity;

    @NotNull(message = "Status is required")
    private CourseOfferingStatus status;
}
