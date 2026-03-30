# expenses — dokumentacja postępu / update projektu

**Repozytorium:** `Jedmajc/expenses`  
**Data aktualizacji:** 2026-03-30 07:23:29  
**Zakres:** podsumowanie realizacji założeń projektu oraz plan dalszych prac.

---

## 1. Kontekst i zmiana kierunku projektu

Pierwotnie zakładano stworzenie **aplikacji webowej** do zarządzania wydatkami i przychodami.  
W trakcie realizacji projekt został jednak **przestawiony na aplikację mobilną (Android / Kotlin)**.

**Powód zmiany:**
- ograniczenia technologiczne w pierwotnym podejściu,
- brak możliwości wdrożenia ambitniejszych rozwiązań w planowanej formie webowej,
- decyzja o przejściu na aplikację mobilną jako bardziej adekwatną do celu i możliwości realizacyjnych.

Efektem jest działająca aplikacja mobilna, rozwijana jako projekt Android w Kotlinie.

---

## 2. Założenia / cele (plan)

Zakładane funkcjonalności (plan minimalny + dodatki):
1. Kontrola budżetu / pilnowanie wydatków.
2. Podsumowania wydatków i przychodów.
3. Edycja danych: wpisy i kategorie (w tym kategorie własne).
4. Dodatkowa funkcja: kalkulator spalania paliwa dla samochodu.

---

## 3. Co zostało zrealizowane (zrobione)

### 3.1. Pilnowanie budżetu
- Użytkownik może dodawać wpisy (wydatki i przychody) z kwotą, kategorią i opcjonalnym opisem.
- Dane są przechowywane lokalnie w aplikacji.

### 3.2. Podsumowania wydatków / przychodów
- Dostępne są zestawienia agregujące wydatki i przychody.
- Można analizować sumy wg kategorii (posortowane od największych wartości).

### 3.3. Edycja wpisów i kategorii
- Możliwa jest praca na historii wpisów (m.in. zarządzanie pozycjami).
- Kategorie:
  - można korzystać z listy kategorii,
  - można dodawać **własne (custom) kategorie**,
  - można zarządzać kategoriami (dodawanie/usuwanie wg dostępnych opcji w UI).

---

## 4. Co nie zostało zrealizowane (braki względem planu)

### 4.1. Kalkulator spalania paliwa
- **Nie zaimplementowano** kalkulatora spalania paliwa dla samochodu.
- Funkcja nie pasuje do aktualnej wizji projektu.

---

## 5. Co będzie robione dalej (kolejne kroki)

### 5.1. Eksport danych
Priorytetem na kolejny etap jest dodanie funkcji **eksportu danych**, tak aby użytkownik mógł:
- wyeksportować wpisy (wydatki/przychody) do pliku,
- otworzyć i analizować dane w innych narzędziach, np. **Excel**.

**Przykładowe formaty docelowe (do decyzji):**
- CSV (najprostszy do Excela),
- XLSX (bardziej „excelowy”, ale wymaga dodatkowych bibliotek),
- ewentualnie JSON jako format techniczny.

**Zakres eksportu (propozycja minimalna):**
- data utworzenia wpisu (jeśli istnieje w modelu),
- typ: wydatek / przychód,
- kwota,
- kategoria,
- opis.

---

## 6. Uwagi końcowe

- Ten dokument zastępuje wcześniejszy plik porównawczy, który zaginął — dlatego opis ma formę „stan na dziś” + plan.
- Najważniejsza zmiana projektowa: **web → mobile (Android/Kotlin)**.
- Aktualny stan projektu pokrywa kluczowe cele budżetowe (rejestracja, kategorie, historia, zestawienia), a dalszy rozwój skupia się na **przenoszalności danych (eksport)**.