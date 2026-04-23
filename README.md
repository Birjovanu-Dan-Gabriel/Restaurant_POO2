# Proiect POO2

**Birjovanu Dan-Gabriel** **Grupa 362** **Tema Aleasa:** Gestionare Pizzerie

---

## Functionalitati implementate:

* La lansarea aplicatiei utilizatorul este pe pagina principala a clientului unde poate vizualiza meniul (**urmeaza sa implementez functia de rezervare);
* In josul paginii utilizatorul are un buton de "Acces personal" unde se pot conecta 3 tipuri de angajati (Manager, Chelner, Bucatar), momentan parola este admin pentru toti;
    
    1. **Manager**
        * Acces la tabelul Angajati;
        * Acces la tabelul Ingrediente(poate adauga tipuri noi);
        * Acces la tabelul Produse ( fiecare produs este alcatuit din ingredientele existente)
        * Acces la Stoc Ingrediente - poate adauga la cantitate
    
    2. **Chelner** * Selecteaza o masa (se poate selecta si nr de mese disponibile)
        * La o anumita masa poate aduga/sterge produse la comanda (sau anula direct comanda)
        * Chelnerul nu poate finaliza comanda (adica nu este platita) pana cand Bucatarul nu finalizeaza el comanda;
        * De asemenea, cand Bucatarul finalizeaza comanda, chelnerul nu mai poate sterge produse din ea / nu poate anula comanda;    
        * Cand un chelner adauga un produs pe comanda ingredientele necesare acestui produs se scad din stocul magazinului (daca comanda este anulata si stergerea scaderea stockului este anulata)
        
    3. **Bucatar** * Un dashboard cu comenzile active, poate apasa pe "finalizare comanda"

---

## Ce urmeaza sa implementez/lipsuri:

* Auditul este implementat (mai sunt si lipsuri la placeholdere), doresc sa specifice si ce angajat a facut actiunea si sa pot prezenta audit-ul in sectiunea managerului;
* In loc de sistemul de login curent voi face unul care sa se folosesaca de angajatii din baza de date (pt a sti ce angajat face si pt a ma folosi de toate clasele create)
* Posibilitatea de rezervare a unei mese (aici voi implementa si clasa client)
* Comentarea mai buna a codului
* Dupa crearea sistemului de login nou voi reface sistemul curent de comenzi Chelner-Bucatar (momentan nu ma folosesc de clasa Comenzi, doresc sa am clasificare pe un angajat anume, nu unul generic)
* Sectiunea si aplicarea automata de discounturi ( managerul poate adauga un discount de tipul ( x Produse - > n% discount)
* Regandirea Stocului (sa nu fie introdus manual, la o anumita ora sau data sa se aprovizioneze automat magazinul / anumite produse sa vina de la anumiti distribuitori)
