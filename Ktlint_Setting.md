# A. git hook 을 이용해 커밋 시 ktlint 강제 적용하는 방법

### 1. 루트 디렉토리에 .editorconfig 파일 생성
(.editorconfig 파일 main 브랜치에 머지시켜놨기 때문에 이 단계는 스킵하셔도 됩니다~)

해당 파일을 바탕으로 ktlint 검사 진행
```editorconfig
root = true

[*]
charset = utf-8
end_of_line = lf
indent_size = 4
indent_style = space
trim_trailing_whitespace = true
insert_final_newline = true
max_line_length = 120
tab_width = 4

[*.{kt,kts}]
ktlint_function_naming_ignore_when_annotated_with=Composable
```

### 2. gradle 에 ktlint plugin 추가
1. project 수준의 gradle 에 추가하기
   (이 부분도 커밋 올려놓았기 때문에 넘어가셔도 됩니다!)
2. **File -> Sync Project widh Gradle Files 로 싱크 맞추기!!**
   (이 부분은 필수!!)
```gradle
plugins {
	id("org.jlleitschuh.gradle.ktlint") version "$최신 버전"
}
```
최신 버전은 아래 링크에서 확인 가능!

https://github.com/JLLeitschuh/ktlint-gradle

### 3. 깃 pre-commit 훅 설정
위에서 추가한 Gradle 플러그인을 통해 ktlint 를 설정. 빌드 시에도 ktlint 체크가 이루어짐.

Git pre-commit 훅이 설정되고, 커밋 전 ktlint를 사용하여 코드 스타일을 검사하는 작업을 수행할
```bash
./gradlew addKtlintCheckGitPreCommitHook
```

### 4. 커밋 하기
ktlint 를 위반하는 코드가 있으면 커밋이 불가

### (번외) 커밋하기 전 ktlint 적용하기
ktlint 를 설치하고 **```ktlint -F```** 명령어를 실행하면 커밋하지 않아도 ktlint 명령어로 코드 정렬할 수 있음
#### ktlint 설치 명령어
```bash
curl -sSLO https://github.com/pinterest/ktlint/releases/download/1.1.0/ktlint && chmod a+x ktlint && sudo mv ktlint /usr/local/bin/
```
<br/>

# B. editorConfig 규칙 기반으로 command + option + L 자동 포맷팅 하기
### 1. 루트 디렉토리에 .editorconfig 파일 생성
- A-1. 방법과 동일
### 2. IDE 설정
- **Android Studio Menu -> Settings > Editor > Code Style > Enable EditorConfig Support 체크** : .editorconfig 파일의 규칙대로 code style 설정됨
- **```command + option + L```** : 설정된 code style 기반으로 코드 자동 포맷팅 수행됨
