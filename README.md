# Java_board_Spring   
간단한 게시판 프로젝트를 Spring에 방식으로 구현한 프로젝트 입니다.  
학습을 위해서 적용할 필요가 없는 Command 패턴을 적용해 의존성 문제를 발생시켰습니다.   
(원래 빼고 Spring 자체 의존성 및 Mapping 시스템을 이용해야했지만 잘 몰라서 Command 썼습니다)   
기존 Java_board_Servlet에서 받은 피드백을 반영했고, 새롭게 피드백을 받아 다음 프로젝트인 Java_board_Vue에 반영 예정입니다.   


- [신규 Java_board_Servlet 피드백](#신규-java_board_servlet-피드백)
    * [log4j2대신 logback 사용하기](#log4j2대신-logback-사용하기)
    * [에러 핸드링 방식 변경](#에러-핸드링-방식-변경)
    * [url을 가져올 때 @PathParam을 사용고려](#url을-가져올-때-pathparam을-사용고려)
    * [if else 활용하기](#if-else-활용하기)
    * [if else 치워버리기](#if-else-치워버리기)
    * [request를 매개변수로 던지지 말기](#request를-매개변수로-던지지-말기)
    * [이전 페이지로 돌아간다고 referer를 사용하지 말기](#이전-페이지로-돌아간다고-referer를-사용하지-말기)
    * [util 클래스를 만든다면 이름은 간결하게](#util-클래스를-만든다면-이름은-간결하게)
    * [null 체크시 "null" 문자열은 별도의 의미를 가질 수 있음](#null-체크시-null-문자열은-별도의-의미를-가질-수-있음)
    * [이름을 지을 때 Wrapper와 Manager를 구분하기](#이름을-지을-때-wrapper와-manager를-구분하기)
    * [MyBatis에서 sqlSessionFactory를 외부에 노출하지 않기](#mybatis에서-sqlsessionfactory를-외부에-노출하지-않기)
    * [SqlSession을 사용한다면 각 처리에서 SqlSession 하나만 사용하기](#sqlsession을-사용한다면-각-처리에서-sqlsession-하나만-사용하기)
    * [rollback 등의 마지막 처리는 Finally에서 하기](#rollback-등의-마지막-처리는-finally에서-하기)
    * [resultMap 대신 resultType 사용하기](#resultmap-대신-resulttype-사용하기)
    * [쿼리문 예쁘게 정리하기](#쿼리문-예쁘게-정리하기)
    * [properties 파일 활용하기](#properties-파일-활용하기)
    * [DTO에서 boolean에 경우 get을 붙이지 말기](#dto에서-boolean에-경우-get을-붙이지-말기)
    * [한줄짜리 주석도 javaDoc 주석을 활용하기](#한줄짜리-주석도-javadoc-주석을-활용하기)
    * [코드 줄이기](#코드-줄이기)
- [신규 Java_board_Spring 피드백](#신규-Java_board_Spring-피드백)
    * [추가 리팩토링](#추가-리팩토링)
    * [Service return 유효성](#Service-return-유효성)
    * [Commans Naming](#commans-naming)
    * [Command 패턴의 필요성](#Command-패턴의-필요성)
    * [프로퍼티 네임은 전부 소문자](#프로퍼티-네임은-전부-소문자)
    * [웹 리소스 하위에 요소에는 언더바(_)가 아닌 그냥 대시(-)를 쓸 것](#웹-리소스-하위에-요소에는-언더바(_)가-아닌-그냥-대시(-)를-쓸-것)

## 이전 Java_board_Servlet 피드백

### log4j2대신 logback 사용하기
log4j2는 이전에 보안문제로 인한 결함이 있었습니다.  
가능하면 logback을 사용하는 것이 좋습니다.   

피드백 반영: Spring에서 기본적으로 제공되는 Logback을 사용했습니다.   

### 에러 핸드링 방식 변경
현재 에러를 핸들링 하기 위해 MainServlet에서 요청 별로 errorMessage Map을 만들고 JSP에 전달하는 방식을 사용중입니다.    
이 방식은 에러가 추가될 떄마다 MainServlet에 코드를 추가해야해 가독성을 망칩니다.    
또한 에러 정의를 Command에서 진행하지만 단순 code를 setAttribute하기 때문에 에러에 대해 이해하기가 어렵습니다.  

예시: MainServlet.java

```java
    // 에러 메시지 Map을 선언
    Map<String, String> errorMessages = new HashMap<>();
    // 요청 별로 에러 메시지를 정의
    if ("/write.do".equals(servletPath)) {
        viewPage = "/boards/free/write.jsp";
        request.setAttribute("command", "articleWrite");
        // 각 요청 별로 정의되기 때문에 가독성을 망침!
        errorMessages.put("1", "입력값 오류!");
        errorMessages.put("2", "게시물 등록 실패!");
        errorMessages.put("3", "파일 등록 실패!");
    }
```

예시: ArticleWriteCommand.java

```java
    // 에러 메시지를 정의하지만 어떤 에러인지 알기 어려움 
    request.setAttribute("error", "2");
```

에러 핸들링은 별도의 클래스를 만들어 에러를 정의하고, 핸들링하도록 하고,       
에러 발생시 호출해 핸들링 하는 방식으로 변경하면 가독성이 좋아질 것 같습니다.    
본인만의 Exception를 만드는 방안도 고려해보면 좋을 것 같습니다  

피드백 반영: CustomException을 생성하고 Service에서 에러를 Throw 시키면 전역 에러 핸들러가 캐치하는 방향으로 진행했습니다.  

### url을 가져올 때 @PathParam을 사용고려
command 방식 사용시 요청 url을 파악하기 위해 다양한 방법을 사용 가능합니다.     
아래는 subString을 활용한 예시입니다.  
```java
    String uri = request.getRequestURI();
    String conPath = request.getContextPath();
    String command = uri.substring(conPath.length());
```
URL을 가져오고 ContextPath를 가져오는 조금 복잡한 과정을 거칩니다.     
추천하는 방식으로는 @PathParam을 사용하는 것입니다.    
추가로 자신만의 Annotation을 만들어 보는 것을 추천합니다.    
method 실행 전 개입하여 데코레이터처럼 동작하는 AOP 개념의 방식을 사용해보는 것을 추천합니다.  

피드백 반영: FrontController에서 @PathVariable을 사용해 url을 가져왔습니다.  
```java
  @RequestMapping("/{pathCommand}_action.do")
  public ResponseEntity handleAllActions(HttpServletRequest request,
      @PathVariable("pathCommand") String pathCommand)
      throws CustomException, Exception{
    }

```

### if else 활용하기
if를 연속적으로 사용하는 것은 성능을 저하시킬 수 있습니다.  
만약 각 if문에 return이 정의되있다면 return 이후의 if는 실행되지 않기 때문에 괜찮습니다.    
하지만 if문에 return이 없다면 조건에 맞는 if문이 실행되어도 다음 if문을 검사하기 때문에 성능이 저하됩니다.    
예시: MainServlet.java

```java
    if ("/list.do".equals(servletPath)) {
      viewPage = "/boards/free/list.jsp";
    }
    // /list.do 조건이 true여도 아래 조건을 다 검사함  
    if ("/write.do".equals(servletPath)) {
      viewPage = "/boards/free/write.jsp";
    }
    if ("/writeAction.do".equals(servletPath)) {
      viewPage = "/list.do";
    }
```

피드백 반영: RequestMapping과 @PathVariable을 사용해 Map에 매핑시키는 방법으로 if else를 치웠습니다.  

### if else 치워버리기
이전 피드백에 if else를 소개했지만 사실 if else를 사용하는 것은 가독성을 떨어트립니다.    
Map과 같은 다양한 방법을 사용해 if else를 치워버리는 것을 추천합니다.  

```java
    // 규모가 점점 늘어난다고 생각하면, 가독성이 매우 안좋아 질 것입니다.
    if ("/list.do".equals(servletPath)) {
      viewPage = "/boards/free/list.jsp";
    }
    if ("/write.do".equals(servletPath)) {
      viewPage = "/boards/free/write.jsp";
    }
    if ("/writeAction.do".equals(servletPath)) {
      viewPage = "/list.do";
    }
```

피드백 반영: RequestMapping과 @PathVariable을 사용해 Map에 매핑시키는 방법으로 if else를 치웠습니다.  

### request를 매개변수로 던지지 말기
request를 매개변수로 던지는 것은 확장성을 떨어트립니다.    
request는 우리가 정의할 수 없습니다. 또한 안에 무었이 들었는지 판단하기가 어렵습니다.    
특정 인터페이스(WAS)에서 만들어주는 데이터는 가능하면 파라미터로 사용하지 않는걸 추천합니다.    
우리가 util을 만든다면 request등과 같이 특정 인터페이스에 종속되는게 아닌 어떤 클래스에서 호출해도 명확하게 이해하고 사용 가능하도록 해야합니다.    
예시: MainServlet.java
```java
    // request에 무엇이 들어가는지 알 수 없고 제어하기 어려움
    // pojo형태(내가 만든 자바 DTO 또는 Map 등) 등으로 전달하는 방식으로 처리하는게 나음
    // 예시 1
    MainCommand mainCommand = MainCommandHelper.getCommand(request);
    mainCommand.execute(request, response);
    // 예시 2
    SearchManager searchManager = new SearchManager(request);
```

피드백 반영: request를 매개변수로 던지지 않고 필요한 parameter를 꺼내 매개변수로 던졌습니다.  
```java
long articleId = RequestUtil.getLongParameter(request.getParameter("articleId"));
```

### 이전 페이지로 돌아간다고 referer를 사용하지 말기
referer는 사용자가 요청을 보낼때 접근 전 이전 페이지의 url을 가져옵니다.    
즉 List.jsp에서 write.jsp로 이동할 때 write.jsp 요청의 referer는 List.jsp가 됩니다.    
하지만 사용자가 직접 url을 입력하여 접근할 경우 referer는 null이 됩니다.    
referer는 목적 자체가 통계 데이터 수집용으로 설계되었습니다.(사용자가 어디로부터 오는가)    
따라서 referer를 사용하여 이전 페이지로 돌아간다는 기능을 구현하는 것은 좋지 않습니다.  

예시: MainServlet.java

```java
    // 이전 페이지가 의도한 페이지가 아닐 수 있음 
    String referer = request.getHeader("referer");
    response.sendRedirect(referer);
```

피드백 반영: referer를 사용하지 않고 Action(처리)에 경우 Ajax와 ResponseEntity를 사용해 요청에 대한 응답처리를 했습니다.  

### util 클래스를 만든다면 이름은 간결하게
util 클래스를 호출해 사용할 때 이름이 길어지면 가독성이 떨어지기 때문에 이름을 간결하게 만드는 것을 추천합니다.    
해당 Class가 어떤 동작을 하는지 설명하기 위해 이름을 짓는건 좋은 일이지만, 가독성 또한 고려해야합니다.    
기능을 명확하게 설명하면서도 간결한 이름을 고려해야합니다.  

예시: ValidationChecker.java

```java
    public class ValidationChecker {
      // 메소드 이름은 소문자로 시작해야함
      // boolean을 리턴하는 메소드는 is로 시작하는게 좋음
      // equals, null체크 등 다양한 방법으로 체크를 하지만 그걸 이름에 다 명시할 필요는 없음
      // 명확하고 간결하게 isEmpty로 바꿔도 됨
      public static boolean CheckStringIsNullOrEmpty(String targetString) {
        return targetString == null || "".equals(targetString) || targetString.isEmpty()
            || "null".equals(targetString);
      }
    }
```

피드백 반영: 메소드 이름을 간결하게 변경했습니다.(isEmpty 등...)  

### null 체크시 "null" 문자열은 별도의 의미를 가질 수 있음
null을 체크한다고 "null".equals를 사용하는 경우가 있습니다.    
이 경우 애초에 "null"이라는 문자열이 들어오지 않는 것이 보장되어야 합니다.    
"null"은 발생하기 어려운 경우입니다.    
만약 "null"이라는 문자열이 들어올 수 있다면 별도의 의도를 가지고 의도적으로 정의한 경우일 수 있습니다.  

예시: ValidationChecker.java

```java
    public class ValidationChecker {
      // null체크시 "null" 문자열은 별도의 의미를 가질 수 있음
      public static boolean CheckStringIsNullOrEmpty(String targetString) {
        return targetString == null || "".equals(targetString) || targetString.isEmpty()
            || "null".equals(targetString);
      }
    }
```

피드백 반영: "null" 문자열을 체크하는 로직을 제거했습니다.

### 이름을 지을 때 Wrapper와 Manager를 구분하기
Wrapper는 특정 클래스(또는 메소드)를 감싸는 클래스(또는 메소드)에 대해 사용할 수 있습니다.    
즉, 해당 클래스가 특정 클래스에 종속적일 때 사용합니다.    
Manager는 여러 기능을 관리하는 클래스(또는 메소드)에 대해 사용할 수 있습니다.    
즉, 해당 클래스가 특정 목적에 연관된 여러 기능을 가지고 있을 때 사용합니다.    
예시로 기본 데이터 형인 int, boolean 등은 Integer, Boolean 등의 Wrapper 클래스가 있습니다.    
본 프로젝트에서 search 데이터를 관리하기 위해 SearchManager 클래스를 만들었습니다.  

추가로 MyBatis에서 SqlSession와 직접적으로 관련된 메소드를 만든다고 가정할 경우 sqlSessionMapper 등의 네이밍을 고려할 수 있습니다.  

피드백 반영: 필요한 경우나 의존성이 매우 긴밀한게 아니라면 Wrapper를 사용하지 않았습니다.  

### MyBatis에서 sqlSessionFactory를 외부에 노출하지 않기
MyBatis에서 SqlSessionFactory를 외부에 노출하지 않는 것이 좋습니다.  
SqlSessionFactory는 자체적으로 SqlSession을 생성하고 관리합니다.  
SqlSessionFactory를 외부에 노출하면 SqlSession을 관리하는데 의도치 않은 조작을 할 수 있습니다.    
따라서 SqlSessionFactory를 반환하는 대신 SqlSession을 반환하는 걸 추천드립니다.  
예시: MyBatisConfig.java

```java
    // sqlSessionFactory를 리턴하는 대신 SqlSession을 생성해서 리턴하는게 좋음
    // SqlSession에 대한 관리를 MyBatis가 자체적으로 해줌(이전에는 직접 Finally에서 close를 해줘야 했음)
  public SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }
```
피드백 반영: SqlSessionTemplate을 사용해 SqlSession을 관리하도록 변경했습니다.  

### SqlSession을 사용한다면 각 처리에서 SqlSession 하나만 사용하기
SqlSession은 요청에 대한 트랜잭션을 관리할 수 있습니다.  
즉 SqlSession을 사용하는 동안에는 트랜잭션을 커밋하거나 롤백할 수 있습니다.  
게시글을 insert 하고 File insert에 실패한 경우 모든 처리를 롤백할 수 있습니다.  
하지만 SqlSession을 게시글, File 별로 나눠서 생성하면 트랜잭션을 관리할 수 없습니다.  

예시:
```java
    MyBatisConfig myBatisConfig = MyBatisConfig.getInstance();
    // auto close를 위한 try-with-resource
    // session을 DAO별로 따로 생성하면 트랜잭션 관리를 할 수 없음
    try (
        SqlSession articleSqlSession = myBatisConfig.getSqlSessionFactory().openSession();
        SqlSession fileSqlSession = myBatisConfig.getSqlSessionFactory().openSession();
    ) {
      // 기타 처리...
    }
```
피드백 반영: SqlSessionTemplate을 사용해 SqlSession을 관리하도록 변경했습니다.  

### rollback 등의 마지막 처리는 Finally에서 하기
try 내부에서 error가 발생한 경우 catch에서 rollback을 처리하는 경우가 있습니다.  
만일 catch가 여러개 존재할 경우 rollback 구문이 catch마다 반복되며 가독성에 좋지 않습니다.    
처리 중 error를 별도로 구분하며 처리 결과에 따라 Finally에서 rollback을 처리하는 것이 좋습니다.  

피드백 반영: SqlSessionTemplate을 사용해 SqlSession을 관리하도록 변경했습니다.  

### resultMap 대신 resultType 사용하기
resultMap은 보통 우리의 DTO를 MyBatis Mapper에서 별도로 정의하고 쿼리 결과를 매핑할 때 사용합니다.    
resultType은 MyBatis Mapper에서 별도로 정의하지 않고 쿼리 결과를 매핑할 때 사용합니다.    
예시: CategoryMapper.xml

```xml
    <mapper namespace="com.board.servlet.yoony.category.CategoryDAO">
      <parameterMap id="categoryParameterMap" type="com.board.servlet.yoony.category.CategoryDTO"></parameterMap>
      <resultMap id="categoryResultMap" type="com.board.servlet.yoony.category.CategoryDTO">
        <id property="categoryId" column="category_id"/>
        <result property="name" column="name"/>
      </resultMap>
      <!-- resultMap을 이용해 기존의 정의된 categoryResultMap을 매핑 -->
      <select id="selectCategoryList" resultMap="categoryResultMap">
        SELECT category_id, name
        FROM category
        ORDER BY category_id ASC
      </select>
      <!-- resultType을 사용해 DTO 자체를 매핑-->
      <select id="selectCategoryList" resultType="com.board.servlet.yoony.category.CategoryDTO">
        SELECT category_id, name
        FROM category
        ORDER BY category_id ASC
      </select>
    </mapper>
```

resultMap 방식은 컬럼과 DTO의 변수명을 자유롭게 매칭 가능합니다.   
주로 복잡한 쿼리문 결과를 매핑할 때 사용합니다.  
resultType 방식은 컬럼과 DTO의 변수명을 자동으로 매칭 하지만, 정해진 규격을 벗어나면 매칭이 어렵습니다.    
resultType을 추천드리는 이유는 런타임에서 Query와 매핑된 DTO를 검사해서 오류가 있는 경우 즉각적으로 알려주는 장점이 있습니다.    
즉각적인 오류 검사를 위해 resultType을 추천드립니다.  

피드백 반영: resultType을 사용해 DTO 자체를 매핑했습니다.  
```xml
<mapper namespace="com.board.spring.yoony.comment.CommentMapper">
  <insert id="insertComment" parameterType="com.board.spring.yoony.comment.CommentDTO">
    INSERT INTO comment
    (article_id, content)
    VALUES
    (#{articleId}, #{content})
  </insert>
  <select id="selectCommentList" resultType="com.board.spring.yoony.comment.CommentDTO" parameterType="long">
    SELECT comment_id, article_id, content, created_date
    FROM comment
    WHERE article_id = #{articleId}
  </select>
</mapper>
```

### 쿼리문 예쁘게 정리하기
Select를 작성할때 반환 컬럼명이 많은 경우 3,4개를 기준으로 줄바꿈을 하면서 가독성을 고려하는 것을 추천드립니다.    
효율적인 쿼리 구성 말고도 단순 가독성 변경또한 신경써야합니다.  

피드백 반영: 쿼리문을 줄바꿈하여 가독성을 높였습니다.  
```xml
    UPDATE article
    SET writer        = #{writer},
        title         = #{title},
        content       = #{content},
        modified_date = current_timestamp()
    WHERE article_id = #{articleId}
      AND password = #{password}
```

### properties 파일 활용하기
설정 등의 정적 자원에 대해서 properties 파일을 활용하는 것이 좋습니다.    
배포 환경, 개발 환경 등 다양한 환경에서 정적 자원에 각각 차이가 있을 수 있습니다.    
서로 다른 환경에 대해서 properties 파일을 활용하여 관리할 수 있습니다.    
다음은 properties 파일을 활용 가능한 예시들 입니다.  
```java
    // MyBatis에서 설정 xml을 읽어올 때 properties를 활용 가능합니다.  
    // 추가로 xml 파일은 소문자로 시작해야합니다.    
    InputStream configuration = Resources.getResourceAsStream("Mybatis.xml");

    // 파일에 대한 저장 설정 시 properties를 활용해 각 서버 환경마다 다른 설정이 가능합니다.  
    String saveDirectory = "C:\\tempUploads";
    int maxPostSize = 10 * 1024 * 1024; // 10MB 제한
    String encoding = "UTF-8";
```

또한 properties를 활용한다면 국제화를 해보는 것도 좋습니다.    
다국어 지원을 위해 properties 파일을 locale에 따라 다르게 설정할 수 있습니다.  

피드백 반영: messages, application.properties 파일을 활용해 각 환경에 맞는 설정을 할 수 있도록 변경했습니다.  


### DTO에서 boolean에 경우 get을 붙이지 말기
DTO에서 boolean에 대해서 get을 붙이지 않는 것이 좋습니다.  
자동으로 Generated된 boolean getter는 is를 붙여줍니다.  
별도로 따로 boolean에 대한 메소드 정의시 is를 활용하는 것이 좋습니다.  

피드백 반영: DTO에서 boolean에 대해서 get을 붙이지 않도록 변경했습니다.  

### 한줄짜리 주석도 javaDoc 주석을 활용하기
DTO에 변수명에 주석을 거는 경우 한줄짜리의 주석을 활용하는 경우가 있습니다.    
추후 문서화를 고려하는 경우 javaDoc 주석을 활용하는 것이 좋습니다.  
예시: PageDTO.java
```java
  // //으로 정의되는 주석대신 /** */을 활용하는 것이 좋습니다.
  // 페이지 번호
  private int pageNum;
  // 페이지당 보여줄 게시물 수
  private int pageSize;
```

피드백 반영: DTO에 필드에 대해서 한줄짜리 주석을 javaDoc 주석으로 변경했습니다.  
```java

  /**
   * 페이지 번호
   */
  private int pageNum;
  /**
   * 페이지당 보여줄 게시물 수
   */
  private int pageSize;
```

### 코드 줄이기
공통적으로 가능한 코드를 줄이고 가독성을 확보하는 것이 좋습니다.  
리팩토링 중심 코드의 코드를 늘 고려하는게 좋습니다.  
Try catch만 줄여도 코드가 줄어듭니다.  
에러를 그냥 던져버리거나, 핸들러를 마련해서 핸들러가 처리를 해도 코드가 줄어듭니다.  

피드백 반영: try catch를 줄이고 error throw 방식을 사용하거나, if else를 제거하는 등 코드를 줄이고자 했습니다.  

## 신규 Java_board_Spring 피드백

### 추가 리팩토링
리팩토링은 구조나 설계를 바꾸는 것이 아닌 코드 구성이나 기능별로 나누는 것도 포함입니다.  
코드를 기능별로 나눠서 깔끔하게 분리시키는 것이 좋습니다.  
코드 중간중간 Validation 체크보단 Validation Manager가 따로 존재해 Validation을 관리하는 것도 좋습니다.  

### Service return 유효성
Service에서 return을 할 때 성공 여부에 따라 true, false를 반환하는 것은 안좋습니다.  
성공 여부보다는 성공 시 반환되는 값이 더 중요합니다.  
Service 로직이 확장되거나 변화할때, true가 아닌 1, 2, 3 등의 값을 반환하게 될 경우 구조를 바꿔야 하기에 확장성이 떨어집니다.  
실패 여부는 차라리 Exception을 발생시키는 것이 좋습니다.  

또한 패스워드 체크 등의 별도 검증은 내부적으로 하거나, 컨트롤러에서 미리 잡아주는 방식도 좋습니다.  

### Commans Naming
```java

  private final Map<String, ActionCommand> commands = new HashMap<>();
  public ActionCommandHelper(DependencyCommand dependencyCommand) {
    this.dependencyCommand = dependencyCommand;
    this.commands.put("write", new ArticleWriteActionCommand(this.dependencyCommand));
    this.commands.put("comment_write", new CommentWriteActionCommand(this.dependencyCommand));
    this.commands.put("modify", new ArticleModifyActionCommand(this.dependencyCommand));
    this.commands.put("delete", new ArticleDeleteActionCommand(this.dependencyCommand));
  }
```
위와 같이 commands는 List 느낌이 들지만, Map으로 되어있습니다.  
차라리 이름에 Map을 붙이는 것을 추천드립니다.  

### Command 패턴의 필요성
Command 패턴을 사용하는 이유는 Command를 통해 Controller와 Service를 분리하기 위함입니다.  
Servlet 방식에서는 직접 구현이 필요하겠지만 Spring 방식에서는 Mapping 기능이 존재하기에 Command 패턴을 사용할 필요가 없습니다.  
Command 패턴을 Spring에서 사용하면 Spring에 라이프 사이클을 벗어나 의존성 주입 등의 혜택을 받을 수 없게 됩니다.  

Domain(Comment, Article, File)을 기준으로 File을 분리하고, 각 RequestMapping은 하나의 Command 기능을 맡도록 구성하는걸 추천드립니다.  

### 프로퍼티 네임은 전부 소문자
프로퍼티 네임은 전부 소문자로 작성하는 것이 좋습니다.  
```xml
thymeleaf.list.table.createdDate=등록 일시
```
프로퍼티 이름을 사용 영역별로 나눈다고 해서 대문자를 사용하는 것은 좋지 않습니다.  
기존 이름을 차라리 단축시키거나 분리하는 것을 추천드립니다.  
```xml
thymeleaf.list.table.created=등록 일시
```

### 웹 리소스 하위에 요소에는 언더바(_)가 아닌 그냥 대시(-)를 쓸 것
리소스 아래에서 쓰이는 모든 요소들에는 언더바 대신 그냥 대시(-)를 쓸 것을 추천드립니다.  
물론 QueryString에서는 카멜케이스를 사용하는 것이 좋습니다.  
