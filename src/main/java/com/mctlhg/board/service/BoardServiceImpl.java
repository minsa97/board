package com.mctlhg.board.service;

import com.mctlhg.board.dto.BoardDTO;
import com.mctlhg.board.dto.PageRequestDTO;
import com.mctlhg.board.dto.PageResultDTO;
import com.mctlhg.board.entity.Board;
import com.mctlhg.board.entity.Member;
import com.mctlhg.board.repository.BoardRepository;
import com.mctlhg.board.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{
    private final BoardRepository repository;
    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO dto){
        log.info(dto);
        Board board=dtoToEntity(dto);
        repository.save(board);
        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO){
        log.info(pageRequestDTO);

        Function<Object[], BoardDTO> fn=(en->entityToDTO((Board)en[0], (Member)en[1], (Long)en[2]));

        Page<Object[]> result= repository.getBoardWithReplyCount(
                pageRequestDTO.getPageable(Sort.by("bno").descending()));

        return new PageResultDTO<>(result,fn);
    }

    @Override
    public BoardDTO get(Long bno){
        Object result=repository.getBoardByBno(bno);
        Object[] arr=(Object[])result;
        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
    }

    @Transactional
    @Override
    public void removeWithReplies(Long bno){ //삭제 기능, 트랜젝션 추가
        replyRepository.deleteByBno(bno);//댓글부터 삭제
        repository.deleteById(bno);
    }

    @Transactional
    @Override
    public void modify(BoardDTO boardDTO){
        Board board= repository.getById(boardDTO.getBno());
        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());
        repository.save(board);
    }
}
