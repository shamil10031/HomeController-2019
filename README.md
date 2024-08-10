# HomeController-2019
Smart Home App

Description:

A prototype app designed to interact with smart home devices, offering basic functionality to send requests for turning devices on or off.

The core feature of the app is face recognition. This feature allows a face to be registered on the server and then used to unlock a smart door lock. The app operates via a UDP socket connection. During face recognition, frames from the camera are sent to the server through the socket connection. Upon successful identification, the server on a Raspberry Pi unlocks the prototype door lock. The functionality to turn smart devices on and off is demonstrated by controlling the LED indicators on the board (the goal was to show a working prototype of the project without using actual smart devices).

<img src="https://user-images.githubusercontent.com/17685189/147873233-fed36ee2-7f30-4fc4-aadf-2f54f0c503b3.png" width="300" />   <img src="https://user-images.githubusercontent.com/17685189/147873241-00074ade-5299-4a8b-9db8-0a08ef937192.png" width="300" />

<img src="https://user-images.githubusercontent.com/17685189/147873253-b8f5aee6-75fa-4a8a-aa63-5e6c66d51c4f.png" width="300" />   <img src="https://user-images.githubusercontent.com/17685189/147873269-fb07f493-db09-49d3-962d-b6ee6b077a84.png" width="300" />
