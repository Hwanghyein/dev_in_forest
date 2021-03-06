package com.devinforest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devinforest.mapper.ReportMapper;
import com.devinforest.vo.Answer;
import com.devinforest.vo.AnswerComment;
import com.devinforest.vo.BlackList;
import com.devinforest.vo.Question;
import com.devinforest.vo.QuestionComment;
import com.devinforest.vo.Report;

@Service
@Transactional
public class ReportService {
	@Autowired private ReportMapper reportMapper;
	
	// 신고 List 출력
	public Map<String, Object> getReportList(String searchWord, int currentPage, int rowPerPage, String reportKind) {
		System.out.println(searchWord + " <--ReportService.getReportList: searchWord");
		System.out.println(currentPage + " <--ReportService.getReportList: currentPage");
		System.out.println(rowPerPage + " <--ReportService.getReportList: rowPerPage");
		System.out.println(reportKind + " <--ReportService.getReportList: reportKind");
		
		// 시작행 구하기
		int beginRow = (currentPage-1) * rowPerPage;
		System.out.println(beginRow + " <--ReportService.getReportList: beginRow");
		
		Map<String, Object> totalCountMap = new HashMap<>();
		totalCountMap.put("searchWord", searchWord);
		totalCountMap.put("reportKind", reportKind);
		
		// 총 신고갯수 구하기
		int reportTotalCount = reportMapper.reportTotalCount(totalCountMap);
		System.out.println(reportTotalCount + " <-- ReportService.getReportList: reportTotalCount");
		int lastPage = reportTotalCount / rowPerPage;
		if(reportTotalCount % rowPerPage != 0) {
			lastPage+=1;
		}
		System.out.println(lastPage + " <--ReportService.getReportList: lastPage");
		
		// List 구하기
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put("searchWord", searchWord);
		inputMap.put("reportKind", reportKind);
		inputMap.put("beginRow", beginRow);
		inputMap.put("rowPerPage", rowPerPage);
		List<Report> reportList =  reportMapper.selectReportList(inputMap);
		System.out.println(reportList.size() + " <-- ReportService.getReportList : reportList.size()");
		
		// List 출력
		Map<String, Object> outputMap = new HashMap<>();
		outputMap.put("reportTotalCount", reportTotalCount);
		outputMap.put("lastPage", lastPage);
		outputMap.put("reportList", reportList);
		
		System.out.println(totalCountMap);
		System.out.println(inputMap);
		System.out.println(outputMap);
		
		return outputMap;
	}
	// 신고내역 상세보기
	public Report getReportOne(int reportNo) {
		return reportMapper.selectReportOne(reportNo);
	}
	// 신고 조치유무 변경 - 이미 블랙된 회원일 경우
	public void modifyReportState(int reportNo) {
		reportMapper.updateReportState(reportNo);
	}
	// 신고된 게시글 삭제유무 확인
	public int getRemoveCheck(int questionNo, int questionCommentNo, int answerNo, int answerCommentNo) {
		int checkNum = 0;
		String check = null;
		if(questionCommentNo==0 && answerNo==0 && answerCommentNo==0) {
			System.out.println("게시글 신고");
			check = reportMapper.selectQuestionNo(questionNo); // 게시글 삭제유무 확인
			System.out.println(check+" <- 게시글 삭제유무 체크");
			if(check != null) {
				checkNum = 1;
				return checkNum;
			}
		}
		if(questionCommentNo!=0) {
			System.out.println("게시글 댓글 신고");
			check = reportMapper.selectQuestionCommentNo(questionCommentNo); // 게시글 댓글 삭제유무 확인
			System.out.println(check+" <- 게시글 댓글 삭제유무 체크");
			if(check != null) {
				checkNum = 1;
				return checkNum;
			}
		}
		if(answerNo!=0 && answerCommentNo==0) {
			System.out.println("게시글 답변 신고");
			check = reportMapper.selectAnswerNo(answerNo); // 게시글 답변 삭제유무 확인
			System.out.println(check+" <- 게시글 답변 삭제유무 체크");
			if(check != null) {
				checkNum = 1;
				return checkNum;
			}
		}else if(answerCommentNo!=0) {
			System.out.println("게시글 답변의 댓글 신고");
			check = reportMapper.selectAnswerCommentNo(answerCommentNo); // 게시글 답변의 댓글 삭제유무 확인
			System.out.println(check+" <- 게시글 답변의 댓글 삭제유무 체크");
			if(check != null) {
				checkNum = 1;
				return checkNum;
			}
		}
		return checkNum;
	}
}
