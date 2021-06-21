package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.base.MergeResult;
import me.zhengjie.service.dto.BoardQueryCriteria;
import me.zhengjie.service.dto.PlanBoardDto;
import me.zhengjie.service.dto.UnfinishedReasonDto;
import me.zhengjie.service.BoardService;
import me.zhengjie.utils.PageUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author HL
 * @create 2021/6/15 8:04
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "看板管理")
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/getPlanBoard")
    @Log("planBoard")
    @ApiOperation("getPlanBoard")
    public Object getPlanBoard(BoardQueryCriteria boardQueryCriteria, Pageable pageable) {
        List<PlanBoardDto> planBoardDtoList = boardService.getPlanBoard(boardQueryCriteria);
        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = planBoardDtoList.size();
        mergeResult.totalPages = planBoardDtoList.size() % pageable.getPageSize() == 0 ? planBoardDtoList.size() / pageable.getPageSize() : planBoardDtoList.size() / pageable.getPageSize() + 1;
        mergeResult.currentPage = pageable.getPageNumber();
        mergeResult.size = pageable.getPageSize();
        mergeResult.content = PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), planBoardDtoList);
        return mergeResult;

    }

    @GetMapping("/getUnfinishedReasons")
    @Log("getUnfinishedReasons")
    @ApiOperation("getUnfinishedReasons")
    public Object getUnfinishedReasons(BoardQueryCriteria boardQueryCriteria, Pageable pageable) {
        List<UnfinishedReasonDto> unfinishedReasonDtos = boardService.getUnfinishedReasons(boardQueryCriteria);
        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = unfinishedReasonDtos.size();
        mergeResult.totalPages = unfinishedReasonDtos.size() % pageable.getPageSize() == 0 ? unfinishedReasonDtos.size() / pageable.getPageSize() : unfinishedReasonDtos.size() / pageable.getPageSize() + 1;
        mergeResult.currentPage = pageable.getPageNumber();
        mergeResult.size = pageable.getPageSize();
        mergeResult.content = PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), unfinishedReasonDtos);
        return mergeResult;
    }
}
