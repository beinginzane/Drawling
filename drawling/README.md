# Drawling 🎨💌
> Draw together, darling.

A real-time collaborative drawing app for couples.

## Tech Stack
- **Android**: Kotlin + Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Real-time**: Socket.io
- **Auth**: Firebase Phone Auth + JWT
- **Network**: Retrofit + OkHttp
- **Image**: Coil
- **Backend**: Node.js + TypeScript (separate repo)

## Setup

### 1. Firebase
1. Go to [Firebase Console](https://console.firebase.google.com)
2. Create project → Add Android app → Package: `com.drawling.app`
3. Download `google-services.json` → place in `/app/`
4. Enable **Phone Authentication** in Firebase Console → Authentication → Sign-in method

### 2. Backend URL
In `app/build.gradle.kts`, update:
```kotlin
buildConfigField("String", "BASE_URL", "\"https://YOUR_BACKEND_URL/\"")
buildConfigField("String", "SOCKET_URL", "\"https://YOUR_BACKEND_URL\"")
```
For local emulator testing use: `http://10.0.2.2:3000`

### 3. Build
```bash
./gradlew assembleDebug
```

## Project Structure
```
app/src/main/java/com/drawling/app/
├── auth/           → OTP + JWT auth
├── canvas/         → Drawing canvas + real-time sync
├── rooms/          → Couple & personal rooms
├── gallery/        → Saved drawings grid
├── surprises/      → Sealed envelope delivery
├── network/        → Retrofit + Socket.io + FCM
├── di/             → Hilt modules
├── navigation/     → NavGraph
├── ui/theme/       → Colors, typography, theme
└── utils/          → Resource sealed class
```

## TODOs before first build
- [ ] Add `google-services.json` to `/app/`
- [ ] Set real `BASE_URL` and `SOCKET_URL` in `build.gradle.kts`
- [ ] Replace launcher icons in `/res/mipmap-*/`
- [ ] Pass JWT token properly in `CanvasScreen` (search TODO_JWT_TOKEN)
- [ ] Send FCM token to backend in `DrawlingFirebaseMessagingService`

## Features
- 📱 Phone OTP login via Firebase
- 🏠 Couple room (real-time drawing sync)
- 🎨 Personal room (private canvas gallery)
- ✏️ Brush, eraser, color picker, size slider, undo
- 💾 Auto-save every 5 minutes + on exit
- 🖼️ Gallery with soft delete
- 🎁 Surprise delivery with envelope reveal animation
- 🔔 FCM push notifications
- 🔗 Deep link invite codes
