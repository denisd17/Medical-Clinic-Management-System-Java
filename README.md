# Sistem de gestiune al unei clinici medicale

## ENTITATI ETAPA 3
Operatiile de tip CRUD se pot realiza pe urmatoarele entitati:
1.Patient (din patient menu)
2.Appointment (din patient menu)
3.MedicalRecord (din medical record menu)
4.Nurse (din employee menu)
5.Doctor (din employee menu)

## Obiecte principale

1. Person
2. Patient
3. Employee
4. Nurse
5. Doctor
6. MedicalRecord
7. Appointment
8. MedicalS
9. Consultation
10. Radiography
11. Test

### Person
Modeleaza atributele unei persoane.

### Patient
Extinde clasa Person.
Modeleaza atributele unui pacient.
Poate avea fisa medicala, insa nu este obligatoriu.

### Employee
Extinde clasa Person

### Nurse
Extinde clasa Employee

### Doctor
Extinde clasa Employee

### MedicalRecord
Fisa medicala a pacientului.
Contine:
- lista de alergii (string)
- lista de boli cronice (string)
- fumator / nefumator (boolean)

### Appointment
Programare
Contine:
- lista de servicii medicale ce vor fi efectuate
- cnp pacient
- cnp doctor (necesar pentru efectuarea consultatiilor)
- cnp asistenta (necesara pentru efectuarea investigatiilor / radiografiilor)
- data programare (date)
- ora programare (int)

### MedicalS
Serviciu medical
Contine:
- pret

### Consultation
Extinde clasa MedicalS
Contine:
- specializare doctor

### Radiography
Extinde clasa MedicalS
Contine:
- zona radiografie (Enum RadiographyArea)

### Test
Extinde clasa MedicalS
Contine:
- tip investigatie (Enum TestType)

## Functionalitati
- creare / stergere / editare detalii / afisare lista / cautare si afisare unul singur pentru Pacienti
- creare / stergere / editare detalii / afisare lista / cautare si afisare unul singur pentru Angajati
- afisare / afisare sortata (crescator | descrescator) dupa pret / editare detalii pentru Servicii medicale
- adaugare programare:
  se vor selecta serviciile medicale precum si data, dupa care sistemul va cauta o ora libera din ziua respectiva,
   pentru a programa pacientul.
- sortare pacienti dupa suma cheltuita in cadrul clinicii (crescator | descrescator)
