# expenses (Android / Kotlin)

Aplikacja do śledzenia **wydatków** i **przychodów** z kategoriami i prostym zestawieniem. Projekt Android w **Kotlin**, budowany przez **Gradle (Kotlin DSL)**. Wykorzystuje m.in. **Room**, **ViewModel**, **Navigation** oraz **ViewBinding**.

## Stack / technologie
- Kotlin
- AndroidX
- Room (+ KSP)
- Lifecycle ViewModel
- Navigation
- ViewBinding
- Gradle Wrapper (`./gradlew`)

## Wymagania
- **Android Studio** (zalecane)
- **JDK 11** (w projekcie ustawione jest `JavaVersion.VERSION_11`)
- Android SDK z API zgodnym z `compileSdk/targetSdk` ustawionym w projekcie

## Jak odpalić lokalnie (Android Studio)
1. Sklonuj repo:
   ```bash
   git clone https://github.com/Jedmajc/expenses.git
   cd expenses
   ```

2. Otwórz w Android Studio: **File → Open** → wybierz folder `expenses`.

3. Poczekaj na **Gradle Sync** (lub kliknij “Sync Now”, jeśli IDE poprosi).

4. Wybierz emulator/urządzenie i kliknij **Run**.

## Jak budować z terminala
> Projekt ma Gradle Wrapper, więc nie musisz instalować Gradle ręcznie.

- Build debug APK:
  ```bash
  ./gradlew assembleDebug
  ```

- Uruchom testy jednostkowe:
  ```bash
  ./gradlew test
  ```

- Uruchom testy instrumentacyjne (wymaga uruchomionego emulatora/urządzenia):
  ```bash
  ./gradlew connectedAndroidTest
  ```

Artefakty APK znajdziesz zwykle w:
`app/build/outputs/apk/`

## Struktura projektu (skrót)
- `app/` – moduł aplikacji
- `app/src/main/` – kod aplikacji, zasoby, manifest
- `app/src/test/` – testy jednostkowe
- `app/src/androidTest/` – testy instrumentacyjne

## Funkcjonalność aplikacji (z perspektywy dev)
Aplikacja składa się z trzech głównych ekranów:

### 1) Home / Dodawanie rekordu
Użytkownik dodaje wpis:
- **kwota**
- typ: **wydatek** lub **przychód**
- **kategoria** z listy
  - dodawanie nowej kategorii przez przycisk **„+”** obok listy
  - usuwanie kategorii przez przycisk z ikoną **łuku** w prawym górnym rogu
- opcjonalny **opis**
- przycisk **Dodaj** zapisuje rekord

### 2) Historia
Lista ostatnio dodanych rekordów:
- możliwość usunięcia wpisu (ikona **kosza**)
- możliwość podejrzenia/filtrowania wpisów (**wydatków/przychodów**) dla **konkretnej kategorii** wybranej z listy

### 3) Zestawienie
Podsumowanie kwot:
- wydatki/przychody agregowane wg kategorii
- sortowanie kategorii od **największej** do **najmniejszej** kwoty

## Troubleshooting
### Problem: Gradle Sync / brak SDK
- Upewnij się, że masz zainstalowane odpowiednie Android SDK (API zgodne z `compileSdk/targetSdk`).
- Jeśli Android Studio proponuje instalację brakujących komponentów SDK — zaakceptuj.

### Problem: złe JDK
- W Android Studio: **Settings/Preferences → Build, Execution, Deployment → Gradle → Gradle JDK**
- Ustaw JDK zgodne z wymaganiami (domyślnie: 11).

## Kontrybucje
Repo jest głównie do użytku własnego, ale PR-y/uwagi mile widziane.
