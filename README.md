### E-book 마켓
본 프로젝트는 주어진 요구사항 정의서를 기반으로 개발한 개인 프로젝트입니다.
프로젝트는 4주간 진행되었습니다.

게시글 작성, 상품 장바구니/주문/결제 및 정산 처리가 기능을 개발한 개인 프로젝트입니다.
아래 토글에서 자세한 요구사항을 확인하실 수 있습니다.

<br>

<details>
<summary>요구사항 정의서</summary>
<div markdown="1">
  
### **회원**

- 회원은 일반회원과 작가회원이 있다.
- 가입 직후는 일반회원이고, 작가명 등록을 하면 작가회원이 된다.
- 작가회원이 되면 상품(책)을 등록할 수 있다.
- 별개로 authLevel 의 값이 7이면 관리자의 역할도 수행할 수 있다.
- 회원가입
    - 회원가입시 축하메일을 발송한다.
- 로그인
    - 스프링 시큐리티를 사용하여 구현한다.
- 로그아웃
    - 스프링 시큐리티를 사용하여 구현한다.
- 프로필
    - 작가명을 입력하고, 작가 회원이 될 수 있다.
- 비밀번호변경
- 아이디 찾기
    - 가입 시 입력한 이메일 주소를 입력하여 아이디를 찾을 수 있다.
- 비밀번호 찾기
    - 로그인 시 필요한 아이디와 이메일로 비밀번호를 찾을 수 있다.
    - 이메일로 임시 비밀번호를 발송한다.

### **글 관리기능**

- 글 작성/수정
    - 마크다운 원문과 렌더링 결과(HTML)까지 같이 저장한다.
- 글 리스트
    - 제목과 글의 해시태그들을 볼 수 있다.
    - 해시태그를 클릭하면 내가 작성한 글 중 해당 해시태그와 관련된 글들을 볼 수 있다.
- 글 삭제
    - 글이 삭제되면 글 리스트로 리다이렉트 한다.
    - 삭제버튼 눌렀을 때 confirm 창으로 삭제여부를 한 번 더 물어본다.
- 글 상세화면
    - 글의 제목, 내용, 해시태그를 모두 출력
    - 글의 내용은 마크다운 해석이 되어야 한다.

### **상품(책) 관리기능**

- 상품 작성/수정
    - 상품의 이름을 작성할 수 있다.
    - 하나의 글 키워드만 고를 수 있다.
    - 등록을 누르면 해당 상품 상세 페이지로 이동해야 한다.
    - 키워드는 수정이 불가능하고 내용은 수정이 가능하다.
- 상품 리스트
    - 모든 상품을 출력한다.
    - 각각 상품은 상품의 이름, 가격, 작가를 출력한다.
- 상품 삭제
    - 상품이 삭제되면 상품 리스트로 리다이렉트 한다.
    - 삭제버튼 눌렀을 때 confirm 창으로 삭제여부를 한 번 더 물어본다.
- 상품 상세화면
    - 상품의 이름, 가격, 작가를 출력한다.
    - 상품 설명을 출력한다.
    - 미리보기는 구현하지 않는다.

### **장바구니**

- 상품이 곧 도서이다.
- 도서를 장바구니에 담을 수 있다.
- 장바구니에서 도서를 제거할 수 있다.
- 장바구니에 담긴 도서들을 가지고 주문을 할 수 있다.

### **주문/결제**

- 주문을 하면 일단 주문의 상태는 준비상태이다.
    - 여기서 결제를 하거나, 주문취소를 할 수 있다.
- 회원이 결제를 할 때 기존에 소유하고 있는 캐시를 사용할 수 있다.
- 본 쇼핑몰에서의 모든 현금 흐름은 CashLog 에 기록된다.
- CashLog 테이블은 사이버 머니에 대한 기록 테이블이다.
- 결제를 하게되면 CashLog 테이블에 돈이 충전된 후 바로 사용된다.
- 토스 페이먼츠를 이용해서 카드 결제를 구현한다.
- 결제가 완료되면 MyBook 테이블에 책이 구매된 책이 추가되어야 한다.

### **환불**

- 결제 후 10분 이내에 환불 할 수 있다.

### **정산데이터 생성**

- 결제가 된 주문품목은 정산품목으로 생성될 수 있다.

### **관리자 페이지**

- 관리자만 관리자 페이지에 접속할 수 있다.
- 개발자가 회원중 임의로 1명을 골라서 관리자로 지정
    - authLevel을 7로 지정

### **정산**

- 건별정산처리가 가능하다.
- 전체정산처리를 진행한다.
    - 여러개의 건들을 선택하여 한번에 정산처리
- 정산비율은 판매자와 멋북스가 5:5로 나눈다.
- 편의상 PG 수수료는 0원으로 가정하고 진행한다.

### **출금신청**

- 사용자는 본인이 보유한 예치금에 대해서 출금신청을 할 수 있다.
- 신청시에는 금액과 통장, 계좌번호를 입력한다.

### **출금신청 처리**

- 관리자만 관리자페이지에서 출금신청목록을 볼 수 있다.
- 해당 내역을 건별로 처리한다.
- 출금 수수료도 없다고 가정한다.

### Rest API 구현
- JWT 로그인 구현(POST /api/v1/member/login)
- 내 도서 리스트 구현(GET /api/v1/myBooks)
- 내 도서 상세정보 구현(GET /api/v1/myBooks/{id})
- 로그인 한 회원의 정보 구현(GET /api/v1/member/me)
- Spring Doc 으로 API 문서화(크롬 /swagger-ui/index.html)

</div>
</details>

<br/>

### Stack
`Java` `Spring Boot` `Thymeleaf` `MySQL` `Spring Data JPA` `Spring Security` `mapStruct` `QueryDsl` `Junit` `Tosspayments API`

<br/>

### ERD
![Untitled](https://github.com/likelion-backendschool/FinalProject_KangMinSeong_team7/assets/71378475/9ffa5ae5-6bf2-4fab-b421-0ce963e16e6e)

<br>

### UI
Thymeleaf으로 UI를 개발했으나, UI 퀄리티는 많이 낮습니다. UI보다는 백엔드 위주로 개발하였습니다.

#### 글, 해시태그
<img width="716" alt="image" src="https://github.com/likelion-backendschool/FinalProject_KangMinSeong_team7/assets/71378475/2469bcb0-73f4-4435-bd29-2ca7ef6df587">


#### 상품
<img width="713" alt="image" src="https://github.com/likelion-backendschool/FinalProject_KangMinSeong_team7/assets/71378475/7550ac1b-5f4a-46fa-85e3-b031f720aca8">
<img width="714" alt="image" src="https://github.com/likelion-backendschool/FinalProject_KangMinSeong_team7/assets/71378475/a93f5454-c751-4b88-802f-305dac45abe7">


#### 장바구니
<img width="714" alt="image" src="https://github.com/likelion-backendschool/FinalProject_KangMinSeong_team7/assets/71378475/b0a76fd8-1a63-48e3-bde6-3200ebf7d9cb">


#### 주문
<img width="390" alt="image" src="https://github.com/likelion-backendschool/FinalProject_KangMinSeong_team7/assets/71378475/e8a1bd0c-8a58-40cc-af66-7ca23331bce2">


#### 정산 데이터 처리
<img width="534" alt="image" src="https://github.com/likelion-backendschool/FinalProject_KangMinSeong_team7/assets/71378475/90cd256c-0f43-49bf-b860-1c86660c580f">


#### 출금 데이터 처리
<img width="715" alt="image" src="https://github.com/likelion-backendschool/FinalProject_KangMinSeong_team7/assets/71378475/7371bf0d-149e-4226-a429-eeb76c341ee9">
