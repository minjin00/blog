## 기능 구현 체크리스트

### 1. 회원가입
- [x] 회원 가입폼
- [x] 같은 ID, Email Check API
- [x] 회원 등록 기능
- [x] 회원 가입 후 로그인 폼으로 이동

### 2. 로그인/로그아웃
- [x] 로그인 폼
- [x] 로그인 기능
- [x] 로그인 성공 후 / 로 이동
- [x] 로그인 실패 후 다시 로그인 폼으로 이동(오류 메시지 출력)
- [ ] Spring Security 이용해서 로그인 구현
- [x] 로그아웃 기능

### 3. 사이트 상단
- [x] 사이트 로고가 좌측 상단에 보여짐
- [x] 사이트 우측에 로그인 링크 or 사용자 정보 보여짐
  - [x] 로그인을 안했을 경우엔 로그인 링크가 보이도록
  - [x] 로그인을 했을 경우엔 사용자 이름이 보이도록
  - [ ] 사용자 이름을 클릭하면 설정, 해당 사용자 블로그 이동 링크, 임시저장글 목록, 로그아웃

### 4. 메인 페이지(/)
- [ ] 블로그 글 목록(최신 순, 조회수 높은 순, 즐겨찾기 순)
- [x] 페이징 처리 또는 무한 스크롤 구현
- [x] 제목, 내용, 사용자 이름 중 하나로 검색 기능

### 5. 블로그 글 쓰기
- [x] 블로그 제목, 내용, 사진 등을 입력하여 글 씀
- [x] "출간하기" 를 누르면 블로그 썸네일(이미지), 공개유무, 시리즈 설정을 하고 다시 "출간하기"를 누르면 글 등록
- [x] "임시저장"을 누르면 글 임시 저장

### 6. 임시글 저장 목록 보기
- [x] 회원 로그인 시 임시글 저장 목록 보기 링크 → 임시 글 저장 목록이 보여짐
- [x] 임시글 저장 목록의 글 제목을 클릭하면 글 수정 가능
- [x] "임시저장", "출간하기" 가능

### 7. 특정 사용자 블로그 글 보기 (/@ 사용자 아이디)
- [x] 사용자 정보 보기
- [x] 사용자가 쓴 글 목록 보기
- [ ] 페이징 처리 또는 무한 스크롤
- [ ] 사용자의 태그 목록 (태그당 글의 수)
- [x] 제목, 내용 중 하나로 검색 기능

### 8. 시리즈 목록 보기
- [ ] 시리즈 목록 보기
- [ ] 시리즈 제목 클릭 시 시리즈에 포함된 블로그 글 목록 보여주기

### 9. 블로그 글 상세 보기
- [x] 메인 페이지에서 제목 클릭 시 블로그 글 상세 보여짐
- [ ] 특정 사용자 블로그에서 제목 클릭 시 블로그 상세 보여짐
- [ ] 시리즈에 속한 블로그 글 목록에서 제목 클릭 시 블로그 글 상세 보여짐

### 10. 사용자 정보 보기
- [ ] 사이트 상단에서 로그인 했을 때 보여지는 로그인 사용자 이름을 클릭하면 사용자 정보가 보여짐
- [ ] 사용자 이름, 이메일 등 출력
- [ ] 회원 탈퇴 링크 제공

### 11. 회원 탈퇴
- [ ] 회원 탈퇴를 하겠냐는 폼 보여짐
- [ ] 폼에서 확인을 하면 회원 탈퇴

### 12. 댓글 목록
- [ ] 상세보기 시 하단에 댓글 목록 보임
- [ ] 댓글, 대댓글이 최신 댓글부터 보여짐
- [ ] 댓글은 최대 20개, 페이징 처리

### 13. 블로그
- [ ] 블로그에 댓글 달기
- [ ] 댓글에 대댓글 달기
- [ ] 글에 좋아요 하기
