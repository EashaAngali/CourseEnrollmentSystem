package com.example.courseenrollmentsystem.DTO.SectionDto;

import com.example.courseenrollmentsystem.Enum.SectionEnum.SectionStatus;
import lombok.*;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class SectionResponseDto {

        private Long sectionId;

        private String sectionName;

        private String sectionCode;

        private Long programId;

        private String programName;

        private Integer semesterNumber;

        private Integer capacity;

        private SectionStatus status;

}
