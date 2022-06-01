# myRemote

### About
*myRemote* is an Android application to act as a remote control for your PC (works best on Windows). This application allows the user to connect to their PC device over Wi-Fi, enabling control over the PC’s mouse, keyboard input, quick access to popular media sites such as YouTube & Netflix, and more. The application is split into 2 parts: client and server.

#### How to Use
- Navigate to [Releases](https://github.com/kell90/myRemote/releases) in this repository and download myRemote_Client.apk, and myRemote_Server.jar.
- Install and run the APK on an Android device or emulator (tested and optimized for the Nexus5X, API > 20).
- Run the myRemote_Server.jar file on a PC.
- Upon running the server-side application, the interface will display an IP address, as well as a port number.
![disconnected](https://user-images.githubusercontent.com/44652363/171479819-6135cce0-de49-4d58-b0eb-261488d658b2.png)
- Enter the IP address and port number into the mobile app’s login page and tap "Connect".
![connection_view](https://user-images.githubusercontent.com/44652363/171479855-9e06d99a-611b-4a21-b72a-57525c3e2a73.png)
- Once a connection is established, you may use the mobile phone as a remote to control the desktop's keyboard and mouse. Connection status can be verified by checking the server application interface.
![remote_view](https://user-images.githubusercontent.com/44652363/171480418-ba0ad5b3-e12a-42bb-afca-d6977a2b63e3.png)
![connected 2](https://user-images.githubusercontent.com/44652363/171480427-6276c7dc-2582-428e-b801-10ed23aac563.png)

------------

#### Source Code Links
[Client Source Code](myRemote_Client/app/src/main/java/com/example/li_evoy/myRemote_Client)

[Server Source Code](myRemote_Server/src/app)
