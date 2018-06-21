** THIS REPO HAS BEEN ARCHIVED AND IS NO LONGER BEING ACTIVELY MAINTAINED **

# VRDeepLinkSDK
This SDK provides code samples on how to construct a URI intent to view 360
immersive contents that are hosted on Facebook, either in Oculus Video/360Photo
app if installed or the Facebook for Android application.

To begin, you will pass in the media FBId of the video/photo content to construct
the parameter object. You can find the fbid of the content by inspecting the url
of the content. For example for video objects, the fbid is the string of numbers
(10153779045038951) at the end of the url -
```
  $ https://www.facebook.com/natgeo/videos/10153779045038951/
```

For 360 photos, it's the value of the fbid param (1195577430501137)
```
  $ https://www.facebook.com/teamcoco/photos/a.1195578577167689.1073741827.108905269168364/1195577430501137
```
Visit:
* https://www.facebook.com/Facebook360 - To see the latest trending 360 Videos on Facebook
* https://360video.fb.com - To learn about creating 360 Videos for Facebook

# Requirements
Requires Android OS version KitKat (4.4) or higher.
To build you'll need Gradle and the Android SDK with build tools 23.0.3 or above.

# Setup
```
  $ git clone git@github.com:fbsamples/VRDeepLinkSDK.git
  $ cd VRDeepLinkSDK/VRDeeplinkSample   
  $ gradle installDebug
  $ adb shell am start -n com.facebook.vrdeeplinksdk.sample/.VRDeeplinkSampleActivity
```
