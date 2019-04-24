For custom camera:

1. Add required permissions in manifest file

    `
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-feature android:name="android.hardware.camera2" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    `

2. Min sdk has to be 21 for camera2 api


## todo ##
1. after exiting camera should be released
