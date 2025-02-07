# TheNewsApp 📰

A modern Android news application that delivers real-time news updates using NewsAPI. Built with Kotlin, XML, and MVVM architecture, leveraging modern Android development tools.

## 🎥 Demo & Screenshots
[Watch Demo on YouTube](https://youtube.com/shorts/hflZDc41jc0?feature=share)

![Image](https://github.com/user-attachments/assets/32cc749c-4c7d-45f8-a808-01bebe16708c)

## ✨ Features
- Browse headlines by country
- Search news articles
- Save favorite articles
- Offline support
- Pagination support
- Clean and modern UI
- MVVM Architecture

## 🛠 Tech Stack
- Kotlin
- XML
- MVVM Architecture
- Coroutines for asynchronous operations
- Room Database for offline caching
- Retrofit for API calls
- Navigation Component
- View Binding
- LiveData
- DiffUtil & RecyclerView
- Glide for image loading

## 📝 API Reference
This app uses the [NewsAPI](https://newsapi.org/) for fetching news data. To run the app, you'll need to:

1. Get an API key from [NewsAPI](https://newsapi.org/register)
2. Create a `local.properties` file in the root directory
3. Add your API key: NEWS_API_KEY=your_api_key_here

## 🚀 Installation
1. Clone the repository
```bash
git clone https://github.com/your-username/TheNewsApp.git
```
2. Open the project in Android Studio

3. Add your NewsAPI key as mentioned above

4. Build and run the application

## 📐 Architecture
The app follows MVVM (Model-View-ViewModel) architecture and Repository pattern:

- **UI Layer**: Activities & Fragments
- **ViewModel Layer**: NewsViewModel
- **Repository Layer**: NewsRepository
- **Data Sources**: 
  - Remote: Retrofit for API calls
  - Local: Room Database

## 🤝 Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 🙏 Acknowledgments
- [NewsAPI](https://newsapi.org/) for providing the news data
- [Retrofit](https://square.github.io/retrofit/)
- [Glide](https://github.com/bumptech/glide)
