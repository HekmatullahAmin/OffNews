# OffNews

OffNews is an Android news application designed with an offline-first approach. It allows users to browse and read news articles seamlessly, even without an active internet connection. By caching articles when a network is available, OffNews ensures that users can stay informed at all times, syncing newly fetched articles with bookmarked ones once a connection is restored.

This project was built using modern Android development techniques, applying the skills and best practices learned from the "Modern Android App Architecture" course offered by Google's Android development experts.

## Features

- **Offline-First Design**: Articles are cached locally, allowing uninterrupted access even when the internet is unavailable.
- **Automatic Syncing**: When new articles are fetched, any previously bookmarked articles are merged, so users always see the most up-to-date bookmarks, whether online or offline.
- **Adaptive Layouts**: The UI adapts to different screen sizes, offering an optimized experience across compact, medium, and expanded displays (like phones, tablets, and foldables).
- **Article Bookmarking**: Bookmark your favorite articles for later reading.
- **Seamless Online and Offline Experience**: When internet is restored, the app automatically updates the cached data with newly fetched articles, while maintaining user bookmarks and preferences.

## Technologies Used

- **Kotlin**: Modern programming language for building Android applications.
- **Jetpack Compose**: Used to design and implement a highly responsive and modern UI.
- **Room**: For local storage, caching news articles so they are available offline.
- **Retrofit**: For making network requests to fetch articles from the NewsAPI.
- **Dagger Hilt**: Manages dependency injection, promoting a clean and modular architecture.
- **Clean Architecture**: Applied different layers for maintainability and scalability:
    - **Data Layer**: Manages API calls and Room database operations.
    - **Domain Layer**: Encapsulates business logic that may be complex or reused by multiple ViewModels. It improves the testability of the app by isolating the business rules from the UI and data handling.
    - **UI Layer**: Renders the user interface and interacts with data through ViewModels.
- **Adaptive Layouts**: Designed to provide a responsive and flexible user interface across different screen sizes (Compact, Medium, Expanded).
- **NewsAPI**: Retrieves the latest news articles from various sources.

## Offline-First Design: What It Means

The offline-first approach ensures that the app remains fully functional even when no internet connection is available. This feature is powered by caching fetched articles in a local Room database. When a user is offline, they can still access all cached articles, bookmarks, and previously fetched content. Once the connection is restored, the app automatically syncs new content with bookmarks and updates the database.

This design philosophy enhances user experience by removing the dependency on internet connectivity, making OffNews usable at any time, regardless of network conditions.

## Security and API Key Management

OffNews uses the [NewsAPI](https://newsapi.org/) to fetch articles. For security reasons, the API key is stored in a file that is not included in version control (.gitignore). If you wish to use the app or contribute, follow these steps:

1. Create a Constants.kt file in the util package.
2. Inside the object Constants block, create a constant API_KEY and assign your own key.

```yaml
object Constants {
    const val API_KEY = "your-api-key-here"
}
```
3. Use this key in the NewsApiService.kt file to make requests to the NewsAPI.

## Contributing

We welcome contributions to enhance the functionality of OffNews! Whether it's fixing a bug, adding a new feature, or improving documentation, your help is appreciated.

To contribute:

1. Fork the repository.
2. Create a new branch (git checkout -b feature-YourFeature).
3. Make your changes and commit them (git commit -am 'Add YourFeature').
4. Push the branch to your fork (git push origin feature-YourFeature).
5. Create a Pull Request.
Feel free to open issues if you encounter any problems or have suggestions!

## Documentation
The codebase is documented using KDoc, making it easier to understand the purpose and function of each class, method, and property. New contributors are encouraged to review the KDoc comments to get familiar with the structure of the project.

## Video Demo

https://github.com/user-attachments/assets/8608571c-fb11-493c-ba46-5374c56a5225

