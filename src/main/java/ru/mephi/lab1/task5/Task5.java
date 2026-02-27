package ru.mephi.lab1.task5;


/**
 * What happens when you cast a double to an int that is larger than the largest
 * possible int value? Try it out.
 */
public class Task5 {

    static void main(){
        double double_num = Math.nextUp(Integer.MAX_VALUE);
        int int_num = (int)double_num;
        IO.println("Double num: " + double_num);
        IO.println("Int num: " + int_num);
    }

    // Int отбрасывает дробную часть и становится равным Integer.MAX_VALUE

}
