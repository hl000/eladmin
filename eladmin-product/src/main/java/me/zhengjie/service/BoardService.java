package me.zhengjie.service;

import me.zhengjie.domain.PlanBoardDto;
import me.zhengjie.domain.UnfinishedReasonDto;

import java.util.List;

/**
 * @author HL
 * @create 2021/6/15 8:23
 */
public interface BoardService {
    List<PlanBoardDto> getPlanBoard();

    List<UnfinishedReasonDto> getUnfinishedReasons();
}
