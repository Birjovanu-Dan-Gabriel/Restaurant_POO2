# Proiect POO2

**Birjovanu Dan-Gabriel** 
**Grupa 362**
**Tema Aleasa:** Gestionare Pizzerie

---

## Functionalitati implementate:

* La lansarea aplicatiei utilizatorul este pe pagina principala a clientului unde poate vizualiza meniul si plasa o rezervare.
* In josul paginii utilizatorul are un buton de "Acces personal" unde se pot conecta 3 tipuri de angajati (Manager, Chelner, Bucatar), momentan parola este admin pentru toti;
    
    1. **Manager**
        * Acces la tabelul Angajati;
        * Acces la tabelul Ingrediente(poate adauga tipuri noi);
        * Acces la tabelul Produse ( fiecare produs este alcatuit din ingredientele existente)
        * Acces la Stoc Ingrediente - poate adauga la cantitate
    
    2. **Chelner**
        * Selecteaza o masa (se poate selecta si nr de mese disponibile)
        * La o anumita masa poate aduga/sterge produse la comanda (sau anula direct comanda)
        * Chelnerul nu poate finaliza comanda (adica nu este platita) pana cand Bucatarul nu finalizeaza el comanda;
        * De asemenea, cand Bucatarul finalizeaza comanda, chelnerul nu mai poate sterge produse din ea / nu poate anula comanda;    
        * Cand un chelner adauga un produs pe comanda ingredientele necesare acestui produs se scad din stocul magazinului (daca comanda este anulata si stergerea scaderea stockului este anulata)
        
    3. **Bucatar**
        * Un dashboard cu comenzile active, poate apasa pe "finalizare comanda"

* Diagrama Bazei de date
![Screenshot 1](src/resources/images/Screenshot%20from%202026-05-28%2014-48-29.png)

![Screenshot 2](src/resources/images/Screenshot%20from%202026-05-28%2014-48-49.png)
---
