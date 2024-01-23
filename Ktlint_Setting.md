# ktlint gradle 설정하기

## 1. gradle 에 ktlint plugin 추가
1. project 수준의 gradle 에 추가하기
   (커밋 올려놓았습니다.)
2. **File -> Sync Project widh Gradle Files 로 싱크 맞추기!!**
```gradle
plugins {
	id("org.jlleitschuh.gradle.ktlint") version "$최신 버전"
}
```
최신 버전은 아래 링크에서 확인 가능!

https://github.com/JLLeitschuh/ktlint-gradle

## 2. 루트 디렉토리에 .editorconfig 파일 생성
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

## 3. ktlintFormat
코드의 스타일 검사 후 자동으로 틀린 부분 수정해줌
```bash
./gradlew ktlintFormat
```

## 4. addKtlintCheckGitPreCommitHook
pre-commit hook 등록

커밋을 진행할 때 자동으로 ktlintCheck 를 하여 맞지 않는 경우 error 발생시킴
```bash
./gradlew addKtlintCheckGitPreCommitHook
```

## 만약 등록한 hook 을 삭제하고 싶다면
```bash
cd .git/hooks
rm ./pre-commit
```

<br/>

> ## 참고
https://msyu1207.tistory.com/entry/%EA%B9%94%EB%81%94%ED%95%9C-%ED%8F%AC%EB%A7%B7%ED%8C%85%EC%9D%84-%EC%9C%84%ED%95%9C-ktlint-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0-feat-kotlin
