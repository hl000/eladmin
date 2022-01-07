package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2021/12/29 16:39
 */
@Data
public class WorkCompletQtySubmitQueryCriteria {
    @Query
    private String fSubDate;
//    @Query
//    private String fArcName;

    @Query(type = Query.Type.INNER_LIKE)
    private String fWorkOrder;

    @Query(type = Query.Type.INNER_LIKE)
    private String fInvCode;

    @Query(type = Query.Type.INNER_LIKE)
    private String fInvName;

    @Query(type = Query.Type.INNER_LIKE)
    private String fInvStd;

    @Query(propName = "id", joinName = "workWorkingProcedure")
    private Integer fArcID;

    @Query(propName = "id", joinName = "workSubmitTimeList")
    private Integer fCompleteTimeId;

    @Query
    private Integer fIsDeleted = 0;
}
