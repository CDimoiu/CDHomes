# CDHomes

CDHomes is a demo real estate Android application, built entirely in Kotlin and following the MVVM architecture.

It leverages the following technologies:
- Kotlin Coroutines & Flows for asynchronous programming
- Hilt for dependency injection
- Jetpack Compose for UI development
- Room for local persistence
- Retrofit for networking

The app provides a series of real estate listings fetched from a remote source, which are then persisted in a local database through a repository layer. These listings are exposed to the ViewModels via UseCases and displayed in the UI using state flows.

Features:
- Swipe to refresh – request new listings from the remote source
- Swipe to dismiss – delete a specific listing from local storage
- Filters with sliders – adjust the displayed list according to custom criteria
- Details screen – tap on a listing to view all available information
