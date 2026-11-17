<img width="1211" height="955" alt="image2" src="https://github.com/user-attachments/assets/3aedb626-3984-4924-bbbd-5d5e6f2f244a" />
<img width="1210" height="956" alt="image3" src="https://github.com/user-attachments/assets/538b86b6-0e49-4a36-8a31-9f4b39f3435a" />
<img width="1208" height="952" alt="image4" src="https://github.com/user-attachments/assets/15cd831d-3f80-4e7d-8464-9354d67076f8" />
<img width="1211" height="954" alt="image1" src="https://github.com/user-attachments/assets/00bc7938-0781-4fdb-aa1b-37af3dce4bc8" />


# Kalkulator w Jetpack Compose – Projekt Laboratoryjny

Mój projekt to zaawansowany kalkulator stworzony w Kotlinie przy użyciu frameworka **Jetpack Compose** (Material 3). Aplikacja została zaprojektowana tak, aby spełniać wszystkie wymagania funkcjonalne, estetyczne i architektoniczne stawiane na zajęciach.

---

## Spis treści
1. [Architektura i logika obliczeń](#1-architektura-i-logika-obliczeń)
2. [Testy jednostkowe JUnit](#2-testy-jednostkowe-junit)
3. [Debugowanie i obsługa błędów](#3-debugowanie-i-obsługa-błędów)
4. [Dopracowanie szczegółów (Zadanie 4)](#4-dopracowanie-szczegółów-zadanie-4)
5. [Sprawozdanie z testów](#5-sprawozdanie-z-testów)

---

## 1. Architektura i logika obliczeń
Całą logikę matematyczną wyciągnąłem całkowicie z interfejsu użytkownika do osobnej funkcji `evaluateMathExpression` oraz klasy parsera (`SimpleExpressionParser`). Dzięki temu silnik kalkulatora działa niezależnie od widoków Compose. 
* **Separator dziesiętny:** Ponieważ biblioteka do parsowania operuje na kropkach, a polska klawiatura potrafi wpisać przecinek, dodałem automatyczną konwersję `.replace(',', '.')`, dzięki czemu użytkownik może bez problemu używać przecinka.
* **Obsługa błędów:** Dzielenie przez zero, pierwiastki z liczb ujemnych czy niedokończone wyrażenia (np. `2+`) nie powodują awarii (*crashu* aplikacji) – zamiast tego program bezpiecznie łapie wyjątki w bloku `try-catch` i wyświetla czytelny komunikat **`Błąd`**.

## 2. Testy jednostkowe JUnit
Żeby szybko i bezproblemowo weryfikować poprawność kodu bez klikania w aplikację, napisałem testy jednostkowe w katalogu `src/test/java/com/example/calculatorapp/CalculatorUnitTest.kt`. 
* Ponieważ funkcja obliczeniowa przyjmuje zwykły `String` i zwraca wynik tekstowy, testy uruchamiają się w ułamku sekundy w czystym środowisku Javy/JUnit **bez potrzeby włączania emulatora**. 
* Dopisane 9 przypadków testowych sprawdza m.in. priorytety działań, nawiasy, błędy oraz działanie separatorów (zarówno kropki, jak i przecinka).

## 3. Debugowanie i obsługa błędów
Podczas tworzenia projektu aktywnie korzystałem z narzędzi debugowania w Android Studio, żeby szybko namierzać ewentualne problemy bez pisania zbędnych logów:
* Stawiałem punkty wstrzymania (*breakpoints*) bezpośrednio przy linijkach kodu, a w razie potrzeby używałem punktów warunkowych (np. zatrzymujących program tylko wtedy, gdy wyrażenie to `2+`).
* Korzystając z trybu **Debug** (ikona robaka), sterowałem wykonaniem programu za pomocą klawiszy **F8** (krok nad) i **F7** (krok w głąb), na bieżąco podglądając stan zmiennych w panelu *Variables*.

## 4. Dopracowanie szczegółów (Zadanie 4)
Zadbałem o to, aby aplikacja wyglądała profesjonalnie i była dopracowana wizualnie:
* **Ikona:** Stworzyłem własną, czytelną ikonę aplikacji (wraz z wariantem monochromatycznym) umieszczoną w zasobach `mipmap`.
* **Typografia:** Wykorzystałem mechanizmy Material 3 z cyframi tabelarycznymi, dzięki czemu podczas wpisywania kolejnych liczb tekst na wyświetlaczu nie „drga” ani nie skacze na boki.
* **Motyw i tryb ciemny:** Aplikacja posiada w pełni działający motyw (`CustomCalcTheme`), który automatycznie dostosowuje się do trybu jasnego lub ciemnego systemu, zachowując idealną czytelność elementów.
* **Lokalizacja:** Nazwa aplikacji jest dynamicznie zaciągana z pliku `strings.xml`. Dodając katalog `res/values-pl/`, zapewniłem, że po zmianie języka systemu na polski, nazwa pod ikoną na ekranie telefonu zmienia się na „Kalkulator”.

## 5. Sprawozdanie z testów
W ramach dokumentacji i sprawozdania z przeprowadzonych prac, poniżej zamieszczam tabelę podsumowującą zachowanie aplikacji dla kluczowych przypadków testowych:

| Wyrażenie / Przypadek | Opis testu | Oczekiwane zachowanie | Rzeczywisty wynik w aplikacji | Status |
| :--- | :--- | :--- | :--- | :--- |
| **`2+2`** | Proste dodawanie | Poprawny wynik | Zwrócono **`4`** | ✅ Zaliczone |
| **`2+2*2`** | Kolejność działań | Mnożenie pierwsze | Zwrócono **`6`** | ✅ Zaliczone |
| **`(2+2)*2`** | Użycie nawiasów | Najpierw nawias | Zwrócono **`8`** | ✅ Zaliczone |
| **`1/0`** | Dzielenie przez zero | Bezpieczna obsługa | Komunikat **`Błąd`**, brak awarii | ✅ Zaliczone |
| **`2+`** | Niepełne wyrażenie | Odporność na błąd składni | Komunikat **`Błąd`** | ✅ Zaliczone |
| **`""`** | Puste pole | Stabilność stanu | Komunikat **`Błąd`** / brak zmiany | ✅ Zaliczone |
| **`sqrt(-1)`** | Błąd dziedziny | Obsługa wyjątku matematycznego | Komunikat **`Błąd`** | ✅ Zaliczone |
| **`2.5+2.5`** | Kropka dziesiętna | Poprawny wynik zmiennoprzecinkowy | Zwrócono **`5`** | ✅ Zaliczone |
| **`2,5+2,5`** | Przecinek dziesiętny | Konwersja i poprawny wynik | Zwrócono **`5`** (po mapowaniu) | ✅ Zaliczone |

### Podsumowanie
Projekt jest w pełni ukończony, przetestowany i gotowy do zaprezentowania. Zastosowane rozwiązania spełniają wszystkie kryteria i gwarantują stabilne działanie aplikacji.
