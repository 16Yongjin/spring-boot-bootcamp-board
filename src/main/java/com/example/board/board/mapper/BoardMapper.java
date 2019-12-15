package com.example.board.board.mapper;

import java.util.List;

import com.example.board.board.dto.BoardDto;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {
  List<BoardDto> selectBoardList() throws Exception;

  void insertBoard(BoardDto board) throws Exception;

  void updateHitCount(int boardIdx) throws Exception;

  BoardDto selectBoardDetail(int boardIdx) throws Exception;

  void updateBoard(BoardDto board) throws Exception;

  void deleteBoard(int boardIdx) throws Exception;
}