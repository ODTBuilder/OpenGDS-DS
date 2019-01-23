[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Korean](https://img.shields.io/badge/language-Korean-blue.svg)](#korean)


<a name="korean"></a>
OpenGDS/DS (다중 버저닝 관리도구)
=======
 이 프로젝트는 국토공간정보연구사업 중 [공간정보 SW 활용을 위한 오픈소스 가공기술 개발]과제의 연구성과 입니다.<br>
오픈소스 분산 버전 관리 시스템인 Geogig을 사용하여 개발한 공간자료 편집 이력 관리 및 협업 지원 분산 버전 관리 시스템(Distributed Version Control System)이며 OpenGDS/Builder(공간자료 편집도구)와 연동하여 공간데이터 편집 및 버전관리가 가능합니다.<br>  


감사합니다.
공간정보기술(주) 연구소 <link>http://www.git.co.kr/
OpenGeoDT 팀

특징
=====
- OpenGDS/DS는 Java 기반의 Library로 Geogig 시스템에 접근하기 위한 REST Client Library 입니다. 
- Geogig은 Geoserver 내에 Plug in 형태로 설치한 후 RESTAPI로 접근합니다. 
- 모든 공간자료는 Geogig을 통해 PostgreSQL 저장소에 버전별로 저장됩니다. 
- OpenGDS/DS는 공간자료 Import 및 삭제 기능을 제외한 편집 기능은 제공하지 않으므로 OpenGDS/Builder(공간자료 편집도구)를 연동하여 사용하는 것을 권장합니다. 

연구기관
=====
세부 책임 : 부산대학교 <link>http://www.pusan.ac.kr/

연구 책임 : 국토연구원 <link>http://www.krihs.re.kr/

Getting Started
=====
### 1. 환경 ###
- Java – OpenJDK 1.8.0.111 64 bit
- eclipse neon 
- PostgreSQL 9.4 
- Geoserver 2.13.2
- geogig-1.2.1

### 2. Geoserver 설치 및 Geogig Plug in 설정 ###
- http://geoserver.org/ 접속 후 Geoserver 2.13.2 Windows Installer 다운로드 <br> 
** jdk 1.8 버전 이상 사용 시 Geoserver 2.8 버전 이상 사용
- Windows Installer 실행 후  C:\Program Files (x86) 경로에 설치
- https://github.com/locationtech/geogig/releases/ 접속하여 geogig-1.2.1-geoserver-2.12-plugin.zip 다운로드 후 C:\Program Files (x86)\GeoServer 2.13.0\webapps\geoserver\WEB-INF\lib 경로에 압축 해제
- 프로젝트 내 guava-23.0.jar 다운로드 후 Geoserver 설치 경로 내 WEB-INF/lib 폴더에 위치
- C:\Program Files (x86)\GeoServer 2.13.2\bin 경로의 startup.bat 실행

### 3. PostgreSQL 설치 및 설정 ###
- http://www.postgresql.org/download/ 접속 후 PostgreSQL 다운로드 및 설치
- pgAdmin 실행 후 Databases 생성 후 New Database 클릭 
- "geogig" Name으로 입력 후 Database 생성 

### 4. Clone or Download ###
- https://github.com/ODTBuilder/OpenGDS-DS 접속 후 Git 또는 SVN을 통해 Clone 하거나 zip 파일 형태로 소스코드 다운로드 

### 5. 소스코드 설치 및 프로젝트 실행 ###
- eclipse 실행 후 Project Import

### 6. Test 코드 작성 ###
- src/test/com/git/gdsbuilder/VersionControlTest.java 클래스 생성
- Creating a GeoGig repository backed by PostgreSQL
<pre><code> // Geogig Repository Information
String baseURL = "http://localhost:9999/geoserver";
String username = "admin";
String password = "geoserver";
String repository = "github";
String dbHost = "localhost";
String dbPort = "5432";
String dbName = "geogig";
String dbSchema = "public";
String dbUser = "postgres";
String dbPassword = "postgis";
String authorName = "github";
String authorEmail = "github@git.co.kr";

// create GeoGig repository
InitRepository init = new InitRepository();
init.executeCommand(baseURL, username, password, repository, dbHost, dbPort, dbName, dbSchema, dbUser,
				dbPassword, authorName, authorEmail);</code></pre>
- Import PostGIS Table
<pre><code>BeginTransaction beginTransaction = new BeginTransaction();
GeogigTransaction transaction = beginTransaction.executeCommand(baseURL, username, password, repository);
String transactionId = transaction.getTransaction().getId();

// PostGIS Table Information
String fidAttrib = "gid";
String table = "gis_osm_transport";

PostGISImport postgisImport = new PostGISImport();
postgisImport.executeCommand(baseURL, username, password, repository, transactionId, fidAttrib, table, dbHost, dbPort, dbSchema, dbName, dbUser, dbPassword);

AddRepository addRepos = new AddRepository();
addRepos.executeCommand(baseURL, username, password, repository, transactionId);

CommitRepository commitRepos = new CommitRepository();
commitRepos.executeCommand(baseURL, username, password, repository, transactionId, "github test", authorName, authorEmail);

EndTransaction endTransaction = new EndTransaction();
endTransaction.executeCommand(baseURL, username, password, repository, transactionId);</code></pre>

- List Repository Tree
<pre><code> // List All Layers In Geogig Repository
LsTreeRepository lsTree = new LsTreeRepository();
GeogigRevisionTree geogigTree = lsTree.executeCommand(baseURL, username, dbPassword, repository, null, false);</code></pre>

- 기타 기능은 개발자 API 참고

사용 라이브러리
=====
1. Geogig 1.2.1 (Eclipse Distribution License (a BSD 3 Clause license)) http://geogig.org/
2. ApachePOI 3.14 (Apache License 2.0) http://poi.apache.org
3. ApacheCommons 1.3.3 (Apache License 2.0) commons.apache.org/proper/commons-logging/
4. JACKSON 1.9.7 (Apache License (AL) 2.0, LGPL 2.1)
5. JSON 20160212 (MIT License)
6. JSON.simple 1.1.1 (The Apache Software License, Version 2.0) http://code.google.com/p/json-simple/

Mail
====
Developer : SG.LEE
ghre55@git.co.kr

