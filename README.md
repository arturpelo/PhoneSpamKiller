# PhoneSpamKiller

Aplikacja Android blokująca połączenia przychodzące od numerów nieobecnych w książce telefonicznej. Działa w tle dzięki systemowemu `CallScreeningService` i zapisuje historię blokad w lokalnej bazie danych.

---

## Funkcje

- **Automatyczna blokada połączeń** – każde połączenie od nieznanego numeru (spoza kontaktów) jest odrzucane w czasie rzeczywistym
- **Ochrona działa w tle** – nie wymaga otwartej aplikacji
- **Lokalna baza danych** – historia wszystkich zablokowanych połączeń z numerem i datą/godziną (Room / SQLite)
- **Proste UI** – widok statusu ochrony, lista blokad, licznik zablokowanych połączeń
- **Czyszczenie historii** – jednym przyciskiem usuń całą historię blokad

---


## Wymagania

| Wymaganie | Wartość |
|---|---|
| Minimalna wersja Androida | **8.0 (API 26)** |
| Docelowa wersja Androida | **14 (API 34)** |
| Język | Kotlin |
| Baza danych | Room (SQLite) |

---

## Uprawnienia

| Uprawnienie | Cel |
|---|---|
| `READ_CONTACTS` | Sprawdzenie czy dzwoniący numer jest w kontaktach |
| `BIND_SCREENING_SERVICE` | Umożliwia systemowi powiązanie z `CallScreeningService` |

---

## Jak uruchomić projekt

### Wymagania wstępne
- [Android Studio](https://developer.android.com/studio) (wersja Hedgehog lub nowsza)
- JDK 8+
- Urządzenie lub emulator z Android 8.0+ (zalecane Android 10+ dla pełnej funkcjonalności)

### Kroki

```bash
# 1. Sklonuj lub pobierz repozytorium
git clone https://github.com/twoj-uzytkownik/PhoneSpamKiller.git

# 2. Otwórz projekt w Android Studio
#    File → Open → wskaż folder PhoneSpamKiller

# 3. Zsynchronizuj Gradle (Android Studio zrobi to automatycznie)

# 4. Podłącz urządzenie lub uruchom emulator

# 5. Uruchom aplikację
#    Run → Run 'app'
```

---

## Pierwsze uruchomienie

1. Uruchom aplikację i naciśnij przycisk **"Włącz ochronę"**
2. Zezwól na dostęp do kontaktów w oknie uprawnień systemowych
3. **(Android 10+)** W oknie systemowym wybierz **PhoneSpamKiller** jako aplikację do filtrowania połączeń
4. Status na ekranie głównym zmieni się na **"● Ochrona AKTYWNA"**

> **Android 8/9:** Pełne blokowanie połączeń wymaga ustawienia PhoneSpamKiller jako domyślnej aplikacji Telefon w ustawieniach systemowych (*Ustawienia → Aplikacje → Domyślne aplikacje → Telefon*).

---

## Architektura

```
app/
└── src/main/java/com/phonespamkiller/
    ├── MainActivity.kt                  # Główny ekran – UI, uprawnienia, rola
    ├── SpamCallScreeningService.kt      # Serwis filtrujący połączenia (działa w tle)
    ├── data/
    │   ├── BlockedCall.kt               # Encja Room (model danych)
    │   ├── BlockedCallDao.kt            # Interfejs zapytań do bazy
    │   └── AppDatabase.kt               # Singleton bazy danych
    ├── ui/
    │   └── BlockedCallsAdapter.kt       # Adapter RecyclerView dla listy blokad
    └── utils/
        └── ContactsHelper.kt            # Wyszukiwanie numeru w książce kontaktów
```

### Schemat działania

```
Połączenie przychodzące
        │
        ▼
SpamCallScreeningService.onScreenCall()
        │
        ├─── numer w kontaktach? ──► TAK ──► Przepuść połączenie
        │
        └─────────────────────────► NIE ──► Zablokuj połączenie
                                             │
                                             ▼
                                     Zapisz do bazy Room
                                     (numer + timestamp)
                                             │
                                             ▼
                                     LiveData odświeża UI
```

---

## Stos technologiczny

- **Kotlin** – język aplikacji
- **Room** – warstwa bazy danych (ORM nad SQLite)
- **LiveData** – reaktywne odświeżanie listy w UI
- **Coroutines** – operacje bazodanowe poza głównym wątkiem
- **RecyclerView + ViewBinding** – lista i wiązanie widoków
- **Material Components** – wygląd UI
- **CallScreeningService** – systemowy API do filtrowania połączeń

---

## Licencja

```
MIT License

Copyright (c) 2024 PhoneSpamKiller

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
