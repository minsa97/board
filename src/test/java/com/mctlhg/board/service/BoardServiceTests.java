package com.mctlhg.board.service;

import com.mctlhg.board.dto.BoardDTO;
import com.mctlhg.board.dto.PageRequestDTO;
import com.mctlhg.board.dto.PageResultDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardServiceTests {
    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister(){
        BoardDTO dto=BoardDTO.builder()
                .title("Test.")
                .content("Test..")
                .writerEmail("user55@aaa.com")
                .build();
        Long bno=boardService.register(dto);
    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO=new PageRequestDTO();
        PageResultDTO<BoardDTO, Object[]> result= boardService.getList(pageRequestDTO);

        for(BoardDTO b: result.getDtoList()){
            System.out.println(b);
        }
    }

    @Test
    public void testGet(){
        Long bno=100L;
        BoardDTO boardDTO=boardService.get(bno);
        System.out.println(boardDTO);
    }

    @Test
    public void testRemove(){
        Long bno=1L;
        boardService.removeWithReplies(bno);
    }

    @Test
    public void testModify(){
        BoardDTO boardDTO=BoardDTO.builder()
                .bno(2L)
                .title("제목 변경")
                .content("내용 변경")
                .build();
        boardService.modify(boardDTO);
    }
}
