package me.zhengjie.service;

import me.zhengjie.service.dto.BoardQueryCriteria;
import me.zhengjie.service.dto.PlanBoardDto;
import me.zhengjie.service.dto.UnfinishedReasonDto;

import java.util.List;

/**
 * @author HL
 * @create 2021/6/15 8:23
 */
public interface BoardService {
    List<PlanBoardDto> getPlanBoard(BoardQueryCriteria boardQueryCriteria);

    List<UnfinishedReasonDto> getUnfinishedReasons(BoardQueryCriteria boardQueryCriteria);
}
