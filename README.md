# Instagram-Helper   [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Instagram--Helper-brightgreen.svg?style=flat-square)](http://android-arsenal.com/details/1/2753)

This is a library project which makes the instagram login much easier. *Works from Android API 10 (Gingerbread) and above*.

![sample](https://github.com/TheLester/Instagram-Helper/blob/master/presentation/login.gif)

Implemented with Instagram API client-Side (Implicit) authentication, which doesn't require store CLIENT SECRET on the client, [more info...](https://instagram.com/developer/authentication/) 
# Gradle

Add it to your app module build gradle.
```
dependencies {
      compile  'com.github.thelester:Instagram-Helper:1.1.1'
}
```
Usage
======
Initalize instagram-helper with your redirect_url and client_id (obtained [here] (https://instagram.com/developer/clients/manage/))
```java    
String scope = "basic+public_content+follower_list+comments+relationships+likes";
//scope is for the permissions
InstagramHelper instagramHelper = new InstagramHelper.Builder()
                                      .withClientId(CLIENT_ID)
                                      .withRedirectUrl(REDIRECT_URL)
                                      .withScope(scope)
                                      .build();
 ```
then launch Login activity from your activity:
```
instagramHelper.loginFromActivity(yourActivity);
```
and handle result:
```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == InstagramHelperConstants.INSTA_LOGIN && resultCode == RESULT_OK) {
            InstagramUser user = instagramHelper.getInstagramUser(this);
            Picasso.with(this).load(user.getData().getProfilePicture()).into(userPhoto);
            userTextInfo.setText(user.getData().getUsername() + "\n"
                            + user.getData().getFullName() + "\n"
                            + user.getData().getWebsite()
            );

        } else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
        }
````
# Dependencies
 * [Gson 2.4](https://github.com/google/gson)
 * [Materialish progress 1.7](https://github.com/pnikosis/materialish-progress)


License
-------
    Copyright 2016 Dogar Dmitry

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
