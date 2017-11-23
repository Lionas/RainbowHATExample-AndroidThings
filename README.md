# RainbowHATExample
Android Things Example for Raspberry Pi 3

## 概要
このプロジェクトは、[PIMORONI](http://pimoroni.com)が発売している Android things スターターキット
「Raspberry Pi 3 Starter Kit」の機能のデモです。

![Box](./images/IMG_0984.JPG)

## デモの様子
[![デモムービー](http://img.youtube.com/vi/hDvWYshAzTo/0.jpg)](http://www.youtube.com/watch?v=hDvWYshAzTo)

## 実装機能

- 以下の動作チェック機能
  - ボタンA/B/C
  - （ボタン上部の）単色LED
  - ブザー
  - レインボーLED(0～6)
  - 7セグメント
  - 温度
  - 気圧

## 使い方

- 操作がない時に気温と気圧を交互に表示します。
  - StarterKitの気温センサーがRaspberry Pi 3のCPUの真上に来るため、気温は高めに出ます。
  
- ボタンA/B/Cのいずれかを押すことで各種モジュールの動作状況が確認できます。
  
  - ボタンA
    - ボタンを押している間、1600Hzのブザーが鳴ります。
    - レインボーLEDが消灯します。
    - ボタンを押している間、7セグメントにブザーの周波数が表示されます。
  
  - ボタンB
    - ボタンを押している間、800Hzのブザーが鳴ります。
    - レインボーLEDがすべて点灯します。
    - ボタンを押している間、7セグメントにブザーの周波数が表示されます。
  
  - ボタンC
    - ボタンを押している間、100Hzのブザーが鳴ります。
    - レインボーLEDが消灯します。
    - ボタンを押している間、7セグメントにブザーの周波数が表示されます
