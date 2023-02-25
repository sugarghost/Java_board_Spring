package com.board.spring.yoony;

import com.board.spring.yoony.article.search.SearchManager;
import com.board.spring.yoony.command.ActionCommand;
import com.board.spring.yoony.command.ActionCommandHelper;
import com.board.spring.yoony.command.DependencyCommand;
import com.board.spring.yoony.command.DownloadCommand;
import com.board.spring.yoony.command.DownloadCommandHelper;
import com.board.spring.yoony.command.MainCommand;
import com.board.spring.yoony.command.MainCommandHelper;
import com.board.spring.yoony.error.CustomException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * Front Controller 패턴으로 모든 요청을 처리하는 컨트롤러
 *
 * @author yoony
 * @version 1.0
 * @see DependencyCommand
 * @see MainCommand
 * @see ActionCommand
 * @see DownloadCommand
 * @since 2023. 02. 26.
 */
@Controller
@RequiredArgsConstructor
public class FrontController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * Command에서 사용할 의존성 주입을 위한 커맨드
   */
  @Autowired
  private DependencyCommand dependencyCommand;

  /**
   * 파일 다운로드를 위한 커맨드
   */
  @Autowired
  private DownloadCommandHelper downloadCommandHelper;
  /**
   * 요청을 처리하기 위한 커맨드
   */
  @Autowired
  private ActionCommandHelper actionCommandHelper;

  /**
   * 화면을 처리하기 위한 커맨드
   */
  @Autowired
  private MainCommandHelper mainCommandHelper;

  /**
   * 파일 다운로드 요청을 처리
   * <p>FileDownloadCommand를 사용하여 요청을 처리
   *
   * @param request
   * @param pathCommand 처리할 command를 매핑하기 위한 String
   * @return ResponseEntity
   * @throws CustomException
   * @throws Exception
   * @author yoony
   * @version 1.0
   * @see DownloadCommand
   * @see DownloadCommandHelper
   * @since 2023. 02. 26.
   */
  @RequestMapping("/{pathCommand}_download.do")
  public ResponseEntity handleAllDownload(HttpServletRequest request,
      @PathVariable("pathCommand") String pathCommand)
      throws CustomException, Exception {
    logger.debug("handleAllDownload(" + pathCommand + ") called");

    DownloadCommand downloadCommand = downloadCommandHelper.getCommand(pathCommand);
    return downloadCommand.execute(request);
  }

  /**
   * 요청을 처리
   * <p>ActionCommand를 사용하여 요청을 처리
   *
   * @param request
   * @param pathCommand 처리할 command를 매핑하기 위한 String
   * @return ResponseEntity
   * @throws CustomException
   * @throws Exception
   * @version 1.0
   * @author yoony
   * @see ActionCommand
   * @see ActionCommandHelper
   * @since 2023. 02. 26.
   */
  @RequestMapping("/{pathCommand}_action.do")
  public ResponseEntity handleAllActions(HttpServletRequest request,
      @PathVariable("pathCommand") String pathCommand)
      throws CustomException, Exception {
    logger.debug("handleAllActions(" + pathCommand + ") called");
    // 요청 응답에 대한 인코딩을 UTF-8로 설정
    request.setCharacterEncoding("UTF-8");

    Map<String, Object> paramMap = new HashMap<>();
    Map<String, Object> model = new HashMap<>();

    ActionCommand actionCommand = actionCommandHelper.getCommand(pathCommand);
    actionCommand.execute(request, paramMap, model);
    return new ResponseEntity(model, HttpStatus.OK);
  }

  /**
   * 화면을 처리
   * <p>MainCommand를 사용하여 요청을 처리
   * <p>SearchManager를 사용하여 검색 조건을 처리
   *
   * @param request
   * @param pathCommand 처리할 command를 매핑하기 위한 String
   * @return ModelAndView
   * @throws CustomException
   * @throws Exception
   * @version 1.0
   * @author yoony
   * @see MainCommand
   * @see MainCommandHelper
   * @see SearchManager
   * @since 2023. 02. 26.
   */
  @RequestMapping("/{pathCommand}.do")
  public ModelAndView handleAllRequests(HttpServletRequest request,
      @PathVariable("pathCommand") String pathCommand)
      throws CustomException, Exception {
    logger.debug("handleAllRequests(" + pathCommand + ") called");
    // 요청 응답에 대한 인코딩을 UTF-8로 설정
    request.setCharacterEncoding("UTF-8");

    Map<String, Object> paramMap = new HashMap<>();
    Map<String, Object> viewModel = new HashMap<>();

    SearchManager searchManager = new SearchManager(
        request.getParameter("pageNum"),
        request.getParameter("searchWord"),
        request.getParameter("categoryId"),
        request.getParameter("startDate"),
        request.getParameter("endDate")
    );
    paramMap.putAll(searchManager.getSearchParamsMap());

    MainCommand mainCommand = mainCommandHelper.getCommand(pathCommand);
    viewModel.put("searchManager", searchManager);
    String viewName = mainCommand.execute(request, paramMap, viewModel);

    ModelAndView modelAndView = new ModelAndView(viewName, viewModel);
    return modelAndView;
  }
}