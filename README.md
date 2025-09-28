# 🚇 Metro Station Alert

> 지하철 하차 알림 앱 - 목적지 역 근처에 도착하면 자동으로 알림을 받아보세요!

[![Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)
[![API](https://img.shields.io/badge/API-24%2B-orange.svg)](https://android-arsenal.com/api?level=24)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20Architecture-blue.svg)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

## 📱 앱 소개

Metro Station Alert는 **위치 기반 지하철 하차 알림 서비스**를 제공하는 Android 앱입니다.
사용자가 설정한 목적지 역에 가까워지면 자동으로 알림을 보내주어, 지하철 이용 시 깜빡하고 내리지 못하는 상황을 방지합니다.

### ✨ 주요 기능

| 기능 | 설명 |
|------|------|
| 🔍 **역 검색 및 관리** | 전국 지하철역 검색, 노선별 필터링, 알림 역 설정 |
| 📍 **위치 기반 알림** | GPS를 이용한 실시간 위치 추적 및 근접 알림 |
| ⭐ **즐겨찾기** | 자주 이용하는 역 북마크 및 실시간 도착 정보 |
| 🔔 **맞춤 알림** | 알림 거리, 제목, 내용 사용자 정의 |
| 🚇 **실시간 정보** | 서울 지하철 실시간 도착 정보 제공 |

## 🏗️ 아키텍처

### Clean Architecture + MVVM 패턴
```
┌─────────────────────┐
│   Presentation      │  ← feature-* (Compose + ViewModel)
├─────────────────────┤
│     Domain          │  ← UseCase + Repository Interface
├─────────────────────┤
│      Data           │  ← Repository Impl + DataSource
└─────────────────────┘
```

### 모듈 구조
![Screenshot 2025-09-25 at 22 19 26](https://github.com/user-attachments/assets/11a85d0b-3b03-4b3b-a4e8-b5cc66c05a29)

| 모듈 | 역할 |
|------|------|
| `app` | 메인 애플리케이션, 의존성 주입 설정 |
| `domain` | 비즈니스 로직, UseCase, 도메인 모델 |
| `data` | 데이터 처리, Repository 구현, API 연동 |
| `feature-search` | 역 검색 및 알림 설정 화면 |
| `feature-bookmark` | 즐겨찾기 및 실시간 도착 정보 |
| `feature-settings` | 사용자 설정 관리 |
| `common-ui` | 공통 UI 컴포넌트 |

## 🛠️ 기술 스택

### UI & Architecture
- **Jetpack Compose** - 모던 UI 프레임워크
- **Material3** - 구글 디자인 시스템
- **MVVM** - 아키텍처 패턴
- **Clean Architecture** - 레이어 분리

### Dependency Injection & Navigation
- **Hilt** - 의존성 주입
- **Navigation Compose** - 화면 내비게이션

### Asynchronous & Data
- **Kotlin Coroutines** - 비동기 처리
- **Flow** - 반응형 데이터 스트림
- **DataStore** - 사용자 설정 저장
- **Room** (부분적) - 로컬 데이터베이스

### Network & Location
- **Retrofit** - HTTP 클라이언트
- **Moshi** - JSON 파싱
- **Google Play Services** - 위치 서비스

### Testing
- **JUnit5** - 단위 테스트
- **MockK** - 모킹 프레임워크
- **Turbine** - Flow 테스트
- **Compose Test** - UI 테스트

## 🚀 주요 특징

### 📍 스마트 위치 알림
- **백그라운드 위치 추적**: Foreground Service를 통한 지속적인 위치 모니터링
- **거리 기반 알림**: 0.5km ~ 5.0km 범위에서 사용자 정의 알림 거리 설정
- **중복 알림 방지**: 알림 후 자동 비활성화, 일정 거리 이동 시 재활성화

### 🔍 고급 검색 기능
- **전체 지하철역 데이터베이스**: 수도권 및 전국 지하철역 정보
- **실시간 검색**: 역명 기반 즉시 검색 및 필터링
- **노선별 필터**: 드롭다운을 통한 특정 노선 역 필터링

### ⭐ 즐겨찾기 시스템
- **북마크 관리**: 자주 이용하는 역 저장 및 관리
- **실시간 도착 정보**: 서울 지하철 Open API 연동
- **양방향 정보**: 상행/하행 방향별 도착 시간 제공

### ⚙️ 맞춤 설정
- **알림 거리 조절**: 슬라이더를 통한 직관적인 거리 설정
- **알림 메시지 커스터마이징**: 제목 및 내용 개인화
- **설정 동기화**: DataStore를 통한 설정 영구 저장

## 📋 권한 요구사항

| 권한 | 용도 |
|------|------|
| `ACCESS_FINE_LOCATION` | 정확한 위치 추적 |
| `ACCESS_COARSE_LOCATION` | 대략적인 위치 정보 |
| `POST_NOTIFICATIONS` | 알림 표시 |
| `FOREGROUND_SERVICE` | 백그라운드 위치 서비스 |
| `INTERNET` | 실시간 지하철 정보 |

## 🔧 설치 및 실행

### 요구사항
- Android Studio Hedgehog | 2023.1.1+
- Android API 24 (Android 7.0)+
- Kotlin 1.9.0+

### 빌드 방법
```bash
# 저장소 클론
git clone https://github.com/yourusername/MetroStationAlert2.git

# 프로젝트 디렉토리로 이동
cd MetroStationAlert2

# Gradle 빌드
./gradlew assembleDebug

# 테스트 실행
./gradlew test
```

## 🧪 테스트

```bash
# 단위 테스트 실행
./gradlew test

# UI 테스트 실행
./gradlew connectedAndroidTest

# 테스트 커버리지 생성
./gradlew jacocoTestReport
```

## 📊 프로젝트 상태

- ✅ **완전한 테스트 커버리지**: 모든 UseCase 및 ViewModel 테스트
- ✅ **Clean Architecture**: 명확한 레이어 분리
- ✅ **모듈화**: 기능별 독립적인 모듈 구조
- ✅ **현대적 기술 스택**: 최신 Android 기술 적용

## 화면
**검색**
<img src="https://github.com/user-attachments/assets/96979eaa-4a63-449c-b814-c4168c8e962e" width=200>
<img src="https://github.com/user-attachments/assets/d6c43fbc-5370-47a7-9604-ecb295d94c23" width=200>
<img src="https://github.com/user-attachments/assets/64374dd9-fd8b-49f9-98df-005797887187" width=200>
<img src="https://github.com/user-attachments/assets/c1aa4d13-4946-45d2-9f01-1146458de4ca" width=200>
<img src="https://github.com/user-attachments/assets/db96b339-624f-44a3-a8b5-92b33921dcfb" width=200>
<img src="https://github.com/user-attachments/assets/28f74618-0837-4402-b89e-a1b42fee57f5" width=200>
<img src="https://github.com/user-attachments/assets/8371f726-b4a4-4391-8248-7085b88a7f13" width=200>
<img src="https://github.com/user-attachments/assets/e2ecf1ef-d017-462f-8366-012600982ede" width=200>


**즐겨찾기**
<img src="https://github.com/user-attachments/assets/8eb234a9-4bf7-4f80-949d-ea88ddae32cf" width=200>
**설정**
<img src="https://github.com/user-attachments/assets/5b17188a-99a8-40ef-8fff-b799c7b34a55" width=200>
