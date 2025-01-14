## [week04] dohoon

### Controller

- `PostController`
    
    ```java
    @RestController
    @RequestMapping("/post")
    public class PostController {
        private final PostService postService;
    
        @Autowired
        public PostController(PostService postService) {
            this.postService = postService;
        }
    ```
    
    - `@RestController` : 컨트롤러 클래스 선언. JSON 형식으로 데이터를 반환
    - `@RequsestMapping` : 공통 URL 경로 설정
    - `@Autowired` : postService에 대한 의존성 주입
- `getAllPost`
    
    ```java
    // Read
        @GetMapping
        public ResponseEntity<List<PostDto>> getAllPost() {
            return new ResponseEntity<>(postService.getAllPost(), HttpStatus.OK);
        }
    ```
    
    - 전체 게시글 조회
        - **HTTP 메서드**: `GET`
        - **URL**: `/post`
        - **목적**: 모든 게시글 조회
        - **어노테이션**: `@GetMapping`
        - **응답 상태**: `HttpStatus.OK`
- `getPostById`
    
    ```java
    // Read
        @GetMapping("/{id}")
        public ResponseEntity<PostDto> getPostById(@PathVariable("id") Long id) {
            try {
                return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    ```
    
    - 특정 게시글 조회
        - **HTTP 메서드**: `GET`
        - **URL**: `/post/{id}`
        - **목적**: 특정 ID에 해당하는 게시글 조회
        - **어노테이션**: `@GetMapping("/{id}")`
        - **응답 상태**:
            - 성공: `HttpStatus.OK`
            - 실패: `HttpStatus.NOT_FOUND`
- `createPost`
    
    ```java
    // Create
        @PostMapping
        public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
            try {
                postService.createPost(postDto);
                return new ResponseEntity<>(postDto, HttpStatus.CREATED);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    ```
    
    - 게시글 생성
        - **HTTP 메서드**: `POST`
        - **URL**: `/post`
        - **목적**: 새로운 게시글 생성
        - **어노테이션**: `@PostMapping`
        - **요청 본문**: `@RequestBody PostDto`
            - HTTP 요청 본문에서 객체 매핑
        - **응답 상태**:
            - 성공: `HttpStatus.CREATED`
            - 실패: `HttpStatus.BAD_REQUEST`
- `updatePost`
    
    ```java
    // Update
        @PatchMapping("/{id}")
        public ResponseEntity<PostDto> updatePost(@PathVariable("id") Long id, @RequestBody PostDto postDto) {
            try {
                postService.updatePost(id, postDto);
                return new ResponseEntity<>(postDto, HttpStatus.OK);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    ```
    
    - 게시글 수정
        - **HTTP 메서드**: `PATCH`
        - **URL**: `/post/{id}`
        - **목적**: 기존 게시글 수정
        - **어노테이션**: `@PatchMapping("/{id}")`
        - **요청 본문**: `@RequestBody PostDto`
        - **응답 상태**:
            - 성공: `HttpStatus.OK`
            - 실패: `HttpStatus.NOT_FOUND`
- `deletePost`
    
    ```java
    // Delete
        @DeleteMapping("/{id}")
        public void deletePost(@PathVariable("id") Long id) {
            postService.deletePost(id);
        }
    ```
    
    - 게시글 삭제
        - **HTTP 메서드**: `DELETE`
        - **URL**: `/post/{id}`
        - **목적**: 특정 ID의 게시글 삭제
        - **어노테이션**: `@DeleteMapping("/{id}")`
        - **응답 상태**:
            - 성공: `HttpStatus.NO_CONTENT`
            - 실패: `HttpStatus.NOT_FOUND`
- `PathVariable` : URL 경로의 변수 값 매핑시 사용

### Service

- `PostService`
    - `getAllPost`
        
        ```java
        public List<PostDto> getAllPost(){
                return postRepository.findAll()
                        .stream()
                        .map(PostDto::fromEntity)
                        .collect(Collectors.toList());
            }
        ```
        
        - **역할**: 모든 게시글 조회
        - **Return** : `List<PostDto>` (게시글 목록)
    - `getPostById`
        
        ```java
        public PostDto getPostById(Long id){
                return postRepository.findById(id)
                        .map(PostDto::fromEntity)
                        .orElseThrow(() -> new RuntimeException("Post not found"));
            }
        ```
        
        - **역할  :** 특정 게시글 조회
        - **Param :** id 게시글 ID
        - **Return :** PostDto - 게시글 정보
    - `createPost`
        
        ```java
        public PostDto createPost(PostDto postDto){
                Post post = postDto.toEntity();
                return PostDto.fromEntity(postRepository.save(post));
            }
        ```
        
        - **역할 :** 게시글 생성
        - **Param :** PostDto 생성할 게시글 정보
        - **Return :** PostDto - 생성된 게시글 정보
        - DTO를 엔티티로 변환한 뒤 저장하고 다시 DTO로 변환하여 반환
    - `updatePost`
        
        ```java
        public PostDto updatePost(Long id, PostDto postDto){
                Post post = postRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Post not found"));
        
                post.setTitle(postDto.getTitle());
                post.setContent(postDto.getContent());
                post.setUpdateDate(LocalDateTime.now());
                postRepository.save(post);
        
                return PostDto.fromEntity(post);
            }
        ```
        
        - **역할 :**  기존 게시글 수정
        - **Param :**
            - 게시글 ID
            - 수정할 게시글 정보
        - **Return :** 수정된 게시글 정보
        - 수정할 필드만 업데이트, 수정 시 `updateDate` 를 현재 시간으로 설정
    - `deletePost`
        
        ```java
        public void deletePost(Long id){
                if(!postRepository.existsById(id)){
                    throw new RuntimeException("Post not found");
                } else {
                    postRepository.deleteById(id);
                }
            }
        ```
        
        - **역할 :** 특정 게시글 삭제
        - **Param :** 게시글 ID
- `PostDto`
    
    ```java
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class PostDto {
        private Long id;
        private String title;
        private String content;
        private LocalDateTime registerDate;
        private LocalDateTime updateDate;
    
        public static PostDto fromEntity(Post post){
            return PostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .registerDate(post.getRegisterDate())
                    .updateDate(post.getUpdateDate())
                    .build();
        }
    
        public Post toEntity(){
            return Post.builder()
                    .title(this.title)
                    .content(this.content)
                    .build();
        }
    }
    ```
    
    - `Getter` , `Setter` , 생성자는 lombok 어노테이션 사용하여 생략
    - `fromEntity`
        - **역할 : `Post`** 엔티티 객체를 `PostDto`로 면환
        - 서비스 계층에서 데이터 조회 후 DTO로 변환하여 반환
        - 데이터베이스에서 클라이언트에게 전달할 때 사용
        - **매핑 필드**: `id`, `title`, `content`, `registerDate`, `updateDate`
    - `toEntity`
        - **역할 :**  `PostDto` 를 `Post 엔티티로 변환
        - 클라이언트에게 받은 DTO 데이터를 기반으로 새 엔티티 생성후 데이터베이스에 저장할 때 사용
        - ID나 날짜 필드는 엔티티에서 관리되므로 DTO에서 직접 매핑하지 않음
        - **매핑 필드**: `title`, `content`

### Entity

- `Post`
    
    ```java
    @Entity
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class Post {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    
        @Column(nullable = false)
        private String title;
    
        @Column(nullable = false)
        private String content;
    
        @CreationTimestamp
        private LocalDateTime registerDate;
    
        @CreationTimestamp
        private LocalDateTime updateDate;
    }
    ```
    
    - 게시글 정보를 데이터베이스에 매핑하기 위한 엔티티 클래스
    - `@Entity`: JPA 엔티티, 데이터베이스의 테이블과 매핑
    - `@Id`: Primary Key로 설정
    - `@GeneratedValue(strategy = GenerationType.IDENTITY)`: ID를 데이터베이스에서 자동으로 생성
        - `IDENTITY`: 데이터베이스에서 Auto Increment 방식으로 ID 생성
    - `@Column(nullable = false)`: NOT NULL 제약 조건
    - `@CreationTimestamp`: Hibernate제공 어노테이션으로, 값이 생성될 때 자동으로 현재 시간이 설정