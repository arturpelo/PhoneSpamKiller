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

### Wersja polska (tłumaczenie informacyjne)

**Licencja MIT**

Copyright (c) 2024 PhoneSpamKiller

Niniejszym udziela się, bezpłatnie, każdej osobie, która wejdzie w posiadanie kopii
niniejszego oprogramowania i związanych z nim plików dokumentacji (dalej „Oprogramowanie"),
zezwolenia na korzystanie z Oprogramowania bez ograniczeń, w tym bez ograniczeń prawa
do używania, kopiowania, modyfikowania, łączenia, publikowania, rozpowszechniania,
sublicencjonowania i/lub sprzedaży kopii Oprogramowania, a także do zezwalania osobom,
którym Oprogramowanie jest dostarczane, na czynienie tego samego, z zastrzeżeniem
następujących warunków:

Powyższa informacja o prawach autorskich oraz niniejsza informacja o zezwoleniu muszą być
zawarte we wszystkich kopiach lub istotnych częściach Oprogramowania.

---

### Wyłączenie odpowiedzialności i zrzeczenie się roszczeń

**OPROGRAMOWANIE JEST DOSTARCZANE „W STANIE, W JAKIM SIĘ ZNAJDUJE" (AS IS), BEZ
JAKICHKOLWIEK GWARANCJI – WYRAŹNYCH ANI DOROZUMIANYCH – W TYM BEZ GWARANCJI
PRZYDATNOŚCI HANDLOWEJ, PRZYDATNOŚCI DO OKREŚLONEGO CELU ORAZ NIENARUSZANIA PRAW
OSÓB TRZECICH.**

W NAJSZERSZYM ZAKRESIE DOPUSZCZALNYM PRZEZ OBOWIĄZUJĄCE PRAWO:

1. **Brak odpowiedzialności autora.** Autor lub autorzy Oprogramowania nie ponoszą
   odpowiedzialności – na zasadzie winy, ryzyka ani na żadnej innej podstawie prawnej –
   za jakiekolwiek roszczenia, szkody lub inne zobowiązania, w tym szkody bezpośrednie,
   pośrednie, incydentalne, szczególne, następcze lub utratę zysku, wynikające z korzystania
   z Oprogramowania, niemożności jego używania lub w inny sposób z nim związane,
   niezależnie od tego, czy do szkody doszło w ramach odpowiedzialności kontraktowej,
   deliktowej (w tym wynikającej z zaniedbania) czy na innej podstawie.

2. **Brak odpowiedzialności za działanie systemu telefonicznego.** Oprogramowanie
   korzysta z systemowego interfejsu `CallScreeningService` udostępnianego przez platformę
   Android. Autor nie odpowiada za pominięcie lub opóźnienie blokowania połączeń,
   utratę połączeń przychodzących, nieprawidłowe rozpoznanie numeru ani za żadne
   konsekwencje wynikające z działania lub braku działania systemu operacyjnego,
   producenta urządzenia lub operatora telekomunikacyjnego.

3. **Zrzeczenie się roszczeń przez użytkownika.** Pobierając, instalując lub
   uruchamiając Oprogramowanie, użytkownik wyraża zgodę na powyższe warunki i
   nieodwołalnie zrzeka się wszelkich obecnych i przyszłych roszczeń wobec autora
   z tytułu szkód wynikłych z korzystania z Oprogramowania lub z niemożności jego
   używania, w tym roszczeń opartych na przepisach o odpowiedzialności za produkt,
   ochronie konsumentów oraz czynach niedozwolonych.

4. **Zakres terytorialny.** Niniejsze wyłączenie odpowiedzialności stosuje się
   w maksymalnym zakresie dozwolonym przez przepisy prawa właściwego dla miejsca
   zamieszkania lub siedziby użytkownika. W jurysdykcjach, gdzie całkowite wyłączenie
   odpowiedzialności nie jest dopuszczalne, odpowiedzialność autora ograniczona jest
   do minimum przewidzianego przez prawo.

---

### English version (original)

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

---

### Disclaimer of Liability and Waiver of Claims

**THE SOFTWARE IS PROVIDED "AS IS", WITHOUT ANY WARRANTY OF ANY KIND, EXPRESS
OR IMPLIED, INCLUDING BUT NOT LIMITED TO WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT.**

TO THE MAXIMUM EXTENT PERMITTED BY APPLICABLE LAW:

1. **No liability of the author.** The author(s) of the Software shall not be
   liable — whether in contract, tort (including negligence), strict liability,
   or any other legal theory — for any claims, damages, or other liability,
   including but not limited to direct, indirect, incidental, special,
   consequential, or punitive damages, or loss of profits, arising out of or
   in connection with the use of, or inability to use, the Software.

2. **No liability for telephone system behavior.** The Software relies on the
   `CallScreeningService` API provided by the Android platform. The author
   accepts no responsibility for missed or delayed call blocking, dropped
   incoming calls, incorrect number identification, or any consequences
   resulting from the behavior of the operating system, device manufacturer,
   or telecommunications operator.

3. **Waiver of claims by the user.** By downloading, installing, or running
   the Software, the user agrees to these terms and irrevocably waives all
   present and future claims against the author arising from any damage caused
   by the use of, or inability to use, the Software, including claims based on
   product liability, consumer protection law, and tort law.

4. **Territorial scope.** This disclaimer applies to the fullest extent
   permitted by the law of the user's jurisdiction. In jurisdictions where a
   complete exclusion of liability is not permitted, the author's liability is
   limited to the minimum extent required by applicable law.
