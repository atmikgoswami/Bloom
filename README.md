
<h1 align="center"> BLOOM   </h1>
<p align="center"> <img alt="API" src="https://img.shields.io/badge/Api%2024+-50f270?logo=android&logoColor=black&style=for-the-badge"/>
  <img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-a503fc?logo=kotlin&logoColor=white&style=for-the-badge"/>
  <img alt="Jetpack Compose" src="https://img.shields.io/static/v1?style=for-the-badge&message=Jetpack+Compose&color=4285F4&logo=Jetpack+Compose&logoColor=FFFFFF&label="/>
</p>

<p id="description">Bloom is a mental wellness and support app designed to provide comprehensive mental health resources and support to users. Our goal is to create a holistic platform that promotes mental well-being and fosters a supportive environment for all users.</p>

<h2>🧐 Features</h2>

Here're some of the project's best features:

*   Secure Google sign in to ensure privacy and safety.
*   Users can maintain personalized journal entries to track their thoughts and progress over time.
*   The app also includes public chatrooms where users can engage in supportive conversations, discuss and share their situations with the broader community, anonymously.
*   Includes Hate speech filtering using BERT classifier to prevent social abuse in a mental wellness app
*   A personalized chatbot Ember to offer immediate assistance and advice...a buddy who is always there for you
*   For professional support the app facilitates connections with certified doctors ensuring users have access to expert help when needed.

<h2>Project Screenshots:</h2>
<p align="start">

<img src="https://res.cloudinary.com/ddgeelsit/image/upload/v1717952054/bloom_screenshots/IMG-20240609-WA0029_dqlqkp.jpg" alt="project-screenshot" width="160px" >

<img src="https://res.cloudinary.com/ddgeelsit/image/upload/v1717952055/bloom_screenshots/IMG-20240609-WA0030_nf9r3m.jpg" alt="project-screenshot" width="160px" >

<img src="https://res.cloudinary.com/ddgeelsit/image/upload/v1717952055/bloom_screenshots/IMG-20240609-WA0031_ncoeer.jpg" alt="project-screenshot" width="160px" >

<img src="https://res.cloudinary.com/ddgeelsit/image/upload/v1717952055/bloom_screenshots/IMG-20240609-WA0032_tg9oqc.jpg" alt="project-screenshot" width="160px" >

<img src="https://res.cloudinary.com/ddgeelsit/image/upload/v1717952055/bloom_screenshots/IMG-20240609-WA0033_puyf5g.jpg" alt="project-screenshot" width="160px" >

<img src="https://res.cloudinary.com/ddgeelsit/image/upload/v1717952055/bloom_screenshots/IMG-20240609-WA0034_hln1ac.jpg" alt="project-screenshot" width="160px" >

<img src="https://res.cloudinary.com/ddgeelsit/image/upload/v1717952055/bloom_screenshots/IMG-20240609-WA0035_t2uvn3.jpg" alt="project-screenshot" width="160px" >

<img src="https://res.cloudinary.com/ddgeelsit/image/upload/v1717952056/bloom_screenshots/IMG-20240609-WA0036_wxxl76.jpg" alt="project-screenshot" width="160px" >

<img src="https://res.cloudinary.com/ddgeelsit/image/upload/v1717952056/bloom_screenshots/IMG-20240609-WA0037_auglfj.jpg" alt="project-screenshot" width="160px" >

<img src="https://res.cloudinary.com/ddgeelsit/image/upload/v1717952056/bloom_screenshots/IMG-20240609-WA0038_mh8vbw.jpg" alt="project-screenshot" width="160px" >

<img src="https://res.cloudinary.com/ddgeelsit/image/upload/v1717952055/bloom_screenshots/IMG-20240609-WA0039_cjxle7.jpg" alt="project-screenshot" width="160px" >

<img src="https://res.cloudinary.com/ddgeelsit/image/upload/v1717952056/bloom_screenshots/IMG-20240609-WA0040_fwh8e2.jpg" alt="project-screenshot" width="160px" >

<img src="https://res.cloudinary.com/ddgeelsit/image/upload/v1717952054/bloom_screenshots/IMG-20240609-WA0041_zibd2r.jpg" alt="project-screenshot" width="160px" >

<img src="https://res.cloudinary.com/ddgeelsit/image/upload/v1717952054/bloom_screenshots/IMG-20240609-WA0042_slis3s.jpg" alt="project-screenshot" width="160px" >
</p>

## Building 🏗️

1. Clone the project
2. Open Android Studio IDE
3. Go to File » New » Project from VCS
4. Paste ``` https://github.com/atmikgoswami/Bloom.git ```
5. Open the project
6. Create a Firebase project and add the ```google-services.json``` in the app directory of the project
7. Grab your ```YOUR_GEMINI_API_KEY``` from [https://api-ninjas.com](https://aistudio.google.com/app/apikey)
8. Now, in your local.properties add the block
``` 
  GEMINI_API_KEY=YOUR_GEMINI_API_KEY
  GEMINI_MODEL_NAME_1=gemini-pro
  GEMINI_MODEL_NAME_2=gemini-pro-vision
```
9. Build and run
  
<h2>💻 Built with</h2>

Technologies used in the project:

- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous calls and tasks to utilize threads.
- [Jetpack Compose UI Toolkit](https://developer.android.com/jetpack/compose) - Modern UI development toolkit.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes.
  - [StateFlow and SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow#:~:text=StateFlow%20is%20a%20state%2Dholder,property%20of%20the%20MutableStateFlow%20class.) - StateFlow and SharedFlow are Flow APIs that enable flows to optimally emit state updates and emit values to multiple consumers.
- [Dependency Injection](https://developer.android.com/training/dependency-injection) -
    - [Dagger-Hilt](https://dagger.dev/hilt/) - A standard way to incorporate Koin dependency injection into an Android application.
    - [Hilt-ViewModel](https://dagger.dev/hilt/view-model) - DI for injecting ```ViewModel```. 
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.
- [Firebase Authentication and Firestore Database](https://firebase.google.com/) - Comprehensive platform for developing mobile and web apps, offering services like authentication and cloud storage. Firestore is its flexible, scalable NoSQL database designed for real-time data synchronization and seamless offline support
- [Google Gemini Pro Model](https://deepmind.google/technologies/gemini/pro/) - Google's most capable and general model, built to be multimodal and optimized for three different sizes: Ultra, Pro and Nano.

# Architecture 👷‍♂️
This app uses [MVVM(Model View View-Model)](https://developer.android.com/topic/architecture#recommended-app-arch)  and Clean architecture.

## Contributing 🤝

Contributions are always welcome. Feel free to make a pull request!

Highly appreciate leaving a :star: if you liked it!
