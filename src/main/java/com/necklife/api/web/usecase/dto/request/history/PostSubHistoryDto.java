package com.necklife.api.web.usecase.dto.request.history;

import com.necklife.api.entity.history.PoseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PostSubHistoryDto {

    private Date changedAt;
    private PoseStatus poseStatus;

}
