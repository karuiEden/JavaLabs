Scanner: метод ioException() — возвращает сохранённый IOException от источника (или null, если ошибок не было). Плюс unchecked-исключения при работе с токенами: InputMismatchException (nextInt/nextDouble), NoSuchElementException (next на конце ввода), и превентивные hasNextX().
```java
if (in.ioException() != null) {
    System.out.println("Read error: " + in.ioException());
}
```
PrintWriter: метод checkError() — сбрасывает буфер и возвращает true, если при записи была ошибка (исключение внутри глотается, остаётся только флаг).
```java
out.println(...);
if (out.checkError()) {
    System.out.println("Write error");
}
```
Почему конструкторы ломают всю идею «простоты»:
Конструкторы new Scanner(File/Path/String) и new PrintWriter(String) бросают checked-исключения (FileNotFoundException / IOException). Значит, уже на первой строке работы с файлом новичок обязан поставить try/catch или throws — то есть сразу столкнуться с механизмом checked-исключений, от которого его хотели избавить. «Без исключений» остаётся только консольный вариант (new Scanner(System.in)), а для файлов цель не достигается: обойтись без обработки исключений всё равно нельзя.