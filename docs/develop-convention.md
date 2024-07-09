# 개발 컨벤션

## 깃

### 브렌치


하나의 브렌치에서는 하나의 기능을 구현한다.

### 커밋


타입은 다음과 같다.

```markdown
feat : 새로운 기능 추가
fix : 버그 수정
docs : 문서 수정
style : 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
refactor : 코드 리펙토링
test : 테스트 코드, 리펙토링 테스트 코드 추가
chore : 빌드 업무 수정, 패키지 매니저 수정
```

커밋 범위의 경우 최대한 적은 수의 파일을 포함하도록 한다.

### PR

PR의 경우 브렌치에서 작업을 마친 이후 `develop` 브렌치로 PR을 보낸다.

리뷰의 경우 2일 이내로 완료하도록 한다.


## 코드

### 정렬

코드 정렬은 `spotless`를 사용하여 정렬한다.

```bash
./gradlew spotlessApply
```

위의 명령어를 통해 정렬할 수 있다.

혹은 `scripts/precommit`을 실행하면 커밋하기 이전에 정렬을 수행하게 설정할 수 있다. (macOS, Linux)

(window의 경우 `.git/hooks/pre-commit`에 `scripts/precommit`을 추가하면 된다.)
