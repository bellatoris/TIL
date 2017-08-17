# npm

node.js 에서의 package 관리자 이다.

```sh
$ npm install express --save
```

--save 옵션을 통해 설치된 Node 모듈은 `package.json` 파일 내의 
`dependencies` 목록에 추가된다. 이후 `app` 디렉토리에서 
`npm install` 을 실행하면 종속 항목 목록 내의 모듈이 자동으로 설치된다.
