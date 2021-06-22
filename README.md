# READY
날짜와 날씨 API를 활용한 시계열 예측 머신러닝 기반 수요 예측 재고관리 앱(안드로이드)
<br>
<br><img width="300" alt="function1" src="https://user-images.githubusercontent.com/38302837/122964278-0a02bd80-d3c2-11eb-9e16-0d2ead2322e5.png">


# Information
## 기능 소개
- 매출 예측 보기(오늘, 내일)
<br><img width="150" alt="function1" src="https://user-images.githubusercontent.com/38307839/122728638-4d223b00-d2b3-11eb-8c95-3b131f7fa92f.gif"> <img width="150" alt="function1" src="https://user-images.githubusercontent.com/38307839/122729008-a8ecc400-d2b3-11eb-9f8b-f1e9b0656c78.gif">
<br>매출 한달 치 데이터를 서버로 전송하여 오늘, 내일 매출 예측량을 계산하여 보여줍니다.<br>
기상청 API 정보와 날짜, 수량 데이터를 바탕으로 LSTM 모델 학습으로 오늘, 내일 예측 데이터를 산출합니다.

- 메뉴 / 매출 데이터 관리
<br><img width="150" alt="function1" src="https://user-images.githubusercontent.com/38307839/122727918-84dcb300-d2b2-11eb-8531-eb573a12c733.gif">
<br>SQLite를 사용하여 메뉴 추가, 수정, 삭제 구현. 매출은 추가만 가능하며 메뉴를 삭제하면 해당되는 매출 데이터도 함께 삭제.<br>

- 매출 통계
<br><img width="150" alt="function1" src="https://user-images.githubusercontent.com/38307839/122728370-09c7cc80-d2b3-11eb-8ab4-6597b0134663.gif">
<br>내부 DB 데이터와 연동하여 메뉴별, 기간별(일주일, 한달), 요일별, 날씨별로 매출 통계를 볼 수 있습니다. 전체 매출 통계는 line chart 형식으로 요일별, 날씨별은 pie chart 형태로 보여지게 됩니다.<br>

# Structure

<img width="500" alt="function1" src="https://user-images.githubusercontent.com/38307839/122729584-48aa5200-d2b4-11eb-9b91-fff96f9165f0.png">


# Android Build Caution
1) You should open 'Ready' directory not 'ForecastConsumption'
2) Create and set your own 'local.properties' file on 'Ready' directory

# Contributor
- [전설](https://github.com/redundant4u)
- [한재원](https://github.com/ellynhan)
