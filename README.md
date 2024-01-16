# Курсовой проект по предмету информационные системы и базы данных 
## Информационная система проведения экзамена на лицензирование звания Охотника в Hunter x Hunter

Описание файлов:
* описание бизнес процессов лежит в файле business_processes.pdf
* даталогическая модель базы данных в datalogical_model.png
* файл create, delete, insert_const_values с расширением sql содержит скрипты для создания, удаления базы данных, а также заполнения константных значений (например, статусы)
* add_functions_and_procedures.sql содержит написанные функции и процедуры


todo list:
- [x] генератор данных
- [x] скрипт заполнения и очистки данных
- [x] протестировать базу данных различными запросами
- [x] выявить наиболее используемые
- [x] предложить индексы (дора)
- [x] обосновать использование индексов (дора)


### **Для макса** 

Теперь есть триггер после доабвления в историю измененяется статуса кандидата(для тех, кто продолжает участие в экзамене)

Когда мы вставляем в trial_in_process для всех кандидатов, у кого статус ДОПУЩЕН К ИСПЫТАНИЮ, он меняется В ПРОЦЕССЕ ИСПЫТАНИЯ. Потом функцией меняем статусы, и они выгружаются в историю и сразу после этого у кого статус ПРОШЕЛ ИСПЫТАНИЕ, он меняется на ДОПУЩЕН К ИСПЫТАНИЮ (за счет нового триггера)

