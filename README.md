# AndroidTV-WebAPP
Is an app for all current AndroidTV versions e.g. MI TVs or FireTV sticks.
## Requirements
- <a href="https://developer.android.com/studio">Android Studio</a>
## URL Change
Go to `java>systems>kiznaiver>webapp>MainActivity.java` <br>
and change the "&#60;Change me&#62;" to your URL so that it looks like this:
```java
12
13    public static final String EXTRA_URL = "https://github.com/Kiznaiver-Systems/AndroidTV-WebAPP/";
14    WebView myWebview;
```
## URL APP Name
Go to `res>values>strings.xml` <br>
and change the "Change ME" to your URL so that it looks like this:
```java
1    <resources>
2        <string name="app_name">Cool Web APP</string>
```
## Build APP
Once you have changed the URL you can build the app via the header `Build>Build Bundle(s)/APK(s)>APK(s)` and thus create the APK<br>
For ADB:<br>
If you want to start the APP via ADB you have to execute 
```bash
adb shell am start systems.kiznaiver.webappp/systems.kiznaiver.webappp.MainActivity
```
in the adb shell

## Images
<details>
<summary><b>Change the URL</b></summary>
<img src="https://github.com/Kiznaiver-Systems/AndroidTV-WebAPP/raw/main/.img/Change_URL.png" title="Change URL">
</details>
<details>
<summary><b>Change the Name</b></summary>
<img src="https://github.com/Kiznaiver-Systems/AndroidTV-WebAPP/raw/main/.img/Change_name.png" title="Change Name">
</details>
<details>
<summary><b>Build the APP</b></summary>
<img src="https://github.com/Kiznaiver-Systems/AndroidTV-WebAPP/raw/main/.img/Build_APP.png" title="Build APP">
</details>
<details>
<summary><b>Screenshots</b></summary>
<br>
<details>
  <summary>APP Install</summary>
  <img src="https://github.com/Kiznaiver-Systems/AndroidTV-WebAPP/raw/main/.img/app_install.png" title="App Install">
  </details>
  <details>
  <summary>APP in Appselector</summary>
  <img src="https://github.com/Kiznaiver-Systems/AndroidTV-WebAPP/raw/main/.img/app_show.png" title="App drawer">
  </details>
  <details>
  <summary>Screenshots</summary>
  <img src="https://github.com/Kiznaiver-Systems/AndroidTV-WebAPP/raw/main/.img/app_inside.png" title="Inside the APP">
  </details>
</details>
