package com.pighand.aio.vo.MKT;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ActivityStatsVO {
    private LocalDate date;
    private Long totalParticipants;
    private List<LocationStatVO> locations;
}